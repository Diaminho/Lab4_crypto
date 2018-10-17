package sample;

import java.io.CharArrayReader;
import java.util.Arrays;

public class Scrambler {
    private int[] key;
    private int[] polynom;
    private int size;
    private int initValue;
    private int[] initValueBin;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[] getInitValueBin() {
        return initValueBin;
    }

    public void setInitValueBin(int[] initValueBin) {
        this.initValueBin = initValueBin;
    }

    public int getInitValue() {
        return initValue;
    }

    public void setInitValue(int initValue) {
        this.initValue = initValue;
        initValueBin=asBitString(initValue,polynom.length);
    }

    public int[] getKey() {
        return key;
    }

    public void setKey(int[] key) {
        this.key = key;
    }

    public Scrambler(){

    }

    public Scrambler(int initValue, int[] polynom, int size){
        setPolynom(polynom);
        setInitValue(initValue);
        setSize(size);

    }

    public int[] getPolynom() {
        return polynom;
    }

    public void setPolynom(int[] polynom) {
        this.polynom = polynom;
    }


    public int[] generateKey(){
        //int size=256;
        key=new int[size];
        for (int i=0;i<size;i++){
            key[i]=LFSR();
        }
        return key;
    }

    public int[] asBitString(int value, int arrSize) {
        int[] res=new int[arrSize];
        String strBinValue= Integer.toBinaryString(value);
        int i=0;
        for (; i < arrSize-strBinValue.length(); i++) {
            res[i]=0;
        }
        for (int k=0; i < arrSize; i++, k++) {
            res[i]=Integer.parseInt(strBinValue.charAt(k)+"");
        }


        //System.out.println("ASBIT length: "+(str+value));
        return res;
    }


    public int LFSR(){
        int bit0=0;
        for(int i=0;i<polynom.length;i++){
            if (polynom[i]==1){
                bit0=bit0^initValueBin[i];
            }
        }
        //CYCLIC SHIFT
        int[] veryTmp=Arrays.copyOfRange(initValueBin,0,initValueBin.length-1);
        int lastValue = initValueBin[initValueBin.length-1];
        System.arraycopy(veryTmp, 0, initValueBin, 1, initValueBin.length-1);
        initValueBin[0]=bit0;
        //
        return initValueBin[initValueBin.length-1];
    }
}
