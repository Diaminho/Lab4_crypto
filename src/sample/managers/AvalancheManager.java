package sample.managers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.FeistelСipher;

import static javafx.scene.chart.XYChart.*;

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
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        avalancheChartID = new LineChart<Number, Number>(x,y);
        avalancheChartID.setTitle("Series");
        Series series1 = new Series();
        Series series2 = new Series();
        series2.setName("cos(x)");
        series1.setName("sin(x)");
        ObservableList<Data> datas = FXCollections.observableArrayList();
        ObservableList<Data> datas2 = FXCollections.observableArrayList();
        for(int i=0; i<20; i++){
            datas.add(new Data(i,Math.sin(i)));
            datas2.add(new Data(i,Math.cos(i)));
        }

        series1.setData(datas);
        series2.setData(datas2);

        avalancheChartID.getData().add(series1);
        avalancheChartID.getData().add(series2);

        //changesCountLabel.setVisible(true);
        //roundsLabel.setVisible(true);
        avalancheChartID.setVisible(true);
    }

}
