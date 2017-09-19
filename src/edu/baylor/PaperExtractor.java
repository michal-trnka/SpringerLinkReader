package edu.baylor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PaperExtractor {

    public List<Article> getArticlesFromCSV(String filePath) throws IOException {
        List<Article> articles = new LinkedList<Article>();
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Article article = new Article();
            article.setTitle(record.get(0)); //For some reason it does not allow "Item Title"
            article.setPublication(record.get("Publication Title"));
            article.setDOI(record.get("Item DOI"));
            article.setAuthors(record.get("Authors"));
            article.setYear(record.get("Publication Year"));
            article.setUrl(record.get("URL"));
            articles.add(article);
        }
        return articles;
    }

    public void addAbstractsToArticles(List<Article> articles) throws IOException {
        int counter = 0;
        for (Article a : articles) {
            counter++;
            //if (counter > 50) break;
            addAbstractToArticle(a);
        }
    }

    public void writeArticles(List<Article> articles, String filename) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write("Title,Authors,URL,Publication,Year,Doi,Abstract");
            writer.newLine();
            for (Article article : articles) {
                //double quotes to allow "," in the titles, abstract and everything...
                String line = "\"" + getDataForCsv(article.getTitle()) + "\","
                        + "\"" + getDataForCsv(article.getAuthors()) + "\","
                        + "\"" + getDataForCsv(article.getUrl()) + "\","
                        + "\"" + getDataForCsv(article.getPublication()) + "\","
                        + "\"" + getDataForCsv(article.getYear()) + "\","
                        + "\"" + getDataForCsv(article.getDOI()) + "\","
                        + "\"" + getDataForCsv(article.getPaperAbstract()) + "\"";
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

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

    private void addAbstractToArticle(Article article) throws IOException {
        Document doc = connect(article.getUrl());
        Elements abstracts = doc.select(".Abstract p");
        String paperAbstract = null;
        for(Element e : abstracts){
            if(paperAbstract == null){
                paperAbstract = e.text();
            }else{
                paperAbstract = paperAbstract + "\n\n" +e.text();
            }
        }
        article.setPaperAbstract(paperAbstract);
    }

    private Document connect(String url) throws IOException {
        try{
            Document doc = Jsoup.connect(url).get();
            return doc;
        }catch (HttpStatusException e){
            //wait as the server can be busy and try again
            try{
                wait(500);
            }catch(InterruptedException f){
                //silently ignore
            }
            return connect(url);
        }
    }

    private String getDataForCsv(String data) {
        if (data == null) {
            return "";
        } else {
            return data;
        }
    }

}
