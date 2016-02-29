//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests
{
	public class GameEvent
	{
		private var _eventId:String;
		private var _name:String;
		private var _description:String;
		private var _iconImageUrl:String;
		private var _iconImageUri:String;
		private var _playerId:String;
		private var _value:Number;
		private var _formattedValue:String;
		private var _isVisible:Boolean;		

		//---------------------------------------------------------------------------------------------------
		public function GameEvent( eventId:String, name:String=null, description:String=null, iconImageUrl:String=null, iconImageUri:String=null,
								   playerId:String=null,value:Number=-1,formattedValue:String=null,isVisible:Boolean=false)
		{
			_eventId = eventId;
			_name = name;
			_description = description;
			_iconImageUrl = iconImageUrl;
			_iconImageUri = iconImageUri;
			_playerId = playerId;
			_value = value;
			_formattedValue = formattedValue;
			_isVisible = isVisible;			
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):GameEvent {
			
			if( jsonObject.eventId == null ) return null;
			
			return new GameEvent( jsonObject.eventId, jsonObject.name, jsonObject.description, jsonObject.iconImageUrl,jsonObject.iconImageUri,
				jsonObject.playerId, jsonObject.value, jsonObject.formattedValue,  jsonObject.isVisible);
		}
		//---------------------------------------------------
		public function get eventId():String { return _eventId; }
		public function get name():String { return _name; }
		public function get description():String { return _description; }	
		public function get iconImageUrl():String { return _iconImageUrl; }	
		public function get iconImageUri():String { return _iconImageUri; }	
		public function get playerId():String { return _playerId; }
		public function get value():Number { return _value; }
		public function get formattedValue():String { return _formattedValue; }	
		public function get isVisible():Boolean { return _isVisible; }		

		//---------------------------------------------------
	}
}