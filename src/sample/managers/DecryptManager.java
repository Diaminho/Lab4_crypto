package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.FeistelСipher;
import sample.controllers.MainController;

public class DecryptManager {
    private static Parent root;
    
    FeistelСipher feistelСipher;
    String[] decrAStr;

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
        feistelСipher=new FeistelСipher();
        String fileName=feistelСipher.getFile();
        feistelСipher.setRounds(3);
        feistelСipher.setBlockSize(8);
        //feistelСipher.test();
        String info=feistelСipher.getInfoFromFile(fileName);
        encodedTextID.setText(info);
        //System.out.println("info "+info);
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        Long [][] numbersLR=new Long[blocksLR.length][2];
        for (int i=0;i<numbersLR.length;i++){
            numbersLR[i]=feistelСipher.getNumberFromBlockLR(blocksLR[i]);
        }

        //System.out.println("1111112222222");


        Long[][] decryptedNumbersLR=new Long[numbersLR.length][2];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decryptedNumbersLR[i]=feistelСipher.doFeist(numbersLR[i], true);
        }


        String[] decrBinStr=new String[decryptedNumbersLR.length];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decrBinStr[i]=feistelСipher.asBitString(Long.toBinaryString(decryptedNumbersLR[i][0]),16*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(Long.toBinaryString(decryptedNumbersLR[i][1]),16*feistelСipher.getBlockSize()/2);
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

}
