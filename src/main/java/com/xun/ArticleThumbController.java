package com.xun;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ArticleThumbController extends Controller {
    @FXML
    private Label titleLabel, summaryLabel, authorLabel, timeLabel;
    private Article article;

    public void setData(Article a){
        titleLabel.setText(a.getTitle());
        authorLabel.setText(a.getAuthor());
        timeLabel.setText(a.getDate());
        summaryLabel.setText(a.getSummary());
        article = a;
    }

    public void readArticle(MouseEvent e){
        Main.switchReader(article);
    }
}
