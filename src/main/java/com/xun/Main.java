package com.xun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
    private static HashMap<Integer, Article> articlesMap = new HashMap<>();
    //private static List<Article> allArticles = new ArrayList<>();
    public static final int HOME_SCENE = 0;
    public static final int SEARCH_SCENE = 1;
    public static final int READER_SCENE = 2;
    private static int activeScene = HOME_SCENE, lastScene = HOME_SCENE;
    private static final String dataFileName = "Main_output.csv";
    

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
            reloadHome();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        readData();
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
        HomeSceneController controller = loaders.get(HOME_SCENE).getController();
        controller.reload();
    }
    //

    //Get articles
    public static void readData (){
        BufferedReader bReader;
        try {
            bReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("output/" + dataFileName)));
            String line;
            while ((line = bReader.readLine()) != null) {
                List<String> data = splitData(line);
                String link = data.get(0);
                String title = data.get(1);
                String date = data.get(2);
                String author = data.get(3);
                String summary = data.get(4);
                String content = data.get(5);
                String category = data.get(6);
                String keywords = data.get(8);
                String source = data.get(9);
                Article article = new Article(link, title, content);
                article.setSummary(summary);
                article.setAuthor(author);
                article.setCategory(category);
                article.setDate(date);
                article.setKeywords(keywords);
                article.setSource(source);
                articlesMap.put(article.hashCode(), article);
            }
            bReader.close();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private static List<String> splitData(String line){
        List<String> data = new ArrayList<>();
        boolean insideComma = false;
        StringBuffer s = new StringBuffer();
        //int column = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '"') {
                if (line.charAt(i+1) == '"' && insideComma) {
                    s.append('"');
                    i++;
                } else {
                    insideComma = !insideComma;
                }
            } else if ((line.charAt(i) == ',' && !insideComma)) {
                data.add(s.toString());
                s.setLength(0);       
            } else if (i == line.length() - 1) {
                s.append(line.charAt(i));
                data.add(s.toString());
                s = new StringBuffer();  
            } else {
                s.append(line.charAt(i));
            }
        }
        return data;
    }  
    
    public static List<Article> getArticlesList() {
        List<Article> articlesList = new ArrayList<>();
        for (Entry<Integer, Article> entry : articlesMap.entrySet()) {
            articlesList.add(entry.getValue());
        }
        return articlesList;
    }
    public static HashMap<Integer, Article> getArticlesMap() {
        return articlesMap;
    }
    

}