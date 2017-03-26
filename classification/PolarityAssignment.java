/**
 * @author: Talk-1
 * Description: Polarity Assignment to the Introduction and Treatments posts obtained from Tinnitus
 */
package com.classification;

import java.io.*;
import java.nio.file.Paths;

import com.crawler.Constants;
import com.dboperations.InsertOperations;

public class PolarityAssignment {

	/**
	 * String that stores the text to guess its polarity.
	 */
	public String text;
	public Sentiwordnet sentiwordnet;
	public int counter = 1;
	public String splChrs = "!-/@#$%^&_+=():.,|\"\'";
	
	/**
	 * SentiWordNet object to query the polarity of a word.
	 */
	public PolarityAssignment(String sentiWordNetFile){
		try{
			sentiwordnet =	new Sentiwordnet(sentiWordNetFile);	
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/* 
	 * Getter setter for counter
	 */
	public void setCounter(int count){
		counter = count;
	}
	
	public int getCounter(){
		return counter;
	}
			
 	/**
	 * This method loads the text to be classified.
	 * @param fileName The name of the file that stores the text.
	 */
	public void load(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Paths.get(fileName).toString()));
			String line,polarity,user,date,description;
			String[] lineText;
			while ((line = reader.readLine()) != null) {
				lineText = line.split(",");
				user = lineText[0];
				date = lineText[2]+","+lineText[3];
				description = lineText[4].toLowerCase().replaceAll("["+splChrs+"]", "");
				//System.out.println("line before stopwords removal\n"+line);
				line = Stopwords.removeStopWords(line);
				//System.out.println("line after stopwords removal\n"+line);
    			/*polarity = classifyAllPOSY(line);
    			if(fileName == Constants.USER_INTRODUCTION_FILE){
    				InsertOperations.insertPolarity(polarity,user,date,Constants.USER_INTRODUCTION);
    			}
    			else{
    				InsertOperations.insertPolarity(polarity,user,date,Constants.TREATMENTS);
    			}
    			*/
				double rank = assignrank(line);
    			if(fileName == Constants.USER_INTRODUCTION_FILE){
    				InsertOperations.insertRank(rank,user,date,Constants.USER_INTRODUCTION);
    			}
    			else{
    				InsertOperations.insertRank(rank,user,date,Constants.TREATMENTS);
    			}
            }
			// System.out.println("===== Loaded text data: " + fileName + " =====");
			reader.close();
			// System.out.println(text);
		}
		catch (IOException e) {
			System.out.println("Problem found when reading: " + fileName);
		}
	}

	/**
	 * This method assign rank to each post 
	 */
	public double assignrank(String text){
			String delimiters = "\\W";
			String[] tokens = text.split(delimiters);
			double feeling = 0;
			for (int i = 0; i < tokens.length; ++i) {
				// Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
				if (!tokens[i].equals("")) {
					// Search as adjetive
					feeling+= sentiwordnet.extract(tokens[i]);
				}
			}
			return feeling;
	}
	/**
	 * This method performs the classification of the text.
	 * Algorithm: Use all POS, say "yes" in case of 0.
	 * @return An string with "no" (negative) or "yes" (positive).
	 */
	public String classifyAllPOSY(String text) {
		String polarity = null;
		try {
			String delimiters = "\\W";
			String[] tokens = text.split(delimiters);
			double feeling = 0;

			for (int i = 0; i < tokens.length; ++i) {
				// Add weights -- positive => +1, strong_positive => +2, negative => -1, strong_negative => -2
				if (!tokens[i].equals("")) {
					// Search as adjetive
					feeling+= sentiwordnet.extract(tokens[i]);
					if(feeling <= -1){
						polarity = "Negative";
					}
					else if(feeling >= -1 && feeling <= 1){
						polarity = "Neutral";
					}
					else{
						polarity = "Positive";
					}
					}
			}
			int count = getCounter();
			count++;
			System.out.println("Polarity of post" + counter + " is "+polarity);
			setCounter(count);
		}
		catch (Exception e) {
			System.out.println("Problem found when classifying the text");
		}
		return polarity;
		// Returns "yes" in case of 0
	}
	

	/**
	 * Main method.
	 * Usage: java SentiWordNetDemo <file>
	 * @param args The command line args.
	 */
	public static void main (String[] args) {
			/*PolarityAssignment classifier;
			//Path curdir = System.getProperty("user.dir");
			Path introFile  = Paths.get("./input/introduction.txt");
			Path sentiWordNetFile = Paths.get("./input/sentiwordnet.txt");
			Path stopwordsFile = Paths.get("./input/stopwords_en.txt");
			classifier = new PolarityAssignment(sentiWordNetFile,stopwordsFile);
			//Loading the file 
			classifier.load(introFile);
			// Comment the approaches you do not want to check
			 * */
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
	
	} 
}