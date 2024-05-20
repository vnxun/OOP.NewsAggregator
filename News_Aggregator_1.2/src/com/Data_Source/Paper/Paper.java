package com.Data_Source;

public class Paper extends Data_Source {
    private String title;
    private String url;
    private String authors;
    private String summary;
    private String hashtag;

    public Paper(String link, String author, String date, String content, String title, String url, String authors, String summary, String hashtag) {
        super(link, author, date, content);
        this.title = title;
        this.url = url;
        this.authors = authors;
        this.summary = summary;
        this.hashtag = hashtag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String toCSV() {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                getLink(), getAuthor(), getDate(), getContent(), title, url, authors, summary, hashtag);
    }
}
