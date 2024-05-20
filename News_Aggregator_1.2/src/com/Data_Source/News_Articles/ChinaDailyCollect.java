package com.Data_Source;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChinaDailyCollect {

    private static final int MAX_ARTICLES = 1000; // Set the maximum number of articles to scrape
    private static final String BASE_URL = "https://newssearch.chinadaily.com.cn/en/search?query=blockchain";
    private static final String OUTPUT_FILE = "input1.csv";

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Laptop\\OneDrive\\Máy tính\\CODE\\Other things\\demo\\chromedriver-win64\\chromedriver.exe");

        // Set ChromeOptions with user agent
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

        // Initialize ChromeDriver with ChromeOptions
        WebDriver driver = new ChromeDriver(options);

        // Maximize the browser window
        driver.manage().window().maximize();

        // Start crawling
        crawl(driver, BASE_URL);

        // Quit the driver
        driver.quit();
    }

    private static void crawl(WebDriver driver, String url) {
        try {
            Set<String> visitedUrls = new HashSet<>();
            int articleCount = 0;

            // Create a new CSV file
            try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
                // Load the first page
                driver.get(url);

                // Start a loop to load more results until the end
                while (true) {
                    // Scroll down to load more articles
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

                    // Wait for a brief moment to let the page load more articles
                    Thread.sleep(2000);

                    // Get all article elements
                    for (WebElement articleElement : driver.findElements(By.cssSelector(".art_detail h4 a"))) {
                        String articleUrl = articleElement.getAttribute("href");

                        // Check if URL has been visited to avoid duplicates
                        if (!visitedUrls.contains(articleUrl)) {
                            // Write article URL to CSV
                            writer.append(articleUrl).append("\n");
                            visitedUrls.add(articleUrl);
                            articleCount++;

                            if (articleCount >= MAX_ARTICLES) {
                                break;
                            }
                        }
                    }

                    try {
                        // Check if the "NEXT" button is present and click it
                        WebElement nextButton = driver.findElement(By.xpath("//div[@class='page rt']//a[contains(text(), 'NEXT')]"));
                        nextButton.click();

                        // Wait for a brief moment to let the page load more articles
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("No more pages to navigate.");
                        break;
                    }
                }
            }

            System.out.println("Crawling completed. Total articles found: " + articleCount);
            System.out.println("Results saved to: " + OUTPUT_FILE);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
