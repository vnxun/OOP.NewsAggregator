package com.example.Crawler;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Twitter extends SocialMedia{

    public Twitter(String Link, String Type, String Author, String Date, String Content, String Id, String Hashtag,
			String Category) {
		super(Link, Type, Author, Date, Content, Id, Hashtag, Category);
	}
    
    public static void main(String[] args) {
        //Initialize webdriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\chromedriver-win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://nitter.poast.org/");

        delay(3000,5000);
        
        //Search "blockchain"
        driver.findElement(By.name("q")).sendKeys(searchQuery);
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        delay(30000, 5000);
        driver.findElement(By.xpath("/html/body/div/div/ul/li[1]/a")).click();
        //Header
        news.add(new String[] { "Link", "Type", "Author", "Date", "Content", "Id", "Hashtag", "Category"}); 
        
        String strOfLink = "";
        String newsId = "";
        String strOfContent = "";
        String strOfDate = "";
        String strOfAuthor = "";
        String strOfHashtag = "";
        String strOfCategory = "";

        boolean keepScaping = true;
        long start = System.currentTimeMillis();
        long end = start + duration;
        //Crawling
        while (keepScaping && System.currentTimeMillis() < end) {
            //List of news in a page
            List<WebElement> items =  driver.findElements(By.className("timeline-item"));
            for (WebElement item : items) {
                String orgLink = item.findElement(By.tagName("a")).getAttribute("href");
                //Get link
                strOfLink ="https://twitter.com" + orgLink.substring(orgLink.indexOf("org") + 3);
                if(strOfLink.indexOf("#") != -1) {
                    newsId = strOfLink.substring(strOfLink.indexOf("status") + 7, strOfLink.indexOf("#"));
                }
                
                if (newsIds.contains(newsId) == false) {//The news has not been crawled
                    newsIds.add(newsId);
                    
                    strOfContent = item.findElement(By.cssSelector("div[class='tweet-content media-body']")).getText();
                    strOfAuthor = item.findElement(By.cssSelector("a[class='fullname']")).getText();
                    // get date
                    strOfDate = getStrDate(item);
                    //get Hashtags
                    strOfHashtag = getStrHashtag(strOfContent);
                    strOfCategory = "";
                    strOfCategory += addToCategory(item, "pinned", "is-pinned, ", "is-not-pinned, ");
                    strOfCategory += addToCategory(item, "retweet-header", "is-retweet, ", "is-not-retweet, ");
                    strOfCategory += addToCategory(item, "replying-to", "is-reply", "is-not-reply");
                    //get instnces of news and add its data to the list in the list
                    Twitter ttw =new Twitter(strOfLink, "Twitter tweet ", strOfAuthor, strOfDate, strOfContent, newsId, strOfHashtag, strOfCategory);
                    news.add(ttw.toStringArray());
                    //Stop the loop 
                    if (news.size() >= numberOfNews + 1) {
                        keepScaping = false;
                        break;
                    }

                }
                
            }
            exportCsv("C:\\Users\\admin\\Downloads\\SocialMediaNewsAggregator - Copy\\demo\\Files\\TwitterCrawlData_3.0.csv", news);
            
            print(news);
            
            try{
                driver.findElement(By.cssSelector("div[class='show-more']")).findElement(By.tagName("a")).click();
            }catch(org.openqa.selenium.NoSuchElementException e) {
                delay(10000, 2000);

                try{//Click "Load newest"
                    driver.findElement(By.linkText("Load newest")).click();
                }catch(org.openqa.selenium.NoSuchElementException e2) {
                    
                    delay(10000, 1000);
                    //Click "Tweets"
                    driver.findElement(By.xpath("/html/body/div/div/ul/li[1]/a")).click();

                }
            }
            
            
            delay(10000, 500);
        }
        delay(2000, 25000);
        
        driver.quit();
    }
    
    public static String addToCategory(WebElement item, String classname, String strTrue , String strFalse ) {
        String category = "";
        if (item.findElements(By.cssSelector("div[class='" + classname + "']")).size() > 0) {
            category += strTrue;
        }else{
            category += strFalse;
        }
        return category;

    }

    public static String getStrDate(WebElement item)  {
        //Format date
        String orgTime = item.findElement(By.className("tweet-date")).findElement(By.tagName("a")).getAttribute("Title");
        orgTime = orgTime.substring(0, orgTime.indexOf(" · "));
        String strMonth = orgTime.substring(0, 3);
        String strDay = orgTime.substring(4, orgTime.indexOf(","));
        if (strDay.length() == 1)
            strDay = "0" + strDay;
        String strYear = orgTime.substring(orgTime.indexOf(",") + 2, orgTime.indexOf(",") + 6);
        String strOfDate = strDay + "-" + strMonth + "-" + strYear;
        strOfDate = formatDate(strOfDate);
        return strOfDate;
    }


}
