package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.managers.GenerateKeyManager;

import java.io.IOException;

public class GenerateKeyController {
    private static Parent root;

    private static GenerateKeyManager generateKeyManagerManager;

    private static Stage primaryStage;

    public GenerateKeyController(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../fxml/keyGen.fxml"));
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Генерация ключа с помощью скремблера");
        generateKeyManagerManager = new GenerateKeyManager(root);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public GenerateKeyController(){

    }

    @FXML
    public void onGenerateButton(){
        generateKeyManagerManager.onGenerateButton();
    }

    @FXML
    public void onSaveButton(){
        generateKeyManagerManager.onSaveButton();
    }

    @FXML
    public void onMenuButton(){
        primaryStage.close();
        try {
            new MainController(primaryStage);
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
