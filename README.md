# UberPriceMapper
The UberPriceMapper is a program utilizing Yelp and Uber APIs to take a user's inputted geocoordinates and search details to return a sorted list of recommended nearby businesses along with their corresponding estimate Uber prices.

## Running UberPriceMapper
After downloading the most current version of the repository, enter the 'src' folder and run YelpGetter.java. For testing purposes, it currently includes a main method that creates a Yelp Getter with the latitude and longitude of Rome, Italy and returns a list of Yelp's businesses within 40000 meters carrying the category 'pizza' (yum!). As we build other parts to the UberPriceMapper, this will def change.

## Project Skeleton
Pulling from our project proposal, we have the following 6 high level tasks:

### 1. Translate user inputted location into data interpretable by Yelp API
This is somewhat taken care of at the moment. A problem I encountered while building the functionality for the Yelp API was that Yelp localizes its legal search categories. That is, Yelp has some list of categories for each region that API calls can search within and they change depending on the region you're in. To solve this, I used an external library that translates geocoordinates (latitude and longitude) to locales (for example, en_US for English, United States). With that library already up and running, this step should be as simple as deciding whether we want to take inputs as a user's latitude and longitude (which is already functional) or add functionality to input an address.

### 2. Call Yelp API to get reviews per location
This is all done! Refer to the Class Design bit to see more regarding how this comes togther. I'll add that our original goal was to simply retrieve a list of businesses, their ratings, and their locations, but it was way too easy to add more funcitonality here so we also retreive information like their pricing scheme, their address, their phone number, review count, categories, etc.

### 3. Translate Yelp location and review information to be inrepretable by Uber API
This is TO-DO. We get the latitude and longitude of each business from the Yelp API. I haven't taken a look at the Uber API docs yet, but I'm guessing that should be sufficient to feed it into the Uber API which would make this step pretty short.

### 4. Call Uber API to get pricing
This is TO-DO. After already learning and setting up the Java infrastructure to call the Yelp API, this shouldn't take _too_ long.

### 5. Implement equation/algorithm to determine user recommendations from information got from Yelp/Uber APIs
This is TO-DO. We've got A LOT of info from the Yelp API and the Uber API should compliement it nicely. Big overall goal here is take that information, run it through our algorithm (hopefully using NETS150 principles), and get a handful of recommended restaurants out the other end. There are some important design decisions to make here:
* What principles do we want to encode into our algorithim? i.e., what theoretical  ideas do we want to use to deliver the best recommendations?
* What information do we want to use? We have access to product pricing, uber pricing, categories, distance, location, and much more, so what is the most relevant?
* How many businesses do we want to recommend?
* What information do we want to include with the recommendation? i.e. do we want to just deliver the recommendation? Or also include why that store was recommended?

### 6. Deliver recommendations to users
This is TO-DO. I think this step is mainly just icing-on-top-the-cake. This could be as simple as 
```Java
System.out.println(recommendations.toString());
```
Or could be as complicated as building out a user interface. I think we should gauge how far we want to go on this step by how fast the other, more necessary steps go.

## Project Design
For the time being, the src folder includes the following classes:

### URLGetter.java
Good ol' URLGetter from the previous homework coded by our good friend Swap. This is used essentially just to build the HTTPURLConnection.

### Business.java
Defines a Business class to handle all necessary fields, constructors, and methods that define a business we may recommend. Includes fields such as pricing, rating, rating count, location, categories, etc. As of now, this class encodes the backbone data structure for how we handle businesses.

### Category.java
Defines a Category class to handle all necessary fields, constructors, and methods that define a category to input or output to/from the Yelp API. Each carries the categories title (interpretable by humans), alias (essentially variable name interpretable by Yelp API), and set of parent categories (ex: 'Food' is a parent category of 'Pizza'). Created to allow easily handling on our end of the categories a business holds.

### YelpGetter.java
Degines a YelpGetter class to handle creation and execution of API calls to the Yelp API. Has an overloaded constructor allowing for the creation of an API call with or without a specifified category to search within. However, every created YelpGetter.java must be called with an inputted latitude, longitude, and search radius. Contains several helper functions managing what constitutes a legal category, how to parse out a received JSON file, and how to get the JSON file associated with a URL request.

At a high level, YelpGetter.java functions with just one field: the URL to follow to interact with the Yelp API. Since every API call to the Yelp API must include a latitude and longitude, those are necessary elements to construct the YelpGetter. The radius is an added necessary input to create the URL because I believe the radius of the businesses to get is a necessary feature of our the UberPriceMapper. The category is an optional element of the URL because the Yelp API allows the making of calls that are and aren't specified by category. Once a YelpGetter is created and the URL field is created, one can call the ```getBusinesses()``` method to retrieve all businesses returned by the Yelp API for that particular request.

## External Libraries
So far, the UberPriceMapper takes advantage of two external libraries:
* [Reverse Country Code by bencampion](https://github.com/bencampion/reverse-country-code)
Used under the Apache-2.0 License
* [Gson by Google Inc.](https://github.com/google/gson)
Used under the Apache-2.0 License