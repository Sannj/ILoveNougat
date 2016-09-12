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

* Android SDK 24
* Android Build Tools v24.0.1
* Android Support Repository
(Android Studio latest version will take care of all the above)
* Git (If you want to pull the project)


###<u>I have all these. What next?</u>

####Getting the project via Git : 
* Go to your favorite terminal.
* Then, `git clone <https repo address>`. 
* Once complete, open Android Studio and choose the project path.  

TADA! It's done! Well, I mean you have the project. Now, let's run this!

Refer to this link if you are stuck at any point of time.
[Github Help](https://help.github.com/articles/adding-an-existing-project-to-github-using-the-command-line/ "Title")  

####Running the project : 

All the source files are in - 
`app/src/main/java/com/example/sbadam2/pricechecker`

Yeah, that was long! 
Now, right click on `MainActivity.java` and hit run.

Choose your emulator device or connect your phone.

And you will see the first screen of the app.

#####What am I supposed to enter??


#####Technical details.
* The initial screen is the `MainActivity.java`. This is the launching activity. Here, you will see a search box and a `Get my product!` button. Enter your query and hit the button.
* The next screen is the `ProductResults.java` activity. Here you will either get your results or an alert dialog [saying no products were found] depending on your search query on the previous screen.
* You will have a list of products on this screen. You can see the product image, it's name, it's price. 
* All the product cards are clickable. You can click on anyone of the product you like.
* On click, you will be redirected to `ProductPage.java` activity. Here, you can see an enlarged image of the product.
* Nope, we are not done yet. 
* If the product you chose on `zappos.com` and has a similar product on `6pm.com` then you'll see two buttons `Take me there` and `Spread this happiness`. (Enter `heels` as your search query and click the first product `Charm Heel` to see this behavior).
* You can click on `Take me there` and this will open your default web browser with the `6pm product link`. 
* You can click on `Spread this happiness` and you will be asked to choose an app to share the 'zappos product'. 

Hope you had fun testing out the app. That's the overall functionality of the app.

#####Running the test.
Well, yes I did write a few tests and they are in `MainActivityTests.java` file.  

1. Just right click on this file and click run. 
2. Android Studio will ask you to choose your device. 
3. Choose your device and watch the tests run. 

###Acknowledgements
Thank you Zappos for this wonderful opportunity. I definitely learnt a lot because of this! You are amazing! :) 

