//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.multiplayer
{
	import flash.events.Event;

	public class TurnBasedEvent extends Event
	{
		public static const MULTIPLAYER_TBM_EVENT:String = "MULTIPLAYER_TBM_EVENT";

		public static const ON_CREATE_MATCH_UI_CANCELED:String = "ON_CREATE_MATCH_UI_CANCELED";
		public static const ON_LOOK_AT_MATCH_UI_CANCELED:String = "ON_LOOK_AT_MATCH_UI_CANCELED";

		public static const ON_INITIATE_MATCH_SUCCESS:String = "ON_INITIATE_MATCH_SUCCESS";
		public static const ON_INITIATE_MATCH_FAILED:String = "ON_INITIATE_MATCH_FAILED";	

		public static const ON_LOAD_INVITATIONS_SUCCESS:String = "ON_LOAD_INVITATIONS_SUCCESS";
		public static const ON_LOAD_INVITATIONS_FAILED:String = "ON_LOAD_INVITATIONS_FAILED";
		public static const ON_LOAD_MATCHES_SUCCESS:String = "ON_LOAD_MATCHES_SUCCESS";
		public static const ON_LOAD_MATCHES_FAILED:String = "ON_LOAD_MATCHES_FAILED";
		public static const ON_LOAD_MATCH_SUCCESS:String = "ON_LOAD_MATCH_SUCCESS";
		public static const ON_LOAD_MATCH_FAILED:String = "ON_LOAD_MATCH_FAILED";	
		
		public static const ON_UPDATE_MATCH_SUCCESS:String = "ON_UPDATE_MATCH_SUCCESS";
		public static const ON_UPDATE_MATCH_FAILED:String = "ON_UPDATE_MATCH_FAILED";
		
		public static const ON_CANCEL_MATCH_SUCCESS:String = "ON_CANCEL_MATCH_SUCCESS";
		public static const ON_CANCEL_MATCH_FAILED:String = "ON_CANCEL_MATCH_FAILED";		
		public static const ON_LEAVE_MATCH_SUCCESS:String = "ON_LEAVE_MATCH_SUCCESS";
		public static const ON_LEAVE_MATCH_FAILED:String = "ON_LEAVE_MATCH_FAILED";		

		public static const ON_NOTIFICATION_TBM_RECEIVED:String = "ON_NOTIFICATION_TBM_RECEIVED";
		public static const ON_NOTIFICATION_TBM_REMOVED:String = "ON_NOTIFICATION_TBM_REMOVED";		
		public static const ON_NOTIFICATION_INVITATION_RECEIVED:String = "ON_NOTIFICATION_INVITATION_RECEIVED";
		public static const ON_NOTIFICATION_INVITATION_REMOVED:String = "ON_NOTIFICATION_INVITATION_REMOVED";		

		private var _responseCode:String;
		private var _matchId:String;
		private var _turnBasedMatch:TurnBasedMatch;
		private var _turnBasedMatches:Vector.<TurnBasedMatch>;
		private var _invitations:Vector.<Invitation>;
		private var _invitationId:String;
		private var _invitation:Invitation;
		
		//---------------------------------------------------
		public function TurnBasedEvent( type:String, responseCode:String, matchId:String=null,turnBasedMatch:TurnBasedMatch=null,turnBasedMatches:Vector.<TurnBasedMatch>=null,
										invitations:Vector.<Invitation>=null,invitationId:String=null,invitation:Invitation=null)
		{
			super( type );
			
			_responseCode = responseCode;
			_matchId=matchId;
			_turnBasedMatch=turnBasedMatch;
			_turnBasedMatches=turnBasedMatches;
			_invitations=invitations;
			_invitationId=invitationId;
			_invitation=invitation;

		}
		//---------------------------------------------------
		public function get responseCode():String { return _responseCode; }
		public function get matchId():String { return _matchId; }
		public function get turnBasedMatch():TurnBasedMatch { return _turnBasedMatch; }	
		public function get turnBasedMatches():Vector.<TurnBasedMatch> { return _turnBasedMatches; }	
		public function get invitations():Vector.<Invitation> { return _invitations; }
		public function get invitationId():String { return _invitationId; }
		public function get invitation():Invitation { return _invitation; }
		//---------------------------------------------------
		
	}
}