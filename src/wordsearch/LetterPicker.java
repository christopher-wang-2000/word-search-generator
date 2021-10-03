// Author: Christopher Wang (christopher.wang@wustl.edu)
//
// This class calculates the letter frequencies from the input file
// and randomly selects characters according to those frequencies.
// WordSearchGenerator.java uses this to fill the grid after placing words.

package wordsearch;
import java.util.*;

public class LetterPicker {
	
	LinkedHashMap<Character, Integer> letters;
	int total;
	
	public LetterPicker(ArrayList<String> words) {
		
		// calculate counts of all characters in complete word list
		letters = new LinkedHashMap<>();
		total = 0;
		for (int i = 0; i < words.size(); i++) {
			for (int j = 0; j < words.get(i).length(); j++) {
				if (letters.containsKey(words.get(i).charAt(j))) {
					letters.put(words.get(i).charAt(j), letters.get(words.get(i).charAt(j))+1);
				}
				else {
					letters.put(words.get(i).charAt(j), 1);
				}
				total++;
			}
		}
	}
	
	public char randomChar() {
		
		// selects a random character with probability proportional to the
		// frequencies originally generated
		Random r = new Random();
		int threshold = r.nextInt(total);
		int current = 0;
		
		for (char c : letters.keySet()) {
			current += letters.get(c);
			if (current > threshold) {
				return c;
			}
		}
		
		return ' ';
	}
	
	class Pair {
		char letter;
		int freq;
		
		private Pair(char l, int f) {
			letter = l;
			freq = f;
		}
		
	}
	
}
