# Welcome to the Classy Games repository! #
This was a school capstone group project that was completed for the 2012 fall semester at [Southeastern Louisiana University](http://www.selu.edu/). It has since seen continued development from [Charles Madere](http://charlesmadere.com/), the original team's leader. [Download the app from the Google Play store](https://play.google.com/store/apps/details?id=com.charlesmadere.android.classygames)! The app runs on every version of Android from v2.2 (Froyo) and beyond.

This project has a server side component with it's own repository. It utilizes Amazon Elastic Beanstalk and a few other cool things. Check out its repository here: [ClassyGames-Server](https://github.com/ScootrNova/ClassyGames-Server).

This project actually has a third repository too! This repository, [ClassyGames-Resources](https://github.com/ScootrNova/ClassyGames-Resources), stores all of the app's images and other media files. For instance, I used Adobe Illustrator a whole bunch in order to create some custom icons. The original `.ai` file for those icons, as well as their exported `.png` files in a ton of different resolutions are all available there.

## How to import Classy Games into your IDE ##
Both of these tutorials assume that each IDE is somewhat configured. By that I mean you already have the [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (JDK) as well as the [Android SDK](https://developer.android.com/sdk/index.html). Please ensure that both are completely up-to-date! The version of Android that Classy Games requires (as of the time of this writing, 05/10/2013) is *Android 4.2.2 Google APIs*, so be sure to download that too. I will try to keep the Android version that this app uses in tandem with the latest from Google, but that may take time.

### Eclipse ###
1. blah blah

### IntelliJ IDEA ###
1. Download IntelliJ IDEA from [their website](https://www.jetbrains.com/idea/). The exact version that I am writing this tutorial for is `12.1.3`, but it should work just fine for others too.
2. When IntelliJ opens it should bring up a *Welcome to IntelliJ IDEA* screen. This screen shows a couple of functions, *Create New Project*, *Import Project*, *Open Project*... Click on *Import Project*.
3. A new dialog will open up called *Select File or Directory to Import*. From here, you'll want to navigate to (and select) the `projects/android/` folder of this repository. On Windows mine is `D:\Users\CMadere\Documents\GitHub\ClassyGames\projects\android`. On OS X mine is `/Users/cmadere/Documents/GitHub/ClassyGames/projects/android`. Click `OK`.
4. An *Import Project* screen will now appear. First select *Import project from external model*, then select *Eclipse*, and then click *Next*.
5. This screen is just a second page of the *Import Project* screen. Make sure that the value for *Select Eclipse projects directory:* is set to the directory that you chose earlier (`/projects/android/`). By default for me all of the rest of the settings here are fine, which means *Create module files near .classpath files* is checked, *Project format:* is *.idea (directory based)*, *Link created blah blah* is unchecked, and *Detect test sources blah blah* is just blank. Click *Next*.
6. Now you should see *Select Eclipse projects to import*. Make sure that all four projects are checked: `actionbarsherlock`, `classygames`, `classygamestest`, and `FacebookSDK`. Check the box for *Open Project Structure after import* and then click *Next*.
7. This is another *Import Project* screen. At the top it should say *Please select project SDK*, and then directly below that it'll list your JDK as well as all of your different Android versions. Be sure to select *Android 4.2.2 Google APIs* and then click *Finish*.
8. A *Module files found* dialog may appear that asks about reusing a couple of `.iml` files, just hit *Yes*.
9. IntelliJ will now begin to show its primary GUI, yay! You may see a dialog titled *Import Android Dependencies From Property Files*. It shows *Add dependency classygames --> FacebookSDK* and *Add dependency classygames --> actionbarsherlock*. Make sure that both of those boxes are checked and then hit *OK*.
10. Now you should for real see the entire IntelliJ window and everything nice and ready to go. Try to run (or debug) the app. At this point if it works for you well you're lucky, but I have quite a few errors that need to be fixed. This is because of issues with dependencies, which we will fix in the next few steps.
11. Go to *File* > *Project Structure*. On the left of the *Project Structure* screen that opens, under *Project Settings*, click on *Modules*.
12. (Thanks for keeping up so far; you're almost done!)
13. You should now see the four projects listed in the *Project Structure* screen: *actionbarsherlock*, *classygames*, *classygamestest*, and *FacebookSDK*. First select the *classygames* module and then click the *Dependencies* tab.
14. Click the *plus* button and then select *Library*. A *Choose Libraries* dialog will open which is going to show all of the necessary libraries for Classy Games. The list could change in the future, but select all of them and then click *Add Selected*. (The listing that I see right now is `android-support-v4`, `gcm`, `GoogleAdMobAdsSdk-6.4.1`, and `universal-image-loader-1.8.4`.) You'll now see an updated list of dependencies for the *classygames* module.
15. Select the *actionbarsherlock* module. Make sure that you're on the *Dependencies* tab, then click the *plus* button, and then select *Library*. This time only select `android-support-v4` and then click *Add Selected*.
16. Finally, select the *FacebookSDK* module. Again, go to the *Dependencies* tab, then click the *plus* button, and then select *Library*. And again, only select `android-support-v4` and then click *Add Selected*. Click *OK* to close the *Project Structure* window.
17. On the top toolbar, click *classygames* and then select *Edit Configurations*. On the right, under *Target Device*, make sure that *Show chooser dialog* is selected. Click *OK*.
18. Run or Debug the project by clicking on the main toolbar at the top. A *Choose Device* window should eventually open up. Select a device and hit *OK*.
19. You should soon see Classy Games running on the device of your choice. Congratulations and thanks for following my tutorial!


## More Information about the App ##
### Versioning Explained ###
+ vX.0.0.0 This would be massive release that includes many big new features.
+ v0.X.0.0 This would be a pretty decent update. Maybe nothing huge in terms of new features, but definitely some nice updates.
+ v0.0.X.0 This would be a relatively small update. Maybe one or two new features or tweaks; some may be completely unnoticeable by the user.
+ v0.0.0.X This would be a bug fix release.

## Classy Games v1.5 ##
Classy Games v1.5 was released on March 3rd, 2013. It features a couple of cool things.

1. Fragments! Classy Games is now far better on an Android powered tablet.
2. Facebook SDK v3.0 upgrade
3. Orientation change support (previously the app forced portrait mode)
4. Support for down to Android v2.2 ([Froyo](https://developer.android.com/about/versions/android-2.2.html))
5. AdMob support
6. More!

### Classy Games v1.5.2 ###
Classy Games v1.5.2 was released on March 29th, 2013. It features:

1. Settings!
2. Caching of some network requests to reduce data usage.
3. Ability to disable notifications.
4. Miscellaneous clean up and optimization.

The v1.5.2 release has a few bug fix releases.

1. v1.5.2.1 was released on March 29th, 2013. Fixed a push notification crash bug.
2. v1.5.2.2 was released on March 29th, 2013. Fixed another push notification crash issue.
3. v1.5.2.3 was released on March 30th, 2013. Fixed an issue where the app wasn't properly retrieving the device's stored registration ID when registering for notifications.
4. v1.5.2.4 was released on March 30th, 2013. Fixed an issue where devices running versions of Android below Honeycomb were crashing at the settings screens.

### Classy Games v1.5.1 (and v1.5.1.x) ###
Classy Games v1.5.1 was released on March 9th, 2013. It features:

1. The ability to forfeit games.
2. The ability to skip your turn directly from the games list.
3. Modularized `GenericGameFragment.java` code.

The v1.5.1 release featured a few small bug fix releases.

1. v1.5.1.1 was released on March 10th, 2013.
2. v1.5.1.2 was released on March 11th, 2013.