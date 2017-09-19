package edu.baylor;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            PaperExtractor paperExtractor = new PaperExtractor();
            PaperFilterer paperFilterer = new PaperFilterer();
            List<Article> articles = paperExtractor.getArticlesFromCSV("C:\\Users\\trnka\\Documents\\Survey\\SpringerLink-raw.csv");
            paperExtractor.addAbstractsToArticles(articles);
            List<Article> filtered = paperFilterer.filterAbstract(articles, Arrays.asList("Authentication","Authorization","Identity"),Arrays.asList("Protocol ","RFID"));
            paperExtractor.writeArticles(filtered,"C:\\Users\\trnka\\Documents\\Survey\\SpringerLink-processed.csv");


        } catch (Exception e) { //Pokemon exception handling
            e.printStackTrace();
        }
    }
}
