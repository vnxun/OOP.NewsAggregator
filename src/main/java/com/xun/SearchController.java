package com.xun;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;

public class SearchController extends Controller implements Initializable{
    private String prompt;
    private final String[] searchOptions = {"Search by keywords", "Search by Title", "Search by Authors"};
    private int searchMode = 0;
    private LocalDate startDate = LocalDate.MIN, endDate = LocalDate.now();

    @FXML
    private Button searchButton, homeSwitch;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchOptionBox;
    @FXML
    private DatePicker searchDatePickerStart, searchDatePickerEnd;
    @FXML
    private ScrollPane searchScrollPane;

        public void search(ActionEvent e){
        switch (searchMode) {
            case 1:
                searchByKeywords(e);
                break;
            case 2:
                searchByKeywords(e);
                break;
            default:
                searchByKeywords(e);
                break;
        }
    }

    private void searchByKeywords(ActionEvent e){
        prompt = searchTextField.getText();
        if (prompt.length() > 0) {
            System.out.println(prompt);
        } 
    }

    public void keyEventHandler(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER) {
            search(null);
        }
    }

    private void getSearchMode(ActionEvent e){
        String selected = searchOptionBox.getValue();
        switch (selected) {
            case "Search by Title":
                searchMode = 1;
                searchTextField.setPromptText("タイトルを入力する");  
                break;
            case "Search by Authors":
                searchMode = 2;
                searchTextField.setPromptText("作者の名前を入力する");
                break;
            default:
                searchMode = 0;
                searchTextField.setPromptText("キーワードを入力する");
                break;
        }
    }
    public void getStartDate(ActionEvent e){
        startDate = searchDatePickerStart.getValue();
        dateCheck();
        System.out.println(startDate + " to " + endDate);
    }
    public void getEndDate(ActionEvent e){
        endDate = searchDatePickerEnd.getValue();
        dateCheck();
        System.out.println(startDate + " to " + endDate);
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
    public void switchHome(ActionEvent e) throws IOException{
        Main.switchScene(Main.HOME_SCENE);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        searchOptionBox.getItems().addAll(searchOptions);
        searchOptionBox.setValue(searchOptions[0]);
        searchOptionBox.setOnAction(this::getSearchMode);

        searchDatePickerEnd.setValue(endDate);
    }
}
