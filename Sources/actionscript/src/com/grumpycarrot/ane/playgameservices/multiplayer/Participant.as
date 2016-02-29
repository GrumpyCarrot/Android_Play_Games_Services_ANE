//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.multiplayer
{

	public class Participant
	{
		public static const STATUS_DECLINED:int = 3;
		public static const STATUS_FINISHED:int = 5;
		public static const STATUS_INVITED:int = 1;
		public static const STATUS_JOINED:int = 2;
		public static const STATUS_LEFT:int = 4;
		public static const STATUS_NOT_INVITED_YET:int = 0;
		public static const STATUS_UNRESPONSIVE:int = 6;
		public static const STATUS_UNKNOWN:int = -1;		

		private var _participantId:String;
		private var _playerId:String;
		private var _displayName:String;
		private var _iconImageUri:String;
		private var _iconImageUrl:String;
		private var _hiResImageUri:String;
		private var _hiResImageUrl:String;
		private var _status:int;

		//---------------------------------------------------------------------------------------------------
		public function Participant(participantId:String,playerId:String=null,displayName:String=null,iconImageUri:String=null,iconImageUrl:String=null,hiResImageUri:String=null,hiResImageUrl:String=null,status:int=-1)
		{
			
			_participantId=participantId;
			_playerId=playerId;
			_displayName=displayName;
			_iconImageUri=iconImageUri;
			_iconImageUrl=iconImageUrl;
			_hiResImageUri=hiResImageUri;
			_hiResImageUrl=hiResImageUrl;	
			_status=status;	

		}
		//---------------------------------------------------------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Participant {
			
			if( jsonObject.participantId == null ) return null;

			return new Participant( jsonObject.participantId, jsonObject.playerId, jsonObject.displayName,
				jsonObject.iconImageUri,jsonObject.iconImageUrl,jsonObject.hiResImageUri,jsonObject.hiResImageUrl,
				jsonObject.status);
		}
		//---------------------------------------------------------------------------------------------------
		public function get participantId():String { return _participantId; }
		public function get playerId():String { return _playerId; }
		public function get displayName():String { return _displayName; }
		public function get iconImageUri():String { return _iconImageUri; }
		public function get iconImageUrl():String { return _iconImageUrl; }
		public function get hiResImageUri():String { return _hiResImageUri; }
		public function get hiResImageUrl():String { return _hiResImageUrl; }
		public function get status():int { return _status; }

		//---------------------------------------------------------------------------------------------------
		
	}
}