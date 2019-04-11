# AndroidWithUnity

Android app with a Unity activity that pauses when leaving the activity and resumes when returning.

The Unity part of the app was built in Unity 2018 and contains 3 simple objects (cube, cylinder, sphere) with scripts enabling rotation
of the objects. It also contains a script with methods later called in the UnityPlayerActivity.java on the android side. The methods simply
enable the activation and deactivation of objects. The Unity project was exported for Android and imported into Android Studio.

The Android was built in Android Studio 3.3. The applications main activity (MainActivity.java) is a simple activity with a button that launches the UnityPlayerActivity.java. 
Upon the first transition into the Unity activity (UnityPlayerActivity.java) the Unity splash screen appears that can not be removed 
with the personal version of Unity. After that the Unity scene appears in a FrameLayout with three buttons below it. 
Using the UnityPlayer.UnitySendMessage() method the buttons send data to C# scripts metioned above causing the scene elements to react.

# Solution for App crashing after exiting UnityPlayerActivity

In this section I document the debuging needed in order to get the app working as it is after encountering the issue mentioned in the title. 
Hopefully this saves somebody a bunch of time that would otherwise be spent searching the web for answers since the documentation
on the subject is quite poor.


I was facing an issue when every time I exited the UnityPlayerActivity the app closed. The first solution was
overriding the onBackPressed() function and making sure the super.onBackPressed was never executed since it somehow triggered the
closure of the entire app.

After that the app was not closing anymore but every time I would try to return to the UnityPlayerActivity the activity would crash and
return to the MainActivity. When trying to reenter the UnityPlayerActivity it would simply relaunch itself as if it was never running.
The solution was adding 
```
android:process=":unityplayer"
```
to the AndroidManifest.xml.

At that point the transition from one activity to the other and back was working fine but the Unity scene kept on restarting itself
(along with the annoying splash screen) every time I reentered UnityPlayerActivity. From what I gathered the reason for it was 
in the way UnityPlayer class implements exiting the activity. Sadly I found no documentation on the matter but I did find a nifty
work around at: https://forum.unity.com/threads/keeping-unity-player-alive-to-reuse-in-other-view.248361/

Putting a wrapper around UnityPlayer and overriding the kill() method did the trick. Only thing left was to make sure both the 
navigation back button and action bar back button both moved to MainActivity via an intent since the default onBackPressed still
caused the UnityPlayerActivity to reset when reentering. 


