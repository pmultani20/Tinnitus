package com.classification;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class Stopwords {

	public static String removeStopWords(String input)
	{
	    StringBuilder sb = new StringBuilder();
	    try{
			CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
		    TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_47, new StringReader(input.trim()));
		    tokenStream = new StopFilter(Version.LUCENE_47, tokenStream, stopWords);
		    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		    tokenStream.reset();
		    while (tokenStream.incrementToken())
		    {
		        String term = charTermAttribute.toString();
		        sb.append(term + " ");
		    }
		    //System.out.println("insode stopwords removl"+sb.toString());
		    tokenStream.close();
		    //System.out.println(result);

	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	    return sb.toString();
	}
	
}
