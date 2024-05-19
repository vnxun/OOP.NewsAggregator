package com.xun;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class PostController {
    @FXML
    private Label authorLabel, contentLabel, platformLabel, timeLabel;
    @FXML
    private Hyperlink hyperlink;

    public void setData(Post p){
        authorLabel.setText(p.getAuthor());
        contentLabel.setText(p.getContent());
        timeLabel.setText(p.getDate());
        platformLabel.setText(p.getSource());
        hyperlink.setText(p.getLink());
    }

    @FXML
    void hyperlinkAction(ActionEvent event) {
        String copyText = hyperlink.getText();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(copyText);
        clipboard.setContent(content);
    }
}
