package com.Data_Source;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class BlockchainNewsCollect {

    private static final int MAX_ARTICLES = 10000; 
    private static final String BASE_URL = "https://blockchain.news/search/Blockchain";
    private static final String OUTPUT_FILE = "output12.csv";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Laptop\\OneDrive\\Máy tính\\CODE\\Other things\\demo\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        crawl(BASE_URL);

        driver.quit();
    }

    private static void crawl(String url) {
        try {
            Set<String> visitedUrls = new HashSet<>();
            int articleCount = 0;
    
            try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
                int pageNumber = 1;
                boolean hasNextPage = true;
    
                while (hasNextPage && articleCount < MAX_ARTICLES) {
                    Document doc = Jsoup.connect(url + "?page=" + pageNumber).get();
    
                    Elements articleElements = doc.select(".card.mb-4");
    
                    for (Element articleElement : articleElements) {
                        Element linkElement = articleElement.selectFirst("h3 a");
                        String articleUrl = linkElement.attr("href");

                        String fullUrl = "https://blockchain.news" + articleUrl;

                        if (!visitedUrls.contains(fullUrl)) {
                            writer.append(fullUrl).append("\n");
                            visitedUrls.add(fullUrl);
                            articleCount++;
                            if (articleCount >= MAX_ARTICLES) {
                                break;
                            }
                        }
                    }
    
                    hasNextPage = doc.select(".pagination li.next").size() > 0;
    
                    pageNumber++;
                }
    
                System.out.println("Crawling completed. Total articles found: " + articleCount);
                System.out.println("Results saved to: " + OUTPUT_FILE);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}