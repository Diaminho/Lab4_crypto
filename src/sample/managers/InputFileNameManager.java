package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.controllers.EncryptController;

public class InputFileNameManager {
    private static Parent root;

    @FXML
    TextField encrFileNameID;

    public InputFileNameManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        encrFileNameID=(TextField) root.lookup("#encrFileNameID");
    }

    @FXML
    public void onOkSaveButton() {
        EncryptManager.setFileName(encrFileNameID.getText());

    }

    @FXML
    public void onCancelButton() {

    }
}
