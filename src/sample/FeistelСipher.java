package sample;

import javax.swing.*;
import java.io.File;

public class FeistelСipher {

    private int round;


    public FeistelСipher(){

    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
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
}
