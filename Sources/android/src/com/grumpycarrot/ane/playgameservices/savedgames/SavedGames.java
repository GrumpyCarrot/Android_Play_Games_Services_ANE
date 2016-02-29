//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.savedgames;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataBuffer;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.Snapshots;
import com.grumpycarrot.ane.playgameservices.Extension;

public class SavedGames
{
	public static final int RC_SAVED_GAMES = 9009;

	private boolean asyncForceReload;//If true, this call will clear any locally cached data and attempt to fetch the latest data from the server.(user refresh)
	private SnapshotMetadataBuffer mSnapshotMetadataBuffer;

	private String asyncOpenGameName;
	private int asyncConflictPolicy;
	private Snapshot currentOpenedSnapshot=null;
	private byte[] mSaveGameData;

	private String asyncNewData;
	private String asyncNewDescription;
	private long asyncNewPlayedTimeMillis;
	private long asyncNewProgressValue;

	//-------------------------------------------------------------------
	public SavedGames()  {}	
	//-------------------------------------------------------------------
	public void showSavedGamesUI(String title,boolean allowAddButton, boolean allowDelete, int maxSnapshotsToShow) {

	    Intent savedGamesIntent = Games.Snapshots.getSelectSnapshotIntent(Extension.context.getApiClient(),title, allowAddButton, allowDelete, maxSnapshotsToShow);
	    Extension.context.getActivity().startActivityForResult(savedGamesIntent, RC_SAVED_GAMES);
	}
	//-------------------------------------------------------------------
	public void onActivityResult(int requestCode,int responseCode, Intent intent) {

        if (responseCode != Activity.RESULT_OK) {
        	Extension.logEvent("User Cancel saved game Intent");
        	Extension.context.sendEventToAir("ON_UI_CANCEL");
            return;
        }
        
		
	    if (intent != null) {
	        if (intent.hasExtra(Snapshots.EXTRA_SNAPSHOT_METADATA)) {
	        	
	        	Extension.logEvent("ActivityResult : Load a snapshot.");
	            SnapshotMetadata snapshotMetadata = (SnapshotMetadata)intent.getParcelableExtra(Snapshots.EXTRA_SNAPSHOT_METADATA);
	            Extension.context.sendEventToAir("ON_UI_LOAD_GAME",SnapshotMetadataToJsonString(snapshotMetadata));

	        } else if (intent.hasExtra(Snapshots.EXTRA_SNAPSHOT_NEW)) {
	        	Extension.logEvent("ActivityResult : Create NewFile");
	        	Extension.context.sendEventToAir("ON_UI_CREATE_GAME");
	        }
	    }	
	}	
	//-------------------------------------------------------------------
	public void loadSnapshots(boolean forceReload) {

		Extension.logEvent("Load The entire game list of saved stuff for player");
		asyncForceReload=forceReload;
		mSnapshotMetadataBuffer=null;
		

	    AsyncTask<Void, Void, Integer> loadSnapshotsTask = new AsyncTask<Void, Void, Integer>() {
	    	//-------------------------------------------------------------------
	        @Override
	        protected Integer doInBackground(Void... params) {
	        	            
	            Snapshots.LoadSnapshotsResult result = Games.Snapshots.load(Extension.context.getApiClient(), asyncForceReload).await();

	            if (result.getStatus().isSuccess()) {
	            	Extension.logEvent("Load all Snapshots Success");
	            	mSnapshotMetadataBuffer=result.getSnapshots();

	            } else{
	            	Extension.logEvent("Error while loading games list: " + result.getStatus().getStatusCode());
	            	mSnapshotMetadataBuffer=null;	
	            	Extension.context.sendEventToAir("ON_LOAD_SAVEDGAMES_FAILED");
	            	
	            }

	            return result.getStatus().getStatusCode();
	        }
	        //-------------------------------------------------------------------
	        @Override
	        protected void onPostExecute(Integer status) {
	           
	        	Extension.logEvent("Load ALL Snapshots Async Finished");
	        	if(mSnapshotMetadataBuffer!=null) sendGameListToFlash();
	        }
	        //-------------------------------------------------------------------
	    };

	    loadSnapshotsTask.execute();
	}	
	//-------------------------------------------------------------------
	private void sendGameListToFlash() {

		int totalGames=mSnapshotMetadataBuffer.getCount();
		Extension.logEvent("Number of game found : "+totalGames);
		
		JSONArray jsonSavedGamesList = new JSONArray();

        for ( int i = 0; i < totalGames; ++i )    			
			jsonSavedGamesList.put( SnapshotMetadataToJsonObject(mSnapshotMetadataBuffer.get(i)) ); 		
		
		Extension.context.sendEventToAir("ON_LOAD_SAVEDGAMES_SUCCESS",jsonSavedGamesList.toString());
		
		mSnapshotMetadataBuffer=null;
	}
	//-------------------------------------------------------------------
	public void openSnapshot(String openGameName,int conflictPolicy) {
		
		asyncOpenGameName=openGameName;
		asyncConflictPolicy=conflictPolicy;
		currentOpenedSnapshot=null;

		Extension.logEvent("openSnapshot");

	    AsyncTask<Void, Void, Integer> openTask = new AsyncTask<Void, Void, Integer>() {
	    	//-------------------------------------------------------------------
	        @Override
	        protected Integer doInBackground(Void... params) {
	            
	            Snapshots.OpenSnapshotResult result = Games.Snapshots.open(Extension.context.getApiClient(), asyncOpenGameName, true, asyncConflictPolicy).await();

	            int status = result.getStatus().getStatusCode();

	            if (status == GamesStatusCodes.STATUS_OK) {
	            	
	            	Extension.logEvent("Open Snapshot Success");
	            	currentOpenedSnapshot = result.getSnapshot();

                    try {
                    	mSaveGameData = currentOpenedSnapshot.getSnapshotContents().readFully();
                    } catch (IOException e) {
                    	Extension.logEvent("Exception reading snapshot: " + e.getMessage());
                    	mSaveGameData = null;
                    }	            	

	            } else {
	            	
	            	Extension.logEvent("Error while loading: " + status);
	            	currentOpenedSnapshot=null;
	            	mSaveGameData =null;
	            	Extension.context.sendEventToAir("ON_OPEN_FAILED");	            	
	            	
	            }
	            
	           
	            return status;
	        }
	        //-------------------------------------------------------------------
	        @Override
	        protected void onPostExecute(Integer status) {

	        	Extension.logEvent("Open Snapshot Async Finished");

	        	if(mSaveGameData!=null) {
	        		Extension.logEvent("Data found");
	        		
	        		if(mSaveGameData.length > 0) Extension.context.sendEventToAir("OPEN_SUCCESS",convertBytesToString(mSaveGameData));
	        		else Extension.context.sendEventToAir("OPEN_SUCCESS","NewSnapshot");
	        	}
	        	
	        	asyncOpenGameName=null;
	        	mSaveGameData=null;

	        }
	        //-------------------------------------------------------------------
	    };

	    openTask.execute();
	}	
	//-------------------------------------------------------------------
	public void writeGame(String uniqueName, String newData, String newDescription, long newPlayedTimeMillis, long newProgressValue) {

		if(isCurrentSnapShotSameAs(uniqueName)) {
			Extension.logEvent("File is Open so we can write on it");
			writeToOpenSnapshot(newData,newDescription,newPlayedTimeMillis,newProgressValue);			
		} else {
			Extension.logEvent("File not open");
			Extension.context.sendEventToAir("WRITE_ERROR_FILE_NOT_OPEN");			
		}	
	}
	//-------------------------------------------------------------------
	private void writeToOpenSnapshot(String newData, String newDescription, long newPlayedTimeMillis, long newProgressValue) {

		Extension.logEvent("writeToOpenSnapshot");
		
		asyncNewData=newData;
		asyncNewDescription=newDescription;
		asyncNewPlayedTimeMillis=newPlayedTimeMillis;
		asyncNewProgressValue=newProgressValue;		
		

	    AsyncTask<Void, Void, Integer> writeTask = new AsyncTask<Void, Void, Integer>() {
	    	//-------------------------------------------------------------------
	        @Override
	        protected Integer doInBackground(Void... params) {

	        	currentOpenedSnapshot.getSnapshotContents().writeBytes(convertStringToBytes(asyncNewData));

	    	    //Create the new MetaData
	    	    boolean isMetaDataChange=false;
	    	    SnapshotMetadataChange metadataChange=null;
	    	    SnapshotMetadataChange.Builder metadataChangeBuilder= new SnapshotMetadataChange.Builder();
	    	    

	    	    if(currentOpenedSnapshot.getMetadata().getDescription() != null) {
		    	    if(currentOpenedSnapshot.getMetadata().getDescription().equals(asyncNewDescription) == false) {
		    	    	metadataChangeBuilder.setDescription(asyncNewDescription); 
		    	    	isMetaDataChange=true;
		    	    }	    	    	
	    	    } else {
	    	    	metadataChangeBuilder.setDescription(asyncNewDescription); 
	    	    	isMetaDataChange=true;	    	    	
	    	    }

	    	    if(currentOpenedSnapshot.getMetadata().getPlayedTime() != asyncNewPlayedTimeMillis) {
	    	    	metadataChangeBuilder.setPlayedTimeMillis(asyncNewPlayedTimeMillis);	//-1 = SnapshotMetadata.PLAYED_TIME_UNKNOWN;
	    	    	isMetaDataChange=true;
	    	    }
	    	    	    	    
	    	    if(currentOpenedSnapshot.getMetadata().getProgressValue() != asyncNewProgressValue) {
	    	    	metadataChangeBuilder.setProgressValue(asyncNewProgressValue);  //-1 = SnapshotMetadata.PROGRESS_VALUE_UNKNOWN
	    	    	isMetaDataChange=true;
	    	    }
	    	    
	    	    if(isMetaDataChange) {
	    	    	Extension.logEvent("MetaData has changed since last time");
	    	    	metadataChange=metadataChangeBuilder.build();
	    	    	
	    	    } else {
	    	    	Extension.logEvent("No change to Metadata");
	    	    	metadataChangeBuilder=null;
	    	    	metadataChange=SnapshotMetadataChange.EMPTY_CHANGE;
	    	    	
	    	    }
	    	    
	            Snapshots.CommitSnapshotResult result = Games.Snapshots.commitAndClose(Extension.context.getApiClient(), currentOpenedSnapshot, metadataChange).await();

	            if (result.getStatus().isSuccess()) {
	            	Extension.logEvent("Write Snapshot Success "+result.getStatus().getStatusCode());
	            	Extension.context.sendEventToAir("WRITE_SUCCESS");
	            		
	            	              
	            } else{
	            	Extension.logEvent("Write Snapshot Error: " + result.getStatus().getStatusCode());
	            	Extension.context.sendEventToAir("WRITE_FAILED");
	            }

	            return result.getStatus().getStatusCode();
	        }
	        //-------------------------------------------------------------------
	        @Override
	        protected void onPostExecute(Integer status) {
	           
	        	Extension.logEvent("Write Snapshot Async Finished "+status);

	        	asyncNewData=null;
	        	asyncNewDescription=null;
            	currentOpenedSnapshot=null;
            	mSaveGameData =null;	    		
	           
	        }
	        //-------------------------------------------------------------------
	    };

	    writeTask.execute();
	}	
	//-------------------------------------------------------------------	
	public void deleteGame(String uniqueName) {

		if(isCurrentSnapShotSameAs(uniqueName)) {
			Extension.logEvent("File is Open so we can delete it");
			deleteSnapshot();			
		} else {
			Extension.logEvent("File not open");
			Extension.context.sendEventToAir("ON_DELETE_ERROR_FILE_NOT_OPEN");			
		}	
	}	
	
	//-------------------------------------------------------------------
	private void deleteSnapshot() {
		
		Extension.logEvent("deleteSnapshot");

	    AsyncTask<Void, Void, Integer> deleteTask = new AsyncTask<Void, Void, Integer>() {
	    	//-------------------------------------------------------------------
	        @Override
	        protected Integer doInBackground(Void... params) {
	            
	            Snapshots.DeleteSnapshotResult result = Games.Snapshots.delete(Extension.context.getApiClient(), currentOpenedSnapshot.getMetadata()).await();

	            if (result.getStatus().isSuccess()) {
	            	Extension.logEvent("Delete Snapshot Success");
	            	Extension.context.sendEventToAir("ON_DELETE_SUCCESS");

	            } else{
	            	Extension.logEvent("Delete Snapshot Failed: " + result.getStatus().getStatusCode());
	            	Extension.context.sendEventToAir("ON_DELETE_FAILED");
	            }

	            return result.getStatus().getStatusCode();
	        }
	        //-------------------------------------------------------------------
	        @Override
	        protected void onPostExecute(Integer status) {
	        	Extension.logEvent("Delete Snapshot Async Finished");
            	currentOpenedSnapshot=null;
            	mSaveGameData =null;	        	
 
	        }
	        //-------------------------------------------------------------------
	    };

	    deleteTask.execute();		
	}
	//-------------------------------------------------------------------  
	private boolean isCurrentSnapShotSameAs(String snapShotName) {
		
		if(currentOpenedSnapshot == null) return false;
		
		if(currentOpenedSnapshot.getMetadata().getUniqueName().equals(snapShotName)) return true;
		else return false;
	}	
    //-------------------------------------------------------------------     
    private String SnapshotMetadataToJsonString( SnapshotMetadata snapshotMetadata) {
        return SnapshotMetadataToJsonObject(snapshotMetadata).toString();
    }
	//-------------------------------------------------------------------
    private JSONObject SnapshotMetadataToJsonObject( SnapshotMetadata snapshotMetadata) {

    	JSONObject jsonSnapshotMetadata = new JSONObject();
        try {
        	
        	jsonSnapshotMetadata.put("uniqueName", snapshotMetadata.getUniqueName());//string
        	jsonSnapshotMetadata.put("description", snapshotMetadata.getDescription());//string
        	jsonSnapshotMetadata.put("lastModifiedTimestamp", snapshotMetadata.getLastModifiedTimestamp());//long
        	jsonSnapshotMetadata.put("playedTime", snapshotMetadata.getPlayedTime());//long
        	jsonSnapshotMetadata.put("progressValue", snapshotMetadata.getProgressValue());//long

        } catch( JSONException e ) {} 

        return jsonSnapshotMetadata;
    }
    //-------------------------------------------------------------------
    private byte[] convertStringToBytes(String value) {
    	return value.getBytes(Charset.forName("UTF-8"));
    }
    //-------------------------------------------------------------------
    private String convertBytesToString(byte[] byteArray) {
        String st = null;
        try {
            st = new String(byteArray, "UTF-8");
            return st;
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return "";
        } 
    }
	//-------------------------------------------------------------------

}
