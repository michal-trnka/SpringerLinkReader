package edu.baylor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
        for (Article a : articles) {
            addAbstractToArticle(a);
        }
    }

    private void addAbstractToArticle(Article article) throws IOException {
        Document doc = Jsoup.connect(article.getUrl()).get();
        Elements abstracts = doc.select(".Abstract p");
        if (abstracts.size() == 1) {
            article.setPaperAbstract(abstracts.get(0).text());
        }else if (abstracts.size() >1 ){
            System.out.println("Something strange");
        }
    }
}
