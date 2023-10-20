package com.xm2013.jfx.control.tableview;

import com.xm2013.jfx.common.FxKit;
import com.xm2013.jfx.control.base.HueType;
import com.xm2013.jfx.control.base.SizeType;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmTableRow<T> extends TableRow<T> {


    class TableRowSkinInfo {
        public Background background;
        public TableRowSkinInfo(Background background){
            this.background = background;
        }
    }

    private Map<Integer, TableRowSkinInfo> infos = new LinkedHashMap<>();

    private static PseudoClass odd = PseudoClass.getPseudoClass("odd");
    private static PseudoClass selected = PseudoClass.getPseudoClass("selected");
    private static PseudoClass filled = PseudoClass.getPseudoClass("filled");
    private static PseudoClass hover = PseudoClass.getPseudoClass("hover");
    private boolean prevSelected = false;
    private double startRadius = 0.01;

    public XmTableRow(){

        tableViewProperty().addListener((ob, ov, nv)->{
            updateSkin(0);

            if(nv instanceof XmTableView){
                XmTableView xtv = (XmTableView) nv;
                Paint borderColor = xtv.getBorderColor();
                setBorder(new Border(new BorderStroke(
                        Color.TRANSPARENT, Color.TRANSPARENT, borderColor, Color.TRANSPARENT,
                        BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0),
                        Insets.EMPTY
                )));
            }
        });

        ObservableSet<PseudoClass> pseudoClassStates = getPseudoClassStates();
        pseudoClassStates.addListener((SetChangeListener<PseudoClass>) change -> {
            boolean isOdd = pseudoClassStates.contains(odd);
            boolean isSelected = pseudoClassStates.contains(selected);
            boolean isHover = pseudoClassStates.contains(hover);

            if(isOdd){
                if(!isSelected && !isHover){
                    updateSkin(1);
                    prevSelected = false;
                    return;
                }
            }

            if(isSelected){
                if(!prevSelected){
                    updateSelectedStatus();
//                    updateSkin(2);
                }

                prevSelected = true;
                return;
            }

            prevSelected = false;
            if(isHover){
                updateSkin(3);
                return;
            }

            updateSkin(0);

        });

    }

    private void updateSelectedStatus(){
        setTextFill(Color.WHITE);

        Color color = ((XmTableView)getTableView()).getColorType().getFxColor();

        startRadius = 0;
        AnimationTimer timer = new AnimationTimer() {
            final double frameDuration = 1_000_000_000.0 / 60;
            long lastUpdate = 0;

            Color color1 = FxKit.getOpacityColor( FxKit.deriveColor(color, 0.5), 0.5);

            @Override
            public void handle(long now) {
                double elapsedNanos = now - lastUpdate;
                if (elapsedNanos >= frameDuration) {

                    if(startRadius<1){
                        startRadius += 0.1;

                        List<Stop> stops = new ArrayList<>();
                        stops.add(new Stop(0, color));
                        stops.add(new Stop(startRadius, color1));
                        stops.add(new Stop(1, color));

                        LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
                        setBackground(new Background(new BackgroundFill(lg, CornerRadii.EMPTY, Insets.EMPTY)));

                        if(!getPseudoClassStates().contains(selected)){
                            if(getPseudoClassStates().contains(odd)){
                                updateSkin(1);
                            }else{
                                updateSkin(0);
                            }
                            stop();
                        }

                    }else{
                        stop();
                        if(!getPseudoClassStates().contains(selected)){
                            if(getPseudoClassStates().contains(odd)){
                                updateSkin(1);
                            }else{
                                updateSkin(0);
                            }
                        }
                    }
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }


    /**
     * 0-默认
     * 1-odd
     * 2-selected
     * 3-hover
     * @param status int
     */
    private void updateSkin(int status){

//        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        TableRowSkinInfo info = getInfo(status);
        if(info == null){
            return;
        }

        TableView<T> tv = tableViewProperty().get();

        if(tv instanceof  XmTableView){
            double fontSize = 14;
            SizeType sizeType = ((XmTableView<T>) tv).getSizeType();
            if(sizeType.equals(equals(SizeType.SMALL))){
                fontSize = 12;
            }else if(sizeType.equals(SizeType.LARGE)){
                fontSize = 16;
            }

            this.setFont(Font.font(this.getFont().getFamily(), fontSize));
        }

        setBackground(info.background);

    }

    private TableRowSkinInfo getInfo(int status){

        if(infos.size() == 0 || infos.get(status) == null){
            buildCellSkinInfo(status);
        }

        return infos.get(status);

    }

    private void buildCellSkinInfo(int status) {

        TableView<T> tableView = getTableView();
        boolean is = tableView instanceof XmTableView;
        if(!is){
            return;
        }

        XmTableView lv = (XmTableView) tableView;
        Paint paint = lv.getColorType().getPaint(),
                bgColor = null;

        HueType hueType = lv.getHueType();

        if(status == 0){
            if(HueType.LIGHT.equals(hueType)){
                bgColor = Color.WHITE;
            }else{
                bgColor = Color.TRANSPARENT;
            }
        }else if(status == 1){
            if(HueType.LIGHT.equals(hueType)){
                bgColor = FxKit.getOpacityPaint(paint, 0.08);
            }else{
                bgColor = FxKit.getOpacityPaint(FxKit.getLightPaint(paint, 0.8), 0.1);
            }
        }else if(status == 2){
            bgColor = paint;

        }else if(status == 3){
            if(HueType.LIGHT.equals(hueType)){
                bgColor = FxKit.getOpacityPaint(paint, 0.2);
            }else{
                bgColor = FxKit.getOpacityPaint(FxKit.getLightPaint(paint, 0.8), 0.2);
            }
        }

        infos.put(status, new TableRowSkinInfo(new Background(new BackgroundFill(bgColor, CornerRadii.EMPTY, Insets.EMPTY))));

    }

}
