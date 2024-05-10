package com.xun;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchEngine {
    private List<Article> allArticles;
    private HashMap<Integer, Keyword> map = new HashMap<>();

    public SearchEngine(List<Article> allArticles) {
        this.allArticles = allArticles;
        indexing();
        printIndex(); // TODO remove this
    }

    private void indexing(){
        if (allArticles.isEmpty()) {
            allArticles = Main.getAllArticles();
            
        }
        for (Article article : allArticles) {
            indexing(article);
        }
    }

    private void indexing(Article a){
        StringBuffer s = new StringBuffer();
        String content = a.getTitle() + " " + a.getSummary() + " " + a.getContent();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (Character.isWhitespace(c)) {
                if (s.length() > 1) {
                    Keyword w = new Keyword(s.toString());
                    if (map.get(w.hashCode()) == null) {
                        w.addArticle(a);    //Keyword not existed yet
                        map.put(w.hashCode(), w);
                    } else {
                        map.get(w.hashCode()).addArticle(a);
                    }
                }
                s.setLength(0);
            } else if (i == content.length() - 1) {
                if (Character.isLetter(c)) {
                    s.append(Character.toLowerCase(c));
                }
                if (s.length() > 1) {
                    Keyword w = new Keyword(s.toString());
                    if (map.get(w.hashCode()) == null) {
                        w.addArticle(a);    //Keyword not existed yet
                        map.put(w.hashCode(), w);
                    } else {
                        map.get(w.hashCode()).addArticle(a);
                    }
                }
            } else if (Character.isLetter(c)) {
                s.append(Character.toLowerCase(c));
            }
        }
    }
    
    public List<Article> search(String keywords){
        List<Article> results = new ArrayList<>();
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < keywords.length(); i++) {
            char c = keywords.charAt(i);
            if (Character.isWhitespace(c)) {
                Keyword w = new Keyword(s.toString());
                if (map.get(w.hashCode()) != null) {
                    for (Article article : map.get(w.hashCode()).getArticles()) {
                        results.add(article);
                    }
                }
                s.setLength(0);
            } else if (i == keywords.length() - 1) {
                if (Character.isLetter(c)) {
                    s.append(Character.toLowerCase(c));
                }
                Keyword w = new Keyword(s.toString());
                if (map.get(w.hashCode()) != null) {
                    for (Article article : map.get(w.hashCode()).getArticles()) {
                        results.add(article);
                    }
                }
            } else if (Character.isLetter(c)) {
                s.append(Character.toLowerCase(c));
            }
        }
        return results;
    }

    //Debug
    private void printIndex(){
        FileWriter writer;
        try {
            writer = new FileWriter("index.csv");
            for (Map.Entry<Integer, Keyword> entry : map.entrySet()) {
                StringBuilder s = new StringBuilder();
                s.append(entry.getValue() + ",");
                for (Article article : entry.getValue().getArticles()) {
                    s.append(article);
                    s.append(",");
                }
                s.append("\n");
                writer.write(s.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
