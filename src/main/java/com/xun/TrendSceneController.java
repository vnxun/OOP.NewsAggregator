package com.xun;


import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class TrendSceneController extends MenuController implements Initializable{
    private LocalDate startDate = LocalDate.parse("2010-01-01"), endDate = LocalDate.now();
    @FXML
    private Stage selectSourcesStage = new Stage();
    @FXML
    private ImageView imageView1, imageView2;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private DatePicker searchDatePickerStart, searchDatePickerEnd;
    @FXML
    private ScrollPane searchScrollPane;

    public void search(ActionEvent ev){
        String query = searchTextField.getText();
        FileWriter writer;
        if (query.length() > 0) {
            try {
                writer = new FileWriter("D:\\java\\oop_news_aggregator_project\\src\\main\\resources\\com\\xun\\Trend_Detection\\trend_query.txt");
                writer.write(query + "," + startDate + "," + endDate);
                writer.close();

                String path1 = getClass().getResource("Trend_Detection/Trend_Detection.py").toURI().toString();
                path1 = path1.substring(6, path1.length());
                String path2 = getClass().getResource("Trend_Detection/BlockchainFrequency.py").toURI().toString();
                path2 = path2.substring(6, path2.length());
                String[] commandArr1 = {"python", path1};
                String[] commandArr2 = {"python", path2};
                Process p = Runtime.getRuntime().exec(commandArr1);
                p.waitFor();
                p = Runtime.getRuntime().exec(commandArr2);
                p.waitFor();
                
                Image i1 = new Image(getClass().getResource("Trend_Detection/frequency_word.png").toURI().toString());
                Image i2 = new Image(getClass().getResource("Trend_Detection/frequency_table.png").toURI().toString());
                imageView1.setImage(i1);
                imageView2.setImage(i2);
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
        }    
    }

    public void keyEventHandler(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER) {
            search(null);
        }
    }

    public void getStartDate(ActionEvent e){
        startDate = searchDatePickerStart.getValue();
        dateCheck();
    }
    public void getEndDate(ActionEvent e){
        endDate = searchDatePickerEnd.getValue();
        dateCheck();
    }
    private void dateCheck(){
        if (endDate.isBefore(startDate)) {
            LocalDate tempDate = endDate;
            endDate = startDate;
            startDate = tempDate;

            searchDatePickerStart.setValue(startDate);
            searchDatePickerEnd.setValue(endDate);
        }
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        searchDatePickerEnd.setValue(endDate);
    }
}
