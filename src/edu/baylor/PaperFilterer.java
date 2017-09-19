package edu.baylor;

import java.util.LinkedList;
import java.util.List;

public class PaperFilterer {

    public List<Article> filterAbstract(List<Article> articles, List<String> containsOr, List<String> notContainsOr) {
        List<Article> filtered = new LinkedList();
        articleLoop:
        for (Article article : articles) {
            if (article.getPaperAbstract() == null) {
                continue;
            }
            for (String word : notContainsOr) {
                if (article.getPaperAbstract().toLowerCase().contains(word.toLowerCase())) {
                    continue articleLoop;
                }
            }
            for (String word : containsOr) {
                if (article.getPaperAbstract().toLowerCase().contains(word.toLowerCase())) {
                    filtered.add(article);
                    continue articleLoop;
                }
            }
        }
        return filtered;
    }
}
