package sample;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FeistelСipher {
    private int rounds;
    private int blockSize;

    public FeistelСipher(){

    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public String getFile(){
        JFileChooser fileopen = new JFileChooser();
        int ret = fileopen.showDialog(null, "Открыть файл");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fileopen.getSelectedFile();
            String fileName=file.getAbsolutePath();
            System.out.println("Имя файла: "+fileName);
            //file.;
            return fileName;
        }
        else return "None";
    }

    public Long[] doFeist(Long[] a, boolean reverse)
    {
        int round = reverse? rounds: 1;
        Long[] res=new Long[2];
        res[0] = a[0];
        res[1] = a[1];
        for (int i = 0; i < rounds; i++)
        {
            if (i < rounds-1) // если не последний раунд
            {
                Long t = res[0];
                res[0] = res[1] ^ func(res[0], round);
                res[1] = t;
            }
            else // последний раунд
            {
                res[1] = res[1] ^ func(res[0], round);
            }
            round += reverse? -1: 1;
        }
        return res;
    }


    private Long func(Long b, int k) {
        return b + k;
    }


    public void test() {
        Long[] a = new Long[2];
        a[0] = Long.valueOf(10000000);
        a[1] = Long.valueOf(200);
        doFeist(a, false);
        System.out.println("a0: "+a[0]+" a1: "+a[1]);
        doFeist(a, true);
        System.out.println("a0: "+a[0]+" a1: "+a[1]);
    }


    public String getInfoFromFile(String filePath){
        String tmpStr;
        String infoString="";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //чтение построчно
            while ((tmpStr = br.readLine()) != null) {
                infoString += tmpStr+"\n";
            }
            System.out.println("Исходный текст: "+infoString);
            return infoString;
            //System.out.println((int)'\n');
        }
        catch (IOException e){
            System.out.println(e);
            return "-1";
        }

    }

    public String[] getBlockInfoBin(String info){
        int blockCount=(int)Math.ceil((double)info.length()/blockSize);
        //System.out.println(info.length());
        String[] blockInfo=new String[blockCount];
        for (int i=0;i<blockInfo.length;i++){
            blockInfo[i]="";
        }
        int k=0;
        for (int i=0;i<info.length();i++){
            if (i==(k+1)*blockSize){
                k++;
            }
            blockInfo[k]+=info.charAt(i);
            //System.out.println(blockInfo[k]);
        }

        String[] blockInfoBin=new String[blockInfo.length];

        for (int i=0;i<blockInfoBin.length;i++){
            blockInfoBin[i]="";
        }

        for (int i=0;i<blockInfoBin.length;i++){
            for (int j=0;j<blockSize;j++) {
                //System.out.println(blockInfoBin[i].length());
                if(j>=blockInfo[i].length()){
                    blockInfoBin[i]+="11111111111";
                }
                else {
                    blockInfoBin[i]+= asBitString(Integer.toBinaryString((int) blockInfo[i].charAt(j)), 11);
                }
                //System.out.println(blockInfoBin[i].length());
            }
        }

        return blockInfoBin;
    }

    public String[] getLeftRightFromBlock(String block){
        String[] blockLR=new String[2];
        for (int i=0;i<blockLR.length;i++){
            blockLR[i]=block.substring(i*block.length()/2,(i+1)*block.length()/2);
        }
        return blockLR;
    }

    public Long[] getNumberFromBlockLR(String[] blockLR){
        Long[] numberLR=new Long[2];
        for (int i=0;i<numberLR.length;i++) {
            numberLR[i]=Long.parseLong(blockLR[i],2);
        }
        return numberLR;
    }

    public String asBitString(String value, int stringSize) {
        String str="";
        for (int i = 0; i < stringSize-value.length(); i++) {
            str+="0";
        }

        //System.out.println("ASBIT length: "+(str+value));
        return str+value;
    }


    public String getStringFromBinary(String binStr){
        String strText="";
        //System.out.println(binStr.length());
        for (int i=0;i<binStr.length();i+=11){
            strText+=(char)Integer.parseInt(binStr.substring(i,i+11),2);
        }
        return strText;

    }
}
