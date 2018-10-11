package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.managers.EncryptManager;
import sample.managers.InputFileNameManager;

import java.io.IOException;

public class InputFileNameController {
    private static InputFileNameManager inputFileNameManager;
    private static Stage primaryStage;
    private static Parent root;
    private static EncryptController encryptController;


    public InputFileNameController(Stage primaryStage, EncryptController encryptController) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../fxml/inputFileName.fxml"));
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Сохрнанение зашифрованной информации");
        inputFileNameManager = new InputFileNameManager(root);
        primaryStage.setScene(new Scene(root));
        this.encryptController=encryptController;
        primaryStage.show();
    }

    public InputFileNameController(){

    }

    @FXML
    public void onOkSaveButton() {
        encryptController.getEncryptManager().setFileName(inputFileNameManager.onOkSaveButton());
        encryptController.getEncryptManager().saveInfo();
        System.out.println(encryptController.getEncryptManager().getFileName());
        primaryStage.close();
    }

    @FXML
    public void onCancelButton() {
        inputFileNameManager.onCancelButton();
        primaryStage.close();
    }
}
