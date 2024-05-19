package com.xun;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;

public class SortArticles implements Comparator<Article>{
    HashMap<Article, Integer> articlesScoreMap;
    public static final int BY_RELEVENCE = 0;
    public static final int BY_DATE = 1;
    private int mode;
    public SortArticles(HashMap<Article, Integer> articlesScoreMap, int mode){
        this.articlesScoreMap = articlesScoreMap;
        this.mode = mode;
    }
    public SortArticles(int mode){
        this.mode = mode;
    }
    @Override
    public int compare(Article a1, Article a2) {
        if (mode == BY_RELEVENCE) {
            int score1 = articlesScoreMap.get(a1);
            int score2 = articlesScoreMap.get(a2);
            return score2 - score1;
        } else {
            LocalDate d1 = a1.getLocalDate();
            LocalDate d2 = a2.getLocalDate();
            // if (d1 == null) {
            //     if (d2 == null) {
            //         return 0;
            //     } else {
            //         return 1;
            //     }
            // } else {
            //     if (d2 == null) {
            //         return -1;
            //     }
            // }
            if (d1.isAfter(d2)) {
                return -1;
            } else if (d1.isBefore(d2)){
                return 1;
            } else {
                return 0;
            }
        }
        
    }   
}