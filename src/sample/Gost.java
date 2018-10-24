package sample;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;


public class Gost {
    private static int rounds=32;
    private static int blockSize=4;
    private int[][] s=new int[8][];

    void setS(){
        s[0]=new int[] {4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3};
        s[1] =new int[] {14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9};
        s[2] =new int[] {5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11};
        s[3] =new int[] {7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3};
        s[4] =new int[] {6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2};
        s[5] =new int[] {4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14};
        s[6] =new int[] {13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12};
        s[7] =new int[] {1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12};
    }


    public Gost(){ setS();}

    public int getBlockSize() {
        return blockSize;
    }


    /*public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }*/

    public int getRounds() {
        return rounds;
    }

    /*public void setRounds(int rounds) {
        this.rounds = rounds;
    }*/

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

    public String[] doEncrypt(String[] a, int[][] subKeys, int funcType, boolean reverse, boolean debug) {
        int[][] a_int=new int[2][16*blockSize/2];
        a_int[0]=binStrToIntArray(a[0]);
        a_int[1]=binStrToIntArray(a[1]);

        String[] deb=new String[rounds];

        String func_res;
        int round = reverse? rounds: 1;
        int[] t;
        for (int i = 0; i < rounds; i++) {

            if (round < 25) // если не последний раунд
            {
                func_res = asBitString(func(asBitString(intArrayToBinStr(a_int[1]), 16 * blockSize / 2), subKeys[(round-1)%subKeys.length]), 16 * blockSize / 2);

            }
            else {
                func_res = asBitString(func(asBitString(intArrayToBinStr(a_int[1]), 16 * blockSize / 2), subKeys[subKeys.length-1-(round)%subKeys.length]), 16 * blockSize / 2);
            }
            t = Arrays.copyOf(a_int[1], a_int[1].length);
            int [] func_resI=binStrToIntArray(func_res);
            //int[] func_resI=Arrays.copyOf(key,);

            for (int ii=0;ii<a_int[1].length;ii++){
                //a_int[0][ii]=a_int[1][ii]^key[(i*32+ii)%key.length];
                a_int[1][ii]=a_int[0][ii]^func_resI[ii];
            }

            a_int[0] = Arrays.copyOf(t, t.length);

            if(debug==true){
                deb[i]= intArrayToBinStr(a_int[0])+intArrayToBinStr(a_int[1]);
            }

            round += reverse ? -1: 1;
        }

        String[] resStr=new String[2];
        resStr[0]=intArrayToBinStr(a_int[1]);
        resStr[1]=intArrayToBinStr(a_int[0]);

        if (debug==true){
            return deb;
        }
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


    public int[] getSBlockFromStr(String str){
        int[] res=new int[8];
        //System.out.println(str.length());
        for (int i=0;i<res.length;i++){
            res[i]=Integer.parseInt(str.substring(i*4,(i+1)*4),2);
            res[i]=s[i][res[i]];
        }
        return res;
    }

    public String getStrFromBlockInt(int[] Sblock){
        StringBuffer res=new StringBuffer();
        for (int i=0;i<Sblock.length;i++){
            res.append(asBitString(Integer.toBinaryString(Sblock[i]),4));
        }
        return res.toString();
    }


    public String func(String b, int[] subKey) {
        String subKeyStr=intArrayToBinStr(subKey);
        // Ri-1+SubKey i mod 2^32)
        Long Ri=Long.parseLong(b,2);
        Long subKeyL=Long.parseLong(subKeyStr,2);
        Long RiSubKey=(Ri+subKeyL)%(long)Math.pow(2,32);
        //
        int[] bInt=getSBlockFromStr(asBitString(Long.toBinaryString(RiSubKey),32));
        String res=getStrFromBlockInt(bInt);
        res=getShift(res,11);
        return res;
    }

    public String getShift(String str,int shift){
        StringBuffer res=new StringBuffer();
        res.append(str.substring(shift));
        res.append(str.substring(0,shift));
        return res.toString();
    }

    public int[][] getSubKeyFirst(int[] key) {
        int[][] subKey=new int[8][32];
        for (int i=0;i<subKey.length;i++){
            for (int j=0;j<subKey[i].length;j++){
                subKey[i][j]=key[i*subKey[i].length+j];
            }
        }
        return subKey;
    }

    public String getInfoFromFile(String filePath){
        String tmpStr;
        String infoString="";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            //чтение построчно
            while ((tmpStr = br.readLine()) != null) {
                infoString += tmpStr+'\n';
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

    public int[][] countChangedBits(String[] block,int[][] subKeys, int funcType){
        int[][] count=new int[3][rounds];
        Arrays.fill(count[0],0);
        Arrays.fill(count[1],0);
        Arrays.fill(count[2],0);
        String[] blockCh=new String[2];
        blockCh[0]=String.valueOf(block[0]);
        char z=(block[1].charAt(0)=='0') ? '1' : '0';
        blockCh[1]= z+ block[1].substring(1);
        //String.valueOf(block[1]);
        int[][] subKeysCh=new int[subKeys.length][subKeys[0].length];
        for (int i=0;i<subKeysCh.length;i++) {
            for (int j=0;j<subKeysCh[i].length;j++) {
                subKeysCh[i][j] = subKeys[i][j];
            }
        }
        System.out.println(subKeysCh[0][0]);

        if(subKeysCh[0][subKeysCh[0].length-1]==0){
            subKeysCh[0][subKeysCh[0].length-1]=1;
        }
        else {
            subKeysCh[0][subKeysCh[0].length-1]=0;
        }


        // funcType1, keyType1, changed bit at block
        //for (int i=0)
        String[] unchF= doEncrypt(block,subKeys,funcType,true,true);
        String[] uncFBlockChanged= doEncrypt(blockCh,subKeys,funcType,true, true);
        String[] uncFKeyChanged= doEncrypt(block,subKeysCh,funcType,true, true);
        String[] uncFKeyChangedBlockChanged= doEncrypt(blockCh,subKeysCh,funcType,true, true);


        for (int i=0;i<unchF.length;i++){
            if (i>0){
                count[0][i]=count[0][i-1];
                count[1][i]=count[0][i-1];
                count[2][i]=count[0][i-1];
            }
            for (int j=0;j<unchF[i].length();j++){
                if (uncFBlockChanged[i].charAt(j)!=unchF[i].charAt(j)){
                    count[0][i]++;
                }
                if (uncFKeyChanged[i].charAt(j)!=unchF[i].charAt(j)){
                    count[1][i]++;
                }
                if (uncFKeyChangedBlockChanged[i].charAt(j)!=unchF[i].charAt(j)){
                    count[2][i]++;
                }
            }
        }
        return count;
    }


    public String deleteSymbol(String str, char symbol) {
        return str.substring(0,str.indexOf(symbol));
    }

}
