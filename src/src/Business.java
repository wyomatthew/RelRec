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
    private String price;
    private String address;
    private String phone;
    private double distance;
    private Category[] categories;
    private String reviews;
    private String id;

    public Business(String name, double lat, double lon, int reviewCount, double rating, String price, String address,
            String phone, double distance, Category[] categories, String id, String reviews) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.reviewCount = reviewCount;
        this.rating = rating;
        this.price = price;
        this.address = address;
        this.phone = phone;
        this.distance = distance;
        this.categories = categories;
        this.id = id;
        this.setReviews(reviews);
    }

    @Override
    public String toString() {
        return "Business [name=" + name + ", reviewCount=" + reviewCount + ", rating=" + rating + ", price=" + price
                + ", address=" + address + ", phone=" + phone + ", distance=" + distance + ", categories="
                + Arrays.toString(categories) + ", reviews=" + reviews + ", id=" + id + "]";
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
    public String getPrice() {
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
  
    @Override
    public String toString() {
        return getName() + " || Review Count = " + getReviewCount() + " || Rating = " + getRating() + " || Price = "
                + getPrice() + " || Address = " + getAddress() + " || Phone = " + getPhone() + " || Distance = "
                + getDistance() + " || Reviews = " + reviews;
    }

    /**
     * @return the reviews
     */
    public String getReviews() {
        return reviews;
    }

}
