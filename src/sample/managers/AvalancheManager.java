package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.FeistelСipher;

public class AvalancheManager {
    private static Parent root;

    FeistelСipher feistelСipher;

    @FXML
    LineChart avalancheChartID;
    TextField inputBlockSizeID;
    TextField inputBitNumberID;
    Label changesCountLabel;
    Label roundsLabel;


    public AvalancheManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        avalancheChartID=(LineChart) root.lookup("#avalancheChartID");
        inputBitNumberID=(TextField) root.lookup("#inputBitNumberID");
        inputBlockSizeID=(TextField) root.lookup("#inputBlockSizeID");
        changesCountLabel=(Label) root.lookup("#changesCountLabel");
        roundsLabel=(Label) root.lookup("#roundsLabel");
    }

    @FXML
    public void onBuildChart(){

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        XYChart.Series series = new XYChart.Series();
        avalancheChartID=new LineChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Раунды");
        yAxis.setLabel("Количество изменившихся битов");

        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));

        avalancheChartID.getData().add(series);

        changesCountLabel.setVisible(true);
        roundsLabel.setVisible(true);
        avalancheChartID.setVisible(true);
    }

}
