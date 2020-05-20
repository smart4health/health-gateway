package pt.uninova.s4h.hub.medx.physiotherapy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Class to draw isometric test chart.
 *
 * @author Fábio Januário
 * @param <X>
 * @param <Y>
 * @email faj@uninova.pt
 * @version 07 October 2019 - v1.0.
 */

public class BarChartIso<X, Y> extends BarChart<X, Y> {
    
    private ObservableList<Data<X, X>> verticalRangeMarkers;
      
    private final Axis xAxis;
    private final Axis yAxis; 
    private final XYChart.Series seriesMax = new XYChart.Series();
    private final XYChart.Series seriesMin = new XYChart.Series();
    private List<Integer> listAngle;
    private int CurrentAngle;
    private List<Integer> listMax;
    private boolean flagMax = false;
    private Data<X,X> lastVRM;
    
    Map<Node, Text> nodeMap = new HashMap<>();      
    
    public BarChartIso(Axis xAxis, Axis yAxis) {
        super(xAxis, yAxis);
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.setTitle("Isometric Test");
        this.xAxis.setLabel("Angle (º)");
        this.yAxis.setLabel("Force (Kgf)");
        this.setLegendVisible(true);
        this.setLegendSide(Side.RIGHT); 
        this.getData().addAll(seriesMax,seriesMin);
        this.xAxis.setAnimated(false);
        this.yAxis.setAnimated(false);
        this.setAnimated(false);
        
        verticalRangeMarkers = FXCollections.observableArrayList(data -> new Observable[]{data.XValueProperty()});
        verticalRangeMarkers = FXCollections.observableArrayList(data -> new Observable[]{data.YValueProperty()}); // 2nd type of the range is X type as well
        verticalRangeMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());        
    }

    public void setAxisLabel(String labelX, String labelY){
        this.xAxis.setLabel(labelX);
        this.yAxis.setLabel(labelY);
    }
    
    @Override
    protected void dataItemAdded(Series<X,Y> series, int itemIndex, Data<X,Y> item) {
        super.dataItemAdded(series, itemIndex, item);
        Text text = new Text(String.valueOf(item.getYValue()));
        nodeMap.put(item.getNode(), text);
        getPlotChildren().add(text);
    }
    
    @Override
    protected void dataItemRemoved(final Data<X,Y> item, final Series<X,Y> series) {
        Node text = nodeMap.get(item.getNode());
        getPlotChildren().remove(text);
        nodeMap.remove(item.getNode());
        super.dataItemRemoved(item,series);
    }

    @Override
    protected void dataItemChanged(Data<X, Y> item) {
        Text text1 = nodeMap.get(item.getNode());
        getPlotChildren().remove(text1);
        Text text = new Text(String.valueOf(item.getYValue()));
        nodeMap.replace(item.getNode(), text);
        getPlotChildren().add(text);
        super.dataItemChanged(item);
    }    
    
    public void addVerticalRangeMarker(Data<X, X> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalRangeMarkers.contains(marker)) {
            return;
        }                   
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        rectangle.setStroke(Color.TRANSPARENT);
        rectangle.setFill(Color.color(0.36, 0.62, 0.71, 0.25));
        marker.setNode(rectangle);
        getPlotChildren().add(rectangle);
        verticalRangeMarkers.add(marker);
    }       

    private void removeVerticalRangeMarker(Data<X, X> marker) {
        if (marker != null){
            if (marker.getNode() != null) {
                getPlotChildren().remove(marker.getNode());
                marker.setNode(null);
            }
            verticalRangeMarkers.remove(marker);
        }
    } 
    
    public void removeVerticalMarker(){
        removeVerticalRangeMarker(lastVRM);
    }
    
    @Override
    protected void layoutPlotChildren() {
        double catSpace = ((CategoryAxis)xAxis).getCategorySpacing();
        this.setCategoryGap(catSpace-200);
        final double avilableBarSpace = catSpace - (this.getCategoryGap());
        double barWidth = (avilableBarSpace);
        final double barOffset = -((catSpace - this.getCategoryGap()) / 2);
        final double zeroPos = (((ValueAxis)yAxis).getLowerBound() > 0) ?
                ((ValueAxis)yAxis).getDisplayPosition(((ValueAxis)yAxis).getLowerBound()) : ((ValueAxis)yAxis).getZeroPosition();
        if (barWidth <= 0) barWidth = 1;
        // update bar positions and sizes
        int catIndex = 0;
        for (String category : ((CategoryAxis)xAxis).getCategories()) {
            int index = 0;
            for (Iterator<Series<X, Y>> sit = getDisplayedSeriesIterator(); sit.hasNext(); ) {
                Series<X, Y> series = sit.next();
                final Data<X,Y> item = series.getData().get(catIndex);
                if (item != null) {
                    final Node bar = item.getNode();
                    final double categoryPos;
                    final double valPos;
                    categoryPos = getXAxis().getDisplayPosition(item.getXValue());
                    valPos = getYAxis().getDisplayPosition(item.getYValue());
                    if (Double.isNaN(categoryPos) || Double.isNaN(valPos)) {
                        continue;
                    }
                    final double bottom = Math.min(valPos,zeroPos);
                    final double top = Math.max(valPos,zeroPos);
                    //bottomPos = bottom;
                    if (index == 0){
                        bar.resizeRelocate( categoryPos + barOffset, bottom, barWidth, top-bottom);
                    } else if (index ==1){
                        bar.resizeRelocate( categoryPos + barOffset + barWidth/4, bottom, barWidth/2, top-bottom);                           
                    }
                    index++;
                }
            }
            catIndex++;
        }            
        for (Node bar : nodeMap.keySet()) {
            Text text = nodeMap.get(bar);
            text.setFont(Font.font("System", FontWeight.BOLD, 28));
            text.setFill(Color.WHITE);
            text.relocate(((bar.getBoundsInParent().getMaxX() + bar.getBoundsInParent().getMinX())/2)-((text.getBoundsInParent().getMaxX()-text.getBoundsInParent().getMinX())/2), bar.getBoundsInParent().getMinY());
        }
        for (Data<X, X> verticalRangeMarker : verticalRangeMarkers) {
            Rectangle rectangle = (Rectangle) verticalRangeMarker.getNode();
            rectangle.setX(getXAxis().getDisplayPosition(verticalRangeMarker.getXValue()) - 120);
            rectangle.setWidth(240);
            rectangle.setY(0d);
            rectangle.setHeight(getBoundsInLocal().getHeight());
            rectangle.toBack();
        }        
    }    
    
    public void StartChart(List<Integer> list){
        this.clearChart();
        this.listAngle = list;
        seriesMax.setName("Max");
        seriesMin.setName("Current");
        listMax = new ArrayList<>();      
        ObservableList<String> StringList = FXCollections.observableArrayList();
        for (Integer angle : list){
            StringList.add(String.valueOf(angle));
            listMax.add(0);
        }
        
        Collections.reverse(StringList);
        xAxis.invalidateRange(StringList);
        ((CategoryAxis)xAxis).setCategories(StringList);
        for (int i=0 ; i<list.size() ; i++){
            seriesMin.getData().add(new XYChart.Data<>(String.valueOf(listAngle.get(i)), 0));
            seriesMax.getData().add(new XYChart.Data<>(String.valueOf(listAngle.get(i)), 0));
        }
    }
    
    public void setCurrentAngle(int current){
        CurrentAngle = current;
        lastVRM = (Data<X,X>)(new Data<>(String.valueOf(listAngle.get(CurrentAngle)), String.valueOf(listAngle.get(CurrentAngle))));
        addVerticalRangeMarker(lastVRM);
    }
    
    public void setCurrentForce(int force){
        XYChart.Data<String,Number> data = new XYChart.Data<>(String.valueOf(listAngle.get(CurrentAngle)), force);
        seriesMin.getData().set(CurrentAngle,data);
        if ((listMax.get(CurrentAngle) < force) && (flagMax)) {
            seriesMax.getData().set(CurrentAngle,new XYChart.Data<>(String.valueOf(listAngle.get(CurrentAngle)), force));
            listMax.set(CurrentAngle, force);
        }    
    }
    
    public void setFlagMax(boolean aux){
        flagMax = aux;
    }
    
    public void clearMax(int angle){
        listMax.set(angle,0);
        seriesMax.getData().set(angle,new XYChart.Data<>(String.valueOf(listAngle.get(angle)),0));
    }
    
    public int getMax(int angle){
        try{
            return listMax.get(angle);
        } catch (IndexOutOfBoundsException ex){
            return -1;
        }         
    }    
    
    public void clearChart(){
        seriesMin.getData().clear();
        seriesMax.getData().clear();
    }
}
