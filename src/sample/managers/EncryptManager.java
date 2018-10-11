package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.FeistelСipher;
import sample.controllers.InputFileNameController;

public class EncryptManager {
    private static Parent root;
    FeistelСipher feistelСipher;
    String[] ecnrString;
    private String fileName;

    @FXML
    Button chooseFileButton;
    TextArea originTextID;
    TextArea encryptedTextID;
    TextField inputRoundCountID;
    TextField inputBlockSizeID;


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
        inputRoundCountID=(TextField) root.lookup("#inputRoundCountID");
        inputBlockSizeID=(TextField) root.lookup("#inputBlockSizeID");
    }

    @FXML
    public void onChooseFile(){
        feistelСipher=new FeistelСipher();
        fileName=feistelСipher.getFile();
        //feistelСipher.setRounds(3);
        //feistelСipher.setBlockSize(16);
        //feistelСipher.test();
        String info=feistelСipher.getInfoFromFile(fileName);
        originTextID.setText(info);
        //System.out.println("info "+info);
    }

    @FXML
    public void onSaveEncrTextButton(){

        //System.out.println(fileName);

    }

    public void saveInfo(){
        FeistelСipher.saveInfoToFile(fileName,ecnrString);
    }



    @FXML
    public void onEncryptButton(){
        String info=feistelСipher.getInfoFromFile(fileName);
        //originTextID.setText(info);
        feistelСipher.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        feistelСipher.setRounds(Integer.parseInt(inputRoundCountID.getText()));
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        feistelСipher.countChangedBits(13,blockInfo[0]);

        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        String[][] encryptedNumbersLR=new String[blocksLR.length][2];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            //System.out.println("blocksLR"+ blocksLR[i][1]);
            encryptedNumbersLR[i]=feistelСipher.doFeist(blocksLR[i], false);
        }

        System.out.println("зашифр");
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            System.out.println("BLOCK: "+i+" L: "+encryptedNumbersLR[i][0]+" R: "+encryptedNumbersLR[i][1]);
        }

        String[] encrBinStr=new String[encryptedNumbersLR.length];
        for (int i=0;i<encryptedNumbersLR.length;i++) {
            encrBinStr[i]=feistelСipher.asBitString(encryptedNumbersLR[i][0],16*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(encryptedNumbersLR[i][1],16*feistelСipher.getBlockSize()/2);
            //System.out.println(i+" len "+encrBinStr[i].length());
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


    public  void setFileName(String text){
        fileName=text;
    }


    @FXML
    public void onMenuButton(){

    }


}
