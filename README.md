Original App Design Project
===

# ClosetWear

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
This app keeps track of your wardrobe and the outfits you've put together with them. It allows you to also share these outfits to others using the app to gain inspo from.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Lifestyle/Fashion
- **Mobile:** Can take photos within the app to directly upload or link to your photos app. Integrates Google Sign-In SDK to allow for easy sign-in. Gestures also allow for easy navigation and interaction in app.
- **Story:** App allows for users to keep track of what they've been wearing (and with what) and what other users have been wearing with similar pieces.
- **Market:** People who are into fashion and looking to have a platform to share and get ideas from. It is also geared towards people that want an easier way to keep track of their wardrobe along with how they've been wearing the clothes in their wardrobe. Additionally, people who are hoping to get into fashion, but don't know where to start, can use the app to view outfits from other users and also be able to track their own progress.
- **Habit:** User will return to the app whenever they get a new piece to add to their wardrobe, or when they have a new fit they want to upload. Additionally, a user will also return to get ideas of what to wear from other users on the app. Lastly, when a user wants to shop they can also get directions to their favorite stores.
- **Scope:** The MVP (uploading wardrobe with outfits, maps and social) is doable in the time span of FBU. From there additional features can be added such as improving on the social algorithm and getting data from certain clothing stores on clothing pieces.

standard product IDs?
## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can login
* User can upload a photo (in app or from folder)
* User can fill out details on clothing/outfit
* User can sign-up/log-in with Google Sign-In SDK
* User can share outfits with other users on the app and view other users' outfits
* User can interact with posts by liking and sharing


**Optional Nice-to-have Stories**

* User can input data about their clothing from certain available stores.
* User can add comments on posts
* User can follow other users they like on the app.
* Improved social algorithm to tailor interests.
* User can get directions to clothing stores near them.
* User can get suggestions for outfits/clothing they may like based on their wardrobe.
* User can add location info for picture taken

### 2. Screen Archetypes

* Login screen
   * User can login
   * User can log-in with Google account
* Upload photo
    * User can take a picture to upload 
    * User can upload picture from photo album
* Clothing/Outfit details
    * User can input details on clothing piece to add to wardrobe
    * User can pair outfits with pieces owned in wardrobe for later reference.
* Timeline
    * User can view other users' outfits
    * User can tap on a photo to get detailed information on outfit and user profile


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Login
* Profile
* Photo upload
* Clothing/Outfit details
* Timeline
* Store list

**Flow Navigation** (Screen to Screen)

* Profile
    * Wardrobe information
* Photo upload
    * Clothing/Outfit details
* Timeline
    * User's profile

## Wireframes
<img src="https://github.com/alexscott08/ClosetWear/raw/master/closetwearWireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
User
| Property    | Type            | Description                  |
| ----------- | --------------- | ---------------------------- |
| objectId    | String          | unique id for post           |
| Property    | Type            | Description                  |
| createdAt   | Number          | date account was created     |
| email       | String          | email address for account    |
| name        | String          | user's display name.         |

ClothingPost
| Property    | Type            | Description                  |
| ----------- | --------------- | ---------------------------- |
| objectId    | String          | unique id for post           |
| image       | File            | image user posts             |
| caption     | String          | description to go with image |
| owner       | Pointer to user | Post author                  |
| createdAt   | Number          | date post is created         |
| itemName    | String          | name of clothing article     |
| brand       | String          | brand associated with item   |
| category    | String          | type of clothing article     |
| subcategory | String          | specific type of clothing    |
| size        | String          | size of clothing             |

OutfitPost
| Property    | Type            | Description                  |
| ----------- | --------------- | ---------------------------- |
| objectId    | String          | unique id for post           |
| image       | File            | image user posts             |
| caption     | String          | description to go with fit   |
| owner       | Pointer to user | Post author                  |
| likesCount  | Number          | number of likes for the post |
| createdAt   | Number          | date post is created         |
| fitItems      | Array of pointers to objectId | all items worn on a fit
| commentsCount | Number                        | number of comments that has been posted to an image 

### Networking
* Home Feed -> Post Details Screen
    * (Read/GET) Query most liked posts
    * (Update/PUT) Like/unlike posts
    * (Create/POST) Create a new comment on a post
    * (Delete) Delete existing comment
* Create Post Screen
    * (Create/POST) Create a new post object
* Profile -> Closet Screen
    * (Read/GET) Query logged in user object
    * (Update/PUT) Update user profile image
    * (Delete) Delete existing post
    * (Update/PUT) Like/unlike posts
    * (Create/POST) Create a new comment on a post
    * (Delete) Delete existing comment
    * (Read/GET) Query all posts where user is author
 `ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                adapter.clear();
                adapter.addAll(posts);
            }
        });`
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]

## FBU App Expectations (based on MVP)
- [x] Your app has multiple views
- [x] Your app interacts with a database (e.g. Parse)
- [x] You can log in/log out of your app as a user
- [x] You can sign up with a new user profile
- [x] Somewhere in your app you can use the camera to take a picture and do something with the picture (take photo of clothing article or outfit and add descriptions and caption)
- [x] Your app integrates with a SDK (Google Sign-In SDK)
- [x] Your app contains at least one more complex algorithm (sorting home screen based on most liked)
- [x] Your app uses gesture recognizers (e.g. double tap to like, e.g. pinch to scale)
- [x] Your app use an animation (double tap to like animation/gesture)
- [x] Your app incorporates an external library to add visual polish (Google MD)
