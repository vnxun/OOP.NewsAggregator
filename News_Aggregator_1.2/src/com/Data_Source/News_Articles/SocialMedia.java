package com.example.Crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.opencsv.CSVWriter;

public abstract class SocialMedia extends Data_Source{
    protected String Id;
	protected String Hashtag;
	protected String Category;
	protected String Type;
    protected static String searchQuery = "blockchain";//search keyword
    protected static Integer numberOfNews = 1000;//number of news to crawl
    protected static long duration = 3600 * 1000;//time to crawl data
    protected static ArrayList<String> newsIds = new ArrayList<String>();
    protected static List<String[]> news = new ArrayList<String[]>();
	public SocialMedia(String Link, String Type, String Author, String Date, String Content, String Id, String Hashtag, String Category) {
		super(Link, Author, Date, Content);
		this.Type = Type;
		this.Id = Id;
		this.Hashtag = Hashtag;
		this.Category = Category;
	}
	public String[] toStringArray() {
		return new String[]{this.Link, this.Type, this.Author, this.Date, this.Content, this.Id, this.Hashtag, this.Category};
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getHashtag() {
		return Hashtag;
	}
	public void setHashtag(String hashtag) {
		Hashtag = hashtag;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	
    public static void exportCsv(String filePath, List<String[]> datalist) {
        File file = new File(filePath); 
    
        try { 
            FileWriter outputfile = new FileWriter(file); 
            CSVWriter writer = new CSVWriter(outputfile); 
    
            writer.writeAll(datalist); 
            writer.close(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        
        } 
    }

    public static void delay(int orgTime, int random) {
        try {
            Thread.sleep(new Random().nextInt(random) + orgTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void print(List<String[]> news){
        System.out.println("Current number of crawled news : " + (news.size() - 1));
        System.out.println("================================================");
    }

    public static String formatDate(String strOfDate) {
        LocalDate time = LocalDate.parse(strOfDate, DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH));
        strOfDate = time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH));
        return strOfDate;
    }

    public static String getStrHashtag(String strOfContent) {
        Boolean firstHashtag = true;
        String strOfHashtag = "";
        for (int i = 0; i < strOfContent.length(); i++) {
            if (strOfContent.charAt(i) == '#') {
                try{
                    if (firstHashtag){
                        strOfHashtag += strOfContent.substring(i, strOfContent.indexOf(" ", i));
                        firstHashtag = false;
                    }else{
                        strOfHashtag += ", " + strOfContent.substring(i, strOfContent.indexOf(" ", i));
                    }
                }catch(java.lang.StringIndexOutOfBoundsException e){
                    if (firstHashtag){
                        strOfHashtag += strOfContent.substring(i);
                        firstHashtag = false;
                    }else{
                        strOfHashtag += ", " + strOfContent.substring(i);
                    }

                }
            }
        }
        return strOfHashtag;
    }

}
