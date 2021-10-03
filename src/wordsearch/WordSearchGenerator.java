// Author: Christopher Wang (christopher.wang@wustl.edu)
// This file contains main and serves as the entry point to the program.
//
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
		System.out.println("Enter the side length of the word search grid:");
		String sizeString = in.nextLine();
		
		// check if size is valid int and within bounds, and if not, then re-prompt and retry
		while (!stringIsNatural(sizeString)) {
			System.out.println("Invalid input! Enter the side length of the word search grid:");
			sizeString = in.nextLine();
		}
		int size = Integer.parseInt(sizeString);
		
		System.out.println("Enter the number of words to use in the word search (or type \"all\" to include all words):");
		String wordNumber = in.nextLine();
		
		// check if word number is valid int and less than total number of words
		// if not, then re-prompt and retry
		boolean wordNumberIsNat = stringIsNatural(wordNumber);
		while (!(wordNumberIsNat || wordNumber.equals("all"))) {
			System.out.println("Invalid input! Enter the number of words to use in the word search (or type \"all\" to include all words):");
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
		
		// print message after all user inputs are taken in
		System.out.println("Generating " + size + " x " + size + " word search using " + wordNo + " out of " + words.size() + " words...");
		System.out.println();
		in.close();
		
		// converts each word to all uppercase with no punctuation or spaces
		for (int i = 0; i < words.size(); i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < words.get(i).length(); j++) {
				// if character is a letter, keep it in the string
				if ((words.get(i).charAt(j) >= 'A' && words.get(i).charAt(j) <= 'Z')
						|| (words.get(i).charAt(j) >= 'a' && words.get(i).charAt(j) <= 'z')) {
					sb.append(words.get(i).charAt(j));
				}
			}
			words.set(i, sb.toString().toUpperCase());
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
					System.out.println("Error: A word search could not be generated! All of your words are too short or too long. Please try again with a new list.");
					System.out.println("Terminating program.");
					return;
				}
				
				else {
					System.out.println("Due to space constraints, only " + wordsUsed.size() + " words have been included in this puzzle.");
					System.out.println();
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
			
			// convert word to all uppercase with no punctuation or spaces
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < w.length(); j++) {
				// if character is a letter, keep it in the string
				if ((w.charAt(j) >= 'A' && w.charAt(j) <= 'Z')
						|| (w.charAt(j) >= 'a' && w.charAt(j) <= 'z')) {
					sb.append(w.charAt(j));
				}
			}
			w = sb.toString().toUpperCase();
			
			// try adding the word to the grid multiple times
			// if it fails too many times, skip and retry with the next word
			int count = 0;
			while ((!addWordToGrid(w, grid, wordsUsed, answers)) && count < wordRetry) {
				count++;
			}
			
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
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// loop through x first
				System.out.print(grid[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		// print out list of words used
		Collections.sort(wordsUsed);
		System.out.println("List of words used: ");
		for (int i = 0; i < wordsUsed.size(); i++) {
			System.out.println(wordsUsed.get(i));
		}
		
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
		    	System.out.println("Error: File is empty!");
				return true;
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
	
	public static boolean addWordToGrid(String w, char[][] grid, ArrayList<String> wordsUsed, HashSet<String> answers) {
		// adds word to grid, returns true and adds it to wordsUsed and answers if successful
		// returns false if unsuccessful due to conflict with other existing word
		
		int size = grid.length;
		int startX, startY;
		Random r = new Random();
		
		// select a valid starting point in the grid at random
		// if w.length() <= size/2 + 1, then any starting point is valid
		if (w.length() < size/2 + 1) {
			startX = r.nextInt(size);
			startY = r.nextInt(size);
		}
		
		// otherwise, the starting point has to be picked near the edges of the grid
		else {
			// randomly choose to pick startX on left or right side
			if (r.nextInt(2) == 0) {
				// pick startX on left side
				startX = r.nextInt(size-w.length()+1);
			}
			else {
				// pick startX on right side
				startX = size - 1 - r.nextInt(size-w.length()+1);
			}
			
			// do the same for picking startY
			if (r.nextInt(2) == 0) {
				// pick startY on top side
				startY = r.nextInt(size-w.length()+1);
			}
			else {
				// pick startY on bottom side
				startY = size - 1 - r.nextInt(size-w.length()+1);
			}
		}
		
		// must determine valid directions for word
		ArrayList<Integer> directions = new ArrayList<>();
		boolean left = (startX + 1 >= w.length());
		boolean right = (size - startX >= w.length());
		boolean up = (startY + 1 >= w.length());
		boolean down = (size - startY >= w.length());
		
		// correspondence between directions and integer values
		if (up) {
			directions.add(1);
		}
		if (up && right) {
			directions.add(2);
		}
		if (right) {
			directions.add(3);
		}
		if (right && down) {
			directions.add(4);
		}
		if (down) {
			directions.add(5);
		}
		if (down && left) {
			directions.add(6);
		}
		if (left) {
			directions.add(7);
		}
		if (left && up) {
			directions.add(8);
		}
		
		// this code should not ever run if the math is correct
		if (directions.size() == 0) {
			System.out.println("An unexpected error occurred with placing a word...");
			return false;
		}
		
		// pick a random direction out of the valid directions
		int dir = directions.get(r.nextInt(directions.size()));
		
		// attempts to place word in grid
		int x = startX;
		int y = startY;
		for (int i = 0; i < w.length(); i++) {
			
			// check if the cell is blank (or already contains the correct letter)
			if (grid[x][y] == 0 || grid[x][y] == w.charAt(i)) {
				
				if (dir == 6 || dir == 7 || dir == 8) {
					x--; // left
				}
				else if (dir == 2 || dir == 3 || dir == 4) {
					x++; // right
				}
				if (dir == 1 || dir == 2 || dir == 8) {
					y--; // up
				}
				else if (dir == 4 || dir == 5 || dir == 6) {
					y++; // down
				}
			}
			
			// if not, there is a conflict and we need to stop
			else {
				return false;
			}
			
		}
		
		// if the check was successful, then retrace our steps and fill in the letters
		x = startX;
		y = startY;
		for (int i = 0; i < w.length(); i++) {
				
			grid[x][y] = w.charAt(i);
			
			if (dir == 6 || dir == 7 || dir == 8) {
				x--; // left
			}
			else if (dir == 2 || dir == 3 || dir == 4) {
				x++; // right
			}
			if (dir == 1 || dir == 2 || dir == 8) {
				y--; // up
			}
			else if (dir == 4 || dir == 5 || dir == 6) {
				y++; // down
			}
			
		}
		
		// if the word was placed in the grid, add it to the word and answer lists
		wordsUsed.add(w);
		answers.add(w);
		
		return true;
	}
	

}
