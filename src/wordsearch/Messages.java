// Author: Christopher Wang (christopher.wang@wustl.edu)
//
// This file contains all messages and methods for printing out messages to the console.

package wordsearch;
import java.util.*;

public class Messages {
	
	public static final String filePrompt = "Enter the path to a text file with words or phrases separated by line breaks:";
	public static final String sizePrompt = "Enter the side length of the word search grid:";
	public static final String wordNumberPrompt = "Enter the number of words to use in the word search (or type \"all\" to include all words):";
	public static final String invalidInput = "Invalid input! ";
	public static final String fileEmpty = "Error: File is empty!";
	public static final String fileNotFound = "Error: File not found!";
	public static final String fileReadFailed = "Error: File read failed!";
	public static final String puzzleFailed = "Error: A word search could not be generated! All of your words are too short or too long. Please try again with a new list.";
	public static final String termination = "Terminating program.";
	public static final String unexpectedError = "An unexpected error occurred when placing a word...";
	
	public static void generatingPuzzle(int gridSize, int wordNo, int listSize) {
		System.out.println("Generating " + gridSize + " x " + gridSize + " word search using " + wordNo + " out of " + listSize + " words...");
		System.out.println();
	}
	
	public static void notAllWordsUsed(int used) {
		System.out.println("Due to space constraints, only " + used + " words have been included in this puzzle.");
		System.out.println();
	}
	
	public static void printList(List<String> l) {
		System.out.println("List of words used: ");
		for (int i = 0; i < l.size(); i++) {
			System.out.println(l.get(i));
		}
	}
	
	public static void printGrid(char[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				// loop through x first
				System.out.print(grid[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
}
