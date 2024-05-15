package com.xun;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class SearchSceneController extends Controller implements Initializable{
    private final String[] searchOptions = {"Search by keywords", "Search by Title", "Search by Authors"};
    private int searchMode = 0;
    private LocalDate startDate = LocalDate.MIN, endDate = LocalDate.now();
    private List<Article> articles;
    private int loadedArticles = 0;
    private SearchEngine searchEngine = new SearchEngine(Main.getArticlesList());
    @FXML
    private Label loadLabel, countLabel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
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
