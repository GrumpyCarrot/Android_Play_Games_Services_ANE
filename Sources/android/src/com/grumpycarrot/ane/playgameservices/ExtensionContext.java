//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import com.adobe.air.ActivityResultCallback;
import com.adobe.air.AndroidActivityWrapper;
import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.ImageManager.OnImageLoadedListener;
import com.grumpycarrot.ane.playgameservices.achievements.Achievements;
import com.grumpycarrot.ane.playgameservices.achievements.functions.*;
import com.grumpycarrot.ane.playgameservices.admob.Banner;
import com.grumpycarrot.ane.playgameservices.admob.Interstitial;
import com.grumpycarrot.ane.playgameservices.admob.functions.*;
import com.grumpycarrot.ane.playgameservices.savedgames.functions.*;
import com.grumpycarrot.ane.playgameservices.turnbasegames.functions.*;
import com.grumpycarrot.ane.playgameservices.eventsquests.EventsQuests;
import com.grumpycarrot.ane.playgameservices.eventsquests.functions.*;
import com.grumpycarrot.ane.playgameservices.functions.*;
import com.grumpycarrot.ane.playgameservices.leaderboards.GameLeaderboards;
import com.grumpycarrot.ane.playgameservices.leaderboards.functions.*;
import com.grumpycarrot.ane.playgameservices.player.CurrentPlayer;
import com.grumpycarrot.ane.playgameservices.player.functions.*;
import com.grumpycarrot.ane.playgameservices.savedgames.SavedGames;
import com.grumpycarrot.ane.playgameservices.turnbasegames.TurnBaseMulti;

import java.util.HashMap;
import java.util.Map;

public class ExtensionContext extends FREContext implements ActivityResultCallback,GameHelper.GameHelperListener  
{
	private GameHelper mHelper;
	
	private View adobeAirView;
	private AndroidActivityWrapper aaw;  
	
	private static OnImageLoadedCallback onImageLoadedCallback;
	public Bitmap currentLoadedUriImage;	
	
	public TurnBaseMulti turnBaseMulti;
	public SavedGames savedGames;
	public Interstitial interstitial;
	public Banner banner;
	public EventsQuests eventsQuests ;
	public CurrentPlayer currentPlayer;
	public Achievements achievements;
	public GameLeaderboards leaderboards ;

	public ExtensionContext() {
		Extension.logEvent("** ExtensionContext Construtor **");

		aaw = AndroidActivityWrapper.GetAndroidActivityWrapper();  
		aaw.addActivityResultListener(this);
		aaw.getActivity(); 

		leaderboards=new GameLeaderboards();
		achievements=new Achievements();
		currentPlayer = new CurrentPlayer();
		eventsQuests = new EventsQuests();
		interstitial = new Interstitial();
		banner = new Banner();
	}	
	//-------------------------------------------------------------------
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent ) {  

		Extension.logEvent("*** onActivityResult *** ");

		if (requestCode == GameHelper.RC_RESOLVE)  onActivityResult_SignIn(requestCode, resultCode, intent);
		else if (requestCode == SavedGames.RC_SAVED_GAMES)  savedGames.onActivityResult(requestCode, resultCode, intent);
		else if (requestCode == EventsQuests.RC_QUEST)  eventsQuests.onActivityResult(requestCode, resultCode, intent);
		else if (requestCode == TurnBaseMulti.RC_LOOK_AT_MATCHES) turnBaseMulti.onActivityResult_LookAtMatches(resultCode, intent);
		else if (requestCode == TurnBaseMulti.RC_SELECT_PLAYERS) turnBaseMulti.onActivityResult_ForSelectPlayers(resultCode, intent);
	
	}
	//-------------------------------------------------------------------
	public void saveAirView() {
		
		adobeAirView=this.getActivity().findViewById(android.R.id.content);
		Extension.logEvent("View Id : "+adobeAirView.getId());	
	}
	//-------------------------------------------------------------------
	public View getAirView() {
		return adobeAirView;
	}	
	//-------------------------------------------------------------------
	@Override
	public void dispose() { 
		
		Extension.logEvent("*** dispose ExtensionContext *** ");

		mHelper.onStop();
		eventsQuests.unregisterQuestUpdateListener();
		if(turnBaseMulti!=null ) turnBaseMulti.unregisterTurnBaseMultiListeners();

		mHelper=null;
		adobeAirView=null;
		aaw=null;
		onImageLoadedCallback=null;
		currentLoadedUriImage=null;
		turnBaseMulti=null;
		savedGames=null;
		interstitial=null;
		banner=null;
		eventsQuests=null;
		currentPlayer=null;
		achievements=null;
		leaderboards=null;
	}
	//-------------------------------------------------------------------
	@Override
	public Map<String, FREFunction> getFunctions()
	{
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();

        //------------------------------------------------------------
        // Sign In/out Functions
        //------------------------------------------------------------		
		functionMap.put("initAPI", new InitAPIFunction());
		functionMap.put("signIn", new SignInFunction());
		functionMap.put("signOut", new SignOutFunction());
		functionMap.put("getActivePlayer", new GetActivePlayerFunction());
		functionMap.put("checkPlayerStats", new CheckPlayerStatsFunction());		
        //------------------------------------------------------------
        // Utils Functions
        //------------------------------------------------------------			
		functionMap.put("loadUriImage", new LoadUriImageFunction());	
		functionMap.put("getCurrentLoadedUriImage", new GetCurrentLoadedUriImageFunction());		
        //------------------------------------------------------------
        // Achievements Functions
        //------------------------------------------------------------	
		functionMap.put("showAchievements", new ShowAchievementsFunction());
		functionMap.put("revealAchievement", new RevealAchievementFunction());
		functionMap.put("unlockAchievement", new UnlockAchievementFunction());
		functionMap.put("incrementAchievement", new IncrementAchievementFunction());
		functionMap.put("setStepsAchievement", new SetStepsAchievementFunction());
		functionMap.put("loadAchievements", new LoadAchievementsFunction());
        //------------------------------------------------------------
        // LeaderBoards Functions
        //------------------------------------------------------------			
		functionMap.put("showAllLeaderboards", new ShowAllLeaderboardsFunction());
		functionMap.put("showLeaderboard", new ShowLeaderboardFunction());
		functionMap.put("reportScore", new ReportScoreFunction());
		functionMap.put("getCurrentPlayerLeaderboardScore", new GetCurrentPlayerLeaderboardScoreFunction());
		functionMap.put("getTopLeaderboard", new GetTopLeaderboardFunction());
		functionMap.put("getPlayerCenteredLeaderboard", new GetPlayerCenteredLeaderboardFunction());
        //------------------------------------------------------------
        // SavedGames Functions
        //------------------------------------------------------------			
		functionMap.put("showSavedGamesUI", new ShowSavedGame_UIFunction());
		functionMap.put("getSavedGamesList", new GetSavedGamesListFunction());
		functionMap.put("openGame", new OpenGameFunction());
		functionMap.put("writeGame", new WriteGameFunction());
		functionMap.put("deleteGame", new DeleteGameFunction());
        //------------------------------------------------------------
        // Events Functions
        //------------------------------------------------------------		
		functionMap.put("submitEvent", new SubmitEventFunction());
		functionMap.put("retrieveEvent", new RetrieveEventFunction());
		functionMap.put("RetrieveEventById", new RetrieveEventByIdFunction());
        //------------------------------------------------------------
        // Quests Functions
        //------------------------------------------------------------			
		functionMap.put("showQuestsUI", new ShowQuestsUIFunction());
		functionMap.put("registerQuestUpdateListener", new RegisterQuestUpdateListenerFunction());
		functionMap.put("unregisterQuestUpdateListener", new UnregisterQuestUpdateListenerFunction());
		functionMap.put("loadQuests", new LoadQuestsFunction());
		functionMap.put("acceptQuest", new AcceptQuestFunction());
		functionMap.put("claimReward", new ClaimRewardFunction());
        //------------------------------------------------------------
        // AdMob Interstitial
        //------------------------------------------------------------	
		functionMap.put("interstitialInit", new InterstitialInitFunction());		
		functionMap.put("interstitialIsLoaded", new InterstitialIsLoadedFunction());
		functionMap.put("interstitialLoad", new InterstitialLoadFunction());
		functionMap.put("interstitialShow", new InterstitialShowFunction());
        //------------------------------------------------------------
        // AdMob Banner
        //------------------------------------------------------------		
		functionMap.put("bannerAdInit", new BannerAdInitFunction());
		functionMap.put("bannerAdLoad", new BannerAdLoadFunction());
		functionMap.put("bannerAdShow", new BannerShowFunction());
		functionMap.put("bannerAdHide", new BannerHideFunction());
		functionMap.put("bannerAdRemove", new BannerRemoveFunction());
		functionMap.put("bannerIsShown", new BannerIsShownFunction());
		functionMap.put("bannerIsActivated", new BannerIsActivatedFunction());			
        //------------------------------------------------------------
        // TurnBaseMultiplayer Basic Functions
        //------------------------------------------------------------			
        functionMap.put("TBM_LookAtMatches_UI", new LookAtMatches_UIFunction());
        functionMap.put("TBM_CreateNewGame_UI", new CreateNewGame_UIFunction());
        functionMap.put("TBM_CreateAutoMatch", new CreateAutoMatchFunction());
        functionMap.put("getInvitations", new GetInvitationsFunction());
        functionMap.put("TBM_AcceptInvitation", new AcceptInvitationFunction());
        functionMap.put("TBM_DeclineInvitation", new DeclineInvitationFunction());
        functionMap.put("TBM_DismissInvitation", new DismissInvitationFunction());       
        functionMap.put("TBM_LoadMatches", new LoadMatchesFunction());
        functionMap.put("TBM_LoadMatch", new LoadMatchFunction());
        functionMap.put("TBM_TakeTurn", new TakeTurnFunction());
        functionMap.put("TBM_FinishMatch", new  FinishMatchFunction());
        functionMap.put("TBM_FinishMatchWithData", new  FinishMatchWithDataFunction());
        functionMap.put("TBM_Rematch", new RematchFunction()); 
        functionMap.put("TBM_DismissMatch", new DismissMatchFunction());
        functionMap.put("TBM_CancelMatch", new CancelMatchFunction());
        functionMap.put("TBM_LeaveMatch", new LeaveMatchFunction());
        functionMap.put("TBM_LeaveMatchDuringTurn", new LeaveMatchDuringTurnFunction());
        //-----------------------------------------------------------

		return functionMap;
	}
	//-------------------------------------------------------------------
	// Sign In API
	//-------------------------------------------------------------------
	public void initAPI(boolean enableSavedGames, boolean enableTurnBaseMulti,boolean connectOnStart,int maxAutoSignInAttempts,boolean showPopUps) {
		if (mHelper == null) {
			int clientsToUse;
			saveAirView();
			mHelper = new GameHelper(this); 
			
			if(enableSavedGames) {
				savedGames = new SavedGames();
				clientsToUse=GameHelper.CLIENT_GAMES | GameHelper.CLIENT_SNAPSHOT;
			}
			else clientsToUse=GameHelper.CLIENT_GAMES;
			
			if(enableTurnBaseMulti) {
				turnBaseMulti=new TurnBaseMulti();
			}
			
			mHelper.setup(clientsToUse,maxAutoSignInAttempts,showPopUps);
			mHelper.onStart(connectOnStart);	
		}
	}
	//-------------------------------------------------------------------
	public void startSignIn() {
		Extension.logEvent("StartSignIn");
		mHelper.beginUserInitiatedSignIn();
	}
	//-------------------------------------------------------------------
	public void onActivityResult_SignIn(int requestCode,int responseCode, Intent intent) {
		
		Extension.logEvent("onActivityResult_SignIn");
		mHelper.onActivityResult(requestCode, responseCode, intent);
	}
	//-------------------------------------------------------------------
	public void signOut()
	{
		Extension.logEvent("signOut");
		mHelper.signOut();
		sendEventToAir("ON_SIGN_OUT_SUCCESS");
	}
	//-------------------------------------------------------------------
	public GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }
	//-------------------------------------------------------------------
	// implements GameHelper.GameHelperListener
	//-------------------------------------------------------------------
	@Override
	public void onSignInFailed() {
		Extension.logEvent( "onSignInFailed");
		sendEventToAir("ON_SIGN_IN_FAIL");
	}
	//-------------------------------------------------------------------
	@Override
	public void onSignInSucceeded() {
		Extension.logEvent( "onSignInSucceeded");
		sendEventToAir("ON_SIGN_IN_SUCCESS");

		if(turnBaseMulti!=null)
			turnBaseMulti.registerTurnBaseMultiListeners();

	}
	//-------------------------------------------------------------------
	//Utils
	//-------------------------------------------------------------------
	public void sendEventToAir(String eventName) {
		dispatchStatusEventAsync(eventName, "");
	}
	//-------------------------------------------------------------------
	public void sendEventToAir(String eventName, String data) {		
		dispatchStatusEventAsync(eventName, data);
	}
	//-------------------------------------------------------------------
    public String getUriString(Uri uri) {
    	
    	if(uri!=null) return uri.toString();
    	else return null;
    }	
	//-------------------------------------------------------------------
	public int[] FREArraytoIntArray(FREArray mFREArray) {
		
		Extension.logEvent("FREArraytoIntArray Started... ");

		int arrayLength;

		try {
			arrayLength=(int) mFREArray.getLength();
		} catch (FREInvalidObjectException e) {
			arrayLength=0;
		} catch (FREWrongThreadException e) {
			arrayLength=0;
		}
		
		int[] intArray= new int[arrayLength];

		for(int i=0; i< arrayLength; i++) {
			try {
				intArray[i]=mFREArray.getObjectAt(i).getAsInt();
			} catch (IllegalStateException e) {
				intArray[i]=-1;
			} catch (IllegalArgumentException e) {
				intArray[i]=-1;
			} catch (FRETypeMismatchException e) {
				intArray[i]=-1;
			} catch (FREInvalidObjectException e) {
				intArray[i]=-1;
			} catch (FREWrongThreadException e) {
				intArray[i]=-1;
			}
		}
		
		Extension.logEvent("FREArraytoIntArray Finished... ");
		
		return intArray;
	}
	//-------------------------------------------------------------------
	// Uri Image Loader
	//-------------------------------------------------------------------
    public void loadImageFromUriString(String uriString) {
    	
    	Extension.logEvent("loadImageFromUriString : "+uriString);
    	
    	if(onImageLoadedCallback == null) onImageLoadedCallback=new OnImageLoadedCallback();
        
    	ImageManager imageManager=ImageManager.create(Extension.context.getActivity().getApplicationContext());
    	imageManager.loadImage(onImageLoadedCallback, Uri.parse(uriString));
    }
	//-------------------------------------------------------------------
	class OnImageLoadedCallback implements OnImageLoadedListener {
        public OnImageLoadedCallback (){}

		@Override
		public void onImageLoaded(Uri uri, Drawable drawable, boolean isRequestedDrawable) {

			BitmapDrawable bitmapDrawable=(BitmapDrawable) drawable;
			currentLoadedUriImage = bitmapDrawable.getBitmap();

			Extension.logEvent("onImageLoaded");
			Extension.context.sendEventToAir("ON_URI_IMAGE_LOADED");
		}
    }    
	//------------------------------------------------------------------- 	
	
}
