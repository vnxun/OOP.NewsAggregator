package com.xun;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

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
    public void hyperlinkAction(ActionEvent e){
        String copyText = hyperlink.getText();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(copyText);
        clipboard.setContent(content);
    }
}
