package com.xun;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class SearchSceneController extends MenuController implements Initializable{
    private LocalDate startDate = LocalDate.MIN, endDate = LocalDate.now();
    private List<Article> articles;
    private int loadedArticles = 0;
    private SearchEngine searchEngine = new SearchEngine(Main.getArticlesList());
    @FXML
    private Label loadLabel, countLabel, sourcLabel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
    @FXML
    private Button searchButton, sourceSelectButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private DatePicker searchDatePickerStart, searchDatePickerEnd;
    @FXML
    private ScrollPane searchScrollPane;

    public void search(ActionEvent e){
        searchByKeywords(e);
        countLabel.setText(articles.size() + " results found");
    }

    private void searchByKeywords(ActionEvent e){
        String query = searchTextField.getText();
        if (query.length() > 0) {
            articles = searchEngine.search(query);
            loadedArticles = 0;
            grid.getChildren().clear();
            load(null);
        } 
    }

    public void load(MouseEvent event){
        try {
            for (int i = 0; i < 30; i++) {
                if (loadedArticles >= articles.size()) {
                    loadLabel.setVisible(false);
                    return;
                }
                grid.add(articles.get(loadedArticles).getThumbnail(), 0, loadedArticles + 1);
                loadedArticles++;
                loadLabel.setVisible(true);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
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
