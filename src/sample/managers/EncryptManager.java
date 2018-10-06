package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import sample.FeistelСipher;

import javax.swing.*;
import java.io.File;

public class EncryptManager {
    private static Parent root;
    FeistelСipher feistelСipher;

    @FXML
    Button ChooseFileButton;


    public EncryptManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        ChooseFileButton=(Button) root.lookup("#ChooseFileButton");

    }

    @FXML
    public void onChooseFile(){
        feistelСipher=new FeistelСipher();
        feistelСipher.getFile();

        FeistelСipher feistel=new FeistelСipher();
        feistel.setRounds(3);
        feistel.test();

    }

}
