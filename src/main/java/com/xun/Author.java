package com.xun;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String name;
    private List<Article> articles = new ArrayList<>();
    
    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Article> getArticlesArray() {
        return articles;
    }

    public void addArticles(Article a) {
        for (Article article : articles) {
            if (a.equals(article)) {
                return;
            }
        }
        articles.add(a);
        //a.addAuthor(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Author other = (Author) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    
}
