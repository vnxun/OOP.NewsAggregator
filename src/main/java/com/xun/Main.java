package com.xun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class Main extends Application {
    
    private static Scene scene;
    private static List<Parent> roots = new ArrayList<>();
    private static List<FXMLLoader> loaders = new ArrayList<>();
    private static List<Article> articles = new ArrayList<>();
    public static final int HOME_SCENE = 0;
    public static final int SEARCH_SCENE = 1;
    public static final int READER_SCENE = 2;
    private static int activeScene = HOME_SCENE, lastScene = HOME_SCENE;
    private static final String dataFileName = "output.csv";
    

    @Override
    public void start(Stage stage) {
        try {
            roots.add(loadRoot("HomeScene"));
            roots.add(loadRoot("SearchScene"));
            roots.add(loadRoot("ArticleReaderScene"));
            scene = new Scene(roots.get(HOME_SCENE));
            stage.setTitle("フィードリーダー");
            stage.getIcons().add(new Image(getClass().getResource("icon.png").toURI().toString()));
            stage.setScene(scene);
            //readData();
            reloadHome();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch();
    }

    public static Parent loadRoot(String rootName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(rootName + ".fxml"));
        Parent root = loader.load();
        loaders.add(loader);
        return root;
    }

    //Switches
    public static void switchScene(int root){
        if (root < roots.size()) {
            scene.setRoot(roots.get(root));
        }
        lastScene = activeScene;
        activeScene = root;
    }
    public static void switchBack(){
        switchScene(lastScene);
    }  
    public static void switchReader(Article a){
        ReaderSceneController readerSceneController = loaders.get(READER_SCENE).getController();
        readerSceneController.setArticle(a);
        switchScene(READER_SCENE);
    }
    public static void reloadHome(){
        readData();
        HomeSceneController controller = loaders.get(HOME_SCENE).getController();
        controller.reload();
    }
    //

    //Get articles
    public static List<Article> readData (){
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(dataFileName)));
            bReader.readLine(); //skip header row
            String line;
            while ((line = bReader.readLine()) != null) {
                String data[] = splitData(line);
                int id = (int) Double.parseDouble(data[0]);
                String link = data[1];
                String title = data[2];
                String date = data[3];
                String author = data[4];
                String summary = data[5];
                String content = data[6];
                String category = data[7];
                String keywords = data[8];
                Article article = new Article(id, link, title, content);
                article.setSummary(summary);
                article.setAuthor(author);
                article.setCategory(category);
                article.setDate(date);
                article.setKeywords(keywords);
                articles.add(article);
            }
            bReader.close();
        } catch (Exception fnfe) {
            fnfe.printStackTrace();
        }
        return articles;
    }

    private static String[] splitData(String line){
        String[] data = new String[9];
        boolean insideComma = false;
        StringBuffer s = new StringBuffer();
        int index = 0;
        for (int i = 0; i < line.length() - 1; i++) {
            if (line.charAt(i) == '"') {
                if (line.charAt(i+1) == '"' && insideComma) {
                    s.append('"');
                    i++;
                } else {
                    insideComma = !insideComma;
                }
            } else if ((line.charAt(i) == ',' && !insideComma)) {
                data[index] = s.toString();
                index++;
                s = new StringBuffer();       
            } else if (i == line.length() - 2) {
                s.append(line.charAt(i));
                data[index] = s.toString();
                index++;
                s = new StringBuffer();  
            } else {
                s.append(line.charAt(i));
            }
        }
        return data;
    }  

    // public static List<Article> getArticles() {
    //     return articles;
    // }
    //
}