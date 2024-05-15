package com.xun;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class Article {
    private String link, title, category, content, date, summary, author, keywords, source;
    private VBox thumbnail = null, reader = null;
    
    //Constructors
    public Article(String link, String title, String content){
        this.link = link;
        this.title = title;
        this.content = content;
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
        if (!summary.isEmpty() && summary.length() > 10) {
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
        if (thumbnail == null) {
            setThumb();
        }
        return thumbnail;
    }
    
    public VBox getReader() {
        if (reader == null) {
            setReader();
        }
        return reader;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override 
    public String toString(){
        return String.format("[%s] %s", hashCode(), link);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
