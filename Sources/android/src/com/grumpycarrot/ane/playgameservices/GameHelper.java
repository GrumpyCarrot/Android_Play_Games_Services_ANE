//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Games.GamesOptions;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.request.GameRequest;


public class GameHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public final static int RC_RESOLVE = 9001;
    private final String GAMEHELPER_SHARED_PREFS = "GAMEHELPER_SHARED_PREFS";
    private final String KEY_SIGN_IN_CANCELLATIONS = "KEY_SIGN_IN_CANCELLATIONS";

    /** Listener for sign-in success or failure events. */
    public interface GameHelperListener {
        /**  Called when sign-in fails. As a result, a "Sign-In" button can be shown to the user; */
        void onSignInFailed();

        /** Called when sign-in succeeds. */
        void onSignInSucceeded();
    }
    
    static final int DEFAULT_MAX_SIGN_IN_ATTEMPTS = 3;
    int mMaxAutoSignInAttempts = DEFAULT_MAX_SIGN_IN_ATTEMPTS;

    // configuration done?
    private boolean mSetupDone = false;

    // are we currently connecting?
    private boolean mConnecting = false;

    // Are we expecting the result of a resolution flow?
    boolean mExpectingResolution = false;

    // was the sign-in flow cancelled when we tried it?
    // if true, we know not to try again automatically.
    boolean mSignInCancelled = false;


    // Google API client object we manage.
    GoogleApiClient mGoogleApiClient = null;
    GameHelperListener mListener = null;

    // Client request flags
    public final static int CLIENT_NONE = 0x00;
    public final static int CLIENT_GAMES = 0x01;
    public final static int CLIENT_SNAPSHOT = 0x08;
    public final static int CLIENT_ALL = CLIENT_GAMES | CLIENT_SNAPSHOT;

    // What clients were requested? (bit flags)
    int mRequestedClients = CLIENT_NONE;
    boolean mConnectOnStart = true;

    /*
     * Whether user has specifically requested that the sign-in process begin.
     * If mUserInitiatedSignIn is false, we're in the automatic sign-in attempt
     * that we try once the Activity is started -- if true, then the user has
     * already clicked a "Sign-In" button or something similar
     */
    boolean mUserInitiatedSignIn = false;

    // The connection result we got from our last attempt to sign-in.
    ConnectionResult mConnectionResult = null;

    // The error that happened during sign-in.
    SignInFailureReason mSignInFailureReason = null;


    Invitation mInvitation;
    TurnBasedMatch mTurnBasedMatch;
    ArrayList<GameRequest> mRequests;

	//-------------------------------------------------------------------
    public GameHelper(GameHelperListener listenerActivity) {
        mListener = listenerActivity;
    }
   
	//------------------------------------------------------------------- 
    // SETUP
	//------------------------------------------------------------------- 
    public void setup(int clientsToUse,int maxAutoSignInAttempts,boolean showPopUps) {
    	
    	if (mSetupDone) { logError("Cannot call setup() more than once!"); return; }
    	
    	mRequestedClients = clientsToUse;
    	mMaxAutoSignInAttempts = maxAutoSignInAttempts;

        if (mGoogleApiClient == null) createApiClientBuilder(showPopUps);

        mSetupDone = true;
    }    
    //-------------------------------------------------------------------    
    private void createApiClientBuilder(boolean showPopUps) {
    	
    	debugLog("createApiClientBuilder : " + mRequestedClients);

        GamesOptions.Builder mGamesApiOptionsBuilder= GamesOptions.builder();
        mGamesApiOptionsBuilder.setShowConnectingPopup(showPopUps);
        GamesOptions mGamesApiOptions=mGamesApiOptionsBuilder.build();

        GoogleApiClient.Builder googleApiClient = new GoogleApiClient.Builder(Extension.context.getActivity(), this, this);
        if(showPopUps) googleApiClient.setViewForPopups(Extension.context.getAirView());

        if (0 != (mRequestedClients & CLIENT_GAMES)) {
        	googleApiClient.addApi(Games.API, mGamesApiOptions);
        	googleApiClient.addScope(Games.SCOPE_GAMES);
        }

        if (0 != (mRequestedClients & CLIENT_SNAPSHOT)) {
        	googleApiClient.addScope(Drive.SCOPE_APPFOLDER);
        	googleApiClient.addApi(Drive.API);

        }

        mGoogleApiClient=googleApiClient.build();
    }  
	//------------------------------------------------------------------- 
    // SIGN IN STUFF
	//------------------------------------------------------------------- 
    public void onStart(boolean connectOnStart) {

        if (!mSetupDone) {logError("Operation attempted without setup!"); return; }
        
        mConnectOnStart=connectOnStart;

        if (mConnectOnStart) {
            if (mGoogleApiClient.isConnected()) {
            	debugLog("client was already connected on onStart()");
            } else {
                debugLog("Connecting client.");
                mConnecting = true;
                mGoogleApiClient.connect();
            }
        } else {
            debugLog("Not attempting to connect becase mConnectOnStart=false");
        }
    }  
    //-------------------------------------------------------------------    
    private void resetSignInCancellations() {
        SharedPreferences.Editor editor = Extension.context.getActivity().getSharedPreferences(GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SIGN_IN_CANCELLATIONS, 0);
        editor.commit();
    }      
	//-------------------------------------------------------------------  
    private int incrementSignInCancellations() {
        int cancellations = getSignInCancellations();
        SharedPreferences.Editor editor = Extension.context.getActivity().getApplicationContext().getSharedPreferences(GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_SIGN_IN_CANCELLATIONS, cancellations + 1);
        editor.commit();
        return cancellations + 1;
    }  
	//-------------------------------------------------------------------  
    private int getSignInCancellations() {
        SharedPreferences sp = Extension.context.getActivity().getApplicationContext().getSharedPreferences(GAMEHELPER_SHARED_PREFS, Context.MODE_PRIVATE);
        return sp.getInt(KEY_SIGN_IN_CANCELLATIONS, 0);
    }  
	//-------------------------------------------------------------------
    private void notifyListener(boolean success) {
        if (mListener != null) {
            if (success) mListener.onSignInSucceeded();
            else mListener.onSignInFailed();
        }
    }   
	//------------------------------------------------------------------- 
    public void beginUserInitiatedSignIn() {

        resetSignInCancellations();
        mSignInCancelled = false;
        mConnectOnStart = true;

        if (mGoogleApiClient.isConnected()) {
        	debugLog("SignIn() called when already connected.");
            notifyListener(true);
            return;
        } else if (mConnecting) {
        	debugLog("SignIn() called when already connecting. Be patient!");
        	//Suggestion: disable the sign-in button on startup and also when it's clicked and re-enable when you get the callback.
            return;
        }

        debugLog("Starting USER-INITIATED sign-in flow.");

        // indicate that user is actively trying to sign in
        mUserInitiatedSignIn = true;

        if (mConnectionResult != null) {
            // We have a pending connection result from a previous failure, so start with that.
            debugLog("beginUserInitiatedSignIn: continuing pending sign-in flow.");
            mConnecting = true;
            resolveConnectionResult();
        } else {
            // We don't have a pending connection result, so start anew.
            debugLog("beginUserInitiatedSignIn: starting new sign-in flow.");
            mConnecting = true;
            connect();
        }
    }      
	//-------------------------------------------------------------------     
    private void connect() {
        if (mGoogleApiClient.isConnected()) {
            debugLog("Already connected.");
            return;
        }
        debugLog("Starting connection.");
        mConnecting = true;
        mInvitation = null;
        mTurnBasedMatch = null;
        mGoogleApiClient.connect();
    } 
	//-------------------------------------------------------------------  
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        debugLog("onActivityResult: reqCode="+requestCode+" responseCode :"+responseCode);

        // no longer expecting a resolution
        mExpectingResolution = false;

        if (!mConnecting) { debugLog("onActivityResult: ignoring because we are not connecting."); return; }

        // We're coming back from an activity that was launched to resolve a connection problem. For example, the sign-in UI.
        if (responseCode == Activity.RESULT_OK) {
            // Ready to try to connect again.
            debugLog("onAR: Resolution was RESULT_OK, so connecting current client again.");
            connect();
        } else if (responseCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED) {
            debugLog("onAR: Resolution was RECONNECT_REQUIRED, so reconnecting.");
            connect();
        } else if (responseCode == Activity.RESULT_CANCELED) {
            // User cancelled.
            debugLog("onAR: Got a cancellation result, so disconnecting.");
            mSignInCancelled = true;
            mConnectOnStart = false;
            mUserInitiatedSignIn = false;
            mSignInFailureReason = null; // cancelling is not a failure!
            mConnecting = false;
            mGoogleApiClient.disconnect();

            // increment # of cancellations
            incrementSignInCancellations();

            notifyListener(false);
        } else {
            // Whatever the problem we were trying to solve, it was not solved. So give up and show an error message.
            debugLog("onAR: responseCode="+responseCode);
            giveUp(new SignInFailureReason(mConnectionResult.getErrorCode(),responseCode));
        }
    }
	//------------------------------------------------------------------- 
    // ON CONNECTED
	//------------------------------------------------------------------- 
    @Override
    public void onConnected(Bundle connectionHint) {
        //debugLog("onConnected: connected!"+connectionHint);

        /*
        if (connectionHint != null) {
            debugLog("onConnected: connection hint provided. Checking for invite.");
            Invitation inv = connectionHint.getParcelable(Multiplayer.EXTRA_INVITATION);
            if (inv != null && inv.getInvitationId() != null) {
                debugLog("onConnected: Invitation "+inv);
                mInvitation = inv;
            }
            
            

            

            // Do we have any requests pending?
            mRequests = Games.Requests.getGameRequestsFromBundle(connectionHint);
            if (!mRequests.isEmpty()) {
                debugLog("onConnected: connection hint has " + mRequests.size()+ " request(s)");
            }

            debugLog("onConnected: connection hint provided. Checking for TBMP game.");
            mTurnBasedMatch = connectionHint.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH);
        }
        */

        debugLog("succeedSignIn");
        mSignInFailureReason = null;
        mConnectOnStart = true;
        mUserInitiatedSignIn = false;
        mConnecting = false;
        notifyListener(true);
    }
	//-------------------------------------------------------------------
    public boolean hasInvitation() { return mInvitation != null; }
	//-------------------------------------------------------------------
    public Invitation getInvitation() {return mInvitation; }
	//-------------------------------------------------------------------
    public void clearInvitation() { mInvitation = null; }    
	//-------------------------------------------------------------------
    public boolean hasTurnBasedMatch() { return mTurnBasedMatch != null; }
	//-------------------------------------------------------------------
    public boolean hasRequests() { return mRequests != null; }
	//-------------------------------------------------------------------
    public void clearTurnBasedMatch() { mTurnBasedMatch = null; }
	//-------------------------------------------------------------------
    public void clearRequests() { mRequests = null; }
	//-------------------------------------------------------------------   
    public TurnBasedMatch getTurnBasedMatch() { return mTurnBasedMatch; }
	//------------------------------------------------------------------- 
    public ArrayList<GameRequest> getRequests() { return mRequests; }
    //-------------------------------------------------------------------
    public GoogleApiClient getApiClient() { return mGoogleApiClient; }
	//-------------------------------------------------------------------
    public boolean isSignedIn() { return mGoogleApiClient != null && mGoogleApiClient.isConnected(); }
	//-------------------------------------------------------------------
    public boolean isConnecting() { return mConnecting; }
	//------------------------------------------------------------------- 
    // SIGN OUT STUFF
	//------------------------------------------------------------------- 
    public void signOut() {
        if (!mGoogleApiClient.isConnected()) { debugLog("signOut: was already disconnected."); return; }

        if (0 != (mRequestedClients & CLIENT_GAMES)) {
            debugLog("Signing out from the Google API Client.");
            Games.signOut(mGoogleApiClient);
        }

        // Ready to disconnect
        debugLog("Disconnecting client.");
        mConnectOnStart = false;
        mConnecting = false;
        mGoogleApiClient.disconnect();
    } 
	//------------------------------------------------------------------- 
    public void onStop() {
        debugLog("onStop");
        
        if (!mSetupDone) {logError("Operation attempted without setup!"); return; }

        if (mGoogleApiClient.isConnected()) {
            debugLog("Disconnecting client due to onStop");
            mGoogleApiClient.disconnect();
        } else {
            debugLog("Client already disconnected");
        }
        mConnecting = false;
        mExpectingResolution = false;

    }
	//------------------------------------------------------------------- 
    // ERRORS
	//-------------------------------------------------------------------   
    /**
     * Attempts to resolve a connection failure. This will usually involve
     * starting a UI flow that lets the user give the appropriate consents
     * necessary for sign-in to work.
     */
    private void resolveConnectionResult() {
        // Try to resolve the problem
        if (mExpectingResolution) {debugLog("We're already expecting the result of a previous resolution."); return;}

        if (Extension.context == null) {
            debugLog("No need to resolve issue, activity does not exist anymore");
            return;
        }

        debugLog("resolveConnectionResult: trying to resolve result: " + mConnectionResult);
        
        if (mConnectionResult.hasResolution()) {
            // This problem can be fixed. So let's try to fix it.
            debugLog("Result has resolution. Starting it.");
            try {
                // launch appropriate UI flow (which might, for example, be the
                // sign-in flow)
                mExpectingResolution = true;
                mConnectionResult.startResolutionForResult(Extension.context.getActivity(),RC_RESOLVE);
            } catch (SendIntentException e) {
                // Try connecting again
                debugLog("SendIntentException, so connecting again.");
                connect();
            }
        } else {
            // It's not a problem what we can solve, so give up and show an error.
            debugLog("resolveConnectionResult: result has no resolution. Giving up.");
            giveUp(new SignInFailureReason(mConnectionResult.getErrorCode()));
            
            mConnectionResult = null;
        }
    }
	//-------------------------------------------------------------------   
    @Override
    public void onConnectionFailed(ConnectionResult result) {

        mConnectionResult = result;
        debugLog("Connection failure:");
        debugLog("   - code: "+ mConnectionResult.getErrorCode());
        debugLog("   - resolvable: " + mConnectionResult.hasResolution());
        debugLog("   - details: " + mConnectionResult.toString());

        int cancellations = getSignInCancellations();
        boolean shouldResolve = false;

        if (mUserInitiatedSignIn) {
            debugLog("onConnectionFailed: WILL resolve because user initiated sign-in.");
            shouldResolve = true;
        } else if (mSignInCancelled) {
            debugLog("onConnectionFailed WILL NOT resolve (user already cancelled once).");
            shouldResolve = false;
        } else if (cancellations < mMaxAutoSignInAttempts) {
            debugLog("onConnectionFailed: WILL resolve because we have below the mMaxAutoSignInAttempts ");
            shouldResolve = true;
        } else {
            shouldResolve = false;
            debugLog("onConnectionFailed: Will NOT resolve; not user-initiated and max attempts ");
        }

        if (!shouldResolve) {
            // Fail and wait for the user to want to sign in.
            debugLog("onConnectionFailed: since we won't resolve, failing now.");
            mConnectionResult = result;
            mConnecting = false;
            notifyListener(false);
            return;
        }

        debugLog("onConnectionFailed: resolving problem...");

        // Resolve the connection result. This usually means showing a dialog or
        // starting an Activity that will allow the user to give the appropriate
        // consents so that sign-in can be successful.
        resolveConnectionResult();
    }
	//-------------------------------------------------------------------  
    /** Called when we are disconnected from the Google API client. */
    @Override
    public void onConnectionSuspended(int cause) {
        debugLog("onConnectionSuspended, cause=" + cause);
        disconnect();
        mSignInFailureReason = null;
        mConnecting = false;
        notifyListener(false);
    }  
	//-------------------------------------------------------------------     
    private void disconnect() {
        if (mGoogleApiClient.isConnected()) {
            debugLog("Disconnecting client.");
            mGoogleApiClient.disconnect();
        }
    } 
	//-------------------------------------------------------------------    
    /**
     * Give up on signing in due to an error. Shows the appropriate error
     * message to the user, using a standard error dialog as appropriate to the
     * cause of the error. That dialog will indicate to the user how the problem
     * can be solved (for example, re-enable Google Play Services, upgrade to a
     * new version, etc).
     */
    private void giveUp(SignInFailureReason reason) {
        mConnectOnStart = false;
        disconnect();
        mSignInFailureReason = reason;

        if (reason.mActivityResultCode == GamesActivityResultCodes.RESULT_APP_MISCONFIGURED) {
        	logError("RESULT_APP_MISCONFIGURED");
        }

        showFailureDialog();
        mConnecting = false;
        notifyListener(false);
    }   
	//-------------------------------------------------------------------     
    private void showFailureDialog() {

        if (mSignInFailureReason != null) {
        	
            int errorCode = mSignInFailureReason.getServiceErrorCode();
            int actResp = mSignInFailureReason.getActivityResultCode();   	
    	

	        switch (actResp) {
	            case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
	            		showToast("GooglePlayError : License check failed.");
	                break;
	            case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
	            		showToast("GooglePlayError : Failed to sign in. Please check your network connection and try again.");
	                break;
	            case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
	            		showToast("GooglePlayError : License check failed.");
	                break;
	            default:
	            	showToast("GooglePlayError : Unknown error : "+ errorCode);
	        }
        }
    }   
	//-------------------------------------------------------------------    
    void debugLog(String message) {
    	Extension.logEvent("GameHelper: " + message);  
    }
	//-------------------------------------------------------------------  
    void logError(String message) {
    	Extension.logEvent("GameHelper ERROR: " + message);
    }   
	//-------------------------------------------------------------------     
    // Represents the reason for a sign-in failure
    public static class SignInFailureReason {
        public static final int NO_ACTIVITY_RESULT_CODE = -100;
        int mServiceErrorCode = 0;
        int mActivityResultCode = NO_ACTIVITY_RESULT_CODE;

        public int getServiceErrorCode() {
            return mServiceErrorCode;
        }

        public int getActivityResultCode() {
            return mActivityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode, int activityResultCode) {
            mServiceErrorCode = serviceErrorCode;
            mActivityResultCode = activityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode) {
            this(serviceErrorCode, NO_ACTIVITY_RESULT_CODE);
        }
    }   
    //-------------------------------------------------------------------
	private static void showToast(String toastText)
	{
		Toast.makeText(Extension.context.getActivity().getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
	}	
	//-------------------------------------------------------------------    

}
