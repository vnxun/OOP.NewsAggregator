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
import java.util.List;
import java.util.Set;

public class TheGuardianCollect {

    private static final int MAX_ARTICLES = 100; // Set the maximum number of articles to scrape
    private static final String BASE_URL = "https://www.google.co.uk/search?as_q=&as_epq=Blockchain&as_oq=&as_eq=&as_nlo=&as_nhi=&lr=&cr=&as_qdr=all&as_sitesearch=www.theguardian.com&as_occt=any&as_filetype=&tbs=#ip=1";
    private static final String OUTPUT_FILE = "output5.csv";

    public static void main(String[] args) {
        // Set the path to chromedriver.exe
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
            // Load the page
            driver.get(url);

            // Scroll down and click "Show more" button until the desired number of articles is reached
            while (articleCount < MAX_ARTICLES) {
                // Scroll to the bottom of the page
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

                // Wait for a brief moment to let the page load more articles
                Thread.sleep(2000);

                // Get all article elements
                for (WebElement articleElement : driver.findElements(By.cssSelector("a.UWckNb"))) {
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

                // Check if "Show more" button is present
                List<WebElement> showMoreButtons = driver.findElements(By.cssSelector("button#sfopt"));
                if (showMoreButtons.size() > 0) {
                    // Click on the first "Show more" button
                    showMoreButtons.get(0).click();
                } else {
                    // No "Show more" button found, exit the loop
                    break;
                }
            }

            System.out.println("Crawling completed. Total articles found: " + articleCount);
            System.out.println("Results saved to: " + OUTPUT_FILE);
        }

    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
}

}
