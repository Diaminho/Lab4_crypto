package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.Scrambler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;


public class GenerateKeyManager {
    private static Parent root;
    private Scrambler scr;

    @FXML
    TextField keySizeID;
    TextArea keyAreaID;
    TextField initValueID;

    public GenerateKeyManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        keySizeID=(TextField) root.lookup("#keySizeID");
        keyAreaID=(TextArea) root.lookup("#keyAreaID");
        initValueID=(TextField) root.lookup("#initValueID");
    }

    @FXML
    public void onGenerateButton(){
        int[] poly=new int[16];
        Arrays.fill(poly,0);
        poly[1]=1;
        poly[14]=1;
        poly[15]=1;
        scr=new Scrambler(Integer.parseInt(initValueID.getText()),poly,Integer.parseInt(keySizeID.getText()));
        scr.generateKey();
        keyAreaID.setText(Arrays.toString(scr.getKey()));
    }

    @FXML
    public void onMenuButton(){

    }

    @FXML
    public void onSaveButton(){
        String fileName="./key.txt";
        StringBuffer keyStr=new StringBuffer();
        int[] key=scr.getKey();
        for (int i=0;i<key.length;i++){
            keyStr.append(key[i]);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            //чтение построчно
            System.out.println(keyStr.toString());
            bw.write(keyStr.toString());
        }
        catch (IOException e){
            System.out.println(e);
        }

    }

}
