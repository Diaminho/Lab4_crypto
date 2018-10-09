package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.managers.DecryptManager;
import sample.managers.EncryptManager;

import java.io.IOException;

public class DecryptController {
    private static Parent root;

    private static DecryptManager decryptManager;

    private static Stage primaryStage;


    public DecryptController(Stage primaryStage) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../fxml/decrypt.fxml"));
        this.primaryStage=primaryStage;
        primaryStage.setTitle("Дешифрование");
        decryptManager = new DecryptManager(root);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public DecryptController(){

    }

    @FXML
    public void onChooseFile(){
        decryptManager.onChooseFile();
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
