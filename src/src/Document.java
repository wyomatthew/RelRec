package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * This class represents one document. It will keep track of the term
 * frequencies.
 * 
 * @author swapneel
 *
 */
public class Document implements Comparable<Document> {

    /**
     * A hashmap for term frequencies. Maps a term to the number of times this terms
     * appears in this document.
     */
    private HashMap<String, Integer> termFrequency;

    /**
     * Reviews to process.
     */
    private String reviews;

    /**
     * The constructor. It takes in the reviews as a String. It will read the
     * reviews and pre-process it.
     * 
     * @param reviews reviews to process
     */
    public Document(String reviews) {
        this.reviews = reviews;
        termFrequency = new HashMap<String, Integer>();
        readAndPreProcess();
    }

    /**
     * This method will read the reviews and do some pre-processing. The following
     * things are done in pre-processing: Every word is converted to lower case.
     * Every character that is not a letter or a digit is removed. We don't do any
     * stemming. Once the pre-processing is done, we create and update the termFrequency map.
     */
    private void readAndPreProcess() {
        String[] words = reviews.split(" ");             
        
        for (int i = 0; i < words.length; i++) {
            String nextWord = words[i];
            if (nextWord != null) {
                String filteredWord = nextWord.replaceAll("[^A-Za-z0-9�?É�?ÓÚáéíóúñ]", "").toLowerCase();

                if (!(filteredWord.equalsIgnoreCase(""))) {
                    if (termFrequency.containsKey(filteredWord)) {
                        int oldCount = termFrequency.get(filteredWord);
                        termFrequency.put(filteredWord, ++oldCount);
                    } else {
                        termFrequency.put(filteredWord, 1);
                    }
                }
            }
        }
    }

    /**
     * This method will return the term frequency for a given word. If this document
     * doesn't contain the word, it will return 0
     * 
     * @param word The word to look for
     * @return the term frequency for this word in this document
     */
    public double getTermFrequency(String word) {
        if (termFrequency.containsKey(word)) {
            return termFrequency.get(word);
        } else {
            return 0;
        }
    }

    /**
     * This method will return a set of all the terms which occur in this document.
     * 
     * @return a set of all terms in this document
     */
    public Set<String> getTermList() {
        return termFrequency.keySet();
    }

	@Override
	public int compareTo(Document o) {
		return 0;
	}
}