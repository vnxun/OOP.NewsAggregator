package com.Data_Source;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockchainNewsCrawl implements Runnable {
    private static final int MAX_DEPTH = 3;
    private static final Logger logger = Logger.getLogger(BlockchainNewsCrawl.class.getName());

    private Thread thread;
    private String firstLink;
    private boolean visited = false; 

    private String title;
    private String date;
    private String author;
    private String summary;
    private String content;
    private String category;
    private String type;
    private String keywords;

    public BlockchainNewsCrawl(String link) {
        System.out.println("PageCrawler created");
        firstLink = link;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        String targetDomain = getDomain(firstLink);
        crawl(0, firstLink, targetDomain);
    }

    private void crawl(int level, String url, String targetDomain) {
        if (level <= MAX_DEPTH && url.startsWith(targetDomain)) {
            Document doc = request(url);

            if (doc != null) {
                String articleLink = url;

                title = extractArticleTitle(doc);
                date = extractArticleDate(doc);
                author = extractArticleAuthor(doc);
                summary = extractArticleSummary(doc);
                content = extractArticleContent(doc);
                category = extractArticleCategory(doc);
                type = extractArticleType(doc);
                keywords = extractArticleKeywords(doc);

                if (type.equals("article")) {
                    printOutput();
                    writeOutputToCSV("output4.csv");
                } else {
                    logger.log(Level.INFO, "The URL does not represent an article");
                }
            }
        }
    }

    private String extractArticleAuthor(Document doc) {
        Element authorElement = doc.selectFirst("meta[name=author]");
        return authorElement != null ? authorElement.attr("content") : "Author not found";
    }

    private String extractArticleTitle(Document doc) {
        String title = doc.title();
        if (title.isEmpty()) {
            // Try to extract title from meta tags
            Element metaTitle = doc.select("meta[property=og:title]").first();
            if (metaTitle != null) {
                title = metaTitle.attr("content");
            }
        }
        return title;
    }
    
    private String extractArticleKeywords(Document doc) {
        Element keywordsElement = doc.selectFirst("meta[name=keywords]");
        return keywordsElement != null ? keywordsElement.attr("content") : "Keywords not found";
    }

    private String extractArticleType(Document doc) {
        Element typeElement = doc.selectFirst("meta[property=og:type]");
        return typeElement != null ? typeElement.attr("content") : "Type not found";
    }
    
    private String extractArticleContent(Document doc) {
        Element contentDiv = doc.selectFirst("div.textbody");
        if (contentDiv != null) {
            StringBuilder contentBuilder = new StringBuilder();
            Elements paragraphElements = contentDiv.select("p");
            for (Element paragraph : paragraphElements) {
                contentBuilder.append(paragraph.text()).append("\n");
            }
            return contentBuilder.toString().trim();
        } else {
            return "Content not found";
        }
    }

    private boolean isBlockchainRelated(String content) {
        return countBlockchainOccurrences(content) >= 3;
    }

    private String extractArticleSummary(Document doc) {
        Element summaryElement = doc.selectFirst("meta[property=og:description]");
        return summaryElement != null ? summaryElement.attr("content") : "Summary not found";
    }

    private String extractArticleCategory(Document doc) {
        Element categoryElement = doc.selectFirst("a.badge.text-bg-danger");
        return categoryElement != null ? categoryElement.text() : "Category not found";
    }

    private String extractArticleDate(Document doc) {
        Element dateElement = doc.selectFirst("meta[name=datePublished]");
        return dateElement != null ? dateElement.attr("content") : "Date not found";
    }
    private Document request(String url) {
        try {
            Connection con = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .timeout(20000); // Set read timeout to 10 seconds (adjust as needed)
            Document doc = con.get();
    
            if (con.response().statusCode() == 200) {
                return doc;
            }
            return null;
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to connect to URL: " + url, e);
            return null;
        }
    }
    

    private String getDomain(String url) {
        String[] parts = url.split("/");
        return parts[0] + "//" + parts[2];
    }

    private String getOutputAsCSV() {
        StringBuilder csvOutput = new StringBuilder();
        csvOutput.append("\"").append(firstLink).append("\"").append(",");
        csvOutput.append("\"").append(title).append("\"").append(",");
        csvOutput.append("\"").append(date).append("\"").append(",");
        csvOutput.append("\"").append(author).append("\"").append(",");
        csvOutput.append("\"").append(summary).append("\"").append(",");
        csvOutput.append("\"").append(content).append("\"").append(",");
        csvOutput.append("\"").append(category).append("\"").append(",");
        csvOutput.append("\"").append(type).append("\"").append(",");
        csvOutput.append("\"").append(keywords).append("\"").append("\n");
        return csvOutput.toString();
    }

    private void printOutput() {
        System.out.println("Link: " + firstLink);
        System.out.println("Title: " + title);
        System.out.println("Date: " + date);
        System.out.println("Author: " + author);
        System.out.println("Summary: " + summary);
        System.out.println("Content: " + "\" " + content + "\"");
        System.out.println("Keywords: " + keywords);
        System.out.println();
    }
    
    public Thread getThread() {
        return thread;
    }
    private int countBlockchainOccurrences(String text) {
        int count = 0;
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.equalsIgnoreCase("blockchain")) {
                count++;
            }
        }
        return count;
    }

    public void writeOutputToCSV(String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            String csvOutput = getOutputAsCSV();
            writer.append(csvOutput);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to write output to CSV file", e);
        }
    }
    private static ArrayList<String> readLinksFromCSV(String fileName) {
        ArrayList<String> links = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                links.add(line.replace("\"", "").trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return links;
    }

    public void markAsVisited() {
        visited = true;
    }

    public boolean hasBeenVisited() {
        return visited;
    }
    public static void main(String[] args) {
        while (true) {
            ArrayList<String> links = readLinksFromCSV("input2.csv");
            ArrayList<BaseCrawler> bots = new ArrayList<>();

            for (String link : links) {
                BaseCrawler crawler = new BaseCrawler(link);
                bots.add(crawler);
            }

            for (BaseCrawler crawler : bots) {
                try {
                    crawler.getThread().join();
                    crawler.writeOutputToCSV("output2.csv");
                    crawler.markAsVisited(); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                TimeUnit.MINUTES.sleep(5); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
