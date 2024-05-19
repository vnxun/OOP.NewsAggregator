package com.xun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Keyword {
    private String word;
    private HashMap<Article, Double> map = new HashMap<>();

    public Keyword(String word) {
        this.word = word;
    }

    // public void addArticle(Article a){
    //     if (map.get(a) == null) {
    //         map.put(a, 1);
    //     } else {
    //         map.put(a, map.get(a) + 1);
    //     }
    // }
    public void addArticle(Article a, double score){
        if (map.get(a) == null) {
            map.put(a, score / a.getContent().length());
        } else {
            map.put(a, map.get(a) + score / a.getContent().length());
        }
    }

    @Override
    public String toString() {
        return word;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((word == null) ? 0 : word.hashCode());
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
        Keyword other = (Keyword) obj;
        if (word == null) {
            if (other.word != null)
                return false;
        } else if (!word.equals(other.word))
            return false;
        return true;
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        for (Entry<Article, Double> entry: map.entrySet()) {
            articles.add(entry.getKey());
        }
        return articles;
    }

    public Double getArticleScore(Article a){
        if (map.get(a) == null) {
            return 0.0;
        }
        return map.get(a);
    }
}
