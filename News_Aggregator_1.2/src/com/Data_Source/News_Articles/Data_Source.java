package com.Data_Source;

public abstract class Data_Source {
    protected String link;
    protected String author;
    protected String date;
    protected String content;

    public Data_Source(String link, String author, String date, String content) {
        this.link = link;
        this.author = author;
        this.date = date;
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
