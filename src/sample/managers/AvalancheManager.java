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
import javafx.scene.layout.AnchorPane;
import sample.FeistelСipher;

import static javafx.scene.chart.XYChart.*;

public class AvalancheManager {
    private static Parent root;

    FeistelСipher feistelСipher;

    @FXML
    LineChart<String, Double>  avalancheChartID;
    TextField inputBlockSizeID;
    TextField inputBitNumberID;
    Label changesCountLabel;
    Label roundsLabel;
    AnchorPane anchorPaneID;

    @FXML
    NumberAxis x;
    @FXML
    NumberAxis y;


    public AvalancheManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        anchorPaneID=(AnchorPane) root.lookup("#anchorPaneID");
        avalancheChartID=(LineChart) root.lookup("#avalancheChartID");
        inputBitNumberID=(TextField) root.lookup("#inputBitNumberID");
        inputBlockSizeID=(TextField) root.lookup("#inputBlockSizeID");
        changesCountLabel=(Label) root.lookup("#changesCountLabel");
        roundsLabel=(Label) root.lookup("#roundsLabel");
    }

    @FXML
    public LineChart onBuildChart(){
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID.setTitle("Ssanina ssanaya");
        //avalancheChartID.setScaleX(3);
        //avalancheChartID.setScaleY(5);
        XYChart.Series<String, Double> series  = new XYChart.Series<String, Double>();
        //series.setName("sin(x)");
        for(int i=0; i<20; i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)i*2));
        }

        //avalancheChartID.setData(datas);
        avalancheChartID.getData().add(series);
        //changesCountLabel.setVisible(true);
        //roundsLabel.setVisible(true);
        avalancheChartID.setVisible(true);
        return avalancheChartID;
        //root.getChildrenUnmodifiable().addAll(avalancheChartID);
    }

}
