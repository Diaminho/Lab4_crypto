package sample;

import javax.swing.*;
import java.io.*;
import java.math.BigInteger;

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
                res[0]=res[1] ^ func(res[0], round);
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

    /*
    public String[] doFeist(String[] a, boolean reverse)
    {
        int round = reverse? rounds: 1;
        String[] res=new String[2];
        res[0] = a[0];
        res[1] = a[1];
        for (int i = 0; i < rounds; i++)
        {
            if (i < rounds-1) // если не последний раунд
            {
                String t = res[0];
                res[0]=Long.toBinaryString(Long.parseLong(res[1],2) ^ func(Long.parseLong(res[0],2), round));
                res[0]=asBitString(res[0],16*blockSize/2);
                res[1] = t;
                res[1]=asBitString(res[1],16*blockSize/2);
            }
            else // последний раунд
            {
                res[1] = Long.toBinaryString(Long.parseLong(res[1],2) ^ func(Long.parseLong(res[0],2), round));
                res[1]=asBitString(res[1],16*blockSize/2);
            }
            round += reverse? -1: 1;
        }
        return res;
    }
    */

    private Long func(Long b, int k) {
        return b ^ k;
    }

/*
    public void test() {
        Long[] a = new Long[2];
        a[0] = Long.valueOf(10000000);
        a[1] = Long.valueOf(200);
        doFeist(a, false);
        System.out.println("a0: "+a[0]+" a1: "+a[1]);
        doFeist(a, true);
        System.out.println("a0: "+a[0]+" a1: "+a[1]);
    }*/


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

    public static void saveInfoToFile(String filePath, String[] info){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for(int i=0;i<info.length;i++){
                bw.write(info[i]);
            }

            //System.out.println("Исходный текст: "+infoString);
            //System.out.println((int)'\n');
        }
        catch (IOException e){
            System.out.println(e);
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
                    blockInfoBin[i]+="0000000000101011";
                }
                else {
                    blockInfoBin[i]+= asBitString(Integer.toBinaryString((int) blockInfo[i].charAt(j)), 16);
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
        for (int i=0;i<binStr.length();i+=16){
            strText+=(char)Integer.parseInt(binStr.substring(i,i+16),2);
        }
        return strText;

    }

    public int countChangedBits(int position, String block){
        int count=0;
        int round = 0;
        StringBuilder tmp_str=new StringBuilder(block);
        String[] unchanged=getLeftRightFromBlock(tmp_str.toString());

        if (tmp_str.charAt(position-1)=='0'){
            tmp_str.setCharAt(position-1,'1');
        }
        else {
            tmp_str.setCharAt(position-1,'0');
        }
        String[] changed=getLeftRightFromBlock(tmp_str.toString());

        Long[] unch=getNumberFromBlockLR(unchanged);
        Long[] ch=getNumberFromBlockLR(changed);


        System.out.println("Место изменения: "+(position-1));
        System.out.println("Исходное число: "+unch[0]+" "+unch[1]);
        System.out.println("Измененн число: "+ch[0]+" "+ch[1]);

        String unchStr;
        String chStr;

        for (int i = 0; i < rounds; i++)
        {
            if (i < rounds-1) // если не последний раунд
            {
                Long tU = unch[0];
                unch[0]= unch[1]^func(unch[0],round);
                unch[1] = tU;

                Long tC = ch[0];
                ch[0]= ch[1]^func(ch[0],round);
                ch[1] = tC;


            }
            else // последний раунд
            {
                ch[1]= ch[1]^func(ch[0],round);
                unch[1]= unch[1]^func(unch[0],round);
                //res[1] = res[1] ^ func(res[0], round);

            }

            unchStr=asBitString(Long.toBinaryString(unch[0]),16*blockSize/2)+asBitString(Long.toBinaryString(unch[1]),16*blockSize/2);
            chStr=asBitString(Long.toBinaryString(ch[0]),16*blockSize/2)+asBitString(Long.toBinaryString(ch[1]),16*blockSize/2);


            for (int ii=0;ii<unchStr.length();ii++){
                if (unchStr.charAt(ii)!=chStr.charAt(ii)) {
                    count++;
                }
            }


            System.out.println("Раунд: "+round);
            System.out.println("Количество изменений: "+count);

            round += 1;
        }


        //System.out.println("зашифрованная исходная строка: "+resUn[0]+resUn[1]);
        //System.out.println("зашифрованная измененн строка: "+resCh[0]+resCh[1]);

        //System.out.println("зашифрованная исходная строка: "+Long.parseLong(resUn[0])+" "+Long.parseLong(resUn[1]));


        return count;
    }


}
