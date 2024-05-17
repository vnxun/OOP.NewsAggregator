package com.xun;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    @FXML
    protected Button newspapersSwitch, socialSwitch, searchSwitch, trendSwitch, backButton;

    public void switchSearch(ActionEvent e) throws IOException{
        Main.switchScene(Main.SEARCH_SCENE);
    }

    public void switchNews(ActionEvent e) throws IOException{
        Main.switchScene(Main.NEWS_SCENE);
    }

    public void switchSocial(ActionEvent e) throws IOException{
        Main.switchScene(Main.NEWS_SCENE);
    }
    
    public void switchBack(){
        Main.switchBack();
    }

}
