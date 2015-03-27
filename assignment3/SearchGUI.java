package assignment3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.*;



public class SearchGUI {

	public static void main(String[] args) {

		InputStream file,file2;
		try {
			file = new FileInputStream("D:/CrawlerData/temp2.ser");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);

			@SuppressWarnings("unchecked")
			HashMap<String,HashMap<String,Object>> table =
			(HashMap<String, HashMap<String, Object>>) input.readObject();

			input.close();

			Hashtable<String,Integer> stopWordsList = new Hashtable<String,Integer>();
			String path = "D:/CrawlerData/StopWords.txt";		
			String TempLine;
			try {
				BufferedReader TextFile = new BufferedReader(new FileReader(path));
				while((TempLine = TextFile.readLine()) != null){
					stopWordsList.put(TempLine, 1);
				}

				TextFile.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}

			HashMap<String,List<String>> table2 = new HashMap<String,List<String>>();		

			String folderLocation = "D:/CrawlerData/WebData/ParseData";
			String readUrl = "D:/CrawlerData/Test2.txt";
			String TempLine1,TempLine2;

			int count = 1;
			BufferedReader TextFile1;
			try {
				TextFile1 = new BufferedReader(new FileReader(readUrl));

				try {
					while((TempLine1 = TextFile1.readLine()) != null){
						int finalWords = 0;
						List<String> TempList = new ArrayList<String>();
						String hash = Integer.toString(TempLine1.hashCode());
						String fileLocation = folderLocation.concat(hash);
						BufferedReader TextFile = new BufferedReader(new FileReader(fileLocation));
						while((TempLine2 = TextFile.readLine()) != null){
							if (!TempLine2.isEmpty()){
								ReturnObject ReturnObj = TokenizeFile(TempLine2, TempList, stopWordsList, finalWords);
								TempList = ReturnObj.List;
							}
						}
						System.out.println(count);
						count++;
						if(!table2.containsKey(TempLine1)){
							table2.put(TempLine1, TempList);
						}
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			//++++=================== ranking and printing==========
			Scanner input1 = new Scanner(System.in);
			String in;

			List<String> tokens = new ArrayList<String>();
			while(true){
				System.out.println("Enter the query or enter exit");
				in = input1.nextLine();
				in = in.toLowerCase();
				tokens.clear();
				if(in.endsWith("exit")){
					break;
				}
				StringTokenizer fileIn = new StringTokenizer(in);				
				while (fileIn.hasMoreTokens()){
					String a = fileIn.nextToken();
					a = a.replaceAll("[']", "");
					if(a.length()>1){
						tokens.add(a);
					}
				}

				HashMap<String, Float> finalrank = new HashMap<String, Float>();
				HashMap<String, Object> currtoken = new HashMap<String, Object>();
				float score=0;
				int i=0;

				while(i<tokens.size()){
					currtoken = table.get(tokens.get(i));
					i++;
					for (Map.Entry<String, Object> me : currtoken.entrySet()) // assuming your map is Map<String, String>
						if(finalrank.containsKey(me.getKey())){
							score = currtoken.get(me.getKey()).Score;
							score = score + finalrank.get(me.getKey());
							finalrank.put(me.getKey(), score);
						}
						else{
							finalrank.put(me.getKey(), currtoken.get(me.getKey()).Score);
						}
					currtoken.clear();        
				}
				//=========================adding title==========================
				file = new FileInputStream("D:/CrawlerData/temp2.ser");
				buffer = new BufferedInputStream(file);
				input = new ObjectInputStream (buffer);

				@SuppressWarnings("unchecked")
				
				HashMap<String,ArrayList<String>> titletable =
					(HashMap<String,ArrayList<String>>) input.readObject();
				score=0;
				for (Map.Entry<String, Float> me : finalrank.entrySet()) // assuming your map is Map<String, String>
					if(titletable.containsKey(me.getKey())){
						score = finalrank.get(me.getKey())*3;
						finalrank.put(me.getKey(), score);
					}

				//=================sorting final map==============

				List<Map.Entry<String, Float>> obj = new ArrayList<Entry<String, Float>>(finalrank.entrySet());
				Collections.sort(obj, new Comparator<Map.Entry<String, Float>>(){
					public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
						Float ent1 = entry1.getValue();
						Float ent2 = entry2.getValue();
						return ent2.compareTo(ent1);
					}});
				//=====================end of sorting==================


				int j=0;
				while(true){
					if(j>5){
						break;
					}

					if(j>obj.size()-1){
						break;
					}
					System.out.printf("%s\t" ,"--->");
					String[] URL = obj.get(j).toString().split("[=]");
					System.out.printf("%s\t" ,URL[0]);
					System.out.println();
					if(table.containsKey(in)){
						HashMap<String,Object> tempMap = table.get(in);
						if(tempMap.containsKey(URL[0])){
							ArrayList<Integer> tempList = tempMap.get(URL[0]).Locations;
							List<String> StrList = table2.get(URL[0]);
							if(tempList.size()>1){
								int temp1 = tempList.get(0);
								int temp2 = tempList.get(1);

								String data = StrList.subList(temp1, temp2+1).toString();
								System.out.println(data);								
							}
							else{
								int temp1 = tempList.get(0);
								if(temp1+10<tempList.size()){
									String data = StrList.subList(temp1, temp1+11).toString();
									System.out.println(data);
								}
								else{
									System.out.println(StrList.get(temp1));
								}
							}
						}
					}
					j++;

				}
				//=======================
			} 
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static ReturnObject TokenizeFile(String TempLine, List<String> TempList, 
			Hashtable<String, Integer> stopWordsList, int numberOfWords){

		TempLine = TempLine.replaceAll("[^a-zA-Z0-9']", " ");

		StringTokenizer fileIn = new StringTokenizer(TempLine);	

		while (fileIn.hasMoreTokens()){
			String a = fileIn.nextToken().toLowerCase();
			a = a.replaceAll("[']", "");
			if (!stopWordsList.containsKey(a) && a.length()>1){
				TempList.add(a);
			}
			numberOfWords += 1;
		}
		return new ReturnObject(TempList,numberOfWords);
	}
}



