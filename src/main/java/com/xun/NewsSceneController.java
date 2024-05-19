package com.xun;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class NewsSceneController extends MenuController{
    @FXML
    private Button reloadButton;
    @FXML
    private ScrollPane homeScrollPane;
    @FXML
    private GridPane homeGrid;
    @FXML
    private Label loadLabel;
    private List<NewsArticle> articles = Main.getNewsArticlesList();
    private int loadedArticles = 0;

    public void reload(){
        loadedArticles = 0;
        homeGrid.getChildren().clear();
        articles = Main.getNewsArticlesList();
        load(null);
    }

    public void load(MouseEvent event){
        try {
            for (int i = 0; i < 30; i++) {
                if (loadedArticles >= articles.size()) {
                    loadLabel.setVisible(false);
                    break;
                }
                homeGrid.add(articles.get(loadedArticles).getCard(), 0, loadedArticles + 1);
                loadedArticles++;
                loadLabel.setVisible(true);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
