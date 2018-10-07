package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import sample.FeistelСipher;

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
        String fileName=feistelСipher.getFile();
        feistelСipher.setRounds(3);
        feistelСipher.setBlockSize(8);
        feistelСipher.test();
        String info=feistelСipher.getInfoFromFile(fileName);
        //System.out.println("info "+info);
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        for(int i=0;i<blockInfo.length;i++){
            System.out.println("BLOCK " + i+" "+blockInfo[i]+" length "+blockInfo[i].length());
        }

        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        for (int i=0;i<blocksLR.length;i++){
            System.out.println("BLOCK: "+i);
            for (int j=0;j<blocksLR[i].length;j++){
                System.out.print(blocksLR[i][j]+" | ");
            }
            System.out.println();
        }

        Long [][] numbersLR=new Long[blocksLR.length][2];
        for (int i=0;i<numbersLR.length;i++){
            numbersLR[i]=feistelСipher.getNumberFromBlockLR(blocksLR[i]);
        }

        for (int i=0;i<numbersLR.length;i++){
            System.out.println("Block: "+i);
            for(int j=0;j<numbersLR[i].length;j++){
                System.out.print(numbersLR[i][j]+" ");
            }
        }

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
            encrBinStr[i]=feistelСipher.asBitString(Long.toBinaryString(encryptedNumbersLR[i][0]),11*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(Long.toBinaryString(encryptedNumbersLR[i][1]),11*feistelСipher.getBlockSize()/2);
            System.out.println(encrBinStr[i]);
        }

        String[] encStr=new String[encrBinStr.length];
        for (int i=0;i<encStr.length;i++) {
            encStr[i]=feistelСipher.getStringFromBinary(encrBinStr[i]);
            System.out.print(encStr[i]);
        }

        //ДЕШИФРОВАНИЕ
        System.out.println("ДЕШИФРОВАНИЕ");
        Long[][] decryptedNumbersLR=new Long[encryptedNumbersLR.length][2];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decryptedNumbersLR[i]=feistelСipher.doFeist(encryptedNumbersLR[i], true);
        }

        String[] decrBinStr=new String[decryptedNumbersLR.length];
        for (int i=0;i<decryptedNumbersLR.length;i++) {
            decrBinStr[i]=feistelСipher.asBitString(Long.toBinaryString(decryptedNumbersLR[i][0]),11*feistelСipher.getBlockSize()/2)+feistelСipher.asBitString(Long.toBinaryString(decryptedNumbersLR[i][1]),11*feistelСipher.getBlockSize()/2);
            System.out.println(encrBinStr[i]);
        }

        String[] decStr=new String[decrBinStr.length];
        for (int i=0;i<decStr.length;i++) {
            decStr[i]=feistelСipher.getStringFromBinary(decrBinStr[i]);
            System.out.print(decStr[i]);
        }



    }

}
