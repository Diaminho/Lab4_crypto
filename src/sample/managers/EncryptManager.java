package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import sample.Gost;
import sample.Scrambler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EncryptManager {
    private static Parent root;
    Gost gost;
    Scrambler scr;
    String[] encString;
    private String fileName;
    private int key;
    private int[] keyBin;

    @FXML
    Button chooseFileButton;
    TextArea originTextID;
    TextArea encryptedTextID;


    public String getFileName() {
        return fileName;
    }

    public EncryptManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        chooseFileButton=(Button) root.lookup("#chooseFileButton");
        originTextID=(TextArea) root.lookup("#originTextID");
        encryptedTextID=(TextArea) root.lookup("#encryptedTextID");

    }

    @FXML
    public void onChooseFile(){
        gost =new Gost();
        fileName= gost.getFile();
        //gost.setRounds(3);
        //gost.setBlockSize(16);
        //gost.test();
        String info= gost.getInfoFromFile(fileName);
        originTextID.setText(info);
        //System.out.println("info "+info);
    }


    @FXML
    public void onChooseKeyFile(){
        String fName= gost.getFile();
        String key="";
        try (BufferedReader br = new BufferedReader(new FileReader(fName))) {
            //чтение построчно
            key=br.readLine();
        }
        catch (IOException e){
            System.out.println(e);
        }
        keyBin=new int[key.length()];
        for (int i=0;i<keyBin.length;i++){
            keyBin[i]=Integer.parseInt(key.charAt(i)+"");
        }
        ///
    }

    @FXML
    public void onSaveEncrTextButton(){

        //System.out.println(fileName);

    }

    public void saveInfo(){
        Gost.saveInfoToFile(fileName, encString);
    }



    @FXML
    public void onEncryptButton(){
        String info= gost.getInfoFromFile(fileName);
        //info=gost.fillText(info);
        System.out.println(info);

        //originTextID.setText(info);
        //gost.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        //gost.setRounds(Integer.parseInt(inputRoundCountID.getText()));
        String[] blockInfo= gost.getBlockInfoBin(info);
        //gost.countChangedBits(13,blockInfo[0]);

        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]= gost.getLeftRightFromBlock(blockInfo[i]);

        }

        int[][] subKeys;
        subKeys= gost.getSubKeyFirst(keyBin);

        String[][] encryptedNumbersLR=new String[blocksLR.length][2];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            //System.out.println("blocksLR"+ blocksLR[i][1]);
            encryptedNumbersLR[i]= gost.doEncrypt(blocksLR[i], subKeys, 1,false,false);
        }

        System.out.println("зашифр");
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            System.out.println("BLOCK: "+i+" L: "+encryptedNumbersLR[i][0]+" R: "+encryptedNumbersLR[i][1]);
        }

        String[] encrBinStr=new String[encryptedNumbersLR.length];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            encrBinStr[i]= gost.asBitString(encryptedNumbersLR[i][0],16* gost.getBlockSize()/2)+ gost.asBitString(encryptedNumbersLR[i][1],16* gost.getBlockSize()/2);
            //System.out.println(i+" len "+encrBinStr[i]);
        }

        encString =new String[encrBinStr.length];
        String encrText="";
        for (int i = 0; i< encString.length; i++) {
            encString[i]= gost.getStringFromBinary(encrBinStr[i]);
            encrText+= encString[i];
            //System.out.print(encString[i]);
        }

        //System.out.println(encrText);
        encryptedTextID.setText(encrText);

    }


    public  void setFileName(String text){
        fileName=text;
    }


    @FXML
    public void onMenuButton(){

    }


}
