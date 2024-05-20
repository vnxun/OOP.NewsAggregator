package com.example.Crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Facebook extends SocialMedia {

    public Facebook(String Link, String Type, String Author, String Date, String Content, String Id, String Hashtag,
			String Category) {
		super(Link, Type, Author, Date, Content, Id, Hashtag, Category);
	}
	
    @SuppressWarnings("null")
    public static void main(String[] args) throws IOException {
        //Initialize webdriver
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\chromedriver-win32\\chromedriver.exe");
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(ops);

        delay(3000, 5000);

        //Log in
        driver.get("https://www.facebook.com/");
        logIn(driver);

        delay(3000, 1000);

        //Search "blockchain"
        WebElement querySearch = driver
                .findElement(By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div[3]/div/div/div/div/div/label"));
        querySearch.click();

        WebElement querySearchInput = querySearch
                .findElement(
                        By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div[3]/div/div/div[1]/div/div/label/input"));
        querySearchInput.sendKeys(searchQuery);
        delay(500, 300);

        querySearchInput.sendKeys(Keys.ENTER);
        delay(5000, 2000);

        driver.findElement(By.linkText("Posts")).click();// Language setting: English US
        delay(15000, 500);

        //Header
        news.add(new String[] { "Link", "Type", "Author", "Date", "Content", "Id", "Hashtag", "Category" });

        String strOfLink = "";
        String newsId = "";
        String strOfContent = "";
        String strOfDate = "";
        String strOfAuthor = "";
        String strOfGroupName = "";
        String strOfHashtag = "";
        String strOfCategory = "";

        boolean keepScaping = true;
        long start = System.currentTimeMillis();
        long end = start + duration;

        int index = 0;
        WebElement item = null;
        //Crawling
        while (keepScaping && System.currentTimeMillis() < end) {
            try {
                //Get news
                item = driver.findElements(By.cssSelector("div[role='feed']"))
                        .get(driver.findElements(By.cssSelector("div[role='feed']")).size() - 1)
                        .findElements(By.cssSelector("div[role='article']")).get(index);
                
                //Crawl data based on categories of news
                if (item.findElements(By.cssSelector("a[role='link']")).get(2).getAttribute("href")
                        .contains("/posts/")) {

                    //Get link
                    strOfLink = item.findElements(By.cssSelector("a[role='link']")).get(3).getAttribute("href");
                    newsId = strOfLink.substring(strOfLink.indexOf("/posts/") + 7, strOfLink.indexOf("?"));
                    //Skip crawled data
                    if (newsIds.contains(newsId)) {
                        index++;
                        continue;
                    } else {
                        newsIds.add(newsId);
                    }
                    //Get raw data
                    strOfDate = item.findElements(By.cssSelector("a[role='link']")).get(3).getText();
                    strOfAuthor = item.findElements(By.cssSelector("a[role='link']")).get(2).getText();
                    strOfCategory = "personal-post, ";

                } else if (item.findElements(By.cssSelector("a[role='link']")).get(2).getAttribute("href")//
                        .contains("_permalinks=")) {

                    strOfLink = item.findElements(By.cssSelector("a[role='link']")).get(4).getAttribute("href");
                    newsId = strOfLink.substring(strOfLink.indexOf("/groups/") + 8, strOfLink.indexOf("/?multi")) + "/"
                            + strOfLink.substring(strOfLink.indexOf("permalink") + 11, strOfLink.indexOf("&"));
                    
                    //Skip already crawled data
                    if (newsIds.contains(newsId)) {
                        index++;
                        continue;
                    } else {
                        newsIds.add(newsId);
                    }
                    //Get raw data
                    strOfDate = item.findElements(By.cssSelector("a[role='link']")).get(4).getText();
                    strOfAuthor = item.findElements(By.cssSelector("a[role='link']")).get(3).getText();
                    strOfGroupName = item.findElements(By.cssSelector("a[role='link']")).get(2).getText();
                    strOfAuthor += "/" + strOfGroupName;
                    strOfCategory = "group-post, ";
                    

                } else {
                    index++;
                    continue;
                }

                //Get content and click "See more" button
                strOfContent = getStrContent(item);

                //Crate date
                strOfDate = getStrDate(strOfDate);

                //get hashtags from content
                strOfHashtag = getStrHashtag(strOfContent);

                //Categorize the news
                if (item.findElements(By.tagName("img")).size() > 0) {
                    strOfCategory += "contains-image, ";
                } else {
                    strOfCategory += "not-contain-image, ";
                }

                if (strOfHashtag != "") {
                    strOfCategory += "contains-hashtag";
                } else {
                    strOfCategory += "not-contain-hashtag";
                }

                //get an instance of the news and add its data to the news list
                Facebook fbp = new Facebook(strOfLink, "Facebook post", strOfAuthor, strOfDate, strOfContent,
                        newsId, strOfHashtag, strOfCategory);
                news.add(fbp.toStringArray());

                index++;

                //Export data at the end
                if (news.size() >= numberOfNews + 1) {
                    exportCsv(
                            "C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\FacebookCrawlData_6.0.csv",
                            news);
                    keepScaping = false;
                }

            } catch (java.lang.IndexOutOfBoundsException e) {
                //Export data at the end of each page
                exportCsv(
                        "C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\FacebookCrawlData_6.0.csv",
                        news);

                //Scroll down at the end of the page
                if (driver.findElements(By.cssSelector("div[role='feed']"))
                        .get(driver.findElements(By.cssSelector("div[role='feed']")).size() - 1)
                        .findElements(By.cssSelector("div[role='article']")).size() <= index) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollBy(0, 4000)", "");

                    print(news);

                    delay(8000, 8000);
                } else {
                    index++;
                    continue;
                }

                delay(8000, 8000);

            }

        }
        delay(25000, 1500);

        //Close the browser
        driver.quit();
    }

    public static void logIn(WebDriver driver) throws IOException {
        File cookieFile = new File("C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\cookies.txt");
        if (cookieFile.createNewFile()) {
            //get cookie.txt at the first log in
            Scanner sc = new Scanner(
                    new File("C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\private_info.txt"));
            String[] info = sc.nextLine().split("[;]");

            driver.findElement(By.id("email")).sendKeys(info[0]);

            delay(5000, 3000);

            driver.findElement(By.id("pass")).sendKeys(info[1]);
            driver.findElement(By.id("pass")).sendKeys(Keys.ENTER);

            sc.close();

            delay(10000, 5000);

            FileWriter fos = new FileWriter(cookieFile);
            BufferedWriter bos = new BufferedWriter(fos);

            for (Cookie cookie : driver.manage().getCookies()) {
                bos.write(cookie.getName() + ";" + cookie.getValue() + ";" +
                        cookie.getDomain() + ";" + cookie.getPath() + ";" + cookie.getExpiry() + ";"
                        + cookie.isSecure());
                bos.newLine();
            }
            bos.flush();
            bos.close();
            fos.close();
        } else {
            //Read cookie.txt and log in at the next time
            Scanner scanner = new Scanner(
                    new File("C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\cookies.txt"));
            while (scanner.hasNextLine()) {
                String[] cookieStrings = scanner.nextLine().split("[;]");
                Cookie cookie = new Cookie(cookieStrings[0], cookieStrings[1], cookieStrings[3]);
                driver.manage().addCookie(cookie);
            }
            scanner.close();

            delay(5000, 3000);

            driver.get("https://www.facebook.com/");

        }
    }

    public static String getStrDate(String strOfDate) {
        //Format date
        String strDay = "";
        String strMonth = "";
        String strYear = "";
        if (strOfDate.contains("at")) {
            strMonth = strOfDate.substring(0, 3);
            strDay = strOfDate.substring(strOfDate.indexOf(" ") + 1, strOfDate.indexOf("at") - 1);
            if (strDay.length() == 1) {
                strDay = "0" + strDay;
            }
            strYear = Year.now().toString();
            strOfDate = formatDate(strDay + "-" + strMonth + "-" + strYear);

        } else if (strOfDate.contains(",")) {
            strMonth = strOfDate.substring(0, 3);
            strDay = strOfDate.substring(strOfDate.indexOf(" ") + 1, strOfDate.indexOf(","));
            if (strDay.length() == 1) {
                strDay = "0" + strDay;
            }
            strYear = strOfDate.substring(strOfDate.indexOf(",") + 2, strOfDate.indexOf(",") + 6);
            strOfDate = formatDate(strDay + "-" + strMonth + "-" + strYear);
        } else if (strOfDate.contains("d")) {
            LocalDate date = LocalDate.now();
            LocalDate localTime = date
                    .minusDays(Integer.parseInt(strOfDate.substring(0, strOfDate.indexOf("d"))));
            strOfDate = localTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
            LocalDateTime time = LocalDateTime.now();
            LocalDateTime localTime = time
                    .minusHours(Integer.parseInt(strOfDate.substring(0, strOfDate.indexOf("h"))));
            strOfDate = localTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return strOfDate;
    }

    public static String getStrContent(WebElement item) {
        //Get content and click "See more" button
        String strOfContent = item.findElements(By.cssSelector("div[dir='auto']")).get(0).getText();
        if (strOfContent.contains("See more")) {
            item.findElement(By.cssSelector("div[dir='auto']")).findElement(By.cssSelector("div[tabindex='0']"))
                    .click();
            strOfContent = item.findElements(By.cssSelector("div[dir='auto']")).get(0).getText();
        }
        return strOfContent;
    }

}

