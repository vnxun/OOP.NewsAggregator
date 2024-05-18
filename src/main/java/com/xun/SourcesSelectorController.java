package com.xun;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SourcesSelectorController implements Initializable {
    @FXML
    private Button cancelButton, okButton;
    @FXML
    private VBox container;
    private SearchEngine searchEngine = new SearchEngine(Main.getArticlesList());
    private List<String> allSources = searchEngine.getAllSources();
    private List<String> selectedSources = new ArrayList<>();
    private boolean started = false;

    public void cancel(ActionEvent e){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void ok(ActionEvent e){
        selectedSources = new ArrayList<>();
        for (Node child : container.getChildren()) {
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                if (checkBox.isSelected()) {
                    selectedSources.add(checkBox.getText());
                }
            }
        }
        if (selectedSources.size() <= 0) {
            for (String sourceName : allSources) {
                selectedSources.add(sourceName);
            }
        }
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public List<String> getSelectedSources() {
        return selectedSources;
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (!started) {
            for (String sourceName : allSources) {
                CheckBox cBox = new CheckBox(sourceName);
                cBox.setSelected(true);
                container.getChildren().add(cBox);
                selectedSources.add(sourceName);
            }
            started = true;
        }
    }
}
