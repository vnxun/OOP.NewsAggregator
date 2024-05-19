package com.xun;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class NewsArticleCardController {
    @FXML
    private Label titleLabel, summaryLabel, sourceLabel, timeLabel;
    private NewsArticle article;

    public void setData(NewsArticle a){
        titleLabel.setText(a.getTitle());
        sourceLabel.setText(a.getSource());
        timeLabel.setText(a.getDate());
        summaryLabel.setText(a.getSummary());
        article = a;
    }

    public void readArticle(MouseEvent e){
        Main.switchReader(article);
    }
}
