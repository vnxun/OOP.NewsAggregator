package com.xun;

import java.time.LocalDate;

import javafx.scene.layout.BorderPane;

public abstract class Article {
    protected String link, content, date, author, source, keywords;
    protected BorderPane card;
    protected LocalDate localDate;
    
    //Constructors
    protected Article(String link, String source, String content, String author, String date, String keywords){
        this.link = link;
        this.source = source;
        this.content = content;
        this.author = author;
        this.keywords = keywords;
        try {
            String d = date.substring(0, 2);
            String m = date.substring(3, 5);
            String y = date.substring(6, 10);
            String s = y + "-" + m + "-" + d;
            if (LocalDate.parse(s.toString()).isAfter(LocalDate.now())) {
                this.localDate = LocalDate.parse((y + "-" + d + "-" + m).toString());
                this.date = y + "-" + d + "-" + m;
            } else {
                this.localDate = LocalDate.parse(s.toString());
                this.date = y + "-" + m + "-" + d;
            }
        } catch (Exception e) {
            localDate = LocalDate.MIN;
        }
      
    }

    //Accessors
    public String getLink() {
        return link;
    }
    public String getContent() {
        return content;
    }
    public String getDate() {
        return date;
    }
    public String getAuthor() {
        return author;
    }
    public String getSource() {
        return source;
    }
    public String getKeywords() {
        return keywords;
    }
    protected void setCard(){
    }
    public BorderPane getCard() {
        if (card == null) {
            setCard();
        }
        return card;
    }
    public LocalDate getLocalDate() {
        return localDate;
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
    @Override 
    public String toString(){
        return String.format("[%s] %s", source, link);
    }


}
