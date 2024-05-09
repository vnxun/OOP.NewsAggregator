package com.xun;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class HomeSceneController extends Controller implements Initializable{
    @FXML
    private Button searchSwitch, reloadButton;
    @FXML
    private TextField searchBox;
    @FXML
    private ScrollPane homeScrollPane;
    @FXML
    private GridPane homeGrid;
    @FXML
    private Label loadLabel;
    private static List<Article> articles = new ArrayList<>();
    private int loadedArticles = 0;

    public void switchSearch(ActionEvent e) throws IOException{
        Main.switchScene(Main.SEARCH_SCENE);
    }
    public void keyEventHandler(KeyEvent e){
        if (e.getCode() == KeyCode.ENTER) {
            search(null);
        }
    }
    public void search(ActionEvent e){
        String prompt = searchBox.getText();
        if (prompt.length() > 0) {
            System.out.println(prompt);
        } 
    }
    public void reload(){
        loadedArticles = 0;
        homeGrid.getChildren().clear();
        load(null);
    }

    public void load(MouseEvent event){
        try {
            articles = Main.readData();
            for (int i = 0; i < 30; i++) {
                if (loadedArticles >= articles.size()) {
                    break;
                }
                homeGrid.add(articles.get(loadedArticles).getThumbnail(), 0, loadedArticles+1);
                loadedArticles++;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }
}
