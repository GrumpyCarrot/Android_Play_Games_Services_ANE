//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.turnbasegames;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.LoadMatchesResponse;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchBuffer;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.CancelMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LeaveMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.LoadMatchesResult;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.grumpycarrot.ane.playgameservices.Extension;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class TurnBaseMulti implements OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener
{
	public final static int RC_SELECT_PLAYERS = 10000;
	public final static int RC_LOOK_AT_MATCHES = 10001;

	//-------------------------------------------------------------------
	public TurnBaseMulti() {
		Extension.logEvent( "TurnBaseMulti init");
	}	
	//-------------------------------------------------------------------
	public void registerTurnBaseMultiListeners() {
		
		Extension.logEvent( "registerTurnBaseMultiListeners");
		
        Games.Invitations.registerInvitationListener(Extension.context.getApiClient(), this);
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(Extension.context.getApiClient(), this);		
	}
	//-------------------------------------------------------------------
	public void unregisterTurnBaseMultiListeners() {

		Games.Invitations.unregisterInvitationListener(Extension.context.getApiClient());
		Games.TurnBasedMultiplayer.unregisterMatchUpdateListener(Extension.context.getApiClient());	
	}
	//-------------------------------------------------------------------
	// CREATE NEW GAME
	//-------------------------------------------------------------------
	public void selectOpponents(int minPlayersToSelect,int maxPlayersToSelect,boolean allowAutomatch) {

		Extension.logEvent( "TurnBaseMulti selectOpponents");
		
    	Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(Extension.context.getApiClient(), minPlayersToSelect,maxPlayersToSelect, allowAutomatch);
    	Extension.context.getActivity().startActivityForResult(intent, RC_SELECT_PLAYERS);	
	}
	//-------------------------------------------------------------------
	public void onActivityResult_ForSelectPlayers(int responseCode, Intent intent) {
		
		Extension.logEvent("TurnBaseMulti selectPlayers RESULT ");

        if (responseCode != Activity.RESULT_OK) {
        	Extension.logEvent("User Cancel Select");
        	Extension.context.sendEventToAir("ON_CREATE_MATCH_UI_CANCELED");
            return;
        }

        final ArrayList<String> invitees = intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
        
        Bundle autoMatchCriteria = null;

        int minAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int maxAutoMatchPlayers = intent.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

        if (minAutoMatchPlayers > 0) {
            autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minAutoMatchPlayers, maxAutoMatchPlayers, 0);
        } else {
            autoMatchCriteria = null;
        }

        TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                .addInvitedPlayers(invitees)
                .setAutoMatchCriteria(autoMatchCriteria).build();

        createGame(tbmc);

	}
	//-------------------------------------------------------------------
    public void createAutoMatch(int minPlayersToSelect,int maxPlayersToSelect) {
    	
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(minPlayersToSelect, maxPlayersToSelect, 0);

        TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
                .setAutoMatchCriteria(autoMatchCriteria).build();
        
        createGame(tbmc);
    }   	
	//-------------------------------------------------------------------
	private void createGame(TurnBasedMatchConfig tbmc) {
		
        Games.TurnBasedMultiplayer.createMatch(Extension.context.getApiClient(), tbmc).setResultCallback(
                new ResultCallback<InitiateMatchResult>() {
            @Override
            public void onResult(InitiateMatchResult result) {
            	processInitiateMatchResult(result);
            }
        });		
	}
    //-------------------------------------------------------------------
    private void processInitiateMatchResult(InitiateMatchResult result) {
    	Extension.logEvent("InitiateMatchResult");

    	if(result.getStatus().isSuccess()){
    		Extension.context.sendEventToAir("ON_INITIATE_MATCH_SUCCESS",TurnBasedMatchToJsonObject(result.getMatch()).toString());
    	} else {
    		Extension.context.sendEventToAir("ON_INITIATE_MATCH_FAILED");
    	}
	
    }	   
	//-------------------------------------------------------------------
	// INVITATION
	//-------------------------------------------------------------------    
    public void loadInvitations(int invitationSortOrder) {

    	int[] matchTurnStatuses= {TurnBasedMatch.MATCH_TURN_STATUS_INVITED};

        Games.TurnBasedMultiplayer.loadMatchesByStatus (Extension.context.getApiClient(), invitationSortOrder, matchTurnStatuses).setResultCallback(
                new ResultCallback<LoadMatchesResult>() {
            @Override
            public void onResult(LoadMatchesResult result) {
            	loadInvitationsProcessResult(result);
            }
        });	 
  	
    }    
	//-------------------------------------------------------------------
    private void loadInvitationsProcessResult(LoadMatchesResult result) {
    	
    	Extension.logEvent("loadInvitationsProcessResult");

    	if(result.getStatus().isSuccess()) {
    		
    		LoadMatchesResponse loadMatchesResponse=result.getMatches();
    		InvitationBuffer invitationBuffer = loadMatchesResponse.getInvitations();

            if(invitationBuffer!=null) {
            	
                JSONArray jsonArray = new JSONArray();   
                int totalInvitation= invitationBuffer.getCount();
                Extension.logEvent("totalInvitation "+totalInvitation);
            	
            	for ( int i = 0; i < totalInvitation; ++i ) {
            		jsonArray.put(InvitationToJsonObject(invitationBuffer.get(i)));
            	}
            	invitationBuffer.close();
            	loadMatchesResponse.close();

            	Extension.context.sendEventToAir("ON_LOAD_INVITATIONS_SUCCESS",jsonArray.toString());

            } else {
            	Extension.logEvent("invitationBuffer is null");
            	Extension.context.sendEventToAir("ON_LOAD_INVITATIONS_FAILED");
            }

    	} else {
    		Extension.logEvent("ON_LOAD_INVITATIONS_FAILED");
    		Extension.context.sendEventToAir("ON_LOAD_INVITATIONS_FAILED");
    	}

    }	
	//-------------------------------------------------------------------
    //Accept an invitation for a turn-based match. This changes the current player's participant status to STATUS_JOINED.
	//-------------------------------------------------------------------
    public void acceptInvitation(String invitationId) {

    	Games.TurnBasedMultiplayer.acceptInvitation(Extension.context.getApiClient(), invitationId).setResultCallback(
                new ResultCallback<InitiateMatchResult>() {
            @Override
            public void onResult(InitiateMatchResult result) {
            	if(result.getStatus().isSuccess()){
            		Extension.context.sendEventToAir("ON_LOAD_MATCH_SUCCESS",TurnBasedMatchToJsonObject(result.getMatch()).toString());
            	} else {
            		Extension.context.sendEventToAir("ON_LOAD_MATCH_FAILED");
            	}       	
            }
        });	 	
    }
	//-------------------------------------------------------------------
    //Decline an invitation for a turn-based match. 
    //Note that this will cancel the match for the other participants and remove the match from the caller's local device. 
	//-------------------------------------------------------------------
    public void declineInvitation (String invitationId) {

    	Games.TurnBasedMultiplayer.declineInvitation(Extension.context.getApiClient(), invitationId);
    }
	//-------------------------------------------------------------------
	//Dismiss an invitation to a turn-based match. Dismissing an invitation will not change the state of the match for the other participants.
	//-------------------------------------------------------------------
    public void dismissInvitation (String invitationId) {

    	Games.TurnBasedMultiplayer.dismissInvitation(Extension.context.getApiClient(), invitationId);
    }
    //-------------------------------------------------------------------
    // Loads Matches
	//-------------------------------------------------------------------
    public void loadMatchesByStatus(int[] matchTurnStatuses) {

    	Extension.logEvent("loadMatchesByStatus "+matchTurnStatuses[0]);
    	
        Games.TurnBasedMultiplayer.loadMatchesByStatus (Extension.context.getApiClient(),  matchTurnStatuses).setResultCallback(
                new ResultCallback<LoadMatchesResult>() {
            @Override
            public void onResult(LoadMatchesResult result) {
            	
            	
            	
            	if(result.getStatus().isSuccess()) {
            		
            		Extension.logEvent("loadMatchesProcessResult");
            		
                    LoadMatchesResponse loadMatchesResponse=result.getMatches();
                    TurnBasedMatchBuffer  myTurnMatches_Buffer = loadMatchesResponse.getMyTurnMatches();
                    TurnBasedMatchBuffer  theirTurnMatches_Buffer = loadMatchesResponse.getTheirTurnMatches();
                    TurnBasedMatchBuffer  completedMatches_Buffer = loadMatchesResponse.getCompletedMatches();

                    int totalMatches;
                    int i;
                    JSONArray jsonArray = new JSONArray();
                    
                    if(myTurnMatches_Buffer!=null) {
                    	
                    	totalMatches=myTurnMatches_Buffer.getCount();
                    	Extension.logEvent("myTurnMatches_Buffer"+totalMatches);
                    	
                    	for (  i = 0; i < totalMatches; ++i ) 
                    		jsonArray.put(TurnBasedMatchToJsonObject(myTurnMatches_Buffer.get(i)));
                   
                    	myTurnMatches_Buffer.close();
                    }
                    
                    if(theirTurnMatches_Buffer!=null) {
                    	
                    	totalMatches=theirTurnMatches_Buffer.getCount();
                    	Extension.logEvent("theirTurnMatches_Buffer"+totalMatches);
                    	
                    	for (  i = 0; i < totalMatches; ++i ) 
                    		jsonArray.put(TurnBasedMatchToJsonObject(theirTurnMatches_Buffer.get(i)));
                    	
                    	theirTurnMatches_Buffer.close();
                    }           
                    
                    if(completedMatches_Buffer!=null) {
                    	
                    	totalMatches=completedMatches_Buffer.getCount();
                    	Extension.logEvent("completedMatches_Buffer"+totalMatches);
                    	
                    	for (  i = 0; i < totalMatches; ++i ) 
                    		jsonArray.put(TurnBasedMatchToJsonObject(completedMatches_Buffer.get(i)));
                    	
                    	completedMatches_Buffer.close();
                    }      

                    loadMatchesResponse.close();
                    
                    Extension.context.sendEventToAir("ON_LOAD_MATCHES_SUCCESS",jsonArray.toString());    		
            	} else {
            		Extension.context.sendEventToAir("ON_LOAD_MATCHES_FAILED");
            	}
            }
        });	 
           	
    }   
    //-------------------------------------------------------------------	
    public void loadMatch(String matchId) {

    	Extension.logEvent("loadMatch");
    	
    	Games.TurnBasedMultiplayer.loadMatch(Extension.context.getApiClient(), matchId)
        .setResultCallback(new ResultCallback<LoadMatchResult>() {
            @Override
            public void onResult(LoadMatchResult result) {

            	if(result.getStatus().isSuccess()){
            		Extension.context.sendEventToAir("ON_LOAD_MATCH_SUCCESS",TurnBasedMatchToJsonObject(result.getMatch()).toString());
            	} else {
            		Extension.context.sendEventToAir("ON_LOAD_MATCH_FAILED");
            	}
            }
        });   	

    }
	//-------------------------------------------------------------------
	public void lookAtMatches_UI() {

		Extension.logEvent( "lookAtMatches_UI");

		Intent inboxIntent=Games.TurnBasedMultiplayer.getInboxIntent(Extension.context.getApiClient());		
		Extension.context.getActivity().startActivityForResult(inboxIntent, TurnBaseMulti.RC_LOOK_AT_MATCHES);		
	}
	//-------------------------------------------------------------------
	public void onActivityResult_LookAtMatches(int responseCode, Intent intent) {
		Extension.logEvent("onActivityResult_LookAtMatches");

        if (responseCode != Activity.RESULT_OK) {
        	Extension.context.sendEventToAir("ON_LOOK_AT_MATCH_UI_CANCELED");
            return;
        }

        TurnBasedMatch match = intent.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

        if (match != null) {
        	Extension.logEvent("TurnBasedMatch Selected");
        	Extension.context.sendEventToAir("ON_LOAD_MATCH_SUCCESS",TurnBasedMatchToJsonObject(match).toString());
        } 

	}    
	//-------------------------------------------------------------------
    // Player Actions
    //-------------------------------------------------------------------
    public void takeTurn(String matchId,String nextParticipantId,String dataToSend) {
    	
    	Extension.logEvent("TakeTurn");

        Games.TurnBasedMultiplayer.takeTurn(Extension.context.getApiClient(), matchId,
                this.convertStringToBytes(dataToSend), nextParticipantId).setResultCallback(
                new ResultCallback<UpdateMatchResult>() {
                    @Override
                    public void onResult(UpdateMatchResult result) {
                    	processUpdateMatchResult(result);
                    }
                });
    }     
    //-------------------------------------------------------------------
    private void processUpdateMatchResult(UpdateMatchResult result) {
    	Extension.logEvent("UpdateMatchResult");

    	if(result.getStatus().isSuccess()){
    		Extension.context.sendEventToAir("ON_UPDATE_MATCH_SUCCESS",TurnBasedMatchToJsonObject(result.getMatch()).toString());
    	} else {
    		Extension.context.sendEventToAir("ON_UPDATE_MATCH_FAILED");
    	}
    }
    //-------------------------------------------------------------------	
    public void finishMatchWithData(String matchId,String matchData) {

    	Extension.logEvent("finishMatchWithData");
    	
    	Games.TurnBasedMultiplayer.finishMatch(Extension.context.getApiClient(), matchId, this.convertStringToBytes(matchData)   )
        .setResultCallback(new ResultCallback<UpdateMatchResult>() {
            @Override
            public void onResult(UpdateMatchResult result) {
            	processUpdateMatchResult(result);
            }
        });   	
    }
    //-------------------------------------------------------------------	
    public void finishMatch(String matchId) {
    	
    	Extension.logEvent("finishMatch");

    	Games.TurnBasedMultiplayer.finishMatch(Extension.context.getApiClient(), matchId  )
        .setResultCallback(new ResultCallback<UpdateMatchResult>() {
            @Override
            public void onResult(UpdateMatchResult result) {
            	processUpdateMatchResult(result);
            }
        });   	
 
    } 
	//-------------------------------------------------------------------
    //Create a rematch of a previously completed turn-based match. 
    //The new match will have the same participants as the previous match.
    //Note that only one rematch may be created from any single completed match, and only by a player that has already called Finish on the match.
    //-------------------------------------------------------------------	
    public void rematch(String matchId) {
    	
    	Extension.logEvent("rematch");

    	Games.TurnBasedMultiplayer.rematch(Extension.context.getApiClient(), matchId  )
        .setResultCallback(new ResultCallback<InitiateMatchResult>() {
            @Override
            public void onResult(InitiateMatchResult result) {
            	processInitiateMatchResult(result);
            }
        });   	
 
    }    
    //-------------------------------------------------------------------     
    //Cancels a turn-based match. Once this call succeeds, the match will be removed from local storage. 
    //Note that this will cancel the match completely, forcing it to end for all players involved.
    //-------------------------------------------------------------------	
    public void cancelMatch(String matchId) {
    	
    	Extension.logEvent("cancelMatch");

    	Games.TurnBasedMultiplayer.cancelMatch(Extension.context.getApiClient(), matchId  )
        .setResultCallback(new ResultCallback<CancelMatchResult>() {
            @Override
            public void onResult(CancelMatchResult result) {

            	if(result.getStatus().isSuccess()){
            		Extension.context.sendEventToAir("ON_CANCEL_MATCH_SUCCESS",result.getMatchId());
            	} else {
            		Extension.context.sendEventToAir("ON_CANCEL_MATCH_FAILED");
            	}
            }
        });   	
 
    }  
	//-------------------------------------------------------------------
    //Delete a match from the server and local storage. 
    //Dismissing a match will not change the state of the match for the other participants, 
    //but dismissed matches will never be shown to the dismissing player again. 
    //-------------------------------------------------------------------	
    public void dismissMatch(String matchId) {
    	
    	Extension.logEvent("dismissMatch");

    	Games.TurnBasedMultiplayer.dismissMatch(Extension.context.getApiClient(), matchId);
    }    
    //-------------------------------------------------------------------    
    //Leave the specified match when it is not the current player's turn. 
    //If this takes the match to fewer than two participants, the match will be canceled.
    //-------------------------------------------------------------------	
    public void leaveMatch(String matchId) {
    	
    	Extension.logEvent("cancelMatch");

    	Games.TurnBasedMultiplayer.leaveMatch(Extension.context.getApiClient(), matchId  )
        .setResultCallback(new ResultCallback<LeaveMatchResult>() {
            @Override
            public void onResult(LeaveMatchResult result) {
            	processLeaveMatchResult(result);
            }
        });   	
 
    } 
	//-------------------------------------------------------------------
    //Leave the specified match during the current player's turn. 
    //If this takes the match to fewer than two participants, the match will be canceled. 
    //The provided pendingParticipantId will be used to determine which participant should act next. 
    //If no pending participant is provided and the match has available auto-match slots, the match will wait for additional players to be found. 
    //If there are no auto-match slots available for this match, a pending participant ID is required. 
    //-------------------------------------------------------------------	
    public void leaveMatchDuringTurn(String matchId,String pendingParticipantId) {
    	
    	Extension.logEvent("leaveMatchDuringTurn");

    	Games.TurnBasedMultiplayer.leaveMatchDuringTurn(Extension.context.getApiClient(), matchId,pendingParticipantId  )
        .setResultCallback(new ResultCallback<LeaveMatchResult>() {
            @Override
            public void onResult(LeaveMatchResult result) {
            	processLeaveMatchResult(result);
            }
        });   	
 
    }   
    //-------------------------------------------------------------------
    private void processLeaveMatchResult(LeaveMatchResult result) {
    	Extension.logEvent("LeaveMatchResult");

    	if(result.getStatus().isSuccess()){
    		Extension.context.sendEventToAir("ON_LEAVE_MATCH_SUCCESS",TurnBasedMatchToJsonObject(result.getMatch()).toString());
    	} else {
    		Extension.context.sendEventToAir("ON_LEAVE_MATCH_FAILED");
    	}
    }     
	//-------------------------------------------------------------------
	// Handle notification events.
	//-------------------------------------------------------------------
	@Override
    public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		Extension.logEvent("->>> A match was updated");

		Extension.context.sendEventToAir("ON_NOTIFICATION_TBM_RECEIVED",TurnBasedMatchToJsonObject(match).toString());
    }
	//-------------------------------------------------------------------
	@Override
    public void onTurnBasedMatchRemoved(String matchId) {
		Extension.logEvent("->>> A match was removed");

		Extension.context.sendEventToAir("ON_NOTIFICATION_TBM_REMOVED",matchId);
    }
	//-------------------------------------------------------------------
	@Override
    public void onInvitationReceived(Invitation invitation) {
		Extension.logEvent("->>> An invitation has arrived");
		
		Extension.context.sendEventToAir("ON_NOTIFICATION_INVITATION_RECEIVED",InvitationToJsonObject(invitation).toString());
    }
	//-------------------------------------------------------------------
	@Override
    public void onInvitationRemoved(String invitationId) {
		Extension.logEvent("->>> An invitation was removed");

		Extension.context.sendEventToAir("ON_NOTIFICATION_INVITATION_REMOVED",invitationId);
    }
    //-------------------------------------------------------------------
    // Helpers
	//-------------------------------------------------------------------
    private JSONObject TurnBasedMatchToJsonObject(TurnBasedMatch match) {
    	
    	JSONObject jsonTurnBasedMatch = new JSONObject();
    	JSONArray jsonParticipants = new JSONArray();
    	int participantNumber;
    	
    	if(match.getParticipants()==null) participantNumber=0;
    	else participantNumber=match.getParticipants().size();

    	for ( int i = 0; i < participantNumber; ++i ) {
    		jsonParticipants.put( ParticipantToJsonObject(match.getParticipants().get(i)) );
    	} 

        try {
        	
        	jsonTurnBasedMatch.put("matchId", match.getMatchId());//String
        	jsonTurnBasedMatch.put("creationTimestamp", match.getCreationTimestamp());//long
        	jsonTurnBasedMatch.put("creatorId", match.getCreatorId());//String
        	jsonTurnBasedMatch.put("lastUpdatedTimestamp", match.getLastUpdatedTimestamp());//long
        	jsonTurnBasedMatch.put("canRematch", match.canRematch());//boolean
        	jsonTurnBasedMatch.put("status", match.getStatus());//int
        	jsonTurnBasedMatch.put("turnStatus", match.getTurnStatus());//int
        	jsonTurnBasedMatch.put("data", convertBytesToString(match.getData()) );//String
        	jsonTurnBasedMatch.put("pendingParticipantId", match.getPendingParticipantId());//String
        	jsonTurnBasedMatch.put("totalParticipants", participantNumber);//int
        	jsonTurnBasedMatch.put("participants", jsonParticipants);//Participants
        	jsonTurnBasedMatch.put("availableAutoMatchSlots",match.getAvailableAutoMatchSlots());//int
        	jsonTurnBasedMatch.put("myParticipantId",getCurrentPlayerParticipantId(match));//String

        } catch( JSONException e ) {} 

        return jsonTurnBasedMatch;
    }
	//-------------------------------------------------------------------
    private JSONObject ParticipantToJsonObject( Participant participant) {

    	JSONObject jsonParticipant = new JSONObject();
    	String playerId;
    	
    	if(participant.getPlayer()!=null) { playerId=participant.getPlayer().getPlayerId(); } 
    	else { playerId="Unknown"; }
    	
        try {

        	jsonParticipant.put("participantId", participant.getParticipantId());//String
        	jsonParticipant.put("playerId", playerId);//String
        	jsonParticipant.put("displayName", participant.getDisplayName());//String
        	jsonParticipant.put("iconImageUri", Extension.context.getUriString(participant.getIconImageUri()));//String
        	jsonParticipant.put("iconImageUrl", participant.getIconImageUrl());//String
        	jsonParticipant.put("hiResImageUri", Extension.context.getUriString(participant.getHiResImageUri()));//String
        	jsonParticipant.put("hiResImageUrl", participant.getHiResImageUrl());//String
        	jsonParticipant.put("status", participant.getStatus());//int

        } catch( JSONException e ) {} 

        return jsonParticipant;
    }
	//-------------------------------------------------------------------
    private JSONObject InvitationToJsonObject( Invitation invitation) {


    	JSONObject jsonInvitation = new JSONObject();
        try {
        	jsonInvitation.put("invitationId", invitation.getInvitationId());//String
        	jsonInvitation.put("invitationType", invitation.getInvitationType());//int
        	jsonInvitation.put("creationTimestamp", invitation.getCreationTimestamp());//long
        	jsonInvitation.put("inviter", ParticipantToJsonObject(invitation.getInviter()));//Participant

        } catch( JSONException e ) {} 

        return jsonInvitation;
    }
    //-------------------------------------------------------------------
    private String getCurrentPlayerParticipantId(TurnBasedMatch match) {
    	
    	Player currentPlayer= Games.Players.getCurrentPlayer(Extension.context.getApiClient());    	
    	
    	return match.getParticipantId(currentPlayer.getPlayerId());
    }
	//-------------------------------------------------------------------    
    private byte[] convertStringToBytes(String value) {
    	return value.getBytes(Charset.forName("UTF-8"));
    }
    //-------------------------------------------------------------------
    private String convertBytesToString(byte[] byteArray) {
        String st = null;
        if(byteArray==null) return "";
        try {
            st = new String(byteArray, "UTF-8");
            return st;
        } catch (UnsupportedEncodingException e1) {
            return "";
        } 
    }
	//-------------------------------------------------------------------
}
