//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests;

import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.event.Event;
import com.google.android.gms.games.event.EventBuffer;
import com.google.android.gms.games.event.Events.LoadEventsResult;
import com.google.android.gms.games.quest.Milestone;
import com.google.android.gms.games.quest.Quest;
import com.google.android.gms.games.quest.QuestBuffer;
import com.google.android.gms.games.quest.QuestUpdateListener;
import com.google.android.gms.games.quest.Quests;
import com.google.android.gms.games.quest.Quests.AcceptQuestResult;
import com.google.android.gms.games.quest.Quests.ClaimMilestoneResult;
import com.google.android.gms.games.quest.Quests.LoadQuestsResult;
import com.grumpycarrot.ane.playgameservices.Extension;


public class EventsQuests implements QuestUpdateListener
{
	public static final int RC_QUEST = 12345;
    private static EventCallback eventCallback;
    private static QuestCallback questCallback;	
    private ResultCallback<ClaimMilestoneResult> mClaimMilestoneResultCallback;
    private ResultCallback<AcceptQuestResult> acceptQuestResultCallback;

	//-------------------------------------------------------------------
	public EventsQuests()  {}	
	//-------------------------------------------------------------------
	// EVENTS
	//-------------------------------------------------------------------
	public void submitEvent(String eventId,int incrementAmount) {
		Extension.logEvent("submitEvent "+ incrementAmount);
		
		if(incrementAmount <= 0) return;
	    Games.Events.increment(Extension.context.getApiClient(), eventId, incrementAmount);
	}
	//-------------------------------------------------------------------
	public void retrieveEvent(boolean forceReload) {

		if(eventCallback == null) eventCallback=new EventCallback();
		PendingResult<LoadEventsResult> pr = Games.Events.load(Extension.context.getApiClient(), forceReload);
		pr.setResultCallback(eventCallback);
	
	}
	//-------------------------------------------------------------------
	public void retrieveEventById(String eventId,boolean forceReload) {

		if(eventCallback == null) eventCallback=new EventCallback();
		PendingResult<LoadEventsResult> pr = Games.Events.loadByIds(Extension.context.getApiClient(), forceReload,eventId);
		pr.setResultCallback(eventCallback);
	}
	//-------------------------------------------------------------------
	class EventCallback implements ResultCallback<LoadEventsResult> {
        public EventCallback (){}

        @Override
        public void onResult(LoadEventsResult result) {

    		if(result.getStatus().isSuccess()) {

    	        EventBuffer eb = result.getEvents();
    	        Extension.logEvent("Event result found : "+ eb.getCount());

    	        JSONArray jsonEvents = new JSONArray();

    	        for (int i=0; i < eb.getCount(); i++) {
    	        	jsonEvents.put( EventToJsonObject(eb.get(i)) );
    	        }
    	        eb.close();

    	        Extension.context.sendEventToAir("ON_RETRIEVE_EVENT_SUCCESS",jsonEvents.toString());

    		} else {
    			Extension.context.sendEventToAir("ON_RETRIEVE_EVENT_FAILED");
    		}
        }
    }    
	//-------------------------------------------------------------------
    private JSONObject EventToJsonObject( Event event) {

    	JSONObject jsonEvent = new JSONObject();
        try {
        	
        	jsonEvent.put("eventId", event.getEventId());//string
        	jsonEvent.put("name", event.getName());//string
        	jsonEvent.put("description", event.getDescription());//string
        	jsonEvent.put("iconImageUrl", event.getIconImageUrl());//string
        	jsonEvent.put("iconImageUri", Extension.context.getUriString(event.getIconImageUri()));//string
        	jsonEvent.put("playerId", event.getPlayer().getPlayerId());//string
        	jsonEvent.put("value", event.getValue());//long
        	jsonEvent.put("formattedValue", event.getFormattedValue());//string
        	jsonEvent.put("isVisible", event.isVisible());//boolean


        } catch( JSONException e ) {} 

        return jsonEvent;
    }
	//-------------------------------------------------------------------
	// QUESTS
	//-------------------------------------------------------------------    
    public void showQuestsUI(int[] questSelector) {

    	if(questSelector.length < 1) return;

        Intent questsIntent = Games.Quests.getQuestsIntent(Extension.context.getApiClient(),questSelector);
        
        Extension.context.getActivity().startActivityForResult(questsIntent, RC_QUEST);
    }
	//-------------------------------------------------------------------
	public void onActivityResult(int requestCode,int responseCode, Intent intent) {

        if (responseCode != Activity.RESULT_OK) {
        	Extension.logEvent("User Cancel Quests Intent");
        	Extension.context.sendEventToAir("ON_QUESTS_UI_CANCEL");
            return;
        }

	    if (intent != null) {

	    	if(intent.hasExtra(Quests.EXTRA_QUEST)) {
	    		Extension.logEvent("EXTRA_QUEST");

	    		Quest quest=intent.getParcelableExtra(Quests.EXTRA_QUEST);
	    		
	    		if(quest.getState() == Quest.STATE_ACCEPTED) {
	    			Games.Quests.showStateChangedPopup(Extension.context.getApiClient(),quest.getQuestId());
	    			Extension.context.sendEventToAir("ON_QUESTS_UI_ACCEPTED", QuestToJsonObject(quest).toString());
	    		} else if(quest.getCurrentMilestone().getState() == Milestone.STATE_COMPLETED_NOT_CLAIMED) {
	    			Extension.context.sendEventToAir("ON_QUESTS_UI_CLAIM_REWARD", QuestToJsonObject(quest).toString());
	    		}
	    	}
	    }	
	}	
	//-------------------------------------------------------------------
    public void loadQuests(int[] questSelector) {
    	if(questSelector.length < 1) return;
        
        if(questCallback == null) questCallback=new QuestCallback();

        Games.Quests.load(Extension.context.getApiClient(), questSelector,Quests.SORT_ORDER_ENDING_SOON_FIRST, true).setResultCallback(questCallback);		
    }
	//-------------------------------------------------------------------
	class QuestCallback implements ResultCallback<LoadQuestsResult> {
        public QuestCallback (){}

        @Override
        public void onResult(LoadQuestsResult result) {

        	if(result.getStatus().isSuccess()) {
        		
	            QuestBuffer qb = result.getQuests();
	            Extension.logEvent("Number of quests:"+qb.getCount());
	            
	            JSONArray jsonQuests = new JSONArray();
	
	            for(int i=0; i < qb.getCount(); i++) {
	            	jsonQuests.put( QuestToJsonObject(qb.get(i)) );
	            }
	            qb.close();
	            
	            Extension.context.sendEventToAir("ON_LOAD_QUESTS_SUCCESS",jsonQuests.toString());

        	} else {
        		Extension.context.sendEventToAir("ON_LOAD_QUESTS_FAILED");
        	}
        	
        }
    }    
	//-------------------------------------------------------------------
    private JSONObject QuestToJsonObject( Quest quest) {

    	JSONObject jsonQuest = new JSONObject();
        try {

        	jsonQuest.put("questId", quest.getQuestId());//string
        	jsonQuest.put("name", quest.getName());//string
        	jsonQuest.put("description", quest.getDescription());//string
        	jsonQuest.put("iconImageUrl", quest.getIconImageUrl());//string
        	jsonQuest.put("bannerImageUrl", quest.getBannerImageUrl());//string
        	jsonQuest.put("iconImageUri", Extension.context.getUriString(quest.getIconImageUri()));//string
        	jsonQuest.put("bannerImageUri", Extension.context.getUriString(quest.getBannerImageUri()));//string
        	jsonQuest.put("startTimestamp", quest.getStartTimestamp());//Number
        	jsonQuest.put("endTimestamp", quest.getEndTimestamp());//Number
        	jsonQuest.put("isEndingSoon", quest.isEndingSoon());//Boolean
        	jsonQuest.put("acceptedTimestamp", quest.getAcceptedTimestamp());//Number
        	jsonQuest.put("lastUpdatedTimestamp", quest.getLastUpdatedTimestamp());//Number
        	jsonQuest.put("state", quest.getState());//int
        	jsonQuest.put("milestone", milestoneToJsonObject(quest.getCurrentMilestone()));//milestone

        } catch( JSONException e ) {} 

        return jsonQuest;
    }
	//-------------------------------------------------------------------
    private JSONObject milestoneToJsonObject( Milestone milestone) {

    	JSONObject jsonMilestone = new JSONObject();
    	String rewardData;
		try {
			rewardData = new String(milestone.getCompletionRewardData(),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			rewardData="Bad Data";
		}
		
        try {
        	jsonMilestone.put("milestoneId", milestone.getMilestoneId());//string
        	jsonMilestone.put("eventId", milestone.getEventId());//string
        	jsonMilestone.put("state", milestone.getState());//int
        	jsonMilestone.put("currentProgress", milestone.getCurrentProgress());//long
        	jsonMilestone.put("targetProgress", milestone.getTargetProgress());//long
        	jsonMilestone.put("rewardData", rewardData);//string
        } catch( JSONException e ) {} 

        return jsonMilestone;
    }
	//-------------------------------------------------------------------
    public void acceptQuest(String questId) {
        
    	createAcceptQuestResultCallback();
    	
    	Games.Quests.accept(Extension.context.getApiClient(), questId).setResultCallback(acceptQuestResultCallback);
    }
	//-------------------------------------------------------------------
	private void createAcceptQuestResultCallback() {
		
		if(acceptQuestResultCallback == null) {
		
			acceptQuestResultCallback = new ResultCallback<AcceptQuestResult>() {
	            @Override
	            public void onResult(AcceptQuestResult result) {
	            	if (result.getStatus().isSuccess()){
	            		Games.Quests.showStateChangedPopup(Extension.context.getApiClient(),result.getQuest().getQuestId());
	            		Extension.logEvent("Accepted succcess"+result.getQuest());
	            		Extension.context.sendEventToAir("ON_QUEST_ACCEPT_SUCCESS");
	            	} else {
	            		Extension.logEvent("Accepted Failed");
	            		Extension.context.sendEventToAir("ON_QUEST_ACCEPT_FAILED");
	            	}
	            }
	        };
		}
	}  
	//-------------------------------------------------------------------
	public void registerQuestUpdateListener(){
		Games.Quests.registerQuestUpdateListener(Extension.context.getApiClient(), this);
	}
	//-------------------------------------------------------------------
	public void unregisterQuestUpdateListener(){
		Games.Quests.unregisterQuestUpdateListener(Extension.context.getApiClient());
	}	
	//-------------------------------------------------------------------
	@Override
	public void onQuestCompleted(Quest quest) {
		
		Extension.logEvent("onQuestCompleted"+quest);

		claimReward(quest.getQuestId(),quest.getCurrentMilestone().getMilestoneId());
	
	}
	//-------------------------------------------------------------------
	public void claimReward(String questId,String milestoneId) {
		
		createMilestoneResultCallback();
		
        Games.Quests.claim(Extension.context.getApiClient(),questId,milestoneId).setResultCallback(mClaimMilestoneResultCallback);			
	}
	//-------------------------------------------------------------------
	private void createMilestoneResultCallback() {
		
		if(mClaimMilestoneResultCallback == null) {
		
	        mClaimMilestoneResultCallback = new ResultCallback<ClaimMilestoneResult>() {
	            @Override
	            public void onResult(ClaimMilestoneResult result) {
	            	if (result.getStatus().isSuccess()){
	            		
	            		String reward;
	                        try {
	        					reward = new String(result.getQuest().getCurrentMilestone().getCompletionRewardData(),"UTF-8");
	        					Extension.logEvent("Congratulations, you got a reward!");
	        					Extension.context.sendEventToAir("ON_CLAIM_REWARD_SUCCESS",reward);
	        				} catch (UnsupportedEncodingException e) {
	        					reward = "Bad Data";
	        					Extension.logEvent("Reward was not claimed due to error.");
	        					Extension.context.sendEventToAir("ON_CLAIM_REWARD_FAILED");
	        				}

	                    } else {
	                    	
	                    	Extension.logEvent("Reward was not claimed due to error.");
	                    	Extension.context.sendEventToAir("ON_CLAIM_REWARD_FAILED");

	                    }
	            }
	        };
		}

	}
	//-------------------------------------------------------------------

	
}
