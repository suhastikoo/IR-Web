package assignment3;

import java.util.*;
import java.io.*;

public class deserial {

	public static void main(String[] args) {
		HashMap<String,ArrayList<String>> title = new HashMap<String,ArrayList<String>>();
		int count=0;
		//String folderLocation = "D:/CrawlerData/WebData/ParseData";
		String readUrl = "D:/CrawlerData/anchor.txt";
		String TempLine1;
		try {
		String[] curr;
		String[] titletemp;
		ArrayList<String> cortitle = new ArrayList<String>();
		//System.out.println(curr[]);
		//ArrayList<String> tempurl = new ArrayList<String>();
		BufferedReader TextFile1 = new BufferedReader(new FileReader(readUrl));
		String check;
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
		ArrayList<String> finaltitle= new ArrayList<String>();
		while((TempLine1 = TextFile1.readLine()) != null ){
			
//			if(checku=)
			curr = TempLine1.split(";");
			check = curr[0];
			if(check.length()>16)
				check = check.substring(0,4);
			if(check.equals("http")){
			curr[1].replaceAll("[^a-zA-Z]", " ");
			
			titletemp = curr[1].split(" ");
			finaltitle.clear();
			for(String str:titletemp){
				if(!stopWordsList.containsKey(str)){
				finaltitle.add(str);	
				}
			}
			
			//System.out.println(curr[1]);
			for(int j=0;j<finaltitle.size()-1;j++){
				cortitle.add(titletemp[j]);
			}
			title.put(curr[0],cortitle);
//			if(title.containsKey(curr[0])){
//				tempurl = title.get(curr[0]);
//				System.out.println(curr[1]);
//				if(!tempurl.contains(curr[1])){
//					tempurl.add(curr[1]);
//					title.put(curr[0], tempurl);
//				}
//			}
//			else{
//				count++;
//				tempurl.add(curr[1]);
//				System.out.println(count);
//				title.put(curr[0], tempurl);
//			}
		}
		}
		TextFile1.close();
		//System.out.println(title);
		}
		catch(IOException ioe)
		{
			System.out.println("I/O Exception!!!");
			
		}
		try
		{
			OutputStream file1 = new FileOutputStream("D:/CrawlerData/title.ser");
			OutputStream buffer1 = new BufferedOutputStream(file1);
			ObjectOutput output = new ObjectOutputStream(buffer1);
			//ObjectOutputStream oos = new ObjectOutputStream(indexFilePath);
			output.writeObject(title);
			output.close();
			file1.close();
			System.out.printf("Serialized HashMap data is saved in title.ser");
		}catch(IOException ioe)
		{
			System.out.println("I/O Exception!!!");
			
		}


	}
}
