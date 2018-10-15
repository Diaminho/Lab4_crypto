package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.stage.Stage;
import sample.managers.AvalancheManager;

import java.io.IOException;

public class AvalancheController {
    private static Parent root;

    private static AvalancheManager avalancheManager;

    private static Stage primaryStage;

    public AvalancheController(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../fxml/chart.fxml"));
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Исследование лавинного эффекта");
        avalancheManager = new AvalancheManager(root);
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.show();
    }

    public AvalancheController(){

    }

    @FXML
    public void onBuildChart() {
        LineChart lc=avalancheManager.onBuildChart();
        //primaryStage.close();


    }

    @FXML
    public void onChooseFile(){

    }

    @FXML
    public void onGoToMenu(){
        primaryStage.close();
        try {
            new MainController(primaryStage);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
