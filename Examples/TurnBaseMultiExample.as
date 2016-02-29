package Example {

	import com.grumpycarrot.ane.playgameservices.PlayGamesServices;
	import com.grumpycarrot.ane.playgameservices.PlayGamesServicesEvent;
	import com.grumpycarrot.ane.playgameservices.multiplayer.TurnBasedEvent;
	import com.grumpycarrot.ane.playgameservices.multiplayer.TurnBasedMatch
	import com.grumpycarrot.ane.playgameservices.multiplayer.Participant
	import com.grumpycarrot.ane.playgameservices.multiplayer.Invitation;

	//---------------------------------------------------------		
	// https://developers.google.com/games/services/android/turnbasedMultiplayer
	//---------------------------------------------------------	
    public class TurnBaseMultiExample {
     
		public var _playGamesServices:PlayGamesServices;

		private var _matchId:String;
		private var _turnBasedMatch:TurnBasedMatch;
		private var _turnBasedMatches:Vector.<TurnBasedMatch>;
		private var _invitations:Vector.<Invitation>;
		private var _invitationId:String;
		private var _invitation:Invitation;

		//---------------------------------------------------------	
		public function TurnBaseMultiExample() {
			
			init();
		}
		//---------------------------------------------------------	
		private function init():void {
			

			_playGamesServices = PlayGamesServices.getInstance();
			_playGamesServices.addEventListener(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT, onGooglePlayGames);
			_playGamesServices.addEventListener(TurnBasedEvent.MULTIPLAYER_TBM_EVENT, onTurnBaseMultiplayer);

			_playGamesServices.initAPI(false,true);

		}
		//---------------------------------------------------------	
		private function onGooglePlayGames(event:PlayGamesServicesEvent):void
		{
			if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_IN_SUCCESS) {
				trace("Sign In Success");
			} else if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_OUT_SUCCESS) {
				trace("Sign Out Success");
			} else if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_IN_FAIL) {
				trace("Sign In Failed");
			}
			
		}
		//---------------------------------------------------------	
		private function onTurnBaseMultiplayer(event:TurnBasedEvent):void {

			if (event.responseCode == TurnBasedEvent.ON_LOAD_INVITATIONS_SUCCESS) {

				_invitations = event.invitations;
				trace("Number of invitations loaded : ",_invitations.length);
				
				if (_invitations.length > 0)  {
					
					_invitation = _invitations[0];
					trace("Invitation ID ", _invitation.invitationId);
					trace("Invitation Inviter", _invitation.inviter.displayName);
				}
			}
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_LOAD_MATCHES_SUCCESS) {

				_turnBasedMatches = event.turnBasedMatches;
				trace("Number of Matches loaded : ",_turnBasedMatches.length);

				if(_turnBasedMatches.length >0)  onMatchLoaded(_turnBasedMatches[0]);
			}				
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_INITIATE_MATCH_SUCCESS) {
				
				trace("Match Ini Success !");
				onMatchLoaded(event.turnBasedMatch);
				
			}	
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_LOAD_MATCH_SUCCESS) {
				trace("Match load Success !");

				onMatchLoaded(event.turnBasedMatch);
			}				
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_UPDATE_MATCH_SUCCESS) {
				trace("Match Update Success !");
				
				onMatchLoaded(event.turnBasedMatch);
			}	
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_CANCEL_MATCH_SUCCESS) {
				trace("Match Cancel Success !");
				
				_matchId = event.matchId;
				trace("_matchId ", _matchId);				
			}				
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_LEAVE_MATCH_SUCCESS) {
				trace("Match Leave Success !");
				
				onMatchLoaded(event.turnBasedMatch);
			}				
			//----------------- Ui User Cancel
			else if (event.responseCode == TurnBasedEvent.ON_CREATE_MATCH_UI_CANCELED) {
				trace("Create Match was canceled by user");
			}
			//-----------------
			else if (event.responseCode == TurnBasedEvent.ON_LOOK_AT_MATCH_UI_CANCELED) {
				trace("Look at Matches was canceled by user");
			}		
			//----------------- Notifications
			else if (event.responseCode == TurnBasedEvent.ON_NOTIFICATION_TBM_RECEIVED) {
				
				trace("Notification : Match received");
				onMatchLoaded(event.turnBasedMatch);
			}
			//----------------- 
			else if (event.responseCode == TurnBasedEvent.ON_NOTIFICATION_TBM_REMOVED) {
				trace("Notification : Match removed");
				_matchId = event.matchId;
				trace("_matchId : ", _matchId);
			}			
			//----------------- 
			else if (event.responseCode == TurnBasedEvent.ON_NOTIFICATION_INVITATION_RECEIVED) {
				trace("Notification : Invitation received");
				
				_invitation = event.invitation;
				trace("Invitation ID ", _invitation.invitationId);
				trace("Invitation Inviter", _invitation.inviter.displayName);
			}
			//----------------- 
			else if (event.responseCode == TurnBasedEvent.ON_NOTIFICATION_INVITATION_REMOVED) {
				trace("Notification : Invitation Removed");
				
				_invitationId = event.invitationId;
				trace("_invitationId ", _invitationId);
			}
			//----------------- 
			else {
				trace("TurnBaseMatch ERROR ");
			}
			
		}
		//---------------------------------------------------------	
		private function onMatchLoaded(turnBasedMatch:TurnBasedMatch):void {
			
			_turnBasedMatch = turnBasedMatch;

			trace("Turn Match JSON String Data : ", _turnBasedMatch.data);

			if(turnBasedMatch.status == TurnBasedMatch.MATCH_STATUS_ACTIVE) {

				trace("OK, it's active. Check on turn status.");
				
				switch (turnBasedMatch.turnStatus) {
					case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
						trace("Your turn to play");
						break;
					case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
						trace("It's not your turn.");
						break;
				}
				
			} else {
				
				switch (turnBasedMatch.status) {
					case TurnBasedMatch.MATCH_STATUS_CANCELED:
						trace("This game was canceled");
						return;
					case TurnBasedMatch.MATCH_STATUS_EXPIRED:
						trace("This game is expired.");
						return;
					case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
						trace("We're still waiting for an automatch partner.");
						return;
					case TurnBasedMatch.MATCH_STATUS_COMPLETE:
						
						if (turnBasedMatch.turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
							trace("This game is over; someone finished it, and so did you!  There is nothing to be done.");
						} else {
							trace("This game is over; someone finished it!  You can only finish it now.");
						}  
				}				
				
			}
	
		}
		//---------------------------------------------------------
		// Sign In / Sign Out Function
		//---------------------------------------------------------
		public function signIn():void { 

			_playGamesServices.signIn();
		}
		//---------------------------------------------------------
		public function signOut():void { 
		
			_playGamesServices.signOut();
		}
		//---------------------------------------------------------
		// Turn Base Multi Functions
		//---------------------------------------------------------
		public function getCompletedMatchesList():void { 
			
			_playGamesServices.turnBasedMultiplayer.loadMatches([TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE]);
		}		
		//---------------------------------------------------------
		public function getMyTurnMatchesList():void { 
			
			_playGamesServices.turnBasedMultiplayer.loadMatches([TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN]);
		}		
		//---------------------------------------------------------
		public function getTheirTurnMatchesList():void { 

			_playGamesServices.turnBasedMultiplayer.loadMatches([TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN]);
		}		
		//---------------------------------------------------------
		public function checkForInvitations():void { 

			_playGamesServices.turnBasedMultiplayer.getInvitations();
		}
		//---------------------------------------------------------	
		public function openSeeAllMatches_UI():void { 

			_playGamesServices.turnBasedMultiplayer.lookAtMatches_UI();
		}

		//---------------------------------------------------------
		public function openFindPlayers_UI():void { 

			_playGamesServices.turnBasedMultiplayer.createNewGame_UI();
		}	
		//---------------------------------------------------------
		 public function StartAutoMatch():void { 

			_playGamesServices.turnBasedMultiplayer.createAutoMatch(1,1);
		}
		//---------------------------------------------------------
		// Accept an invitation for a turn-based match. This changes the current player's participant status to STATUS_JOINED.
		//---------------------------------------------------------
		public function acceptInvitation():void { 

			_playGamesServices.turnBasedMultiplayer.acceptInvitation(_invitation.invitationId);
		}
		//-------------------------------------------------------------------
		// Dismiss an invitation to a turn-based match. Dismissing an invitation will not change the state of the match for the other participants.
		//-------------------------------------------------------------------
		public function dismissInvitation():void { 

			_playGamesServices.turnBasedMultiplayer.dismissInvitation(_invitation.invitationId);

		}		
		//-------------------------------------------------------------------
		// Decline an invitation for a turn-based match. 
		// Note that this will cancel the match for the other participants and remove the match from the caller's local device. 
		//-------------------------------------------------------------------
		public function declineInvitation():void { 

			_playGamesServices.turnBasedMultiplayer.declineInvitation(_invitation.invitationId);
		}
		//-------------------------------------------------------------------
		// Delete a match from the server and local storage. 
		// Dismissing a match will not change the state of the match for the other participants, 
		// but dismissed matches will never be shown to the dismissing player again. 
		//-------------------------------------------------------------------
		public function dismissMatch():void { 

			_playGamesServices.turnBasedMultiplayer.dismissMatch(_turnBasedMatch.matchId);

		}
		//-------------------------------------------------------------------     
		// Cancels a turn-based match. Once this call succeeds, the match will be removed from local storage. 
		// Note that this will cancel the match completely, forcing it to end for all players involved.
		//-------------------------------------------------------------------
		public function cancelMatch():void { 

			_playGamesServices.turnBasedMultiplayer.cancelMatch(_turnBasedMatch.matchId);
		}
		//-------------------------------------------------------------------    
		// Leave the specified match when it is not the current player's turn. 
		// If this takes the match to fewer than two participants, the match will be canceled.
		//-------------------------------------------------------------------	
		public function leaveMatch():void { 

			_playGamesServices.turnBasedMultiplayer.leaveMatch(_turnBasedMatch.matchId);
		}

		//---------------------------------------------------------
		public function playTurn(jsonData:String):void { 

			var nextParticipantId:String = _turnBasedMatch.getNextParticipantId();
			trace("NextParticipantId : ", nextParticipantId);
			
			_playGamesServices.turnBasedMultiplayer.takeTurn(_turnBasedMatch.matchId, nextParticipantId, jsonData);

		}		
		//-------------------------------------------------------------------
		// Leave the specified match during the current player's turn. 
		// If this takes the match to fewer than two participants, the match will be canceled. 
		// The provided pendingParticipantId will be used to determine which participant should act next. 
		// If no pending participant is provided and the match has available auto-match slots, the match will wait for additional players to be found. 
		// If there are no auto-match slots available for this match, a pending participant ID is required. 
		//-------------------------------------------------------------------
		public function leaveMatchDuringTurn():void { 

			_playGamesServices.turnBasedMultiplayer.leaveMatchDuringTurn(_turnBasedMatch.matchId,_turnBasedMatch.getNextParticipantId());
		}		
		
		//---------------------------------------------------------
		 public function FinishMatch(jsonData:String=null):void { 
			_playGamesServices.turnBasedMultiplayer.finishMatch(_turnBasedMatch.matchId, jsonData);

		}
		//-------------------------------------------------------------------
		// Create a rematch of a previously completed turn-based match. 
		// The new match will have the same participants as the previous match.
		// Note that only one rematch may be created from any single completed match, and only by a player that has already called Finish on the match.
		//-------------------------------------------------------------------		
		 public function RematchButton():void { 

			_playGamesServices.turnBasedMultiplayer.rematch(_turnBasedMatch.matchId);
		}		
		//---------------------------------------------------------
		
		}
}