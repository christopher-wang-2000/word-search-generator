package cw.word.search;
import java.io.*;
import java.util.*;

public class WordSearchGenerator {
	
	public static final int minSize = 5;
	public static final int maxSize = 20;

	public static void main(String[] args) {
		
		// read in filename
	    Scanner in = new Scanner(System.in);
	    System.out.println("Enter the path to a text file with words or phrases separated by line breaks:");
	    String filename = in.nextLine();
	    
	    // read in words from file
	    ArrayList<String> words = new ArrayList<>();
	    boolean failure = wordList(filename, words);
	    
	    // if it didn't work, re-prompt and retry
	    if (failure) {
	    	System.out.println("Enter the path to a text file with words or phrases separated by line breaks:");
		    filename = in.nextLine();
		    failure = wordList(filename, words);
	    }
	    
	    // read in grid length
		System.out.println("Enter the side length of the word search grid (between 5 and 20):");
		String sizeString = in.nextLine();
		
		// check if size is valid, and if not, then re-prompt and retry
		while (!sizeIsValid(sizeString)) {
			System.out.println("Invalid input! Enter the side length of the word search grid (between 5 and 20):");
			sizeString = in.nextLine();
		}
		
		
		
		in.close();
	    
	}
	
	public static boolean wordList(String filename, ArrayList<String> words) {
		// open provided file, read in lines, and add words to list
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	words.add(line);
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found!");
			return true;
			
		} catch (IOException e) {
			System.out.println("Error: File read failed!");
			return true;
		}
	    
	    return false;
	}
	
	public static boolean sizeIsValid(String s) {
		
		// if string is too long, it is not valid
		if (s.length() > 9) {
			return false;
		}
		
		// if string has non-numeric characters, it is not valid
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < 48 || s.charAt(i) > 57) {
				return false;
			}
		}
		
		// check if size is within bounds
		return (Integer.parseInt(s) >= minSize && Integer.parseInt(s) <= maxSize);
		
	}
	
	

}
