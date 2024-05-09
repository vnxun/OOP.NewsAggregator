package com.xun;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class Article {
    private int id = -1;
    private String link = "", title = "", category = "", content = "", date = "", summary = "", author = "", keywords = "";
    //private boolean check;
    private VBox thumbnail, reader;
    
    //Constructors
    public Article(int id, String link, String title, String content){
        this.id = id;
        this.link = link;
        this.title = title;
        this.content = content;
        try {
            this.summary = content.substring(0, 100);
        } catch (Exception e) {
            this.summary = content.substring(0, content.length());
        }
             
    }

    private void setThumb(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleThumb.fxml"));
            thumbnail = loader.load();
            ArticleThumbController thumbController = loader.getController();
            thumbController.setData(this);   
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setReader(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleReader.fxml"));
            reader = loader.load();
            ArticleReaderController readerController = loader.getController();
            readerController.setData(this);   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Accessors
    public int getId() {
        return id;
    }

    public String getLink() {
        return link;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        if (!summary.isEmpty()) {
            this.summary = summary;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Article other = (Article) obj;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        return true;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public VBox getThumbnail() {
        setThumb();
        return thumbnail;
    }
    
    public VBox getReader() {
        setReader();
        return reader;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
