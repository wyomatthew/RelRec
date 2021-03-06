package src;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

/**
 * This class implements the friend recommendations feature through
 * triadic closure of the history of searches from other users compared
 * to the current user's searches.
 * 
 * @author Nancy
 *
 */

public class User {
	
	/**
	 * @param category Category to add to user's searches
	 * @param name Name of user to make search for
	 * @return Friend recommendation given search history
	 */
	public static String getFriendRecommendation(String category, String name) {
	       //gets the user input from the category text field
        //adds new search to store in the user txt file
        String mostFreqSearch;
        try {
            // get most frequent search
            mostFreqSearch = addUserSearch(category, name);
            
            //creates list of friend recommendations based on triadic closure
            List<String> friendRecs = highestFreq(mostFreqSearch, name);
            
            String recs = "";
            //iterates through the list of friend recommendations to present to user
            for (String friend : friendRecs) {
                recs += friend + " ";
            }
            
            return recs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return "";
	}
	
	/**
	 * This method adds the user category input into the user's search history and 
	 * and then computes the most frequent searched category. If there is a new user,
	 * then this method will generate a new txt file for the user.
	 * @param userInputSearch String that user input into the category text field
	 * @return String representing the highest frequency category search
	 */
	public static String addUserSearch(String userInputSearch, String user) throws IOException {
        //Creates a file with the current user name
		File file = new File("src/" + user + ".txt");
		//Checks if the user is a returning user or new user
        if (!file.exists()) {
        	file.createNewFile(); //creates a new file
        	PrintWriter pw = new PrintWriter(file);
        	//writes the latest user search to the txt file
            pw.println(userInputSearch);
            pw.close();
            
            //adds the name of the new user in the users txt file
            Writer output;
    		output = new BufferedWriter(new FileWriter("src/users.txt", true));  
    		
    		//adds the user category input into the user txt file to store
    		output.append(user);
    		output.append("\n"); //separates the search
    		
    		//closes the txt file
    		output.close();
        }else{
        	//Creates the writer to edit the user txt file and update with any searches
    		Writer output;
    		output = new BufferedWriter(new FileWriter("src/" + user + ".txt", true));  //clears file every time
    		
    		//adds the user category input into the user txt file to store
    		output.append(userInputSearch);
    		output.append("\n"); //separates the search
    		
    		//closes the txt file
    		output.close();
        } 
		
		//Creates input stream and scanner
		FileInputStream fin = new FileInputStream("src/" + user + ".txt");
		Scanner yourFile = new Scanner(fin);
		
		//creates hash map to store the frequency of the categories
		HashMap<String, Integer> yourSearchMap = new HashMap<String, Integer>();
		
		//goes through the entire txt file and adds the frequencies to hash map
		while(yourFile.hasNext()) {
			//Get the next word
			String nextSearch = yourFile.next().toLowerCase();
			if (yourSearchMap.containsKey(nextSearch)) {
				//if nextSearch is already present in searchMap
				yourSearchMap.put(nextSearch, yourSearchMap.get(nextSearch) + 1);
			}else {
				//if nextSearch is not present in searchMap
				yourSearchMap.put(nextSearch, 1);
			}
		}
		
		//closes file
		yourFile.close();
		
		//gets and stores the max value of the hash map
		int maxValueInMap=(Collections.max(yourSearchMap.values())); 
		String maxSearch = ""; // ***** will set the max search to the one that shows up first (assumption)*********
		//iterates through the hash map to find the corresponding search to the max frequency
        for (Entry<String, Integer> entry : yourSearchMap.entrySet()) {  // Iterate through HashMap
            if (entry.getValue()==maxValueInMap) {
            	//gets the name of the category that has the highest frequency
            	maxSearch = entry.getKey();
            }
        }
        //returns the highest frequency category
		return maxSearch;		

	}	
	
	/**
	 * This method scans the file and generates a frequency map of the number of occurrences 
	 * of searches in txt file. Then calculates, compares, and returns the highest frequency.
	 * @param userName String of the name of the user
	 * @param mostFrequentSearch String of the most frequent category that was searched from user
	 * @return Integer of of the number of times the most frequent category was searched
	 */
	public static Integer scanFileFreq (String userName, String mostFrequentSearch) throws FileNotFoundException{ 
		//Creates input stream and scanner
		FileInputStream fin = new FileInputStream("src/" + userName + ".txt");
		Scanner userFile = new Scanner(fin);
		
		//Creates HashMap containing searches and number of occurrences
		HashMap<String, Integer> searchMap = new HashMap<String, Integer>();		
		
		//Reads through file and puts the searches in a frequency map
		while(userFile.hasNext()) {
			//Get the next word
			String nextSearch = userFile.next().toLowerCase();
			if (searchMap.containsKey(nextSearch)) {
				//if nextSearch is already present in searchMap
				searchMap.put(nextSearch, searchMap.get(nextSearch) + 1);
			}else {
				//if nextSearch is not present in searchMap
				searchMap.put(nextSearch, 1);
			}
		}
		
		//closing file
		userFile.close();
		
		Integer freqSearch = 0;
		
		if (searchMap.containsKey(mostFrequentSearch)) {
			freqSearch = searchMap.get(mostFrequentSearch.toLowerCase());
		}
		
		return freqSearch;
	}
	
	/**
	 * This method scans through all the user files and then returns the highest 
	 * frequency of the most searched word of the logged in user
	 * @param mostFrequentSearch String that holds the most frequent word of the logged in user
	 * @param userName String that is the current user's name
	 * @return List<String> of the recommended friends based off of triadic closure
	 */
	public static List<String> highestFreq(String mostFrequentSearch, String userName) throws IOException, FileNotFoundException {
		//Creates input stream and scanner
		FileInputStream fin = new FileInputStream("src/users.txt");
		Scanner scan = new Scanner(fin);
		
		//stores the highest frequency
		int highest = 0;
		
		//Arraylist of strings that store the friend recommendations based on triadic closure principle
		List<String> highestUser = new ArrayList<String>();
		
		//iterates through the user file an compares the frequency of the category searches to return highest
		while(scan.hasNext()) {
			String user = scan.next();
			if (!user.equalsIgnoreCase(userName)) {
				int userFreq = scanFileFreq(user, mostFrequentSearch);
				//updates friend recommendation of new highest frequency user
				if (userFreq > highest) {
					highest = userFreq;
					highestUser.clear();
					highestUser.add(user);
				//adds to friend recommendation if multiple users have highest frequency
				} else if (userFreq == highest) {
					highestUser.add(user);
				//no changes if lower frequency
				} else {
					continue;
				}
			}else {
				continue;
			}
		}
		
		//closes file
		scan.close();
		
		//returns friend recommendations
		return highestUser;
	}				
}
