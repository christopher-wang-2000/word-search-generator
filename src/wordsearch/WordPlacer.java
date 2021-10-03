// Author: Christopher Wang (christopher.wang@wustl.edu)
//
// This file contains the code to randomly position a word in the grid.

package wordsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WordPlacer {

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
		
		// determine valid directions for word
		ArrayList<Integer> directions = validDirections(startX, startY, size, w.length());
		
		// this code should not ever run if the math is correct
		if (directions.size() == 0) {
			System.out.println(Messages.unexpectedError);
			return false;
		}
		
		// pick a random direction out of the valid directions
		int dir = directions.get(r.nextInt(directions.size()));
		
		// attempts to place word in grid
		int[] coord = new int[] {startX, startY};

		for (int i = 0; i < w.length(); i++) {
			
			// check if the cell is blank (or already contains the correct letter)
			if (grid[coord[0]][coord[1]] == 0 || grid[coord[0]][coord[1]] == w.charAt(i)) {
				
				increment(coord, dir);
				
			}
			
			// if not, there is a conflict and we need to stop
			else {
				return false;
			}
			
		}
		
		// if the check was successful, then retrace our steps and fill in the letters
		coord = new int[] {startX, startY};
		
		for (int i = 0; i < w.length(); i++) {
				
			grid[coord[0]][coord[1]] = w.charAt(i);
			increment(coord, dir);
			
		}
		
		// if the word was placed in the grid, add it to the word and answer lists
		wordsUsed.add(w);
		answers.add(w);
		
		return true;
	}
	
	public static ArrayList<Integer> validDirections(int sX, int sY, int size, int l) {
		// determines all valid word directions and adds them to a list
		
		ArrayList<Integer> directions = new ArrayList<>();
		boolean left = (sX + 1 >= l);
		boolean right = (size - sX >= l);
		boolean up = (sY + 1 >= l);
		boolean down = (size - sY >= l);
		
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
		
		return directions;
	}
	
	public static void increment(int[] coord, int dir) {
		
		if (dir == 6 || dir == 7 || dir == 8) {
			coord[0]--; // left
		}
		else if (dir == 2 || dir == 3 || dir == 4) {
			coord[0]++; // right
		}
		
		if (dir == 1 || dir == 2 || dir == 8) {
			coord[1]--; // up
		}
		else if (dir == 4 || dir == 5 || dir == 6) {
			coord[1]++; // down
		}
		
	}
	
}
