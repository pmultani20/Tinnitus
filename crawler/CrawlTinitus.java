package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.classification.PolarityAssignment;
import com.dboperations.InsertOperations;


/**
 * Program to Crawl the Posts from Tinitustalk.com
 */
public class CrawlTinitus {

	private String fileIntro;
	private String fileTreatment;
	Statistics st = new Statistics();
	public int counter;
	public String splChrs = "!-/@#$%^&_+=():.,|\"\'";

	/*
	 * Parameterised constructor
	 */
	public CrawlTinitus(String file1, String file2, int count) {
		fileIntro = Paths.get(file1).toString();
		fileTreatment = Paths.get(file2).toString();
		counter = count;

	}

	/*
	 * Getter setter method
	 */
	public void setCount(int count) {
		counter = count;
	}

	public int getCount() {
		return counter;
	}

	public void run() {
		// Validate.isTrue(args.length == 1, "usage: supply url to fetch");
		// String url = args[0];

		String urlIntro = Constants.INTRODUCTION_URL;
		String urlTreatment = Constants.TREATMENTS_URL;
		// System.out.println("Introduction URL is: "+urlIntro+"\nTreatment URL
		// is: "+urlTreatment);
		ArrayList<String> urllist = new ArrayList<String>();
		urllist.add(urlIntro);
		int count;

		// Adding all 63 pages into arraylist
		for (int i = 2; i <= Constants.TOTAL_INTRODUCTION_PAGES; i++) {
			urllist.add(urlIntro + "page-" + i);
		}
		/*
		 * Deleting the file before crawling
		 */

		deleteFile();

		try {
			// Crawling Introduction
			for (int j = 0; j < urllist.size(); j++) {
				// System.out.println(urllist.get(j));
				count = getCount();
				// if(j==0)
				crawlIntroduction(urllist.get(j), count);

			}

			// Writing the statistics to file
			st.write("introduction");
			st.display("introduction");

			// Crawling Treatment
			setCount(0);
			count = getCount();
			crawlTreatment(urlTreatment, count);
			count = getCount();
			crawlTreatment(urlTreatment + "page-2", count);
			st.write("treatment");
			st.display("treatment");
			st.write("users");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Deleting the crawled data files
	 */
	public void deleteFile() {
		File file1 = new File(fileIntro);
		File file2 = new File(fileTreatment);
		file1.delete();
		file2.delete();
	}

	/*
	 * Crawling Introduction posts from http://www.tinnitustalk.com/
	 */
	private void crawlIntroduction(String url, int count) throws IOException {
		Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("div.titleText");
		FileWriter writer = new FileWriter(fileIntro, true);
		for (Element link : links) {
			String publishDate = "";
			String title = link.children().select("a.PreviewTooltip").text().replaceAll("[" + splChrs + "]", "");
			String user = link.children().select("a.username").text();
			publishDate = link.children().select("abbr.DateTime").text();
			System.out.println("before if if" + publishDate);
			if (publishDate.isEmpty()) {
				publishDate = link.children().select("span.DateTime").text();
			}
			String suburl = link.children().select("a.PreviewTooltip").attr("href");
			String myurl = link.baseUri() + "/" + suburl;
			String description = Jsoup.connect(myurl).get().select("blockquote").first().text()
					.replaceAll("[" + splChrs + "]", "");

			/* Displaying the data on the screen */

			System.out.println("Post " + count);
			System.out.println("User: " + user);
			System.out.println("Title: " + title);
			System.out.println("Publish Date: " + publishDate);
			System.out.println("URL: " + link.children().select("a.PreviewTooltip").attr("href"));
			System.out.println("Description: " + description);
			System.out.println("");
			
			try{
			/* calling Statistics function */
			st.findStatistics("introduction", description, user);

			/* Writing to database */
			InsertOperations.insert(Constants.USER_INTRODUCTION, user, title, publishDate, description);

			/* Writing the data to file */


			    writer.append(user);
			    writer.append(Constants.COMMA_DELIMITER);
			    writer.append(title);
			    writer.append(Constants.COMMA_DELIMITER);
			    writer.append(publishDate);
			    writer.append(Constants.COMMA_DELIMITER);
			    //writer.write("URL: "+link.children().select("a.PreviewTooltip").attr("href")+"\n");
			    writer.append(description);
    			writer.append(Constants.NEW_LINE_SEPERATOR);
			    
			} catch (IOException e) {
	            System.out.println("Error in CsvFileWriter !!!");
	            e.printStackTrace();

			}
			count++;

		}
		writer.close();
		System.out.println("Data is written to file " + fileIntro);
		setCount(count);

	}

	/*
	 * Crawling treatments posts from http://www.tinnitustalk.com
	 */

	private void crawlTreatment(String url,int count) throws IOException {
    	Document doc = Jsoup.connect(url).get();
    	Elements links = doc.select("div.titleText");
    	Elements urls = doc.select("a.PreviewTooltip");
	    FileWriter writer = new FileWriter(fileTreatment,true);
	    
    	for (Element link:links) {
    			String title = link.children().select("a.PreviewTooltip").text().replaceAll("["+splChrs+"]", "");
    			String user = link.children().select("a.username").text();
    			String publishDate = link.children().select("abbr.DateTime").text();
    			if(publishDate.isEmpty()){
    				publishDate = link.children().select("span.DateTime").text();
    			}
    			String suburl = link.children().select("a.PreviewTooltip").attr("href");
    			String myurl = link.baseUri()+"/"+suburl;
    			String description = Jsoup.connect(myurl).get().select("blockquote").first().text().replaceAll("["+splChrs+"]", "");
    			
    			/*
    			 * Finding the statistics
    			 */
    			
    			st.findStatistics("treatment",description,user);
    			
    			/* 
    			 * Writing data to Mysql Database
    			 */
    			/* Writing to database */
					InsertOperations.insert(Constants.TREATMENTS,user,title,publishDate,description);
   			
    			/*
    			 * Writing data to file
    			 */
    			try{

    			    writer.append(user);
    			    writer.append(Constants.COMMA_DELIMITER);
    			    writer.append(title);
    			    writer.append(Constants.COMMA_DELIMITER);
    			    writer.append(publishDate);
    			    writer.append(Constants.COMMA_DELIMITER);
    			    //writer.write("URL: "+link.children().select("a.PreviewTooltip").attr("href")+"\n");
    			    writer.append(description);
        			writer.append(Constants.NEW_LINE_SEPERATOR);
    			    
    			} catch (IOException e) {
    	            System.out.println("Error in CsvFileWriter !!!");
    	            e.printStackTrace();

    			}
    	    	count++;
			
    	}

    	setCount(count);
    	writer.close();
    	System.out.println("Data is written to file "+fileTreatment);
    }

	/*
	 * Main method for initialization
	 */
	public static void main(String[] args) {
		String file1 = Constants.USER_INTRODUCTION_FILE;
		String file2 = Constants.TREATMENT_FILE;
		File intro = new File(Paths.get(file1).toString());
		File treat = new File(Paths.get(file2).toString());
		//if(!intro.exists() && !treat.exists()){
		//Crawling the data and Statistics calculation
		CrawlTinitus crawl = new CrawlTinitus(file1, file2, 0);
		crawl.run();
		//}
		
		/*
		//Polarity assignment
		PolarityAssignment classifier;
		//Path curdir = System.getProperty("user.dir");
		String introFile  = Constants.USER_INTRODUCTION_FILE;
		String treatmentfile  = Constants.TREATMENT_FILE;
		String sentiWordNetFile = Constants.SENTIWORDNET;
		classifier = new PolarityAssignment(sentiWordNetFile);
		//Loading the file 
		classifier.load(introFile);
		classifier.load(treatmentfile);
		// Comment the approaches you do not want to check
		*/
	}

}
