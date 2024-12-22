import java.util.PriorityQueue;

//import FindKeyWordsInFile.Node;

public class FindKeyWordsInFileTest extends FindKeyWordsInFile {
	
	 public FindKeyWordsInFileTest(AVLTree<String, Integer> frequencies, AVLTree<String, Void> english,
			AVLTree<String, Integer> keywords, PriorityQueue<FindKeyWordsInFile.Node> pq) {
		super(frequencies, english, keywords, pq);
		// TODO Auto-generated constructor stub
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
	        PriorityQueue<Node> priorityQueue = new PriorityQueue<Node>();
	    	
	    	FindKeyWordsInFile test = new FindKeyWordsInFile(wordFrequencies, englishWords, keywordFrequencies, priorityQueue);	    	

	    	// Test Case 1: Check if input file is read and word frequency tree is created.
	        System.out.println("++++++++++++++++++++++++++");
	    	try {
	    		computeWordFrequencies(inputFileName, test.getWordFrequencies());
	    		
   		 if (test.getWordFrequencies().getRoot() != null) System.out.println("Test 1 passed. File read and word frequencies created.\n");
   		 
   		 else System.out.println("Test 1 failed. The tree was not created.\n");
   		 
	 } catch (Exception e) {
		 System.out.println("Test 1 failed. File could not be read.\n");
	 }
	    	
	    	
	    	// Test Case 2: Check if k most frequently words found correctly.
	      
        	
        	
        	// Test Case 3: Check if program filters common English words correctly
	        System.out.println("++++++++++++++++++++++++++");

	        try {
	            filterCommonEnglishWords(englishWords, keywordFrequencies, englishWordsFileName, priorityQueue, k);
	            if (test.getEnglishWords().getRoot() != null) System.out.println("Test 3 passed. Common english words were filtered correctly.\n");
	      		 
	            else System.out.println("Test 3 failed. The common english words were not filtered correctly.\n");
	        
	        } catch (Exception e) {
	        	System.out.println("Test 3 failed. Common english words not filtered correctly.\n");
	        }
        	
	        
	        // Test Case 5: Check if program handles empty input files
	        System.out.println("++++++++++++++++++++++++++");

	        String[] args2 = {args[0], "file4.txt", args[2]};
	        try {
	        	FindKeyWordsInFile.main(args2);
	        	System.out.println("Test 5 Passed. Empty file handled correctly.\n");

	        } catch (Exception e) {
	        	System.out.println("Test 5 failed. Empty file not handled correctly.\n");
	        }
	        
	        
	        // Test Case 6: Check if program handles non-existing input files
	        System.out.println("++++++++++++++++++++++++++");

	        String[] args3 = {args[0], "noFile.txt", args[2]};
	        try {
	        	FindKeyWordsInFile.main(args3);
	        	System.out.println("Test 6 Passed. Non-existing file handled correctly.\n");

	        } catch (Exception e) {
	        	System.out.println("Test 6 failed. Non-existing file not handled correctly.\n");
	        }
	 }
	 

}

