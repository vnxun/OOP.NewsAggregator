package com.Data_Source;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ChinaDaily extends Articles {
    public ChinaDaily(String link, String author, String date, String content) {
        super(link, author, date, content);
    }

    @Override
    public void extractArticleInformation(Document doc) {
        Element titleElement = doc.selectFirst("title");
        if (titleElement != null) {
            setTitle(titleElement.text());
        } else {
            setTitle("Title not found");
        }

        Element authorElement = doc.selectFirst("meta[name=cXenseParse:author]");
        if (authorElement != null) {
            setAuthor(authorElement.attr("content"));
        } else {
            setAuthor("Author not found");
        }
    }

    @Override
    public void printInfo() {
        System.out.println("Title: " + getTitle());
        System.out.println("Author: " + getAuthor());
    }
}
