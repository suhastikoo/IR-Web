package assignment3;

import java.util.*;

public class sortindex {
	
 public static HashMap<String, Float> sort(HashMap<String, Float> finalrank){
	 List<Map.Entry<String, Float>> obj = new ArrayList<Map.Entry<String, Float>>(finalrank.entrySet());
		Collections.sort(obj, new Comparator<Map.Entry<String, Float>>(){
			public int compare(Map.Entry<String, Float> entry1, Map.Entry<String, Float> entry2) {
				Float ent1 = entry1.getValue();
				Float ent2 = entry2.getValue();
				return ent2.compareTo(ent1);
			}});
		
		return finalrank;

 }

}
