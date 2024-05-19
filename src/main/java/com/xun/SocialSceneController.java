package com.xun;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;

public class SocialSceneController extends MenuController implements Initializable{
    @FXML
    private Button reloadButton;
    @FXML
    private ScrollPane homeScrollPane;
    @FXML
    private GridPane homeGrid;
    @FXML
    private Label loadLabel;
    private List<Post> articles = Main.getPostsList();// = Main.getArticlesList();
    private int loadedArticles = 0;

    public void reload(){
        loadedArticles = 0;
        homeGrid.getChildren().clear();
        articles = Main.getPostsList();
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }
}
