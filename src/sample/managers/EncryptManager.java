package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.FeistelСipher;
import sample.controllers.InputFileNameController;
import sample.controllers.MainController;

import java.math.BigInteger;

public class EncryptManager {
    private static Parent root;
    FeistelСipher feistelСipher;
    String[] ecnrString;
    private static String fileName;

    @FXML
    Button chooseFileButton;
    TextArea originTextID;
    TextArea encryptedTextID;


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
        feistelСipher=new FeistelСipher();
        String fileName=feistelСipher.getFile();
        feistelСipher.setRounds(3);
        feistelСipher.setBlockSize(8);
        //feistelСipher.test();
        String info=feistelСipher.getInfoFromFile(fileName);
        originTextID.setText(info);
        //System.out.println("info "+info);
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        /*
        for(int i=0;i<blockInfo.length;i++){
            System.out.println("BLOCK " + i+" "+blockInfo[i]+" length "+blockInfo[i].length());
        }
        */

        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        /*
        for (int i=0;i<blocksLR.length;i++){
            System.out.println("BLOCK: "+i);
            for (int j=0;j<blocksLR[i].length;j++){
                System.out.print(blocksLR[i][j]+" | ");
            }
            System.out.println();
        }
        */
        Long[][] numbersLR=new Long[blocksLR.length][2];
        for (int i=0;i<numbersLR.length;i++){
            numbersLR[i]=feistelСipher.getNumberFromBlockLR(blocksLR[i]);
        }

        /*
        for (int i=0;i<numbersLR.length;i++){
            System.out.println("Block: "+i);
            for(int j=0;j<numbersLR[i].length;j++){
                System.out.print(numbersLR[i][j]+" ");
            }
        }
        */

        Long[][] encryptedNumbersLR=new Long[numbersLR.length][2];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            encryptedNumbersLR[i]=feistelСipher.doFeist(numbersLR[i], false);
        }

        System.out.println("зашифр");
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            System.out.println("BLOCK: "+i+" L: "+encryptedNumbersLR[i][0]+" R: "+encryptedNumbersLR[i][1]);
        }

        String[] encrBinStr=new String[encryptedNumbersLR.length];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            encrBinStr[i]=feistelСipher.asBitString(Long.toBinaryString(encryptedNumbersLR[i][0]),16*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(Long.toBinaryString(encryptedNumbersLR[i][1]),16*feistelСipher.getBlockSize()/2);
            System.out.println(i+" len "+encrBinStr[i].length());
        }

        ecnrString=new String[encrBinStr.length];
        String encrText="";
        for (int i=0;i<ecnrString.length;i++) {
            ecnrString[i]=feistelСipher.getStringFromBinary(encrBinStr[i]);
            encrText+=ecnrString[i];
            //System.out.print(encStr[i]);
        }

        encryptedTextID.setText(encrText);

    }

    @FXML
    public void onSaveEncrTextButton(){
        try {
            new InputFileNameController(new Stage());
            //String fileName=inputFileNameController.getFileName();
            //System.out.println(fileName);
        } catch(Exception e) {
            e.printStackTrace();
        }

        //encrFileID.setText(fileName);
        System.out.println(fileName);
        FeistelСipher.saveInfoToFile("text_enc.txt",ecnrString);


    }


    public static void setFileName(String text){
        fileName=text;
    }

    @FXML
    public void onMenuButton(){

    }


}
