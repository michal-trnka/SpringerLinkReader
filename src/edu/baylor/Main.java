package edu.baylor;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            PaperExtractor paperExtractor = new PaperExtractor();
            List<Article> articles = paperExtractor.getArticlesFromCSV("C:\\Users\\trnka\\Documents\\Survey\\SpringerLink-raw.csv");
            paperExtractor.addAbstractsToArticles(articles);


        } catch (Exception e) { //Pokemon exception handling
            e.printStackTrace();
        }
    }
}
