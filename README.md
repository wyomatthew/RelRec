# RelRec
RelRec is a program utilizing the Yelp API to take a user's inputed geocoordinates and search details to return a sorted list of recommended nearby businesses. Program makes use of cosine similarity to identify businesses that need to be recommended and uses triadic closure for making friend recommendations based prior users' full look-up history of categories. 


## Categories Used
Social Networks
Physical Networks (Internet)
Recommendations
Advanced topics related to the class (privacy in social networks, recommendations, etc.)


## Work Breakdown
Matthew - 

Tina - implemented ``getReviews()`` function that gets reviews of appropriate businesses from Yelp API.
       implemented ``Recommendations`` class, which handles GUI and user interaction, does cosine similarity
       of user's description and reviews of business, creates vectors of user and businesses with appropriate 
       entries, computed angle between them, sorts the businesses, and displays top (up to 5) businesses to the user. 


Nancy -

## Setting up the project
TODO


## Running the project
After downloading the most current version of the repository, enter the 'src' folder and run Recommendations.java. 
A panel should pop-up. The input is 
- Name
  Uesr's name; name field should not be left empty
  
- Category
  Valid input: any Yelp category
  Link to yelp categories: https://www.yelp.com/developers/documentation/v3/all_category_list
  Category field should not be left empty

- Price range
  Valid inputs: 0, 1, 2, 3, 4
  Number corresponds to number of dollar(or other currency) symbols for businesses
  0 being least expensive, and 4 being most expensive
  If field is left empty, default value will be used, which is 0.
  
- Description
  Valid Input: any number of words that describe the business or category
  For example, "tasty, cosy, friendly, etc..."
  This field should not be left empty.

- latitude
  Valid input: any latitude within countries where Yelp has access to businesses
  For example, random location is Rome: 41.8933203
  This field should not be left empty.
  
- longitude
  Valid input: any longitude within countries where Yelp has access to businesses
  For example, random location is Rome: 12.4829321
  This field should not be left empty.

- Distance
  Valid input: any number
  Note that the distance is in meters. 
  If the user leaves it empty, the default value will be used,
  which is 0.
  
- Radius
  Valid input: any number
  Note that the radius is in meters. 
  If field is left empty, the default value will be used, which is 30000
  
- Rating
  Valid input: 0, 1, 2, 3, 4, 5
  If left empty, default value will be used, which is 5.


After giving input and pressing "Get Recommendations" button, user will be able to see 
the pop-up that includes up to five recommended businesses. Below businesses, there are 
friend recommendations based on the prior category searches of other users.
Note that the program might take a while to run. Also, sometimes we get an error from server
when program makes API calls. Please, if this happens just re-press the "Get Recommendations"
button. It should fix it. 

## Project Skeleton
Pulling from our project proposal, we have the following 6 high level tasks:

### 1. Take user inputed location and search to build query
We'd like to use document search ideas plus cosine similarity to return the best businesses matching the user input. To do this, we need to be able to take in a user query and construct the vector we will use to compare against all business vectors when available. We need to make a few decisions here:
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


### 2. Translate user inputed location into data interpretable by Yelp API
We can consider implementing some more reverse geocoding to accept a user inputed address or use other external tools to use device location, but this is functional as is with latitude and longitude.

### 2. Call Yelp API to get reviews per location
This is mostly completed. We need to complete the ``getReviews()`` method to also retrieve that when pulling all relevant businesses.

### 3. Form individual business vectors
This is TODO. We need to take each business and all of its metrics to form one vector we can compare against the user query to return the best businesses. We should choose here what information to include in each vector.

### 4. Compute cosine similarity of each business to user query
This is TODO. However, we have a nice equation for computing cosine similarity so this should be a pretty fast implementation. It's also worth noting here that I believe cosine similarity takes all dimensions of the vector as essentially weighted equally, so perhaps we should decide if its worth weighting certain fields.

### 5. Deliver recommendations
There are some important design decisions to make here:
* How many businesses do we want to recommend?
* What information do we want to include with the recommendation? i.e. do we want to just deliver the recommendation? Or also include why that store was recommended?

### 6. Deliver friend recommendations to users
This feature delivers the users friend recommendations based on triadic closure property. The most frequent search of th euser is recommendated to become friends withsomeone else who has used the applicationa nd also searched the the same category search most frequently out of everyone. 

### 7. Deliver recommendations to users
I think this step is mainly just icing-on-top-the-cake. This could be as simple as 
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
Designs a YelpGetter class to handle creation and execution of API calls to the Yelp API. Has an overloaded constructor allowing for the creation of an API call with or without a specified category to search within. However, every created YelpGetter.java must be called with an inputted latitude, longitude, and search radius. Contains several helper functions managing what constitutes a legal category, how to parse out a received JSON file, and how to get the JSON file associated with a URL request.

At a high level, YelpGetter.java functions with just one field: the URL to follow to interact with the Yelp API. Since every API call to the Yelp API must include a latitude and longitude, those are necessary elements to construct the YelpGetter. The radius is an added necessary input to create the URL because I believe the radius of the businesses to get is a necessary feature. The category is an optional element of the URL because the Yelp API allows the making of calls that are and aren't specified by category. Once a YelpGetter is created and the URL field is created, one can call the ``getBusinesses()`` method to retrieve all businesses returned by the Yelp API for that particular request.

We also make calls to API to get reviews of all the businesses appropriate to category, using ``getReviews()`` method.

### Recommendations.java

GUI
Panel is created with all the necessary TextFields. HintTextField class is created within
this java file. It shows a hint if the TextField is not clicked. If the user clicks it it to
type something, hint will disappear. 
Main panel includes a "Get Recommendations" button that shows a MessageDialog and
displays recommendations for businesses and friends.

Cosine Similarity for Description and reviews
We make use of a slightly modified VectorSpaceModel.java file from the class. We find
cosine similarity between user's description of a place and reviews of a business.
This number describing similarity to user's description is used as one entry in the final
business vector in the following step.


Cosine Similarity between user and business vectors
To find the businesses that match the user input the most, we create user and business vectors that
include entries: price, distance, rating, and reviews cosine similarity to user description.
We compute angles between user and all other business vectors and recommend businesses
that make the smallest angles with user vector. 
Class creates ``angleBetween()`` method that computes and angle between
two vectors.

Most of the above logic is included in ``getRecommendations()`` method.


### Users.java
Defines a users class that delivers friend recommendations to the user based on triadic closure of the most frequent category searches. The class creates methods that reads into txt files that represent every user that has logged into the application. The txt files are read and a hash map that creates a frequency chart of the category and the frequency of those user's category searches is created. The frequencies are then compared against the user's most frequent category and then using triadic closure, a friend recommendation based on the most frequent category searches is generated and then returned.


## External Libraries
We take advantage of two external libraries:
* [Reverse Country Code by bencampion](https://github.com/bencampion/reverse-country-code)
Used under the Apache-2.0 License
* [Gson by Google Inc.](https://github.com/google/gson)
Used under the Apache-2.0 License
