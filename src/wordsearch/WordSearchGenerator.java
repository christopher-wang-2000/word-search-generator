// Author: Christopher Wang (christopher.wang@wustl.edu)
//
// This file contains main and serves as the entry point to the program.
// This program generates a word search of customizable length and number of words
// based on an input text file containing words separated by line breaks.

package wordsearch;
import java.io.*;
import java.util.*;

public class WordSearchGenerator {
	
	// minimum acceptable word length
	public static final int minWordLength = 2;
	
	// number of times to try adding a word to the grid before giving up
	public static final int wordRetry = 100;

	public static void main(String[] args) {
		
		// read in filename
	    Scanner in = new Scanner(System.in);
	    System.out.println(Messages.filePrompt);
	    String filename = in.nextLine();
	    
	    // read in words from file
	    ArrayList<String> words = new ArrayList<>();
	    boolean failure = wordList(filename, words);
	    
	    // if it didn't work, re-prompt and retry
	    if (failure) {
	    	System.out.println(Messages.filePrompt);
		    filename = in.nextLine();
		    failure = wordList(filename, words);
	    }
	    
	    // read in grid length
		System.out.println(Messages.sizePrompt);
		String sizeString = in.nextLine();
		
		// check if size is valid int and within bounds, and if not, then re-prompt and retry
		while (!stringIsNatural(sizeString)) {
			System.out.println(Messages.invalidInput + Messages.sizePrompt);
			sizeString = in.nextLine();
		}
		int size = Integer.parseInt(sizeString);
		
		System.out.println(Messages.wordNumberPrompt);
		String wordNumber = in.nextLine();
		
		// check if word number is valid int and less than total number of words
		// if not, then re-prompt and retry
		boolean wordNumberIsNat = stringIsNatural(wordNumber);
		while (!(wordNumberIsNat || wordNumber.equals("all"))) {
			System.out.println(Messages.invalidInput + Messages.wordNumberPrompt);
			wordNumber = in.nextLine();
			wordNumberIsNat = stringIsNatural(wordNumber);
		}
		int wordNo;
		if (wordNumber.equals("all")) {
			wordNo = words.size();
		}
		else {
			wordNo = Integer.parseInt(wordNumber);
			if (wordNo > words.size()) {
				wordNo = words.size();
			}
		}
		
		in.close();
		
		// print message after all user inputs are taken in
		Messages.generatingPuzzle(size, wordNo, words.size());
		
		// converts all words to uppercase
		for (int i = 0; i < words.size(); i++) {
			words.set(i, toUpper(words.get(i)));
		}
		
		// create LetterPicker object to generate letter frequencies
	    LetterPicker lp = new LetterPicker(words);
	    
		// initialize objects for generating the puzzle and lists of words used and answers
		char[][] grid = new char[size][size];
		ArrayList<String> wordsUsed = new ArrayList<>();
		HashSet<String> answers = new HashSet<>();
		Random r = new Random();
		
		// select and add a random word to the grid, one at a time
		for (int i = 0; i < wordNo; i++) {
			
			// if all words have run out (due to too many being invalid)
			if (words.isEmpty()) {
				
				// if zero words were added, terminate the program
				if (wordsUsed.isEmpty()) {
					System.out.println(Messages.puzzleFailed);
					System.out.println(Messages.termination);
					return;
				}
				
				// if some words were used, say so and exit the loop
				else {
					Messages.notAllWordsUsed(wordsUsed.size());
					break;
				}
				
			}
			
			// get a random word and remove it from the list
			int index = r.nextInt(words.size());
			String w = words.get(index);
			words.remove(index);
			
			// if the word is too short or too long, it cannot be used
			// continue and retry with the next word
			if (w.length() > size || w.length() < minWordLength) {
				i--; // decrement i to add another word instead
				continue;
			}
			
			// try adding the word to the grid multiple times
			// if it fails too many times, skip and retry with the next word
			int count = 0;
			while ((!WordPlacer.addWordToGrid(w, grid, wordsUsed, answers)) && count < wordRetry) {
				count++;
			}
			
			// if the word couldn't be added
			if (count == wordRetry) {
				i--; // decrement i to add another word instead
			}
			
		}
		
		// fill remainder of grid with letters randomly
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == 0) {
					grid[i][j] = lp.randomChar();
				}
			}
		}
		
		// print out finished grid
		Messages.printGrid(grid);
		
		// sort and print out list of words used
		Collections.sort(wordsUsed);
		Messages.printList(wordsUsed);
		
	}
	
	public static boolean wordList(String filename, ArrayList<String> words) {
		// open provided file, read in lines, and add words to list
	    try {
	    	BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		    String line;
		    boolean empty = true;
		    while ((line = br.readLine()) != null) {
		    	words.add(line);
		    	empty = false;
		    }
		    
		    if (empty) {
		    	System.out.println(Messages.fileEmpty);
				return true;
		    }
		    
		} catch (FileNotFoundException e) {
			System.out.println(Messages.fileNotFound);
			return true;
			
		} catch (IOException e) {
			System.out.println(Messages.fileReadFailed);
			return true;
		}
	    
	    return false;
	}
	
	public static boolean stringIsNatural(String s) {
		// checks if a string is a natural number (positive integer)
		
		// if string is too long, it is not valid
		if (s.length() > 9) {
			return false;
		}
		
		// if string has non-numeric characters, it is not valid
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) < '0' || s.charAt(i) > '9') {
				return false;
			}
		}
		
		// only natural number if non-zero
		return Integer.parseInt(s) != 0;
		
	}
	
	public static String toUpper(String s) {
		// removes non-letter characters and converts string to uppercase 
		
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < s.length(); j++) {
			// if character is a letter, keep it in the string
			if ((s.charAt(j) >= 'A' && s.charAt(j) <= 'Z')
					|| (s.charAt(j) >= 'a' && s.charAt(j) <= 'z')) {
				sb.append(s.charAt(j));
			}
		}
		return sb.toString().toUpperCase();
	}
	
	
	

}
