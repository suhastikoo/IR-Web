package assignment3;


import java.util.List;

public class ReturnObject {
	public List<String> List;
	int numberOfWords;
	public ReturnObject(List<String> List, int numberOfWords){
		this.List = List;
		this.numberOfWords = numberOfWords;
	}
	public ReturnObject TempFunction(List<String> List, int numberOfWords){
		return new ReturnObject(List,numberOfWords);
	}

}