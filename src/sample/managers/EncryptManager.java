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

        //AAA
        String subK=gost.intArrayToBinStr(subKeys[0]);
        String x=gost.getXi(blocksLR[0],0);
        int[][] a=gost.genA(blocksLR,subKeys);
        System.out.println("AAAA");
        for (int i=0;i<a.length;i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j]+" ");
            }
            System.out.println();
        }

        ///BBB
        int[][] b=gost.genB(blocksLR,subKeys);
        System.out.println("BBBB");
        for (int i=0;i<b.length;i++) {
            for (int j = 0; j < b[i].length; j++) {
                System.out.print(b[i][j]+" ");
            }
            System.out.println();
        }

        /////D1
        double d1=gost.getD1(b,blocksLR.length);
        System.out.println("d1: "+d1);
        ////D2
        double d2=gost.getD2(a);
        System.out.println("d2: "+d2);
        ////D3
        double d3=gost.getD3(b,blocksLR.length);
        System.out.println("d3: "+d3);
        ////D4
        double d4=gost.getD4(b,blocksLR.length);
        System.out.println("d4: "+d4);

        ////

        String[][] encryptedNumbersLR=new String[blocksLR.length][2];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            //System.out.println("blocksLR"+ blocksLR[i][1]);
            encryptedNumbersLR[i]= gost.doEncrypt(blocksLR[i], subKeys, false,false);
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
