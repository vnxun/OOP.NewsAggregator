package com.xun;


import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class ArticleReaderController extends Controller{
    @FXML
    private Label authorLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private Label keywordsLabel;

    @FXML
    private Label summaryLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Hyperlink hyperlink;

    public void setData(Article a){
        titleLabel.setText(a.getTitle());
        authorLabel.setText(a.getAuthor());
        timeLabel.setText(a.getDate());
        summaryLabel.setText(a.getSummary());
        categoryLabel.setText(a.getCategory());
        contentLabel.setText(a.getContent());
        keywordsLabel.setText(a.getKeywords());
        hyperlink.setText(a.getLink());
    }
}
