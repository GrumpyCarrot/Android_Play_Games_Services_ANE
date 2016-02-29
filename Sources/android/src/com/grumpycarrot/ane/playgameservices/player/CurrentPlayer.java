//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.player;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.stats.PlayerStats;
import com.google.android.gms.games.stats.Stats.LoadPlayerStatsResult;
import com.google.android.gms.games.stats.Stats;
import com.grumpycarrot.ane.playgameservices.Extension;


public class CurrentPlayer
{

	//-------------------------------------------------------------------
	public CurrentPlayer() {}	
	//-------------------------------------------------------------------
	public void checkPlayerStats() {

		Extension.logEvent("checkPlayerStats");
		
		PendingResult<LoadPlayerStatsResult> result = Games.Stats.loadPlayerStats(Extension.context.getApiClient(), false);
	    
	    
	    result.setResultCallback(new ResultCallback<Stats.LoadPlayerStatsResult>() {
	    	
	        public void onResult(Stats.LoadPlayerStatsResult result) {
	            Status status = result.getStatus();
	            if (status.isSuccess()) {
	                PlayerStats stats = result.getPlayerStats();

	                if (stats != null) {
	                    
	                    Extension.logEvent("Stats Loaded : "+ stats );
	                    Extension.context.sendEventToAir( "ON_PLAYERSTATS_LOADED", StatsToJsonString(stats) );

	                } else {
	                	Extension.logEvent("No stats available!");
	                }
	            } 
	        }
	    });
	    
	}
    //-------------------------------------------------------------------     
    private String StatsToJsonString( PlayerStats stats) {
        return StatsToJsonObject(stats).toString();
    }
	//-------------------------------------------------------------------
    private JSONObject StatsToJsonObject( PlayerStats stats) {

    	JSONObject jsonPlayerStats = new JSONObject();
        try {

        	jsonPlayerStats.put("averageSessionLength", Float.toString(stats.getAverageSessionLength()));//float
        	jsonPlayerStats.put("daysSinceLastPlayed", stats.getDaysSinceLastPlayed());//int
        	jsonPlayerStats.put("numberOfPurchases", stats.getNumberOfPurchases());//int
        	jsonPlayerStats.put("numberOfSessions", stats.getNumberOfSessions());//int
        	jsonPlayerStats.put("sessionPercentile", Float.toString(stats.getSessionPercentile()));//float
        	jsonPlayerStats.put("spendPercentile", Float.toString(stats.getSpendPercentile()));//float        	

        } catch( JSONException e ) {

        } 

        return jsonPlayerStats;
    }
	//-------------------------------------------------------------------
    public String getActivePlayer() {
    	Extension.logEvent( "getActivePlayer");
    	
    	Player player=Games.Players.getCurrentPlayer(Extension.context.getApiClient());
    	return PlayerToJsonObject(player).toString();
    }

	//-------------------------------------------------------------------
    public JSONObject PlayerToJsonObject( Player player) {

    	JSONObject jsonPlayer = new JSONObject();
    	if(player==null) return jsonPlayer;

        try {

        	jsonPlayer.put("id", player.getPlayerId());
        	jsonPlayer.put("displayName", player.getDisplayName());
        	jsonPlayer.put("iconImageUri",Extension.context.getUriString(player.getIconImageUri()));
        	jsonPlayer.put("iconImageUrl", player.getIconImageUrl());
        	jsonPlayer.put("hiResImageUri", Extension.context.getUriString(player.getHiResImageUri()));
        	jsonPlayer.put("hiResImageUrl", player.getHiResImageUrl());

        } catch( JSONException e ) {} 
        
        return jsonPlayer;
    } 
    //-------------------------------------------------------------------

}
