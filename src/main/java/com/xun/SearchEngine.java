package com.xun;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SearchEngine {
    private List<Article> allArticles;
    private HashMap<String, Keyword> wordsMap = new HashMap<>();
    private HashMap<String, List<Article>> newsSourceMap = new HashMap<>();

    public SearchEngine(List<Article> allArticles) {
        this.allArticles = allArticles;
        indexing();
        printIndex(); // TODO for debugging
    }

    private void indexing(){
        if (allArticles.isEmpty()) {
            allArticles = Main.getArticlesList();
            
        }
        for (Article article : allArticles) {
            indexing(article);
        }
    }

    private void indexing(Article a){
        scoring(a, split(a.getContent()), 1);
        scoring(a, split(a.getSummary()), 10);
        scoring(a, split(a.getKeywords()), 100);
        scoring(a, split(a.getTitle()), 1000);
        
        String np = a.getSource();
        if (newsSourceMap.get(np) == null) {
            List<Article> list = new ArrayList<>();
            list.add(a);
            newsSourceMap.put(np, list);
        } else {
            newsSourceMap.get(np).add(a);
        }
    }
    private void scoring(Article a, List<String> wordsList, int score){
        for (String word : wordsList) {
            Keyword kw = new Keyword(word);
            if (wordsMap.get(word) == null) {
                kw.addArticle(a, score);
                wordsMap.put(word, kw);
            } else {
                wordsMap.get(word).addArticle(a, score);
            }
        }
    }

    private List<String> split(String string){
        List<String> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (Character.isWhitespace(c) || c == ',' || c == ';') {
                if (buffer.length() > 1) {
                    list.add(buffer.toString());
                }
                buffer.setLength(0);
            } else if (i == string.length() - 1) {
                if (Character.isLetter(c)) {
                    buffer.append(Character.toLowerCase(c));
                } else if (Character.isDigit(c)) {
                    buffer.append(c);
                }
                if (buffer.length() > 1) {
                    list.add(buffer.toString());
                }
            } else if (Character.isLetter(c)) {
                buffer.append(Character.toLowerCase(c));
            } else if (Character.isDigit(c)) {
                buffer.append(c);
            }
        }
        return list;
    }
    
    public List<Article> search(String query){
        HashMap<Article, Integer> articlesScoreMap = new HashMap<>();
        List<String> wordsList = split(query);
        for (String word : wordsList) {
            if (wordsMap.get(word) != null) {
                for (Article article : wordsMap.get(word).getArticles()) {
                    int score = wordsMap.get(word).getArticleScore(article);
                    if (articlesScoreMap.get(article) == null) {
                        articlesScoreMap.put(article, score);
                    } else {
                        articlesScoreMap.put(article, articlesScoreMap.get(article) + score);
                    }
                }
            }
        }
        List<Article> results = new ArrayList<>();
        for (Entry<Article, Integer> entry : articlesScoreMap.entrySet()) {
            results.add(entry.getKey());
        }
        results.sort(new SortArticles(articlesScoreMap));
        return results;
    }

    //Debug
    private void printIndex(){
        FileWriter writer;
        try {
            writer = new FileWriter("index.csv");
            for (Entry<String, Keyword> entry : wordsMap.entrySet()) {
                StringBuilder s = new StringBuilder();
                s.append(entry.getValue() + ",");
                for (Article article : entry.getValue().getArticles()) {
                    s.append("score:" + entry.getValue().getArticleScore(article) + " ");
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
    //

    private class SortArticles implements Comparator<Article>{
        HashMap<Article, Integer> articlesScoreMap;

        public SortArticles(HashMap<Article, Integer> articlesScoreMap){
            this.articlesScoreMap = articlesScoreMap;
        }
        @Override
        public int compare(Article a1, Article a2) {
            int score1 = articlesScoreMap.get(a1);
            int score2 = articlesScoreMap.get(a2);
            return score2 - score1;
        }   
    }
}
