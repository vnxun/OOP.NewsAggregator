package com.Data_Source;

import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class the_blockchainCollect {

    private static final int MAX_ARTICLES = 1000; // Set the maximum number of articles to scrape

    public static void main(String[] args) {
        for (int i = 101; i <= 700; i++) {
            String baseUrl = "https://www.the-blockchain.com/page/" + i + "/?s=Blockchain";
            crawl(baseUrl);
        }
    }

    private static void crawl(String url) {
        try {
            int articleCount = 0;

            // Create a new CSV file
            String fileName = "output7.csv";
            try (FileWriter writer = new FileWriter(fileName, true)) { // Append mode
                Document doc = Jsoup.connect(url).get();
                System.out.println("Connected to the URL: " + url); // Print statement after successful connection

                Elements articleElements = doc.select(".tdb_module_loop.td_module_wrap.td-animation-stack.td-cpt-post"); // Selecting article elements

                for (Element articleElement : articleElements) {
                    Element linkElement = articleElement.selectFirst(".entry-title a");
                    String articleUrl = linkElement.absUrl("href");

                    // Write article URL and title to CSV
                    writer.append(escapeCsvField(linkElement.text())).append(",");
                    writer.append(escapeCsvField(articleUrl)).append("\n");

                    articleCount++;

                    if (articleCount >= MAX_ARTICLES) {
                        System.out.println("Reached the maximum number of articles.");
                        break;
                    }
                }

                System.out.println("Crawling completed. Total articles found: " + articleCount);
            }

        } catch (HttpStatusException e) {
            // Skip printing anything for 403 Forbidden errors
            System.err.println("HttpStatusException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to escape CSV fields to handle special characters
    private static String escapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return "\"\"";
        }
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }
}
