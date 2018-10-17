package sample;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

public class FeistelСipher {
    private int rounds;
    private int blockSize;

    public FeistelСipher(){ }

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

    public String[] doFeist(String[] a, int[][] subKeys, int funcType, boolean reverse) {
        int[][] a_int=new int[2][16*blockSize/2];
        a_int[0]=binStrToIntArray(a[0]);
        a_int[1]=binStrToIntArray(a[1]);


        String func_res;
        int round = reverse? rounds: 1;
        int[] t;
        for (int i = 0; i < rounds; i++)
        {

            if (i < rounds-1) // если не последний раунд
            {
                t = Arrays.copyOf(a_int[0], a_int[0].length);
                if (funcType==1) {
                    func_res = asBitString(func(asBitString(intArrayToBinStr(a_int[0]), 16 * blockSize / 2), subKeys[round - 1]), 16 * blockSize / 2);
                }
                else {
                    func_res = asBitString(funcTwo(asBitString(intArrayToBinStr(a_int[0]), 16 * blockSize / 2), subKeys[round - 1]), 16 * blockSize / 2);
                }
                int [] func_resI=binStrToIntArray(func_res);
                //int[] func_resI=Arrays.copyOf(key,);

                for (int ii=0;ii<a_int[0].length;ii++){
                    //a_int[0][ii]=a_int[1][ii]^key[(i*32+ii)%key.length];
                    a_int[0][ii]=a_int[1][ii]^func_resI[ii];
                }

                a_int[1] = Arrays.copyOf(t, t.length);
            }

            else // последний раунд
            {

                if (funcType==1) {
                    func_res = asBitString(func(asBitString(intArrayToBinStr(a_int[0]), 16 * blockSize / 2), subKeys[round - 1]), 16 * blockSize / 2);
                }
                else {
                    func_res = asBitString(funcTwo(asBitString(intArrayToBinStr(a_int[0]), 16 * blockSize / 2), subKeys[round - 1]), 16 * blockSize / 2);
                }
                int [] func_resI=binStrToIntArray(func_res);

                for (int ii=0;ii<a_int[1].length;ii++){
                    //a_int[1][ii]=a_int[1][ii]^key[(i*32+ii)%key.length];
                    a_int[1][ii]=a_int[1][ii]^func_resI[ii];
                }
            }
            round += reverse? -1: 1;
        }

        String[] resStr=new String[2];
        resStr[0]=intArrayToBinStr(a_int[0]);
        resStr[1]=intArrayToBinStr(a_int[1]);

        return resStr;
    }


    public int[] binStrToIntArray(String binStr){
        int[] res=new int[binStr.length()];
        //System.out.println("binStrToIntArray");
        //System.out.println(binStr);
        for (int i=0;i<res.length;i++){
            res[i]=Integer.parseInt(String.valueOf(binStr.charAt(i)));
            //System.out.print(res[i]);
        }
        //System.out.println();
        //System.out.println("binStrToIntArray");
        return res;
    }


    public String intArrayToBinStr(int[] a){
        StringBuffer res=new StringBuffer();
        //System.out.println("intArrayToBinStr");
        for (int i=0;i<a.length;i++){
            //System.out.println("I ++++"+i);
            //System.out.print(a[i]);
            res.append(Character.forDigit(a[i], 10));
        }
        //System.out.println();
        //System.out.println(res);
        //System.out.println("intArrayToBinStr");
        return res.toString();
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

    public String func(String b, int[] subKey) {
        StringBuilder resStr=new StringBuilder();
        for (int i=0;i<subKey.length;i++){
            resStr.insert(i,subKey[i]);
        }
        System.out.println(resStr);
        return resStr.toString();
    }

    public String funcTwo(String b, int[] subKey) {
        int[] poly=new int[16];
        Arrays.fill(poly,0);
        poly[14]=1;
        poly[15]=1;
        Scrambler scr=new Scrambler(1,poly,32);
        int[] key=scr.generateKey();
        int[] tmp_b=binStrToIntArray(b);
        System.out.println("FUNC TWO "+tmp_b.length);
        //int[] tmp_k=binStrToIntArray(k);
        int[] res=new int[tmp_b.length];
        StringBuilder resStr=new StringBuilder();
        for (int i=0;i<res.length;i++){
            res[i]=tmp_b[i]^key[i%key.length];
            res[i]=subKey[i%subKey.length]^res[i];
            resStr.insert(i,res[i]);
        }

        //System.out.println(resStr);
        return resStr.toString();
    }

    public int[][] getSubKeyFirst(int[] key) {
        int[][] subKey=new int[rounds][32];
        for (int i=0;i<subKey.length;i++){
            for (int j=0;j<subKey[i].length;j++){
                subKey[i][j]=key[(i*subKey[i].length+j)%key.length];
            }
        }
        return subKey;
    }

    public int[][] getSubKeySecond(int[] key) {
        int[] poly=new int[8];
        Arrays.fill(poly,0);
        poly[6]=1;
        poly[7]=1;
        StringBuffer keyStr=new StringBuffer();
        for (int i=0;i<key.length;i++){
            keyStr.append(key[i]);
        }

        int[] initValue=new int[rounds];
        for (int i=0;i<rounds;i++) {
            initValue[i]=Integer.parseInt(keyStr.substring(i, 8),2);
        }
        Scrambler scr=new Scrambler(0,poly,32);
        int[][] subKey=new int[rounds][32];
        for (int i=0;i<subKey.length;i++){
            scr.setInitValue(initValue[i]);
            int[] keyTmp=scr.generateKey();
            for (int j=0;j<subKey[i].length;j++){
                subKey[i][j]=keyTmp[j];
            }
        }
        return subKey;
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
            //System.out.println("Исходный текст: "+infoString);
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
                    blockInfoBin[i]+="1111111111111111";
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


    /*
    public int countChangedBits(int position, String block){
        int count=0;
        //int round = 0;

        StringBuilder tmp_str=new StringBuilder(block);
        String[] unchanged=getLeftRightFromBlock(tmp_str.toString());

        if (tmp_str.charAt(position-1)=='0'){
            tmp_str.setCharAt(position-1,'1');
        }
        else {
            tmp_str.setCharAt(position-1,'0');
        }

        String[] changed=getLeftRightFromBlock(tmp_str.toString());

        int[][] a_int=new int[2][16*blockSize/2];
        a_int[0]=binStrToIntArray(unchanged[0]);
        a_int[1]=binStrToIntArray(unchanged[0]);

        int[][] b_int=new int[2][16*blockSize/2];
        b_int[0]=binStrToIntArray(changed[0]);
        b_int[1]=binStrToIntArray(changed[0]);

        String func_res, func_resB;
        int round = 1;
        int[] t;
        int[] t1;
        for (int i = 0; i < rounds; i++)
        {

            if (i < rounds-1) // если не последний раунд
            {
                t = Arrays.copyOf(a_int[0], a_int[0].length);
                t1= Arrays.copyOf(b_int[0], b_int[0].length);
                func_res=asBitString(func(asBitString(intArrayToBinStr(a_int[0]),16*blockSize/2),asBitString(Integer.toString(round,2),16*blockSize/2)),16*blockSize/2);
                func_resB=asBitString(func(asBitString(intArrayToBinStr(b_int[0]),16*blockSize/2),subKeys[i],16*blockSize/2)),16*blockSize/2);
                int [] func_resI=binStrToIntArray(func_res);
                int [] func_resIB=binStrToIntArray(func_resB);

                for (int ii=0;ii<a_int[0].length;ii++){
                    a_int[0][ii]=a_int[1][ii]^func_resI[ii];
                    b_int[0][ii]=b_int[1][ii]^func_resIB[ii];
                    if (a_int[0][ii]!=b_int[0][ii]){
                        count++;
                    }
                }
                a_int[1] = Arrays.copyOf(t, t.length);
                b_int[1] = Arrays.copyOf(t1, t1.length);

                for (int ii=0;ii<a_int[1].length;ii++){
                    if (a_int[1][ii]!=b_int[1][ii]){
                        count++;
                    }
                }

            }

            else // последний раунд
            {

                func_res=asBitString(func(asBitString(intArrayToBinStr(a_int[0]),16*blockSize/2),asBitString(Integer.toString(round,2),16*blockSize/2)),16*blockSize/2);
                int [] func_resI=binStrToIntArray(func_res);

                func_resB=asBitString(func(asBitString(intArrayToBinStr(b_int[0]),16*blockSize/2),asBitString(Integer.toString(round,2),16*blockSize/2)),16*blockSize/2);
                int [] func_resIB=binStrToIntArray(func_resB);


                for (int ii=0;ii<a_int[1].length;ii++){
                    a_int[1][ii]=a_int[1][ii]^func_resI[ii];
                    b_int[1][ii]=b_int[1][ii]^func_resI[ii];

                    if (a_int[1][ii]!=b_int[1][ii]){
                        count++;
                    }

                }
            }

            System.out.println("Раунд "+i+" Число изменившихся бит"+count);
            round ++;
        }

        return count;
    }*/


    public String deleteSymbol(String str, char symbol) {
        return str.substring(0,str.indexOf(symbol));
    }

}
