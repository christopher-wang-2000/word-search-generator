Word Search Generator (Created 2021 October 2)
Author: Christopher Wang (christopher.wang@wustl.edu)

This program generates a word search based on the following user-provided inputs:
- The name of a file containing custom word list (separated by line break)
- The size (edge length) of the square word search grid
- The number of words to use from the list

The following .java files are found in word-search-generator/src/wordsearch:
WordSearchGenerator.java serves as the entry point to the program and contains the code for obtaining user input and randomly selecting words to include.
WordPlacer.java contains the code that attempts to add a given word to the word search grid with a random starting point and orientation.
LetterPicker.java contains the code that calculates letter frequencies in the input list and uses these frequencies to randomly select letters to fill blank space in the grid.
Messages.java contains all messages to the user and functions for printing out lists and the finished word search grid.

pokemon_list is provided as a test example for an input file.

How it works:
1) The program asks the user for an input file, a grid size, and a number of words to use.
2) The program converts all words to uppercase without spaces or punctuation and calculates the letter frequencies across the entire list.
3) The program creates the grid and randomly selects words one at a time to be added to the grid. For each word, the program will generate a random starting point and orientation (eight possibilities, including diagonals) in the grid.
5) The program will check whether the word can be added to the grid there, or if there is a conflicting letter in one of the cells due to a word that has already been placed. (Words can overlap if they share the same letter in the same cell.)
6) If adding the word fails, the program will keep trying for a certain number of times (given by wordRetry) before giving up and trying a different word instead.
7) After the desired number of words have been added (or after the program has attempted to add all words in the input file), the program will fill the remaining grid space with random letters according to the letter frequencies from the input list.
8) The program will print out the finished puzzle along with a list of the words that were included.

Constants:
- int minWordLength = 2; This dictates the minimum acceptable length of a word that can be included. This prevents single letters from being added to the puzzle as valid words.
- int wordRetry = 100; This dictates the number of times the program will attempt to add a word to the grid before giving up and moving onto the next word.

If all of the words are too short (smaller than minWordLength) or too long to fit in the grid, no words can be added to the puzzle, and the program will print out an error message and terminate.
