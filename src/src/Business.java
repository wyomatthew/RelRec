/**
 * 
 */
package src;

import java.util.Arrays;

/**
 * @author Matthew
 *
 */
public class Business {
    private String name;
    private double lat;
    private double lon;
    private int reviewCount;
    private double rating;
    private double price;
    private String address;
    private String phone;
    private double distance;
    private Category[] categories;
    private String reviews;
    private double reviewsCosSimilarity;
    private double cosOfAngleWithUserVector;
    private String id;

    public Business(String name, double lat, double lon, int reviewCount, double rating, String price, String address,
            String phone, double distance, Category[] categories, String id, String reviews) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.address = address;
        this.phone = phone;
        this.distance = distance;
        this.categories = categories;
        this.id = id;
        this.setReviews(reviews);
        
        this.price = price.length();
        
    }

    @Override
    public String toString() {
        return "Business [name=" + name + ", reviewCount=" + reviewCount + ", rating=" + rating + ", price=" + price
                + ", address=" + address + ", phone=" + phone + ", distance=" + distance + ", categories="
                + Arrays.toString(categories) + ", reviews=" + reviews + ", id=" + id + "]";
    }
    
    /**
     * Sets cosine of angle between business vector and user vector
     * @param cosine
     */
    public void setCosOfAngleWithUserVec(double cosine) {
    	this.cosOfAngleWithUserVector = cosine;
    }
    

    /**
     * Gets cosine of angle between business vector and user vector
     * @return
     */
    public double setCosOfAngleWithUserVec() {
    	return cosOfAngleWithUserVector;
    }
    
    
    /**
     * Set how how much reviews are similar to query
     * @param revSimilarityToQuery
     */
    public void setReviewsCosSimilarity(double revSimilarityToQuery) {
    	this.reviewsCosSimilarity = revSimilarityToQuery;
    }
    

    /**
     * Get how how much reviews are similar to query
     * @return reviewsCosSimilarity
     */
    public double getReviewsCosSimilarity() {
    	return reviewsCosSimilarity;
    }
    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return the coordinates
     */
    public double[] getCoords() {
        return new double[] { this.lat, this.lon };
    }

    /**
     * @return the reviewCount
     */
    public int getReviewCount() {
        return reviewCount;
    }

    /**
     * @return the rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * 
     * @return the categories
     */
    public Category[] getCategories() {
        return this.categories;
    }

    /**
     * @return the reviews
     */
    public String getReviews() {
        return reviews;
    }

    /**
     * @param reviews the reviews to set
     */
    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

}
