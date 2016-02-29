//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.multiplayer
{

	public class TurnBasedMatch
	{
		public static const MATCH_STATUS_ACTIVE:int = 1;
		public static const MATCH_STATUS_AUTO_MATCHING:int = 0;
		public static const MATCH_STATUS_CANCELED:int = 4;
		public static const MATCH_STATUS_COMPLETE:int = 2;
		public static const MATCH_STATUS_EXPIRED:int = 3;
		public static const MATCH_STATUS_UNKNOWN:int = -1;
		
		public static const MATCH_TURN_STATUS_COMPLETE:int = 3;
		public static const MATCH_TURN_STATUS_MY_TURN:int = 1;
		public static const MATCH_TURN_STATUS_THEIR_TURN:int = 2;
		public static const MATCH_TURN_STATUS_UNKNOWN:int = -1;
		//public static const MATCH_TURN_STATUS_INVITED:int = 0;

		private var _matchId:String;
		private var _creationTimestamp:Number;
		private var _creatorId:String;
		private var _lastUpdatedTimestamp:Number;
		private var _canRematch:Boolean;
		private var _status:int;
		private var _turnStatus:int;
		private var _data:String;
		private var _pendingParticipantId:String;
		private var _totalParticipants:int;
		private var _participants:Vector.<Participant>;
		private var _availableAutoMatchSlots:int;
		private var _myParticipantId:String;
		

		//---------------------------------------------------
		public function TurnBasedMatch(matchId:String,creationTimestamp:Number=-1,creatorId:String=null,lastUpdatedTimestamp:Number=-1,canRematch:Boolean=false,status:int=-1,turnStatus:int=-1,
									   data:String=null,pendingParticipantId:String=null,totalParticipants:int=-1,participants:Vector.<Participant>=null,availableAutoMatchSlots:int=-1,myParticipantId:String=null)
		{
			_matchId=matchId;
			_creationTimestamp=creationTimestamp;
			_creatorId=creatorId;
			_lastUpdatedTimestamp=lastUpdatedTimestamp;
			_canRematch=canRematch;
			_status=status;
			_turnStatus=turnStatus;
			_data=data;
			_pendingParticipantId=pendingParticipantId;
			_totalParticipants=totalParticipants;
			_participants=participants;	
			_availableAutoMatchSlots=availableAutoMatchSlots;	
			_myParticipantId=myParticipantId;	
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):TurnBasedMatch {

			var participants:Vector.<Participant> = new <Participant>[];

			for each ( var participantsObject:Object in jsonObject.participants ) {
				participants.push( Participant.fromJSONObject( participantsObject ) );
			}

			return new TurnBasedMatch( jsonObject.matchId,jsonObject.creationTimestamp, jsonObject.creatorId,
				jsonObject.lastUpdatedTimestamp, jsonObject.canRematch, jsonObject.status,
				jsonObject.turnStatus, jsonObject.data, jsonObject.pendingParticipantId,jsonObject.totalParticipants,
				participants,jsonObject.availableAutoMatchSlots, jsonObject.myParticipantId );			
			
		}
		//---------------------------------------------------
		public function getNextParticipantId():String {
			
			if(_myParticipantId==null) return null;

			var desiredIndex:int = -1;
			
			for (var i:int = 0; i < _totalParticipants; i++) {

				if (_participants[i].participantId==_myParticipantId) {
					desiredIndex = i + 1;
				}
			}		
			
			if (desiredIndex < _totalParticipants) {
				return _participants[desiredIndex].participantId;
			}			
		
			
			if (_availableAutoMatchSlots <= 0) {
				// You've run out of automatch slots, so we start over.
				return _participants[0].participantId;
			} else {
				// You have not yet fully automatched, so null will find a new person to play against.
				return null;
			}			
			
			return null;
		}
		//---------------------------------------------------
		public function get matchId():String { return _matchId; }
		public function get creationTimestamp():Number { return _creationTimestamp; }
		public function get creatorId():String { return _creatorId; }
		public function get lastUpdatedTimestamp():Number { return _lastUpdatedTimestamp; }
		public function get canRematch():Boolean { return _canRematch; }
		public function get status():int { return _status; }
		public function get turnStatus():int { return _turnStatus; }
		public function get data():String { return _data; }
		public function get pendingParticipantId():String { return _pendingParticipantId; }
		public function get totalParticipants():int { return _totalParticipants; }
		public function get participants():Vector.<Participant> { return _participants; }	
		public function get availableAutoMatchSlots():int { return _availableAutoMatchSlots; }
		public function get myParticipantId():String { return _myParticipantId; }
		//---------------------------------------------------
	}
}