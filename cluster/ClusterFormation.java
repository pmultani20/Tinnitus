package com.cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import com.crawler.Constants;
import com.dboperations.InsertOperations;

public class ClusterFormation {

	public void merge(String cluster, String file) throws IOException{
			BufferedReader reader1 = new BufferedReader(new FileReader(Paths.get(cluster).toString()));
			BufferedReader reader2 = new BufferedReader(new FileReader(Paths.get(file).toString()));
			String line1,line2,user,date,clusterNumber;
			String[] lineText;
			while (((line1 = reader1.readLine()) != null) && (line2 = reader2.readLine()) != null) {
				lineText = line2.split(",");
				user = lineText[0];
				date = lineText[2]+","+lineText[3];
				clusterNumber = line1;
    			if(file == Constants.USER_INTRODUCTION_FILE){
    				InsertOperations.insertCluster(clusterNumber,user,date,Constants.USER_INTRODUCTION);
    			}
    			else{
    				InsertOperations.insertCluster(clusterNumber,user,date,Constants.TREATMENTS);
    			}
            }
		reader1.close();
		reader2.close();
	}
	public static void main(String[] args){
		String introCluster = "./input/introductionClusters.txt";
		String introfile = "./input/tinitusIntroduction.txt";
		String treatCluster = "./input/treatmentClusters.txt";
		String treatmentfile = "./input/tinitusTreatment.txt";
		ClusterFormation clusterform = new ClusterFormation();
		try{
			clusterform.merge(introCluster,introfile);
			//clusterform.merge(treatCluster,treatmentfile);
		}catch(IOException e){
			e.printStackTrace();	
		}

	}
	
}
