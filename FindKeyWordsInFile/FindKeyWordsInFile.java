import java.io.BufferedReader;
import java.io.FileReader;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.*;

// Create three AVL trees of common English words, words in a text file, and words that appear often in the text file that are not common English words
// Print k amount of instances of the key and value pairs of the frequent words that are not in the common English words file, k given as a parameter
public class FindKeyWordsInFile {
	private AVLTree<String, Integer> wordFrequency;
	private AVLTree<String, Void> english;
	private AVLTree<String, Integer> keywords;
	private PriorityQueue<Node> priority;
	
	public FindKeyWordsInFile(AVLTree<String, Integer> frequencies, AVLTree<String, Void> english, AVLTree<String, Integer> keywords, PriorityQueue<Node> pq) {
		this.wordFrequency = frequencies;
		this.english = english;
		this.keywords = keywords;
		this.priority = pq;
	}
	
	// Node class to populate a priority queue
	public class Node implements Comparable<Node> {
		public String key; // key of the node
		private Integer value; // value of the node

		public Node(String key, Integer value) {
			this.key = key;
			this.value = value;
		}

		
		// Override the comparable compareTo method to sort the priority queue by highest to lowest value
		@Override
		public int compareTo(Node n) {
			if(this.value < n.value) return 1;
			else if(this.value > n.value) return -1; 
			else return 0;
		}
	}

        
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java FindKeyWordsInFile k file.txt MostFrequentEnglishWords.txt");
            System.exit(1);
        }

        int k = Integer.parseInt(args[0]);
        String inputFileName = args[1];
        String englishWordsFileName = args[2];
        
        AVLTree<String, Integer> wordFrequencies = new AVLTree<>();
        AVLTree<String, Void> englishWords = new AVLTree<>();
        AVLTree<String, Integer> keywordFrequencies = new AVLTree<>();
    	PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    	FindKeyWordsInFile keys = new FindKeyWordsInFile(wordFrequencies, englishWords, keywordFrequencies, priorityQueue);

 
    	// Call each function to populate the trees and priority queue
        try {
            //Part 1
            // function name => computeWordFrequencies
        	computeWordFrequencies(inputFileName, wordFrequencies);
            
            //Part 2
            // function name => findKMostFrequentWords
        	keys.findKMostFrequentWords(wordFrequencies, priorityQueue);

            //Part 3
            // function name => filterCommonEnglishWords
            filterCommonEnglishWords(englishWords, keywordFrequencies, englishWordsFileName, priorityQueue, k);
            
        } catch (Exception e) {
        	System.out.println("You have encountered the following error(s): ");
            e.printStackTrace();
        }
        System.out.println("Most frequently found uncommon English words are: \n");
    	keywordFrequencies.inOrderTraversal();								// The k most frequent uncommon English words are printed using an in-order traversal of the AVL tree

    } 
    
    
    // Create an AVL tree to store the the words as keys and how many times that word shows up in the text file as its value
    public static AVLTree<String, Integer> computeWordFrequencies(String inputFileName, AVLTree<String, Integer> wordFrequencyTree) {
    
    	// Create a readable file
    	try {
    		FileReader inputFile = new FileReader("src/" + inputFileName);
    		BufferedReader br = new BufferedReader(inputFile);
    		String s;
    		String word = null;
    		
    		// If the text file is empty, print a message
    		if (br.readLine() == null) System.out.println("This file is empty.");
    		
    		// Read through the text file to put each word and its frequency into an AVL tree-based dictionary
    		while((s=br.readLine()) != null) {									// While the text file still has more lines, continue constructing the dictionary
    			Scanner scan = new Scanner(s);
    			
    			// While there are still words in the current line of the text file, add new words to the dictionary or increase existing words' frequency count
    			while(scan.hasNext()) {
    				word = scan.next().toLowerCase().replaceAll("[^a-zA-Z0-9]+$",  "");		// Remove trailing punctuation from the next word, make lower case and store in variable "word"
    				
    				// If the current word is already in the dictionary, increase its value by 1
    				if(wordFrequencyTree.get(word) != null) {
    					int newValue = wordFrequencyTree.get(word) + 1;
    					wordFrequencyTree.put(word, newValue);
    				}
    				else wordFrequencyTree.put(word, 1);						// If the current word is not in the dictionary, add it with its frequency value set to 1
    			}
    			// Close the files that were created to read the text file
    			scan.close();
    		}   br.close();

    	} catch (Exception e){	
        	System.out.println("You have encountered the following error(s): ");
    		e.printStackTrace();												// Print error information if any errors are caught
    	}
    	
    	return wordFrequencyTree;
    }
    
    
    // Find the k amount of most frequent words found in the text file that was given as a parameter
    public PriorityQueue<Node> findKMostFrequentWords(AVLTree<String, Integer> wordFrequencyTree, PriorityQueue<Node> priorityQueue) {
    	
    	List<String> nodeString = wordFrequencyTree.returnNodeList();	// Create a list of node keys

    	// Populate the priority queue in order from highest to lowest node value
		for (String var : nodeString) {
			int t = wordFrequencyTree.get(var);
			Node node = new Node(var, t);
			priorityQueue.add(node);
		}
		return priorityQueue;
		}

    
    // Create a tree that stores the common English words, then compare the k most frequent words from the text file, adding words that were not found in the common English words file into another tree
    public static AVLTree<String, Integer> filterCommonEnglishWords(AVLTree<String, Void> englishWordsTree, AVLTree<String, Integer> keywordFrequenciesTree, String englishFileName, PriorityQueue<Node> priorityQueue, int k) {
    	    	
    	// Create readable files to populate an AVL tree with the common English words found in the common English words file
    	try {
    		FileReader englishFile = new FileReader("src/" + englishFileName);
    		BufferedReader br = new BufferedReader(englishFile);
    		String s;
    		String word = null;
    		
    		// If the text file is empty, print a message
    		if (br.readLine() == null) System.out.println("This file is empty.");
    		
    		// Read through the text file to put each word and its frequency into an AVL tree-based dictionary
    		while((s=br.readLine()) != null) {									// While the text file still has more lines, continue constructing the dictionary
    			Scanner scan = new Scanner(s);
    			
    			// While there are still words in the current line of the text file, add new words to the dictionary or increase existing words' frequency count
    			while(scan.hasNext()) {
    				word = scan.next().toLowerCase();
    				englishWordsTree.put(word, null);
    			}
    			// Close the files that were created to read the text files
    			scan.close();
    		}   br.close();

    	} catch (Exception e){	
        	System.out.println("You have encountered the following error(s): ");
    		e.printStackTrace();												// Print error information if any errors are caught
    	}
  
    	// Iterate through the priority queue, storing the k most frequently used words that are not in the common English words file into a new tree
    	while(k > 0) {

    		// While the priority queue still has elements and k words have not yet been added, continue adding words to the new tree of frequently found uncommon English words
    		if(priorityQueue.peek() != null) {
    			Node temp = priorityQueue.peek();
    			String tempKey = temp.key;
    			
    			// If the next nodes' key from the priority queue is found in the common English words, it is removed. If not found, it is added to the new tree of the k most frequent uncommon English words
    			if(englishWordsTree.inOrder(tempKey, englishWordsTree.getRoot())) {
    				priorityQueue.poll();
    				AVLTree.check = false;										// Static check variable set back to false to be able to continue checking if other words are found in the common English words				
    			}
    			else {
    				keywordFrequenciesTree.put(temp.key, temp.value);			// If not found in the common English words, the priority queue nodes' key and value is added to the k most frequent uncommon English words' tree
    				priorityQueue.poll();										// The node is then removed from the priority queue
    				k--;
    			}
    		}
    		else {
    			System.out.println("No words in the file left to examine.");	// If the priorityQueue has less words than requested, this message is printed
    			break;
    		}
    	}
    	
    	return keywordFrequenciesTree;
    }
    
    // Return word frequency tree
    public AVLTree<String, Integer> getWordFrequencies() {
    	return wordFrequency;
    }
    
    
    // Return English words tree
    public AVLTree<String, Void> getEnglishWords() {
    	return english;
    }
    
    
    // Return keyword frequencies tree
    public AVLTree<String, Integer> getKeywords() {
    	return keywords;
    }
    
    
    // Return keywords priority queue
    public PriorityQueue<Node> getKeywordQueue() {
    	return priority;
    }

    public String getKey() {
    	return priority.peek().key;
    }
    
}
