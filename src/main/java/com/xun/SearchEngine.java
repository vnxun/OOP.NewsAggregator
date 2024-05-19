package com.xun;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SearchEngine {
    private List<Article> allArticles;
    private HashMap<String, Keyword> wordsMap = new HashMap<>();
    private HashMap<String, String> lemmatizerMap = new HashMap<>();
    private HashMap<String, List<Article>> allSources = new HashMap<>();

    public SearchEngine(List<Article> allArticles) {
        this.allArticles = allArticles;
        readLemmatizer();
        indexing();
        //printIndex(); printLemmatizer(); // TODO for debugging
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
        scoring(a, tokenize(a.getContent()), 1);
        scoring(a, tokenize(a.getKeywords()), 5);
        if (a instanceof NewsArticle) {
            NewsArticle na = (NewsArticle) a;
            scoring(na, tokenize(na.getSummary()), 2);
            scoring(na, tokenize(na.getTitle()), 10);
        }
        String np = a.getSource();
        if (allSources.get(np) == null) {
            List<Article> list = new ArrayList<>();
            list.add(a);
            allSources.put(np, list);
        } else {
            allSources.get(np).add(a);
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

    private List<String> tokenize(String string) {
        List<String> list = new ArrayList<>();
        try {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (Character.isWhitespace(c) || c == ',' || c == ';') {
                    if (buffer.length() > 1) {
                        list.add(lemmatize(buffer.toString()));
                    }
                    buffer.setLength(0);
                } else if (i == string.length() - 1) {
                    if (Character.isLetter(c)) {
                        buffer.append(Character.toLowerCase(c));
                    } else if (Character.isDigit(c)) {
                        buffer.append(c);
                    }
                    if (buffer.length() > 1) {
                        list.add(lemmatize(buffer.toString()));
                    }
                } else if (Character.isLetter(c)) {
                    buffer.append(Character.toLowerCase(c));
                } else if (Character.isDigit(c)) {
                    buffer.append(c);
                }
            }
        } catch (NullPointerException e) {
            
        }
        return list;
    }
    private String lemmatize(String s){
        String lemma = lemmatizerMap.get(s);
        if (lemma != null) {
            return lemmatizerMap.get(s);
        }
        return s;
        
    }
    
    private void readLemmatizer(){
        BufferedReader bReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("lemmatizer.csv")));
        String line;
        try {
            while ((line = bReader.readLine()) != null) {
                String[] lineContent = line.split(",");
                lemmatizerMap.put(lineContent[0], lineContent[2]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Article> search(String query, List<String> sources, LocalDate startDate, LocalDate endDate){
        HashMap<Article, Integer> articlesScoreMap = new HashMap<>();
        List<String> wordsList = tokenize(query);
        for (String word : wordsList) {
            if (wordsMap.get(word) != null) {
                for (Article article : wordsMap.get(word).getArticles()) {
                    if (sourceCheck(sources, article) && dateCheck(article, startDate, endDate)) {
                        int score = wordsMap.get(word).getArticleScore(article);
                        if (articlesScoreMap.get(article) == null) {
                            articlesScoreMap.put(article, score);
                        } else {
                            articlesScoreMap.put(article, articlesScoreMap.get(article) + score);
                        }
                    }
                    
                }
            }
        }
        List<Article> results = new ArrayList<>();
        for (Entry<Article, Integer> entry : articlesScoreMap.entrySet()) {
            results.add(entry.getKey());
        }
        results.sort(new SortArticles(articlesScoreMap, SortArticles.BY_RELEVENCE));
        return results;
    }
    public List<Article> search(List<String> sources, LocalDate startDate, LocalDate endDate){
        List<Article> results = new ArrayList<>();
        for (Article article : allArticles) {
            if (sourceCheck(sources, article) && dateCheck(article, startDate, endDate)) {
                results.add(article);
            }
        }
        return results;
    }
    private boolean sourceCheck(List<String> sources, Article a){
        for (String source : sources) {
            if (a.getSource().equals(source)) {
                return true;
            }
        }
        return false;
    }
    private boolean dateCheck(Article a, LocalDate startDate, LocalDate endDate){
        if (a.getLocalDate().isBefore(endDate) && a.getLocalDate().isAfter(startDate)){
            return true;
        } else if (a.getLocalDate().isEqual(endDate) || a.getLocalDate().isEqual(startDate)){
            return true;
        } 
        return false;
    }

    // //Debug
    // private void printIndex(){
    //     FileWriter writer;
    //     try {
    //         writer = new FileWriter("WordsIndex.csv");
    //         for (Entry<String, Keyword> entry : wordsMap.entrySet()) {
    //             StringBuilder s = new StringBuilder();
    //             s.append(entry.getValue() + ",");
    //             for (Article article : entry.getValue().getArticles()) {
    //                 s.append("score:" + entry.getValue().getArticleScore(article) + " ");
    //                 s.append(article);
    //                 s.append(",");
    //             }
    //             s.append("\n");
    //             writer.write(s.toString());
    //         }
    //         writer.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }  
    // }
    // private void printLemmatizer(){
    //     FileWriter writer;
    //     try {
    //         writer = new FileWriter("LemmatizerIndex.csv");
    //         for (Entry<String, String> entry : lemmatizerMap.entrySet()) {
    //             StringBuilder s = new StringBuilder();
    //             s.append(entry.getKey() + ",");
    //             s.append(entry.getValue());
    //             s.append("\n");
    //             writer.write(s.toString());
    //         }
    //         writer.close();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }  
    // }
    //



    public List<String> getAllSources() {
        List<String> list = new ArrayList<>();
        for (Entry<String, List<Article>> entry : allSources.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }
}
