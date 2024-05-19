package com.xun;




import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;

public class ReaderSceneController extends MenuController{
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Hyperlink hyperlink;

    public void setArticle(NewsArticle a){
        scrollPane.setContent(a.getReader());
    }
}
