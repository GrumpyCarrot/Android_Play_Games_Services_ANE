//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.multiplayer
{
	import flash.external.ExtensionContext;

	public class TurnBasedMultiplayer
	{
		private var _context:ExtensionContext;
		
		public static const SORT_ORDER_MOST_RECENT_FIRST:int = 0;
		public static const SORT_ORDER_SOCIAL_AGGREGATION:int = 1;

		//---------------------------------------------------
		public function TurnBasedMultiplayer(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//---------------------------------------------------
		public function lookAtMatches_UI():void
		{
			_context.call("TBM_LookAtMatches_UI");
		}	
		//---------------------------------------------------
		public function createNewGame_UI(minPlayersToSelect:int=1,maxPlayersToSelect:int=1,allowAutomatch:Boolean=true):void
		{
			_context.call("TBM_CreateNewGame_UI",  minPlayersToSelect,maxPlayersToSelect,allowAutomatch);	
		}
		//---------------------------------------------------
		public function createAutoMatch(minPlayersToSelect:int=1,maxPlayersToSelect:int=1):void
		{
			_context.call("TBM_CreateAutoMatch",minPlayersToSelect,maxPlayersToSelect);	
		}			
		//---------------------------------------------------
		public function getInvitations(invitationSortOrder:int=SORT_ORDER_SOCIAL_AGGREGATION):void
		{
			_context.call("getInvitations",invitationSortOrder);	
		}
		//---------------------------------------------------
		public function acceptInvitation(invitationId:String):void
		{
			_context.call("TBM_AcceptInvitation",invitationId);	
		}	
		//---------------------------------------------------
		public function declineInvitation(invitationId:String):void
		{
			_context.call("TBM_DeclineInvitation",invitationId);	
		}	
		//---------------------------------------------------
		public function dismissInvitation(invitationId:String):void
		{
			_context.call("TBM_DismissInvitation",invitationId);	
		}	
		//---------------------------------------------------
		public function loadMatches(matchTurnStatuses:Array):void
		{
			_context.call("TBM_LoadMatches",matchTurnStatuses);	
		}	
		//---------------------------------------------------		
		public function loadMatch(matchId:String):void
		{
			_context.call("TBM_LoadMatch", matchId);
		}			
		//---------------------------------------------------		
		public function takeTurn(matchId:String,nextParticipantId:String,dataToSend:String=""):void
		{
			_context.call("TBM_TakeTurn", matchId, nextParticipantId,dataToSend);	
		}
		//---------------------------------------------------
		public function finishMatch(matchId:String,matchData:String=null):void
		{
			if(matchData==null) _context.call("TBM_FinishMatch", matchId);
			else _context.call("TBM_FinishMatchWithData", matchId,matchData);
		}	
		//---------------------------------------------------
		public function rematch(matchId:String):void
		{
			_context.call("TBM_Rematch", matchId);
		}	
		//---------------------------------------------------		
		public function dismissMatch(matchId:String):void
		{
			_context.call("TBM_DismissMatch", matchId);
		}			
		//---------------------------------------------------
		public function cancelMatch(matchId:String):void
		{
			_context.call("TBM_CancelMatch", matchId);
		}			
		//---------------------------------------------------
		public function leaveMatch(matchId:String):void
		{
			_context.call("TBM_LeaveMatch", matchId);
		}	
		//---------------------------------------------------
		public function leaveMatchDuringTurn(matchId:String,pendingParticipantId:String):void
		{
			_context.call("TBM_LeaveMatchDuringTurn", matchId);
		}	
		//---------------------------------------------------	
	}
}