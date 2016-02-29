//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.multiplayer
{
	public class Invitation
	{
		
		public static const INVITATION_TYPE_REAL_TIME:int = 0;
		public static const INVITATION_TYPE_TURN_BASED:int = 1;

		private var _invitationId:String;
		private var _invitationType:int;
		private var _creationTimestamp:Number;
		private var _inviter:Participant;

		//---------------------------------------------------------------------------------------------------
		public function Invitation(invitationId:String=null,invitationType:int=-1,creationTimestamp:Number=-1,inviter:Participant=null)
		{
			
			_invitationId=invitationId;
			_invitationType=invitationType;
			_creationTimestamp=creationTimestamp;
			_inviter=inviter;

		}
		//---------------------------------------------------------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Invitation {
			
			if( jsonObject.invitationId == null ) return null;

			return new Invitation( jsonObject.invitationId, jsonObject.invitationType, jsonObject.creationTimestamp, 
				Participant.fromJSONObject(Object(jsonObject.inviter))
			);
		}
		//---------------------------------------------------------------------------------------------------
		public function get invitationId():String { return _invitationId; }
		public function get invitationType():int { return _invitationType; }
		public function get creationTimestamp():Number { return _creationTimestamp; }
		public function get inviter():Participant { return _inviter; }
		//---------------------------------------------------------------------------------------------------
		
	}
}