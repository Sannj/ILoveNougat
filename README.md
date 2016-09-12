# <u>ILoveNougat</u>
For - 

![Zappos logo](https://upload.wikimedia.org/wikipedia/en/7/76/Zappos_logo.png)

Hello there! I'm Sanjana, Arizona State University Computer Science Grad student.
This is the app I developed for Zappos :D 
More like for getting the Zappos internship! <i>Give it to me already </i> :see_no_evil:

Let me explain what this app is about.

##The super amazing price checker!!
<b>ILoveNougat</b> is a price checking app. 
This app compares prices between Zappos.com and 6pm.com (Zappos’ sister site). It takes input from the user as a search query and loads the results from the Zappos endpoint. The products are displayed in a RecyclerView. When a user taps on a product in the RecyclerView, the 6pm endpoint is queried to find a match for the exact product. If the product exists and is cheaper on 6pm, then the user is notified about his luck :D 


##Time to set this up!

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Pre-requisites

* Android SDK 17
* Android Build Tools v24.0.1
* Android Support Repository
(Android Studio latest version will take care of all the above)
* Git (If you want to pull the project)


###<u>I have all these. What next?</u>

####Getting the project via Git : 
* Open your favorite terminal.
* Then, `git clone <https repo address>`. 
* Once complete, open Android Studio and choose the project path.  

TADA! It's done! Well, I mean you have the project. Now, let's run this! :smiley:

Refer to this link if you are stuck at any point of time.
[Github Help](https://help.github.com/articles/adding-an-existing-project-to-github-using-the-command-line/ "Title")  

####Running the project : 

All the source files are in - 
`app/src/main/java/com/example/sbadam2/pricechecker`

Yeah, that was long! :no_mouth:

1. Now, right click on `MainActivity.java` and hit run.
2. Choose your emulator device or connect your phone.
3. And you will see the first screen of the app.

#####What am I supposed to enter??
Yaaay! Testing time! :smile:

* On the first screen, you should see a search box and a submit button.
* Now, enter your favorite product details in it. It can be anything as specific as `Kate Spade` or as generic as `heels`. 
* Now, the app will quickly ask Zappos for your favorite product and display them for you.
* You can scroll down and look at all of them.
* Once you spotted the picture of the product you like, Click it!
* You can see details about the product you liked while the app queries 6pm for the exact/similar products. 
* If the app finds the same or a similar product for a lesser price then you can see a button `Take me there`. This button will open the default web browser for you with the 6pm product page. 
* If not, this button will not appear.
* Now that you found a product you liked, how about you let your friends know too? Click the `Spread the happiness` button. Choose your favorite sharing app and send it away! 

Hope you had fun testing out the app. :sweat_smile:

#####And now a little info about the <u>Technical details</u>:
* The first screen is `MainActivity.java`. This is the launching activity. This is a simple activity with couple of TextViews, a EditText and a button.
* The next screen that you get is `ProductResults.java` activity. Here you will either get your results or an alert dialog (saying no products were found) depending on your search query on the previous screen. The restcall to the Zappos API is made here.
* You can see the alert dialog by entering any gibberish text like `blaahblaah`.
* Otherwise, you will have a list of products on this screen. Each product is placed on a CardView and these bunch of CardViews are placed on the RecyclerView.
* All the product cards are clickable. You can click on anyone of the product you like.
* On click, you will be redirected to `ProductPage.java` activity. Here, you can see an enlarged image of the product.
* Nope, we are not done yet. This activity has the business logic and also the rest call to 6pm API is made here.
* If the product you chose on `Zappos.com` and has a similar product on `6pm.com` then you'll see two buttons `Take me there` and `Spread this happiness`. (Enter `heels` as your search query and click the first product `Charm Heel` to see this behavior).
* You can click on `Take me there` and this will open your default web browser with the `6pm product link`. 
* You can click on `Spread this happiness` and you will be asked to choose an app to share the 'zappos product'. 

And that's the overall functionality of the app :smile:

#####Running the tests.
Well, yes I did write a few tests and they are in `MainActivityTests.java` :information_desk_person: 

1. Just right click on this file and click run. 
2. Android Studio will ask you to choose your device. 
3. Choose your device and watch the tests run. 

###Acknowledgements
Thank you Zappos for this wonderful opportunity. I definitely learnt a lot because of this! You are amazing! :) 

