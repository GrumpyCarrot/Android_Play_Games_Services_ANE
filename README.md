# Play_Games_Services_ANE
==================================================

Play Games Services for Android ANE (created for version 8115000)

Features :

-  Sign in/out
- Achievements
- Leaderboards
- Saved Games
- Turn-based Multiplayer
- Events and Quests
- Player Stats
- Ads (Admobs Banner and Interstitial)

Check out the example folder to see how to use.

-----------------

This ANE requires [Android_Google_Play_Services_Lib_ANE](https://github.com/GrumpyCarrot/Android_Google_Play_Services_Lib_ANE)

-----------------

Don't forget to add to the manifest :

```
		<!-- GooglePlay Games Services -->
		<meta-data android:name="com.google.android.gms.games.APP_ID" android:value="\ MY_APP_ID" />
		<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />

		<!-- Only if you use Admob -->
		  <activity android:name="com.google.android.gms.ads.AdActivity"
					android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"
					android:theme="@android:style/Theme.Translucent" />
```


-----------------

This ANE was created with  -swf-version=30 / AIR 19.

```
<extensionID>com.grumpycarrot.ane.playgameservices</extensionID>
```

------------------------------------------------
 [GrumpyCarrot](http://www.grumpycarrot.com)