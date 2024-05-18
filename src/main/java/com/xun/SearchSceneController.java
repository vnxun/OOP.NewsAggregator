package com.xun;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SearchSceneController extends MenuController implements Initializable{
    private LocalDate startDate = LocalDate.MIN, endDate = LocalDate.now();
    private List<Article> articles;
    private int loadedArticles = 0;
    private SearchEngine searchEngine = new SearchEngine(Main.getArticlesList());
    private List<String> selectedSources = searchEngine.getAllSources();
    private boolean started = false;
    @FXML
    private Stage selectSourcesStage = new Stage();
    private SourcesSelectorController csController;
    @FXML
    private Label loadLabel, countLabel, sourceLabel;
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
            articles = searchEngine.search(query, selectedSources);
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

    public void selectSources(MouseEvent ev){
            selectSourcesStage.showAndWait();
            selectedSources = csController.getSelectedSources();
            if (selectedSources.size() < searchEngine.getAllSources().size()) {
                StringBuffer sb = new StringBuffer();
                for (String source : selectedSources) {
                    sb.append(source + ", ");
                }
                sourceLabel.setText("Souces: " + sb.toString());
            } else {
                sourceLabel.setText("Souces: All");
            }

    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!started) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/SourcesSelector.fxml"));
                Parent root = loader.load();
                csController = loader.getController();
                Scene scene = new Scene(root);
    
                selectSourcesStage.setScene(scene);
                selectSourcesStage.setTitle("Select news sources");
                selectSourcesStage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            searchDatePickerEnd.setValue(endDate);
            started = true;
        }
        
    }
}
