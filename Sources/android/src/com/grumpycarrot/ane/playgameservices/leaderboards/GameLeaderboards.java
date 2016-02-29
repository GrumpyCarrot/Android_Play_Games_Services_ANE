//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.leaderboards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.android.gms.games.leaderboard.Leaderboards.LoadScoresResult;
import com.grumpycarrot.ane.playgameservices.Extension;

public class GameLeaderboards {

	private final int RC_UNUSED = 5001;

	//-------------------------------------------------------------------
	public GameLeaderboards() {}
	//-------------------------------------------------------------------
	public void showAllLeaderboards()
	{
		Extension.context.getActivity().startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(Extension.context.getApiClient()), RC_UNUSED);
	}
	//-------------------------------------------------------------------
	public void showLeaderboard(String leaderboardId)
	{
		Extension.context.getActivity().startActivityForResult(Games.Leaderboards.getLeaderboardIntent(Extension.context.getApiClient(),leaderboardId), RC_UNUSED);
	}
	//-------------------------------------------------------------------
	public void reportScore(String leaderboardId, int highScore)
	{
		Games.Leaderboards.submitScore(Extension.context.getApiClient(), leaderboardId, highScore);
	}
	//-------------------------------------------------------------------
	public void currentPlayerLeaderboardScore(String leaderboardId, int span, int leaderboardCollection)
	{
		Games.Leaderboards.loadCurrentPlayerLeaderboardScore(Extension.context.getApiClient(),leaderboardId, span, leaderboardCollection).setResultCallback(
				new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {
					@Override
					public void onResult(Leaderboards.LoadPlayerScoreResult result) {
						
						if(result.getStatus().isSuccess()) {
							
							LeaderboardScore leaderboardScore=result.getScore();
							
							if(leaderboardScore!=null) {
								Extension.context.sendEventToAir( "ON_PLAYER_SCORE_LOADED",leaderboardScoreToJsonObject(leaderboardScore).toString());
							} else {
								Extension.context.sendEventToAir( "ON_PLAYER_SCORE_UNKNOWN");
							}

						} else {
							 Extension.context.sendEventToAir( "ON_PLAYER_SCORE_LOAD_FAILED");
						}
					}
		});	
	}
	//-------------------------------------------------------------------
    private JSONObject leaderboardScoreToJsonObject( LeaderboardScore leaderboardScore) {

    	JSONObject jsonScore = new JSONObject();

        try {
        	jsonScore.put("displayRank", leaderboardScore.getDisplayRank());//String
        	jsonScore.put("displayScore", leaderboardScore.getDisplayScore());//String
        	jsonScore.put("rank",leaderboardScore.getRank());//long
        	jsonScore.put("rawScore", leaderboardScore.getRawScore());//long
        	jsonScore.put("player", Extension.context.currentPlayer.PlayerToJsonObject(leaderboardScore.getScoreHolder()));//player

        } catch( JSONException e ) {

        } 
        
        Extension.logEvent("jsonScore: "+jsonScore);

        return jsonScore;
    }
	//-------------------------------------------------------------------
	public void getTopLeaderboard( String leaderboardId,int span, int leaderboardCollection,int maxResults ) {
		
		if(maxResults > 25) maxResults=25;
		else if(maxResults < 1) maxResults=1;

		Games.Leaderboards.loadTopScores(Extension.context.getApiClient(),leaderboardId,span,leaderboardCollection,maxResults,true).setResultCallback(
				new ResultCallback<LoadScoresResult>() {
					@Override
					public void onResult(LoadScoresResult result) {
						processLoadScoresResult(result);
					}
		});	
		
	}
	//-------------------------------------------------------------------
	public void getPlayerCenteredLeaderboard( String leaderboardId,int span, int leaderboardCollection, int maxResults ) {
		
		if(maxResults > 25) maxResults=25;
		else if(maxResults < 1) maxResults=1;

		Games.Leaderboards.loadPlayerCenteredScores(Extension.context.getApiClient(),leaderboardId,span,leaderboardCollection,maxResults,true).setResultCallback(
				new ResultCallback<LoadScoresResult>() {
					@Override
					public void onResult(LoadScoresResult result) {
						processLoadScoresResult(result);
					}
		});	
		
	}	
	//-------------------------------------------------------------------
    public void processLoadScoresResult( LoadScoresResult result ) {

    	
    	if(result.getStatus().isSuccess()) {
            LeaderboardScoreBuffer leaderboardScoreBuffer = result.getScores();
            Leaderboard leaderboard=result.getLeaderboard();

            if(leaderboardScoreBuffer==null) {
            	Extension.context.sendEventToAir( "ON_LEADERBOARD_FAILED");
            } else {
                Extension.context.sendEventToAir( "ON_LEADERBOARD_LOADED", scoresResultToJsonObject(leaderboard,leaderboardScoreBuffer).toString() );   	          	
            }

    	} else {
    		Extension.context.sendEventToAir( "ON_LEADERBOARD_FAILED");
    	}

    }	
	//-------------------------------------------------------------------
	private JSONObject scoresResultToJsonObject(Leaderboard leaderboard, LeaderboardScoreBuffer scoreBuffer ) {

		JSONObject jsonLeaderboard = new JSONObject();
		
        try {
        	jsonLeaderboard.put("leaderboardId", leaderboard.getLeaderboardId());//String
        	jsonLeaderboard.put("displayName", leaderboard.getDisplayName());//String
        	jsonLeaderboard.put("iconImageUri",Extension.context.getUriString(leaderboard.getIconImageUri()));//String
        	jsonLeaderboard.put("iconImageUrl", leaderboard.getIconImageUrl());//String
        	jsonLeaderboard.put("leaderboardScores", scoreBufferToJsonArray(scoreBuffer));//JSONArray
        } catch( JSONException e ) {} 		
        
        return jsonLeaderboard;  
	}
	//-------------------------------------------------------------------
	private JSONArray scoreBufferToJsonArray(LeaderboardScoreBuffer scoreBuffer ) {
		
		int scoresNb = scoreBuffer.getCount();
		JSONArray jsonScores = new JSONArray();
		
		for ( int i = 0; i < scoresNb; ++i ) {
			LeaderboardScore score = scoreBuffer.get(i);
			jsonScores.put( leaderboardScoreToJsonObject(score) );
		}
		
		return jsonScores;		
	}
	//-------------------------------------------------------------------
  
}
