package com.xun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Keyword {
    private String word;
    private HashMap<Integer, Article> map = new HashMap<>();

    public Keyword(String word) {
        this.word = word;
    }

    public void addArticle(Article a){
        if (map.get(a.hashCode()) == null) {
            map.put(a.hashCode(), a);
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
        for (Entry<Integer, Article> entry: map.entrySet()) {
            articles.add(entry.getValue());
        }
        return articles;
    }
    
}
