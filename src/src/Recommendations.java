package src;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.FocusEvent;

/**
 * Class gets all the necessary input from the user and gives recommendations
 * of nearby businesses.
 * 
 * @author Tina
 *
 */

public class Recommendations {
    public static final int SCREEN_WIDTH = 1100;
    public static final int SCREEN_HEIGHT = 100;
	
	static JFrame mainFrame;
	
	static JButton getRecommendations;
	
	static HintTextField category;
	static HintTextField priceRange;
	static HintTextField queryReview;
	static HintTextField latitude;
	static HintTextField longitude;
	static HintTextField radius;
	static HintTextField distance;
	static HintTextField rating;
	
	static JPanel topBar;
	
	
	public static void main(String[] args) {
        mainFrame = new JFrame("Recommendations");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setResizable(true);

        initUI();
        
        mainFrame.setVisible(true);
        
        //-----------Testing---------------
//        getRecommendations("pizza", "1",
//        		"tasty, love, great, spot, sssss", "39.95556", 
//        		"-75.21339", "30000",
//        		"5");
        //---------------------------------
	}
	
	
	/**
	 * Initializes panel
	 */
	@SuppressWarnings("serial")
    private static void initUI() {
        // init all of the UI elements
		getRecommendations = new JButton("Get Recommendations");
		category = new HintTextField("Food Category");
		priceRange = new HintTextField("Price Range");
		queryReview = new HintTextField("Description");
		latitude = new HintTextField("Latitude");
		longitude = new HintTextField("Longitude");
		distance = new HintTextField("Distance");
		radius = new HintTextField("Radius");
		rating = new HintTextField("Rating");
		
		category.setColumns(10);
		priceRange.setColumns(10);
		queryReview.setColumns(10);
		latitude.setColumns(10);
		longitude.setColumns(10);
		distance.setColumns(10);
		radius.setColumns(10);
		rating.setColumns(10);
        
        // add the action listener to the recommendations button
		getRecommendations.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (category.getText().length() == 0 || priceRange.getText().length() == 0 ||
                		queryReview.getText().length() == 0 || latitude.getText().length() == 0 ||
                		longitude.getText().length() == 0 || distance.getText().length() == 0 ||
                		radius.getText().length() == 0 || rating.getText().length() == 0) {
                    showErrorMessage("Empty Input");
                } else {
                    getRecommendations(category.getText(), priceRange.getText(),
                    		queryReview.getText(), latitude.getText(),
                    		longitude.getText(), distance.getText(),
                    		radius.getText(), rating.getText());
            		category.setColumns(10);
            		priceRange.setColumns(10);
            		queryReview.setColumns(10);
            		latitude.setColumns(10);
            		longitude.setColumns(10);
            		distance.setColumns(10);
            		radius.setColumns(10);
            		rating.setColumns(10);
                }
            }
        });
        
        
        // construct the UI
        topBar = new JPanel();
        JPanel inputPanel = new JPanel();

        inputPanel.add(category, BorderLayout.NORTH);
        inputPanel.add(priceRange, BorderLayout.NORTH);
        inputPanel.add(queryReview, BorderLayout.NORTH);
        inputPanel.add(latitude, BorderLayout.NORTH);
        inputPanel.add(longitude, BorderLayout.NORTH);
        inputPanel.add(distance, BorderLayout.NORTH);
        inputPanel.add(radius, BorderLayout.NORTH);
        inputPanel.add(rating, BorderLayout.NORTH);
        inputPanel.add(getRecommendations, BorderLayout.NORTH);
        
        topBar.add(inputPanel, BorderLayout.NORTH);
        
        mainFrame.add(topBar, BorderLayout.NORTH);
    }
	
	/**
	 * Takes in user input, does cosine similarity of user query and businesses reviews, 
	 * creates vectors of user and businesses and gives recommendation of businesses based 
	 * on the angle between user and business vectors. The larger the cosine, the higher the 
	 * business is in recommendation list
	 * @param cat category
	 * @param price price
	 * @param query user query
	 * @param lat latitude
	 * @param lon longitude
	 * @param distance radius
	 * @param rating rating 
	 */
	static void getRecommendations(String cat, String price, String query, String lat,
			String lon, String distance, String radius, String rating) {
		//getAllBusinesses from YelpGetter
		YelpGetter yelp = new YelpGetter(Double.parseDouble(lat), Double.parseDouble(lon), 
				Integer.parseInt(radius), cat);
		Business[] businesses = yelp.getBusinesses();
		//do cosine similarity on reviews of businesses and query
		//create documents for reviews
		ArrayList<Document> docList = new ArrayList<Document>();
		for (int i= 0; i < businesses.length; i++) {
			docList.add(new Document(businesses[i].getReviews()));
		}
		Document userQuery = new Document(query);
		docList.add(userQuery);
		Corpus corpus = new Corpus(docList);
		VectorSpaceModel vectorSpace = new VectorSpaceModel(corpus);
		
		for (int i = 0; i < docList.size() - 1; i++) {
			Document doc = docList.get(i);
			//Set reviews cosSimilarity of a business
			businesses[i].setReviewsCosSimilarity(vectorSpace.cosineSimilarity(userQuery, doc));
		}
		
		//do cosine similarity on complete "vectors" of businesses
		//instantiate vectors for user and businesses
		ArrayList<Double> userElements = new ArrayList<Double>();
		userElements.add(Double.parseDouble(price));
		userElements.add(Double.parseDouble(distance));
		userElements.add(Double.parseDouble(rating));
		userElements.add(1.0);
		ArrayList<Double> userVector = new ArrayList<Double>(userElements);
		
		ArrayList<ArrayList<Double>> vectorList = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < businesses.length; i++) {
			ArrayList<Double> businessElements = new ArrayList<Double>();
			businessElements.add(businesses[i].getPrice());
			businessElements.add(businesses[i].getDistance());
			businessElements.add(businesses[i].getRating());
			businessElements.add(businesses[i].getReviewsCosSimilarity());
			vectorList.add(businessElements);
		}
		
		//compute cosine of angle between user input vector and business vectors
		Map<Double, Business> unsortedCosMap = new TreeMap<>();
		for (int i = 0; i < businesses.length; i++) {
			double cos = cosineSimilarity(vectorList.get(i), userVector);
			businesses[i].setCosOfAngleWithUserVec(cos);
			unsortedCosMap.put(cos, businesses[i]);
		}

		
		//Get the top businesses, display them for the user
		ArrayList<Business> recommendedBusinesses = topBusinesses(unsortedCosMap, 5);
		String toDisplay = "";
		for (int i = 0; i < recommendedBusinesses.size(); i++) {
			toDisplay += i + 1 + ". " + recommendedBusinesses.get(i).getName() + "\n";
		}
		//-------Testing------
//		System.out.println(toDisplay);
		//--------------------
	    try {
	        JOptionPane.showMessageDialog(null, toDisplay);
	    } catch (Exception e) {
	        showErrorMessage("An exception was thrown during execution");
	    }
		
	}
	
	/**
	 * sorts the input map of cosines and businesses and returns specified number of top 
	 * businesses
	 * @param cosMap cosine-business map
	 * @param numTop number of top businesses
	 * @return list of recommended businesses
	 */
	static ArrayList<Business> topBusinesses(Map<Double, Business> cosMap, int numTop) {
		ArrayList<Business> topList = new ArrayList<Business>();
		Map<Double, Business> sortedMap = new TreeMap<>(cosMap);
		Set<Double> keySet = sortedMap.keySet();
		ArrayList<Double> keyList = new ArrayList<Double>(keySet);
		int size = sortedMap.size();
		for (int i = size - 1; i > size - 1 - numTop; i--) {
			topList.add(sortedMap.get(keyList.get(i)));
		}
		return topList;
	}
	
	/**
	 * Computes the magnitude of a vector
	 * @param v vector to compute a magnitude of
	 * @return magnitude of a vector
	 */
	static double getMagnitude(ArrayList<Double> v) {
		double magnitude = 0;
		
		Iterator<Double> it = v.iterator();
		while (it.hasNext()) {
			double next = it.next();
			magnitude += next * next;
		}
		
		return Math.sqrt(magnitude);
	}
	
	/**
	 * Computed dot product of two vectors
	 * @param vec1
	 * @param vec2
	 * @return dot product value
	 */
	static double dotProduct(ArrayList<Double> vec1, ArrayList<Double> vec2) {
		double product = 0;
		for (int i = 0; i < vec1.size(); i++) {
			product += vec1.get(i) * vec2.get(i);
		}
		return product;
	}
	
	/**
	 * Computed cosine of an angle between two vectors
	 * @param vec1
	 * @param vec2
	 * @return cosine of an angle
	 */
	static double cosineSimilarity(ArrayList<Double> vec1, ArrayList<Double> vec2) {
		return dotProduct(vec1, vec2) / (getMagnitude(vec1) * getMagnitude(vec2));
	}
	
	
	/**
	 * Displays error message
	 * @param message
	 */
	static void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
	
	

}

/**
 * Text field class with a hint. It shows a hint when field is empty
 */
@SuppressWarnings("serial")
class HintTextField extends JTextField implements FocusListener {

    private final String hint;
    private boolean showHint;

    public HintTextField(final String hint) {
        super(hint);
        this.hint = hint;
        this.showHint = true;
        super.addFocusListener(this);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            showHint = false;
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            showHint = true;
        }
    }

    @Override
    public String getText() {
        return showHint ? "" : super.getText();
    }
	
	
}
