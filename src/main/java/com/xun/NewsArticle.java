package com.xun;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class NewsArticle extends Article{
    private String title, category, summary;
    private VBox reader;

    //Constructors
    public NewsArticle(String link, String title, String category, String content, String date, String summary, String author, String keywords, String source){
        super(link, source, content, author, date, keywords);
        this.title = title;
        this.category = category;
        setSummary(summary);
    }

    @Override
    protected void setCard(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/NewsArticleCard.fxml"));
            card = loader.load();
            NewsArticleCardController cardController = loader.getController();
            cardController.setData(this);   
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setReader(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/NewsArticleReader.fxml"));
            reader = loader.load();
            NewsArticleReaderController readerController = loader.getController();
            readerController.setData(this);   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public VBox getReader() {
        if (reader == null) {
            setReader();
        }
        return reader;
    }

    //Accessors
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }

    public String getSummary() {
        return summary;
    }
    public String getKeywords() {
        return keywords;
    }
    
    private void setSummary(String summary) {
        if (!summary.isEmpty() && summary.length() > 18) {
            this.summary = summary;
        } else {
            try {
                StringBuilder s = new StringBuilder(); //take the first sentence of the content
                for (int i = 0; i < content.length(); i++) {
                    if (!(content.charAt(i) == '.' && content.charAt(i+1) == ' ')) {
                        s.append(content.charAt(i));
                    } else {
                        s.append('.');
                        break;
                    }
                }
                this.summary = s.toString();
            } catch (Exception e) {
                this.summary = content;
            }
        }
    }


}
