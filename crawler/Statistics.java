package com.crawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
	private Map<String, Integer> introduction = new HashMap<String, Integer>();
	private Map<String, Integer> treatment = new HashMap<String, Integer>();
	private Map<String, Integer> userMap = new HashMap<String, Integer>();
	public BufferedReader reader;
	public Statistics(){
		try{
			reader = new BufferedReader(new FileReader(Paths.get("./input/keywords.txt").toString()));
			String line = "";
			while((line = reader.readLine())!= null)
			{
				introduction.put(line, 0);
				treatment.put(line, 0);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
		}
		}
	
	public void findStatistics(String type,String desc,String user){
		if(type == "introduction"){
			for(Map.Entry<String, Integer> entry : introduction.entrySet()){
				if(desc.toLowerCase().matches(".*\\b"+entry.getKey().toLowerCase()+"\\b.*")){
					introduction.put(entry.getKey(), entry.getValue() + 1);
					//System.out.println("Matched: "+entry.getKey()+"Count: "+entry.getValue());
					if(userMap.containsKey(user)){
						userMap.put(user, userMap.get(user)+1);
					}
					else{
						userMap.put(user, 1);
					}
				}
			}
		}
		else{
			for(Map.Entry<String, Integer> entry : treatment.entrySet()){
				if(desc.toLowerCase().matches(".*\\b"+entry.getKey().toLowerCase()+"\\b.*")){
					treatment.put(entry.getKey(), entry.getValue() + 1);
					//System.out.println("Matched: "+entry.getKey()+"Count: "+entry.getValue());
					if(userMap.containsKey(user)){
						userMap.put(user, userMap.get(user)+1);
					}
					else{
						userMap.put(user, 1);
					}
				}
			}
		}

	}
	
	public void display(String type){
		if(type == "introduction"){
			System.out.println("Statistics of Introduction");
			for(Map.Entry<String, Integer> entry : introduction.entrySet()){
				System.out.println("Keyword: "+entry.getKey() + "Count: "+entry.getValue());
			}
		}
		else{
			System.out.println("Statistics of Treatment");
			for(Map.Entry<String, Integer> entry : treatment.entrySet()){
				System.out.println("Keyword: "+entry.getKey() + "Count: "+entry.getValue());
			}
		}
	}
	
	/*
	 * Writing statistics to file
	 */
	
	public void write(String type) throws IOException{
		//final String dir = System.getProperty("user.dir");
		final String COMMA_DELIMITER = ",";
	    final String NEW_LINE_SEPARATOR = "\n";
	    final String FILE_HEADER = "Keyword, Count";
	    FileWriter writer_1 = new FileWriter("./output/introduction.csv",true);
		FileWriter writer_2 = new FileWriter("./output/treatment.csv",true);
		FileWriter writer_3 = new FileWriter("./output/users.csv",true);
		if(type == "introduction"){
			writer_1.append(FILE_HEADER.toString());
			writer_3.append(NEW_LINE_SEPARATOR);
			for(Map.Entry<String, Integer> entry : introduction.entrySet()){
				writer_1.append(entry.getKey());
				writer_1.append(COMMA_DELIMITER);
				writer_1.append(entry.getValue().toString());
				writer_1.append(NEW_LINE_SEPARATOR);
				
			}
		}
		else if(type=="treatment"){
			writer_2.append(FILE_HEADER.toString());
			writer_3.append(NEW_LINE_SEPARATOR);
			for(Map.Entry<String, Integer> entry : treatment.entrySet()){
				writer_2.append(entry.getKey());
				writer_2.append(COMMA_DELIMITER);
				writer_2.append(entry.getValue().toString());
				writer_2.append(NEW_LINE_SEPARATOR);
				
			}
		}
		else{
			
			writer_3.append("User,Count".toString());
			writer_3.append(NEW_LINE_SEPARATOR);
			for(Map.Entry<String, Integer> entry : userMap.entrySet()){
				writer_3.append(entry.getKey());
				writer_3.append(COMMA_DELIMITER);
				writer_3.append(entry.getValue().toString());
				writer_3.append(NEW_LINE_SEPARATOR);
				
			}
		}
		writer_1.close();
		writer_2.close();
		writer_3.close();
		reader.close();
		
		
	}

}
