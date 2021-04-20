package src;

import java.net.*;
import java.util.*;
import com.google.gson.*;

import uk.recurse.geocoding.reverse.Country;
import uk.recurse.geocoding.reverse.ReverseGeocoder;

// Test Eclipse change
/**
 * Class dedicated to manage the getting of relevant, nearby businesses given a
 * user's inputted geocoordinates, search radius, and search categories.
 * 
 * @author Matthew
 * 
 */
public class YelpGetter {
    // initialize fields
    private String request;
    private static final String reqBaseBusiness = "https://api.yelp.com/v3/businesses/search?";
    private static final String reqBaseCategory = "https://api.yelp.com/v3/categories?";
    private static final String key = "Bearer y0ygyyghI5JK4vcMW4MNgi3-wY_k6pSIVOEg9g-g34WPp9kJJZS9uRrV_WQTt2FulH4Ni1axPQ3dAmXo0d8jPs1izmltTQ1Z1xa9bznUN3dZcqZw2SRbzAV8S4F0YHYx";
    private static final double[] MATTHEWS_HOUSE = new double[] { 39.95556, -75.21339 };
    private static final double[] GUADALAJARA = new double[] { 20.666841, -103.361932 };
    private static final double[] ROME = new double[] { 41.8933203, 12.4829321 };

    /**
     * Constructs YelpGetter from inputted coordinates, search radius, and relevant
     * category
     *
     * @param lat      Starting latitude (-90 <= lat <= 90)
     * @param lon      Starting longitude (-180 <= lon <= 180)
     * @param rad      Radius within which to search (0 <= rad <= 40000)
     * @param category Category in which to search for businesses
     */
    public YelpGetter(double lat, double lon, int rad, String category) {
        // verify lat, lon, and radius are valid
        if (lat > 90 || lat < -90) {
            throw new IllegalArgumentException("Latitude must be within -90 <= lat <= 90");
        } else if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be within -180 <= lon <= 180");
        } else if (rad < 0 || rad > 40000) {
            throw new IllegalArgumentException("Radius must be positive and <= 40000");
        }

        // case on whether or not inputted category is a valid category in inputted
        // coordinates
        Category[] validCategories = getValidCategories(lat, lon);
        boolean isValid = false;
        String categoryAlias = category;

        // iterate through valid categories searching for a match
        for (int categoryIndex = 0; categoryIndex < validCategories.length; categoryIndex++) {
            Category curr = validCategories[categoryIndex];
            String currTitle = curr.getTitle();
            String currAlias = curr.getAlias();

            // case on whether or not inputted category matches title, alias, or neither
            if (category.equalsIgnoreCase(currTitle)) {
                // category matches title, get related alias and note category is valid
                isValid = true;
                categoryAlias = currAlias;
                break;
            } else if (category.equalsIgnoreCase(currAlias)) {
                // category matches alias, store alias and note category is valid
                isValid = true;
                categoryAlias = currAlias;
                break;
            } // neither matched, continue iterating
        }

        if (isValid) {
            // inputted category isValid, build query from inputted coordinates and got
            // alias
            this.request = reqBaseBusiness + "latitude=" + lat + "&longitude=" + lon + "&radius=" + rad + "&categories="
                    + categoryAlias;
        } else {
            throw new IllegalArgumentException("Category not found within Yelp's legal categories");
        }
    }

    /**
     * Constructs YelpGetter from inputted coordinates, and search radius
     *
     * @param lat Starting latitude (-90 <= lat <= 90)
     * @param lon Starting longitude (-180 <= lon <= 180)
     * @param rad Radius within which to search (0 <= rad <= 40000)
     */
    public YelpGetter(double lat, double lon, int rad) {
        // verify lat, lon, and radius are valid
        if (lat > 90 || lat < -90) {
            throw new IllegalArgumentException("Latitude must be within -90 <= lat <= 90");
        } else if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be within -180 <= lon <= 180");
        } else if (rad < 0 || rad > 40000) {
            throw new IllegalArgumentException("Radius must be positive and <= 40000");
        }

        // build query from inputted coordinates
        this.request = reqBaseBusiness + "latitude=" + lat + "&longitude=" + lon + "&radius=" + rad;
    }

    /**
     * @return Array of businesses returned by the instance's request
     */
    public Business[] getBusinesses() {
        // get contents from request
        String rawContents = getContentsFromRequest(this.request);

        // parse JsonObject from raw contents
        JsonObject businessesObj = parseJsonStringToObject(rawContents);

        // get businesses JsonArray from JsonObject
        JsonArray businessesArr = businessesObj.get("businesses").getAsJsonArray();
        int numBusinesses = businessesArr.size();

        // iterate through businesses filling return array
        Business[] businesses = new Business[numBusinesses];
        for (int businessIndex = 0; businessIndex < numBusinesses; businessIndex++) {
            // get current business object from array
            JsonObject currBusinessObj = businessesArr.get(businessIndex).getAsJsonObject();

            // get all relevant primitive fields
            String name = currBusinessObj.get("name").getAsJsonPrimitive().getAsString();

            int reviewCount = currBusinessObj.get("review_count").getAsJsonPrimitive().getAsInt();
            double rating = currBusinessObj.get("rating").getAsJsonPrimitive().getAsDouble();
            String price;
            try {
                price = currBusinessObj.get("price").getAsJsonPrimitive().getAsString();
            } catch (Exception e) {
                price = "Pricing Unknown";
            }
            String phone = currBusinessObj.get("display_phone").getAsJsonPrimitive().getAsString();
            double distance = currBusinessObj.get("distance").getAsJsonPrimitive().getAsDouble();

            // get coordinates
            JsonObject coordinateObj = currBusinessObj.getAsJsonObject("coordinates");
            double lat = coordinateObj.get("latitude").getAsJsonPrimitive().getAsDouble();
            double lon = coordinateObj.get("longitude").getAsJsonPrimitive().getAsDouble();

            // get address
            JsonObject locationObj = currBusinessObj.getAsJsonObject("location");
            JsonArray addressArr = locationObj.getAsJsonArray("display_address");
            int numLines = addressArr.size();
            String address = "";
            for (int lineIndex = 0; lineIndex < numLines; lineIndex++) {
                String currLine = addressArr.get(lineIndex).getAsJsonPrimitive().getAsString();

                if (lineIndex < numLines - 1) {
                    currLine += "; ";
                }

                address += currLine;
            }

            // get categories
            JsonArray categoriesArr = currBusinessObj.getAsJsonArray("categories");
            int numCategories = categoriesArr.size();

            // iterate through categories filling currCategories array
            Category[] categories = new Category[numCategories];
            for (int categoryIndex = 0; categoryIndex < numCategories; categoryIndex++) {
                // get curr category object
                JsonObject currCategoryObj = categoriesArr.get(categoryIndex).getAsJsonObject();

                // build current category
                String currTitle = currCategoryObj.getAsJsonPrimitive("title").getAsString();
                String currAlias = currCategoryObj.getAsJsonPrimitive("alias").getAsString();
                Category currCategory = new Category(currAlias, currTitle);

                // add to currCategories array
                categories[categoryIndex] = currCategory;
            }

            // build current business
            Business currBusiness = new Business(name, lat, lon, reviewCount, rating, price, address, phone, distance,
                    categories);

            // add current business to businesses array
            businesses[businessIndex] = currBusiness;
        }

        // return filled array of businesses
        return businesses;
    }

    /**
     * @return request
     */
    public String getRequest() {
        return this.request;
    }

    /**
     * Testing function to print all businesses of a YelpGetter to the console
     */
    public void printAllBusinesses() {
        // get array of businesses
        Business[] businesses = this.getBusinesses();

        // iterate through all businesses
        for (int i = 0; i < businesses.length; i++) {
            Business curr = businesses[i];

            // print current business
            System.out.println(curr.toString());
        }
    }

    /**
     * Uses the user's latitude and longitude to identify a locale and get all
     * searchable categories by the Yelp API
     * 
     * @param lat Latitude of user position
     * @param lon Longitude of user position
     * @return Category array of all valid categories within the user's region
     */
    private static Category[] getValidCategories(double lat, double lon) {
        // initialize request to access valid categories
        String categoryRequest = reqBaseCategory;

        // parse locale from geocoordinates using Reverse Country Code converter
        ReverseGeocoder geocoder = new ReverseGeocoder();
        Optional<Country> cOption = geocoder.getCountry(lat, lon);

        // case on whether or not country was retrieved from x and y
        if (cOption.isPresent()) {
            // country is was retrieved, proceed with getting categories
            Country c = cOption.get();

            // get locales from country
            List<Locale> locales = c.locales();

            // get top locale from retrieved list
            Locale l = locales.get(0);

            // parse to string
            String lString = l.toString();

            // generate category request for Yelp API from retrieved locale
            categoryRequest += "locale=" + lString;

            // parse JsonObject from request
            String requestContents = getContentsFromRequest(categoryRequest);
            JsonObject categoryObj = parseJsonStringToObject(requestContents);

            // get array of categories from categoryObj
            JsonArray categoryArr = categoryObj.getAsJsonArray("categories");
            int numCategories = categoryArr.size();

            // iterate through array filling new array of Category Object
            Category[] categories = new Category[numCategories];

            for (int categoryIndex = 0; categoryIndex < numCategories; categoryIndex++) {
                // get current category as a JSON object
                JsonObject currCategoryObj = categoryArr.get(categoryIndex).getAsJsonObject();

                // pull alias and title from JSON object
                String alias = currCategoryObj.get("alias").getAsJsonPrimitive().getAsString();
                String title = currCategoryObj.get("title").getAsJsonPrimitive().getAsString();

                // pull JSON Array of parent aliases from JSON object
                JsonArray parentAliasArr = currCategoryObj.get("parent_aliases").getAsJsonArray();
                int numParents = parentAliasArr.size();

                Set<String> parentAliases = new HashSet<String>();

                // add all parent aliases in array to String array of aliases
                for (int parentIndex = 0; parentIndex < numParents; parentIndex++) {
                    // get current parent object
                    JsonPrimitive currParent = parentAliasArr.get(parentIndex).getAsJsonPrimitive();

                    // pull String from primitive
                    String currParentAlias = currParent.getAsString();

                    // add to parentAliases if it is non-empty and not already included
                    if (!currParentAlias.isEmpty() && !parentAliases.contains(currParentAlias)) {
                        parentAliases.add(currParentAlias);
                    }
                }

                Category currCategory = new Category(alias, title, parentAliases);
                categories[categoryIndex] = currCategory;
            }

            // return filled array of categories
            return categories;
        } else {
            // country was not retrieved, throw exception
            throw new IllegalArgumentException("Country could not be retrieved from inputted geocoordinates");
        }
    }

    /**
     * Helper function to get JSON String contents of a request to the Yelp API
     *
     * @param req String representation of request to parse into JSON
     * @return String representation of JSON response from Yelp API
     */
    private static String getContentsFromRequest(String req) {
        // open connection
        URLGetter url = new URLGetter(req);
        HttpURLConnection connection = url.getConnection();

        // add authorization key to header
        connection.addRequestProperty("Authorization", key);
        List<String> contents = url.getContents();

        // return first and only string of contents
        return contents.get(0);
    }

    /**
     * Helper function to translate JSON String contents to JsonObject
     *
     * @param contents String contents of JSON
     * @return JsonObject encoded by inputted String
     */
    private static JsonObject parseJsonStringToObject(String contents) {
        // initialize parser
        JsonStreamParser parser = new JsonStreamParser(contents);

        // run parser through string
        JsonElement element = null;
        while (parser.hasNext()) {
            element = parser.next();
        }

        // translate parsed element into object
        return element.getAsJsonObject();
    }

    public static void main(String[] args) {
        YelpGetter yg = new YelpGetter(ROME[0], ROME[1], 40000, "Pizza");
        yg.printAllBusinesses();
    }
}