package com.xun;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;

public class ReaderSceneController extends Controller {
    @FXML
    private Button backButton;

    @FXML
    private Button homeSwitch;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button searchSwitch;

    @FXML
    private Hyperlink hyperlink;


    public void switchSearch(ActionEvent e) throws IOException{
        Main.switchScene(Main.SEARCH_SCENE);
    }

    public void switchHome(ActionEvent e) throws IOException{
        Main.switchScene(Main.HOME_SCENE);
    }
    public void switchBack(){
        Main.switchBack();
    }

    public void setArticle(Article a){
        scrollPane.setContent(a.getReader());
    }
}
