package cw.word.search;
import java.io.*;
import java.util.*;

public class WordSearchGenerator {

	public static void main(String[] args) {
		
		// read in words from file
	    Scanner in = new Scanner(System.in);
	    System.out.println("Enter the path to a text file with words or phrases separated by line breaks:");
	    String filename = in.nextLine();
		ArrayList<String> words = wordList(filename);
	    
	}
	
	public static ArrayList<String> wordList(String filename) {
		// open provided file, read in lines, and add words to list
	    ArrayList<String> words = new ArrayList<>();
	    try {
	    	
	    	BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	words.add(line);
		    }
		    
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			System.out.println("File not found!");
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	    
	    return words;
	}
	
	

}
