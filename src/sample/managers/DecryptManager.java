package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import sample.Gost;
import sample.Scrambler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DecryptManager {
    private static Parent root;
    
    Gost gost;
    String[] decrAStr;
    String fileName;
    Scrambler scr;
    private int key;
    private int[] keyBin;

    @FXML
    TextArea encodedTextID;
    TextArea decodedTextID;

    public DecryptManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        decodedTextID=(TextArea) root.lookup("#decodedTextID");
        encodedTextID=(TextArea) root.lookup("#encodedTextID");
    }

    @FXML
    public void onChooseFile(){
        gost =new Gost();
        fileName= gost.getFile();
        //gost.setRounds(3);
        //gost.setBlockSize(8);
        //gost.test();
        String info= gost.getInfoFromFile(fileName);
        encodedTextID.setText(info);
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
    }


    @FXML
    public void onDecryptButton(){
        String info= gost.getInfoFromFile(fileName);
        //gost.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        //gost.setRounds(Integer.parseInt(inputRoundsID.getText()));
        String[] blockInfo= gost.getBlockInfoBin(info);
        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]= gost.getLeftRightFromBlock(blockInfo[i]);
        }

        int[][] subKeys;
        subKeys= gost.getSubKeyFirst(keyBin);

        String[][] decryptedNumbersLR=new String[blocksLR.length][2];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decryptedNumbersLR[i]= gost.doEncrypt(blocksLR[i], subKeys,true,false);
        }


        String[] decrBinStr=new String[decryptedNumbersLR.length];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decrBinStr[i]= gost.asBitString(decryptedNumbersLR[i][0],16* gost.getBlockSize()/2)+ gost.asBitString(decryptedNumbersLR[i][1],16* gost.getBlockSize()/2);
            //System.out.println(decrBinStr[i]);
        }

        decrAStr=new String[decrBinStr.length];
        String decrStr="";
        for (int i=0;i<decrAStr.length;i++) {
            decrAStr[i]= gost.getStringFromBinary(decrBinStr[i]);
            decrStr+=decrAStr[i];
            //System.out.print(encStr[i]);
        }

        System.out.print(decrStr);
        decodedTextID.setText(decrStr);
    }

    @FXML
    public void onSaveButton(){
        Gost.saveInfoToFile("decr.txt",decrAStr);
    }

}
