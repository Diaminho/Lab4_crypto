package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import sample.Gost;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AvalancheManager {
    private static Parent root;
    String fileName;
    String keyFile;

    Gost gost;

    @FXML
    LineChart<String, Double> avalancheChartID;
    LineChart<String, Double> avalancheChartID1;
    LineChart<String, Double> avalancheChartID2;
    Text textID;
    Text keyID;


    @FXML
    NumberAxis x;
    @FXML
    NumberAxis y;


    public AvalancheManager(Parent root){
        this.root=root;
        init();
    }

    private void init(){
        avalancheChartID=(LineChart) root.lookup("#avalancheChartID");
        avalancheChartID1=(LineChart) root.lookup("#avalancheChartID1");
        avalancheChartID2=(LineChart) root.lookup("#avalancheChartID2");
        keyID=(Text) root.lookup("#keyID");
        textID=(Text) root.lookup("#textID");
    }

    @FXML
    public void onBuildChart(){
        String info= gost.getInfoFromFile(fileName);
        System.out.println(info);
        //gost.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        //gost.setRounds(Integer.parseInt(inputRoundsID.getText()));
        String[] blockInfo= gost.getBlockInfoBin(info);
        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]= gost.getLeftRightFromBlock(blockInfo[i]);
        }

        String key="";
        try (BufferedReader br = new BufferedReader(new FileReader(keyFile))) {
            //чтение построчно
            key=br.readLine();
        }
        catch (IOException e){
            System.out.println(e);
        }
        int[] keyBin=new int[key.length()];
        for (int i=0;i<keyBin.length;i++){
            keyBin[i]=Integer.parseInt(key.charAt(i)+"");
        }


        int[][] subKeys;
        subKeys= gost.getSubKeyFirst(keyBin);
        int[][] count11= gost.countChangedBits(blocksLR[0],subKeys);


        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID.setTitle("Измененный бит в тексте");
        avalancheChartID.setStyle("-fx-font-size: " + 10 + "px;");
        XYChart.Series<String, Double> series  = new XYChart.Series<String, Double>();
        for(int i = 0; i< gost.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[0][i]));
        }

        avalancheChartID.getData().add(series);
        avalancheChartID.setCreateSymbols(false);
        avalancheChartID.setVisible(true);

        //2 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID1.setTitle("Измененный бит в ключе");
        avalancheChartID1.setStyle("-fx-font-size: " + 10 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i = 0; i< gost.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[1][i]));
        }

        avalancheChartID1.getData().add(series);
        avalancheChartID1.setCreateSymbols(false);
        avalancheChartID1.setVisible(true);


        //3 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID2.setTitle("Измененный бит и в ключе и в тексте");
        avalancheChartID2.setStyle("-fx-font-size: " + 10 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i = 0; i< gost.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[2][i]));
        }

        avalancheChartID2.getData().add(series);
        avalancheChartID2.setCreateSymbols(false);
        avalancheChartID2.setVisible(true);

    }

    @FXML
    public void onChooseFile(){
        gost =new Gost();
        fileName= gost.getFile();
        String info= gost.getInfoFromFile(fileName);
        String tmp=fileName.substring(fileName.lastIndexOf("/")+1);
        textID.setText(tmp);
        textID.setVisible(true);
    }

    @FXML
    public void onChooseKeyFile(){
        keyFile= gost.getFile();
        String tmp=keyFile.substring(keyFile.lastIndexOf("/")+1);
        keyID.setText(tmp);
        keyID.setVisible(true);
    }

}
