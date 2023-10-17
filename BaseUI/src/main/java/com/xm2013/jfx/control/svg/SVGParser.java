/*
 * MIT License
 *
 * Copyright (c) 2023 tuxming@sina.com / wechat: t5x5m5
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.xm2013.jfx.control.svg;

public class SVGParser {
    final String svgpath;
    final int len;
    int pos;
    boolean allowcomma;

    public SVGParser(String svgpath) {
        this.svgpath = svgpath;
        this.len = svgpath.length();
    }

    public boolean isDone() {
        return (toNextNonWsp() >= len);
    }

    public char getChar() {
        return svgpath.charAt(pos++);
    }

    public boolean nextIsNumber() {
        if (toNextNonWsp() < len) {
            switch (svgpath.charAt(pos)) {
                case '-':
                case '+':
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                case '.':
                    return true;
            }
        }
        return false;
    }

    public float f() {
        return getFloat();
    }

    public float a() {
        return (float) Math.toRadians(getFloat());
    }

    public float getFloat() {
        int start = toNextNonWsp();
        this.allowcomma = true;
        int end = toNumberEnd();
        if (start < end) {
            String flstr = svgpath.substring(start, end);
            try {
                return Float.parseFloat(flstr);
            } catch (NumberFormatException e) {
            }
            throw new IllegalArgumentException("invalid float ("+flstr+
                    ") in path at pos="+start);
        }
        throw new IllegalArgumentException("end of path looking for float");
    }

    public boolean b() {
        toNextNonWsp();
        this.allowcomma = true;
        if (pos < len) {
            char flag = svgpath.charAt(pos);
            switch (flag) {
                case '0': pos++; return false;
                case '1': pos++; return true;
            }
            throw new IllegalArgumentException("invalid boolean flag ("+flag+
                    ") in path at pos="+pos);
        }
        throw new IllegalArgumentException("end of path looking for boolean");
    }

    private int toNextNonWsp() {
        boolean canbecomma = this.allowcomma;
        while (pos < len) {
            switch (svgpath.charAt(pos)) {
                case ',':
                    if (!canbecomma) {
                        return pos;
                    }
                    canbecomma = false;
                    break;
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    break;
                default:
                    return pos;
            }
            pos++;
        }
        return pos;
    }

    private int toNumberEnd() {
        boolean allowsign = true;
        boolean hasexp = false;
        boolean hasdecimal = false;
        while (pos < len) {
            switch (svgpath.charAt(pos)) {
                case '-':
                case '+':
                    if (!allowsign) return pos;
                    allowsign = false;
                    break;
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    allowsign = false;
                    break;
                case 'E': case 'e':
                    if (hasexp) return pos;
                    hasexp = allowsign = true;
                    break;
                case '.':
                    if (hasexp || hasdecimal) return pos;
                    hasdecimal = true;
                    allowsign = false;
                    break;
                default:
                    return pos;
            }
            pos++;
        }
        return pos;
    }
}
