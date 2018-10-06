package sample;

import javax.swing.*;
import java.io.File;

public class FeistelСipher {
    private int rounds;


    public FeistelСipher(){

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

        public void feist(int[] a, boolean reverse)
        {
            int round = reverse? rounds: 1;
            int l = a[0];
            int r = a[1];
            for (int i = 0; i < rounds; i++)
            {
                if (i < rounds-1) // если не последний раунд
                {
                    int t = l;
                    l = r ^ func(l, round);
                    r = t;
                }
                else // последний раунд
                {
                    r = r ^ func(l, round);
                }
                round += reverse? -1: 1;
            }
            a[0] = l;
            a[1] = r;
        }


        private int func(int b, int k)
        {
            return b + k;
        }


        public void test()
        {
            int[] a = new int[2];
            a[0] = 10000000;
            a[1] = 200;
            feist(a, false);
            System.out.println("a0: "+a[0]+" a1: "+a[1]);
            feist(a, true);
            System.out.println("a0: "+a[0]+" a1: "+a[1]);
        }


        public void getBinInfoFromFile(String filePath){
            int[] info=new int[2];


        }

    public int[] binToDec(int[] bin) {
        int[] dec=new int[bin.length];
        for (int i=0;i<dec.length;i++)
            dec[i]=Integer.parseInt(bin[i]+"",2);
        return dec;
    }

    public String asBitString(String value, int stringSize) {
        String str="";
        for (int i = 0; i < stringSize-value.length(); i++) {
            str+='0';
        }
        return str+value;
    }



}
