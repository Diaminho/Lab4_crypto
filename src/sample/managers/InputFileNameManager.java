package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

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
    public String onOkSaveButton() {
        return encrFileNameID.getText();

    }

    @FXML
    public void onCancelButton() {

    }


    }
