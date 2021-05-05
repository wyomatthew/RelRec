# Untitled NETS150 Project
The Untitled NETS150 Project is a program utilizing the Yelp API to take a user's inputed geocoordinates and search details to return a sorted list of recommended nearby businesses.

## Building the project
After downloading the most current version of the repository, enter the 'src' folder and run YelpGetter.java. For testing purposes, it currently includes a main method that creates a Yelp Getter with the latitude and longitude of Rome, Italy and returns a list of Yelp's businesses within 40000 meters carrying the category 'pizza' (yum!). As we build other parts of the project, we should expect this to change.

## Project Skeleton
Pulling from our project proposal, we have the following 6 high level tasks:

### 1. Take user inputed location and search to build query
This is TODO. Later on, we'd like to use document search ideas plus cosine similarity to return the best businesses matching the user input. To do this, we need to be able to take in a user query and construct the vector we will use to compare against all business vectors when available. We need to make a few decisions here:
* What metrics should we take from the user and what should be autofilled by the program? For example, I believe we will definitely want to autofill the rating field (since the user will always want the highest rated locations). Still, we will definitely want to take a user inputed restaurant type to compare against our business.

Want from user:
- Category
- Price range (localized for region, parsed to double quantity of currency signs)
- Search query (for reviews)
- latitude
- longitude

Also, in an overloaded constructor for testing/crazy people who want a terribly reviewed place:
- Category
- Price range
- Search query
- Distance
- Radius
- Rating
- latitude
- longitude

* What information should we parse from the user search query as a category vs what should we parse for the document search for reviews? For example, if the user inputs Pizza, should we only get business from the Yelp API that say pizza? Or should we use that for document search in the review similarity portion? Both?

TODO!!

* Figure out how to interpret price
* Fix the bug with TextField. Might be better to use InputDialog instead?
* Do triadic closure

### 2. Translate user inputed location into data interpretable by Yelp API
Tentatively completed! We can consider implementing some more reverse geocoding to accept a user inputed address or use other external tools to use device location, but this is functional as is with latitude and longitude.

### 2. Call Yelp API to get reviews per location
This is mostly completed. We need to complete the ``getReviews()`` method to also retrieve that when pulling all relevant businesses.

### 3. Form individual business vectors
This is TODO. We need to take each business and all of its metrics to form one vector we can compare against the user query to return the best businesses. We should choose here what information to include in each vector.

### 4. Compute cosine similarity of each business to user query
This is TODO. However, we have a nice equation for computing cosine similarity so this should be a pretty fast implementation. It's also worth noting here that I believe cosine similarity takes all dimensions of the vector as essentially weighted equally, so perhaps we should decide if its worth weighting certain fields.

### 5. Deliver recommendations
This is TODO. There are some important design decisions to make here:
* How many businesses do we want to recommend?
* What information do we want to include with the recommendation? i.e. do we want to just deliver the recommendation? Or also include why that store was recommended?

### 6. Deliver recommendations to users
This is TODO. I think this step is mainly just icing-on-top-the-cake. This could be as simple as 
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

At a high level, YelpGetter.java functions with just one field: the URL to follow to interact with the Yelp API. Since every API call to the Yelp API must include a latitude and longitude, those are necessary elements to construct the YelpGetter. The radius is an added necessary input to create the URL because I believe the radius of the businesses to get is a necessary feature. The category is an optional element of the URL because the Yelp API allows the making of calls that are and aren't specified by category. Once a YelpGetter is created and the URL field is created, one can call the ``getBusinesses()`` method to retrieve all businesses returned by the Yelp API for that particular request.

## External Libraries
We take advantage of two external libraries:
* [Reverse Country Code by bencampion](https://github.com/bencampion/reverse-country-code)
Used under the Apache-2.0 License
* [Gson by Google Inc.](https://github.com/google/gson)
Used under the Apache-2.0 License