package com.Data_Source;

import org.jsoup.nodes.Document;

public class WSJ extends Articles {
    public WSJ(String link, String author, String date, String content) {
        super(link, author, date, content);
    }

    @Override
    protected void extractArticleInformation(Document doc) {
        setTitle(doc.selectFirst("title").text());
        setAuthor(doc.selectFirst("meta[name=author]").attr("content"));
    }

    @Override
    public void printInfo() {

        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());

    }
}
