package com.xun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static HashMap<Integer, Parent> roots = new HashMap<>();
    private static HashMap<Integer,FXMLLoader> loaders = new HashMap<>();
    private static List<Article> articles = new ArrayList<>();
    public static final int NEWS_SCENE = 0;
    public static final int SOCIAL_SCENE = 5;
    public static final int SEARCH_SCENE = 100;
    public static final int READER_SCENE = 9999;
    public static final int TREND_SCENE = 12345;
    private static int activeScene = NEWS_SCENE, lastScene = NEWS_SCENE;
    private static final String newsFile = "Main_output_1.2.csv";
    private static final String[] postsFiles = {"TwitterCrawlData.csv","FacebookCrawlData_2.0.csv"};

    @Override
    public void start(Stage stage) {
        try {
            loadRoot(NEWS_SCENE, "NewsScene");
            loadRoot(SEARCH_SCENE, "SearchScene");
            loadRoot(READER_SCENE, "ArticleReaderScene");
            loadRoot(SOCIAL_SCENE, "SocialScene");
            scene = new Scene(roots.get(NEWS_SCENE));
            stage.setTitle("見ないほうがいい");
            stage.getIcons().add(new Image(getClass().getResource("icon.png").toURI().toString()));
            stage.setScene(scene);
            reloadHome();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        readNews();
        readPosts();
        articles.sort(new SortArticles(SortArticles.BY_DATE));
        launch();
    }

    private static Parent loadRoot(int i,String rootName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/" + rootName + ".fxml"));
        Parent root = loader.load();
        loaders.put(i, loader);
        roots.put(i, root);
        return root;
    }

    //Switches
    public static void switchScene(int root){
        if (root == TREND_SCENE) {
            return;
        }
        scene.setRoot(roots.get(root));
        lastScene = activeScene;
        activeScene = root;
    }
    public static void switchBack(){
        switchScene(lastScene);
    }  
    public static void switchReader(NewsArticle a){
        ReaderSceneController readerSceneController = loaders.get(READER_SCENE).getController();
        readerSceneController.setArticle(a);
        switchScene(READER_SCENE);
    }
    public static void reloadHome(){
        NewsSceneController controller = loaders.get(NEWS_SCENE).getController();
        controller.reload();
        SocialSceneController socialSceneController = loaders.get(SOCIAL_SCENE).getController();
        socialSceneController.reload();
    }
    //

    //Get articles
    private static void readNews(){
        BufferedReader bReader;
        String line = "", nextLine = "";
        try {
            bReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("source/" + newsFile)));
            nextLine = bReader.readLine();
            line = "";
            int count = 0;
            while (line!= null) {
                while (true) {
                    nextLine = bReader.readLine();
                    if (nextLine == null) {
                        if (count < 10) {
                            line += "\n" + nextLine;
                            count++;
                        } else {
                            line = null; 
                            break;
                        }
                    } else if (nextLine.length() < 7 || !nextLine.substring(0, 6).equals("https:")) {
                        line += "\n" + nextLine;
                        count = 0;
                    } else {
                        List<String> data = splitData(line);
                        if (data.size() >= 7) {
                            String link = data.get(0);
                            String title = data.get(1);
                            String date = data.get(2);
                            String author = data.get(3);
                            String summary = data.get(4);
                            String content = data.get(5);
                            String category = data.get(6);
                            String keywords = data.get(8);
                            String source = data.get(9);
                            NewsArticle article = new NewsArticle(link, title, category, content, date, summary, author, keywords, source);
                            articles.add(article);
                        }
                        line = nextLine;
                        count = 0;
                        break;
                    }
                }
            }
            bReader.close();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
    private static void readPosts(){
        BufferedReader bReader;
        String line = "", nextLine = "";
        int count = 0;
        for (String fileName : postsFiles) {
            try {
                bReader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("source/" + fileName)));
                nextLine = bReader.readLine();
                line = "";
                while (line != null) {
                    while (true) {
                        nextLine = bReader.readLine();
                        if (nextLine == null) {
                            if (count < 10) {
                                line += "\n" + nextLine;
                                count++;
                            } else {
                                line = null; 
                                break;
                            }
                        } else if (nextLine.length() < 7 || !nextLine.substring(0, 7).equals("\"https:")) {
                            line += "\n" + nextLine;
                            count = 0;
                        } else {
                            List<String> data = splitData(line);
                            if (data.size() >= 7) {
                                String link = data.get(0);
                                String date = data.get(3);
                                String author = data.get(2);
                                String content = data.get(4);
                                String source = data.get(1);
                                String hashtags = data.get(6);
                                Post post = new Post(link, source, content, author, date, hashtags);
                                articles.add(post);
                            }
                            line = nextLine;
                            count = 0;
                            break;
                        }
                    }
                }
                bReader.close();
            } catch (IOException | IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        
    }

    private static List<String> splitData(String line){
        List<String> data = new ArrayList<>();
        boolean insideComma = false;
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < line.length(); i++) {
            try {
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
            } catch (IndexOutOfBoundsException e) {
                //Ignore
            }
        }
        return data;
    }  
    
    public static List<NewsArticle> getNewsArticlesList() {
        List<NewsArticle> list = new ArrayList<>();
        for (Article article : articles) {
            if (article instanceof NewsArticle) {
                list.add((NewsArticle)article);
            }
        }
        return list;
    }
    public static List<Post> getPostsList(){
        List<Post> list = new ArrayList<>();
        for (Article article : articles) {
            if (article instanceof Post) {
                list.add((Post)article);
            }
        }
        return list;
    }
    public static List<Article> getArticlesList(){
        return articles;
    }
    // public static List<T> getList<T>(){

    // }

}