package sample.managers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Gost;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AvalancheManager {
    private static Parent root;
    String fileName;

    Gost feistelСipher;

    @FXML
    LineChart<String, Double> avalancheChartID;
    LineChart<String, Double> avalancheChartID1;
    LineChart<String, Double> avalancheChartID2;
    LineChart<String, Double> avalancheChartID3;
    LineChart<String, Double> avalancheChartID4;
    LineChart<String, Double> avalancheChartID5;
    LineChart<String, Double> avalancheChartID6;
    LineChart<String, Double> avalancheChartID7;
    LineChart<String, Double> avalancheChartID8;
    LineChart<String, Double> avalancheChartID9;
    LineChart<String, Double> avalancheChartID10;
    LineChart<String, Double> avalancheChartID11;
    TextField inputBlockSizeID;
    TextField inputRoundsID;
    Label changesCountLabel;
    Label roundsLabel;


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
        avalancheChartID3=(LineChart) root.lookup("#avalancheChartID3");
        avalancheChartID4=(LineChart) root.lookup("#avalancheChartID4");
        avalancheChartID5=(LineChart) root.lookup("#avalancheChartID5");
        avalancheChartID6=(LineChart) root.lookup("#avalancheChartID6");
        avalancheChartID7=(LineChart) root.lookup("#avalancheChartID7");
        avalancheChartID8=(LineChart) root.lookup("#avalancheChartID8");
        avalancheChartID9=(LineChart) root.lookup("#avalancheChartID9");
        avalancheChartID10=(LineChart) root.lookup("#avalancheChartID10");
        avalancheChartID11=(LineChart) root.lookup("#avalancheChartID11");
        inputRoundsID=(TextField) root.lookup("#inputRoundsID");
        inputBlockSizeID=(TextField) root.lookup("#inputBlockSizeID");
        changesCountLabel=(Label) root.lookup("#changesCountLabel");
        roundsLabel=(Label) root.lookup("#roundsLabel");
    }

    @FXML
    public void onBuildChart(){
        String info=feistelСipher.getInfoFromFile(fileName);
        System.out.println(info);
        //gost.setBlockSize(Integer.parseInt(inputBlockSizeID.getText()));
        //gost.setRounds(Integer.parseInt(inputRoundsID.getText()));
        String[] blockInfo=feistelСipher.getBlockInfoBin(info);
        String[][] blocksLR=new String[blockInfo.length][2];
        for (int i=0;i<blocksLR.length;i++){
            blocksLR[i]=feistelСipher.getLeftRightFromBlock(blockInfo[i]);
        }

        String fName="key.txt";
        String key="";
        try (BufferedReader br = new BufferedReader(new FileReader(fName))) {
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
        subKeys=feistelСipher.getSubKeyFirst(keyBin);
        int[][] count11=feistelСipher.countChangedBits(blocksLR[0],subKeys,1);
        int[][] count12=feistelСipher.countChangedBits(blocksLR[0],subKeys,2);

        /*
        subKeys=gost.getSubKeySecond(keyBin);
        */
        int[][] count21=feistelСipher.countChangedBits(blocksLR[0],subKeys,1);
        int[][] count22=feistelСipher.countChangedBits(blocksLR[0],subKeys,2);


        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID.setTitle("Фун 1, Ключ 1, измененный бит в тексте");
        avalancheChartID.setStyle("-fx-font-size: " + 7 + "px;");
        XYChart.Series<String, Double> series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[0][i]));
        }

        avalancheChartID.getData().add(series);
        avalancheChartID.setVisible(true);

        //2 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID1.setTitle("Фун 1, Ключ 1, измененный бит в ключе");
        avalancheChartID1.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[1][i]));
        }

        avalancheChartID1.getData().add(series);
        avalancheChartID1.setVisible(true);


        //3 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID2.setTitle("Фун 1, Ключ 1, измененный бит и в ключе и в тексте");
        avalancheChartID2.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count11[2][i]));
        }

        avalancheChartID2.getData().add(series);
        avalancheChartID2.setVisible(true);


        //4 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID3.setTitle("Фун 2, Ключ 1, измененный бит в тексте");
        avalancheChartID3.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count12[0][i]));
        }

        avalancheChartID3.getData().add(series);
        avalancheChartID3.setVisible(true);


        //5 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID4.setTitle("Фун 2, Ключ 1, измененный бит в ключе");
        avalancheChartID4.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count12[1][i]));
        }

        avalancheChartID4.getData().add(series);
        avalancheChartID4.setVisible(true);



        //6 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID5.setTitle("Фун 2, Ключ 1, измененный бит в ключе и в тексте");
        avalancheChartID5.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count12[2][i]));
        }

        avalancheChartID5.getData().add(series);
        avalancheChartID5.setVisible(true);



        //7 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID6.setTitle("Фун 1, Ключ 2, изм бит в тексте");
        avalancheChartID6.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count21[0][i]));
        }

        avalancheChartID6.getData().add(series);
        avalancheChartID6.setVisible(true);


        //8 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID7.setTitle("Фун 1, Ключ 2, изм бит в ключе");
        avalancheChartID7.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count21[1][i]));
        }

        avalancheChartID7.getData().add(series);
        avalancheChartID7.setVisible(true);



        //9 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID8.setTitle("Фун 1, Ключ 2, изм бит в ключе и тексте");
        avalancheChartID8.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count21[2][i]));
        }

        avalancheChartID8.getData().add(series);
        avalancheChartID8.setVisible(true);

        //10 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID9.setTitle("Фун 2, Ключ 2, изм бит в тексте");
        avalancheChartID9.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count22[0][i]));
        }

        avalancheChartID9.getData().add(series);
        avalancheChartID9.setVisible(true);

        //11 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID10.setTitle("Фун 2, Ключ 2, изм бит в ключе");
        avalancheChartID10.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count22[1][i]));
        }

        avalancheChartID10.getData().add(series);
        avalancheChartID10.setVisible(true);

        //12 graph
        x = new NumberAxis();
        y = new NumberAxis();

        //avalancheChartID;
        avalancheChartID11.setTitle("Фун 2, Ключ 2, изм бит в ключе и тексте");
        avalancheChartID11.setStyle("-fx-font-size: " + 7 + "px;");
        series  = new XYChart.Series<String, Double>();
        for(int i=0; i<feistelСipher.getRounds(); i++){
            series.getData().add(new XYChart.Data<String, Double>(""+i, (double)count22[2][i]));
        }

        avalancheChartID11.getData().add(series);
        avalancheChartID11.setVisible(true);

    }

    @FXML
    public void onChooseFile(){
        feistelСipher=new Gost();
        fileName=feistelСipher.getFile();
        //gost.setRounds(3);
        //gost.setBlockSize(8);
        //gost.test();
        String info=feistelСipher.getInfoFromFile(fileName);
    }

}
