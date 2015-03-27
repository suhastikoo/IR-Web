package assignment3;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;

import assignment3.Object;


public class AddToDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Reached Here");
		InputStream file;
		try {
			System.out.println("Reached Here");
			file = new FileInputStream("D:/CrawlerData/temp1.ser");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);
			@SuppressWarnings("unchecked")
			HashMap<String,HashMap<String,Object>> table =
			(HashMap<String, HashMap<String, Object>>) input.readObject();
			input.close();
			
			System.out.println("Reached Here");
			System.out.println("Table size:" + table.size());
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

}
