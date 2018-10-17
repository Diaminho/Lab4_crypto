package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.managers.AvalancheManager;

import java.io.IOException;

public class MainController {
    public static Stage primaryStage;

    public MainController(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/menu.fxml"));
        //primaryStage = primaryStage;
        primaryStage.setTitle("Главное меню");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public MainController() {}

    @FXML
    public void onToEncryptButton() {
        primaryStage.close();
        try {
            new EncryptController(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    @FXML
    public void onToDecryptButton() {
        primaryStage.close();
        try {
            new DecryptController(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @FXML
    public void onGenerateKeyButton() {
        primaryStage.close();
        try {
            new GenerateKeyController(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }




    @FXML
    public void onGoToAvalancheButton() {
        primaryStage.close();
        try {
            new AvalancheController(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
