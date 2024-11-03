import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * 
 * @author ricardoveras 
 * Student number: 250692934
 * Western ID: rveras
 * Professor: Ahmed Ibrahim
 * Computer Science 2210B, Winter 2023
 * Friday, February 17, 2023
 *
 */

// This program takes in two files, a dictionary text file and a second file to check the spelling of the contained words using the dictionary file
// The dictionary is made using a hashtable. The dictionary file is read, storing all of the words in a dictionary
// The second file is then read, checking word by word if a correctly spelled word can be found using substitution, omission, insertion or removal
// The words found to be correctly spelled after this process are displayed. If no suggestions are found, that is displayed
public class Spell {
	static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	static String dictionaryName;
	static String fileToCheck;
    static Hashtable<String, Integer> dictionary = new Hashtable<String, Integer>();	// Will store dictionary keys and corresponding values
    static Boolean loaded, insertion, reversal, omission, replacement=false;			// "loaded" will be set to true when the dictionary is filled, the other variables will be set to true if a correctly spelled word is found due to their corresponding function
    static Hashtable<String, Boolean> dictCheck = new Hashtable<String, Boolean>();		// Will be filled with the dictionary keys and false if the dictionary is empty, true otherwise
    
    
    Spell(){
        
    	// Load dictionary words from file into hashtable    	    	
        Scanner fileName = new Scanner(dictionaryName);					// Create Scanner object of the dictionary file name

        File dictFile = new File("src/" + fileName.nextLine());			// Create File object "dictFile" with the dictionary file's location
        fileName.close();

    	Integer count = 0;												// Initialize a counter at zero, to be assigned as the value for corresponding dictionary keys
        
    	// Iterate through each word in the file
        try {
        	Scanner dictScan = new Scanner(dictFile);					// Create Scanner object of the dictionary file
        	while (dictScan.hasNext()) {
        		loaded = true;											// If the file has any lines, static variable "bool" is set to true
        		String word = dictScan.next();							// Create String object of the next word in the file
        		dictionary.put(word, count);							// Set the word corresponding to the key, and its corresponding count value, in the hashtable dictionary
        		count++;												// Increment the count value
        	}
        	
        	dictScan.close();											// Close the dictionary file
        }
        catch(FileNotFoundException e)  {System.out.println("The file you specified has not been found.");	// If the dictionary file is not found, an error message is printed
        }

        // Load words in fileToCheck.txt
                
		Scanner fileName2 = new Scanner(fileToCheck);					// Create Scanner object of the file for checking spelling of the words it contains

        File checkFile = new File("src/" + fileName2.nextLine());		// Create File object of the file to check's filename
        fileName2.close();
        
        // Iterate through each word in the file
        try {
        	Scanner checkScan = new Scanner(checkFile);					// Create Scanner object to read through the spell checking file
        	
        	// While the spell checking file has more words, set the next word to "str" and then check its spelling
        	while (checkScan.hasNext()) {
        		String str = checkScan.next();
        		checkSpelling(str);
        		}
        	
        	checkScan.close();											// Close the spell checking file
        }
   
        // If the spell checking file specified is not found, an error message is displayed
        catch(FileNotFoundException f) {
        	System.out.println("The file you specified has not been found.");
        }
    }
   

    public static void main(String[] args) {
    	dictionaryName = args[2];										// Set the dictionary name argument to static variable "dictionaryName"
    	fileToCheck = args[3];											// Set the spell checking file name argument to static variable "fileToCheck"		

        Spell spell = new Spell();										// Initiate an object of type Spell 

    }

    
    // This function checks if the dictionary is loaded or not, returning a new dictionary hashtable storing the same keys but with the boolean variable "bool", false being an empty dictionary, true meaning there are words in the dictionary
    public static Hashtable<String, Boolean> getDictionary(){
    	String str = dictionary.keys().toString();
    	Hashtable<String, Boolean> dictCheck = new Hashtable<String,Boolean>(dictionary.size());
    	dictCheck.put(str, loaded);										// Set the dictionary words as keys in the new dictionary hashtable and for the values store true for a loaded dictionary, false otherwise
    	
    	return dictCheck;												// Return the new dictionary hashtable
    }

    
    // This function takes a String word as an argument to check if the word exists in the dictionary. 
    // If the word exists, it will print it with a message "Correct Spelling:" to the console.
    // Else it will call the suggestCorrections function to provide the correct word from the words given in the dictionary file.
    public static boolean checkSpelling(String word){
    	if(dictionary.containsKey(word.toLowerCase())) {
    		System.out.println("Correct Spelling: " + word);
    		return true;												// If the word is correctly spelled, true is returned
    	} else suggestCorrections(word.toLowerCase());					// If the word is incorrectly spelled, suggestCorrections() is called to suggest potential correct spellings
    	
    	return false;													// If the word is incorrectly spelled, false is returned
    }

    
    // This function takes a String input word as argument.
    // It starts by printing the message word: Incorrect Spelling to the console.
    // The function also uses four different methods (correctSpellingWithSubstitution,
    // correctSpellingWithOmission, correctSpellingWithInsertion, correctSpellingWithReversal)
    // to generate possible corrected spellings for the input word.
    // The function then returns the suggestions list which contains the possible corrected spellings.
    public static boolean suggestCorrections(String word) {
    	System.out.println(word + ": Incorrect Spelling");
    	
    	correctSpellingSubstitution(word.toLowerCase());
    	correctSpellingWithOmission(word.toLowerCase());
    	correctSpellingWithInsertion(word.toLowerCase());
    	correctSpellingWithReversal(word.toLowerCase());
    	
    	if(Spell.replacement == true) return true;						// If a correctly spelled word was found using replacement, true is returned
    	if(Spell.omission == true) return true;							// If a correctly spelled word was found using omission, true is returned
    	if(Spell.insertion == true) return true;						// If a correctly spelled word was found using insertion, true is returned
    	if(Spell.reversal == true) return true;    						// If a correctly spelled word was found using reversal, true is returned

    	return false;													// If no correctly spelled words were found, false is returned
    }

    
    // This function takes in a string word and tries to correct the spelling by substituting letters and 
    // check if the resulting new word is in the dictionary.
    static String correctSpellingSubstitution(String word) {
    	char[] wordChars = new char[word.length()];
		wordChars = word.toLowerCase().toCharArray();					// Put the characters comprising "word" into a char array
    	ArrayList<String> correctWords = new ArrayList<String>();		// Initialize an array list to store the new correctly spelled words due to substitution
    	
    	// Iterate through the characters in the input word
    	for(int i=0; i<word.length(); i++) {
    		
    		// Swap each letter of the word with each letter of the alphabet
    		for(int j=0; j<26; j++) {
    			char temp = alphabet[j];
    			wordChars[i] = temp;
    			
    			// If the new word with a swapped character is in the dictionary, it is added to the array list of correctly spelled words
    			if(dictionary.containsKey(wordChars.toString())) {
        			if(!correctWords.contains(wordChars.toString())) {	// If the correctWords does not already have the valid spell checked word, it is added to the list of correct words
        				correctWords.add(wordChars.toString());
        			}
    				replacement = true;									// Static variable "replacement" is set to true if a correctly spelled word was found using substitution
    			}
    		}
    	}
    	
    	if(correctWords.size() != 0)
    		System.out.println("The word corrections found using substitution are: " + correctWords);	// Print correctWords array if any correctly spelled words were found
    	else System.out.println("No suggestions for " + word + " by substitution.");
    		
    		return word;						
    }

    
    // This function tries to omit (in turn, one by one) a single character in the misspelled word 
    // and check if the resulting new word is in the dictionary.
    static String correctSpellingWithOmission(String word) {
    	StringBuilder newWord = new StringBuilder(word.toLowerCase());	// Create StringBuilder object containing "word" in lower case
		ArrayList<String> correctWords = new ArrayList<String>();		// Initialize array list to store correctly spelled words

		// Delete one character at a time from word and subsequently check if its in the dictionary file
    	for(int i=0; i<word.length(); i++) {
    		StringBuilder tempWord = new StringBuilder(newWord);		// Create tempWord with "word" in it
    		tempWord.deleteCharAt(i);									// Remove one character from tempWord

    		// Check if the word with one character removed is in the dictionary
    		if(dictionary.containsKey(String.valueOf(tempWord))) {
    			if(!correctWords.contains(String.valueOf(tempWord))) {	// If the correctWords does not already have the valid spell checked word, it is added to the list of correct words
    				correctWords.add(String.valueOf(tempWord));
    			}
    				omission = true;									// Static variable "omission" is set to true if a correctly spelled word was found using omission
    		}
    	}
    	
    	if(correctWords.size() != 0)
    		System.out.println("The word corrections found using omission are: " + correctWords);	// Print correctWords array if any correctly spelled words were found
    	else System.out.println("No suggestions for " + word + " by omission.");

		return word;
    }

    // This function tries to insert a letter in the misspelled word 
    // and check if the resulting new word is in the dictionary.
    static ArrayList<String> correctSpellingWithInsertion(String word) {
    	StringBuffer newWord = new StringBuffer(word.toLowerCase());	// Create StringBuffer object containing "word" in lower case
		ArrayList<String> correctWords = new ArrayList<String>();		// Initialize array list to store correctly spelled words
		
		for(int i=0; i<=word.length(); i++) {
			for(int j=0; j<alphabet.length; j++) {
	    		StringBuffer tempWord = new StringBuffer(newWord);		// Create tempWord with "word" in it
	    		tempWord.insert(i, alphabet[j]);
	    		
	    		if(dictionary.containsKey(String.valueOf(tempWord))) {
	    			if(!correctWords.contains(String.valueOf(tempWord))) {	// If the correctWords does not already have the valid spell checked word, it is added to the list of correct words
	    				correctWords.add(String.valueOf(tempWord));
	    			}
	    				insertion = true;								// Static variable "insertion" is set to true if a correctly spelled word was found using omission
	    		}
			}
		}
		
		if(correctWords.size() != 0)
			System.out.println("The word corrections found using insertion are: " + correctWords);	// Print correctWords array if any correctly spelled words were found
    	else System.out.println("No suggestions for " + word + " by insertion.");

				return correctWords;
    }
		    
    
    // This function tries swapping every pair of adjacent characters 
    // and check if the resulting new word is in the dictionary.
    static String correctSpellingWithReversal(String word) {
    	char[] wordChars = new char[word.length()];
		wordChars = word.toLowerCase().toCharArray();
		ArrayList<String> correctWords = new ArrayList<String>();		// Initialize array list to store correctly spelled words
		
		//Iterate through "word", swapping adjacent characters
		for(int i=0; i<word.length()-1; i++) {
	    		StringBuffer tempWord = new StringBuffer(String.valueOf(wordChars));	// Create tempWord with "word" in it so two letters can be swapped
	    		char temp = wordChars[i];		
	    		tempWord.setCharAt(i, wordChars[i+1]);	
	    		tempWord.setCharAt(i+1, temp);

	    		// If the dictionary contains the corrected word, it is added to the correctWords array
	    		if(dictionary.containsKey(String.valueOf(tempWord))) {
	    			if(!correctWords.contains(String.valueOf(tempWord))) {	// If the correctWords does not already have the valid spell checked word, it is added to the list of correct words
	    				correctWords.add(String.valueOf(tempWord));
	    			}
	    				reversal = true;								// Static variable "reversal" is set to true if a correctly spelled word was found using reversal
	    		}
		}		
		
		if(correctWords.size() != 0)
			System.out.println("The word corrections found using reversal are: " + correctWords);	// Print correctWords array if any correctly spelled words were found
    	else System.out.println("No suggestions for " + word + " by reversal.\n");
		
		return word;
    }

}