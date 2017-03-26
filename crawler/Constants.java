/*
 * @author: talk-1
 * Description: Constant values
 * 
 */
package com.crawler;

public class Constants {

	public static String url = "jdbc:mysql://localhost:3306/tinnitus?verifyServerCertificate=false&useSSL=true";
	public static String username = "root";
	public static String password = "tiger";
	public static String USER_INTRODUCTION = "userintroduction";
	public static String TREATMENTS = "treatments";
	public static String REPLIES = "replies";
	public static String USER_INTRODUCTION_FILE = "./input/tinitusIntroduction.txt";
	public static String TREATMENT_FILE = "./input/tinitusTreatment.txt";
	public static String SENTIWORDNET = "./input/sentiwordnet.txt";
	public static String INTRODUCTION_URL = "https://www.tinnitustalk.com/forums/introduce-yourself.11/";
	public static String TREATMENTS_URL = "https://www.tinnitustalk.com/forums/treatments.13/";
	public static int TOTAL_INTRODUCTION_PAGES = 63;
	public static String NEW_LINE_SEPERATOR = "\n";
	public static String COMMA_DELIMITER = ",";

}
