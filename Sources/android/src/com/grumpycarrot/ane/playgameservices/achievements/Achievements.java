//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.achievements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;
import com.grumpycarrot.ane.playgameservices.Extension;


public class Achievements  {

	private final int RC_UNUSED = 5001;

	//-------------------------------------------------------------------
	public Achievements() {}
	//-------------------------------------------------------------------
	public void showAchivements()
	{
		Extension.context.getActivity().startActivityForResult(Games.Achievements.getAchievementsIntent(Extension.context.getApiClient()), RC_UNUSED);
	}
	//-------------------------------------------------------------------
	public void revealAchivement(String achievementId)
	{
		Games.Achievements.reveal(Extension.context.getApiClient(), achievementId);	
	}
	//-------------------------------------------------------------------
	public void unlockAchivement(String achievementId)
	{
		Extension.logEvent("unlockAchivement ");
		Games.Achievements.unlock(Extension.context.getApiClient(), achievementId);	
	}
	//-------------------------------------------------------------------
	public void incrementAchivement(String achievementId, int numSteps)
	{
		Games.Achievements.increment(Extension.context.getApiClient(), achievementId, numSteps);
	}
	//-------------------------------------------------------------------
	public void setStepsAchivement(String achievementId, int numSteps)
	{
		Games.Achievements.setSteps(Extension.context.getApiClient(), achievementId, numSteps);
	}
	//-------------------------------------------------------------------
	public void loadAchievements(boolean forceReload)
	{

		Games.Achievements.load(Extension.context.getApiClient(), forceReload).setResultCallback(
				new ResultCallback<LoadAchievementsResult>() {
					@Override
					public void onResult(LoadAchievementsResult result) {
						
						if(result.getStatus().isSuccess()) {

							AchievementBuffer achievementBuffer=result.getAchievements();
							int totalAchi=achievementBuffer.getCount();
							
							Extension.logEvent("LoadAchievementsResult : "+totalAchi);
							
			    	        JSONArray jsonAchievements = new JSONArray();

			    	        for (int i=0; i < totalAchi; i++) {
			    	        	jsonAchievements.put( AchievementToJsonObject(achievementBuffer.get(i)) );
			    	        }
			    	        achievementBuffer.close();

			    	        Extension.context.sendEventToAir("ON_ACHIEVEMENTS_LOADED",jsonAchievements.toString());				

						} else {
							Extension.context.sendEventToAir("ON_ACHIEVEMENTS_LOADING_FAILED");
						}						

					}
		});
	}
	//-------------------------------------------------------------------
    private JSONObject AchievementToJsonObject( Achievement achievement) {

    	boolean isIncremental;
    	
    	if(achievement.getType() == Achievement.TYPE_INCREMENTAL) isIncremental=true;
    	else isIncremental=false;

    	JSONObject jsonAchievement = new JSONObject();
        try {
        	jsonAchievement.put("achievementId", achievement.getAchievementId());//string
        	jsonAchievement.put("name", achievement.getName());//string
        	jsonAchievement.put("description", achievement.getDescription());//string
        	jsonAchievement.put("state", achievement.getState());//int
        	jsonAchievement.put("revealedImageUri", Extension.context.getUriString(achievement.getRevealedImageUri()));//string
        	jsonAchievement.put("revealedImageUrl", achievement.getRevealedImageUrl());//string
        	jsonAchievement.put("unlockedImageUri", Extension.context.getUriString(achievement.getUnlockedImageUri()));//string
        	jsonAchievement.put("unlockedImageUrl", achievement.getUnlockedImageUrl());//string
        	jsonAchievement.put("type", achievement.getType());//int
        	jsonAchievement.put("xpValue", achievement.getXpValue());//long

        	if(isIncremental) {
            	jsonAchievement.put("currentSteps", achievement.getCurrentSteps());//int
            	jsonAchievement.put("totalSteps", achievement.getTotalSteps());//int
        	} else {
            	jsonAchievement.put("currentSteps", -1);//int
            	jsonAchievement.put("totalSteps", -1);//int        		
        	}

        } catch( JSONException e ) {} 

        return jsonAchievement;
    }
  //-------------------------------------------------------------------   
}
