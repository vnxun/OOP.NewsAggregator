package com.Data_Source;

import org.jsoup.nodes.Document;

public abstract class Articles extends Data_Source {
    protected String title;
    protected String summary;
    protected String category;
    protected String type;
    protected String keywords;

    public Articles(String link, String author, String date, String content) {
        super(link, author, date, content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public abstract void extractArticleInformation(Document doc);

    public abstract void printInfo();
}

