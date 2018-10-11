package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.FeistelСipher;
import sample.controllers.MainController;

public class DecryptManager {
    private static Parent root;
    
    FeistelСipher feistelСipher;
    String[] decrAStr;
    String fileName;

    @FXML
    TextArea encodedTextID;
    TextArea decodedTextID;
    TextField inputBlockSizeID;
    TextField inputRoundsID;


    public DecryptManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        decodedTextID=(TextArea) root.lookup("#decodedTextID");
        encodedTextID=(TextArea) root.lookup("#encodedTextID");
        inputRoundsID=(TextField) root.lookup("#inputRoundsID");
        inputBlockSizeID=(TextField) root.lookup("#inputBlockSizeID");
    }

    @FXML
    public void onChooseFile(){
        feistelСipher=new FeistelСipher();
        fileName=feistelСipher.getFile();
        //feistelСipher.setRounds(3);
        //feistelСipher.setBlockSize(8);
        //feistelСipher.test();
        String info=feistelСipher.getInfoFromFile(fileName);
        encodedTextID.setText(info);
        //System.out.println("info "+info);

    }

    @FXML
    public void onDecryptButton(){
        feistelСipher.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        feistelСipher.setRounds(Integer.parseInt(inputRoundsID.getText()));
        String info=feistelСipher.getInfoFromFile(fileName);
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        String[][] decryptedNumbersLR=new String[blocksLR.length][2];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decryptedNumbersLR[i]=feistelСipher.doFeist(blocksLR[i], true);
        }


        String[] decrBinStr=new String[decryptedNumbersLR.length];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decrBinStr[i]=feistelСipher.asBitString(decryptedNumbersLR[i][0],16*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(decryptedNumbersLR[i][1],16*feistelСipher.getBlockSize()/2);
            //System.out.println(decrBinStr[i]);
        }

        decrAStr=new String[decrBinStr.length];
        String decrStr="";
        for (int i=0;i<decrAStr.length;i++) {
            decrAStr[i]=feistelСipher.getStringFromBinary(decrBinStr[i]);
            decrStr+=decrAStr[i];
            //System.out.print(encStr[i]);
        }

        decodedTextID.setText(decrStr);
    }

    @FXML
    public void onSaveButton(){
        FeistelСipher.saveInfoToFile("decr.txt",decrAStr);
    }

}
