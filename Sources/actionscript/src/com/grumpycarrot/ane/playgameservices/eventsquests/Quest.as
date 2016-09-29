//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests
{

	public class Quest
	{
		public static const STATE_ACCEPTED:int = 3;
		public static const STATE_COMPLETED:int = 4;
		public static const STATE_EXPIRED:int = 5;
		public static const STATE_FAILED:int = 6;
		public static const STATE_OPEN:int = 2;
		public static const STATE_UPCOMING:int = 1;

		private var _questId:String;
		private var _name:String;
		private var _description:String;
		private var _iconImageUrl:String;
		private var _bannerImageUrl:String;
		private var _iconImageUri:String;
		private var _bannerImageUri:String;	
		private var _startTimestamp:Number;
		private var _endTimestamp:Number;
		private var _isEndingSoon:Boolean;	
		private var _acceptedTimestamp:Number;
		private var _lastUpdatedTimestamp:Number;	
		private var _state:int;	
		private var _milestone:Milestone;

		//---------------------------------------------------------------------------------------------------
		public function Quest( questId:String,name:String=null,description:String=null,
							   iconImageUrl:String=null,bannerImageUrl:String=null,iconImageUri:String=null,bannerImageUri:String=null,
							   startTimestamp:Number=-1,endTimestamp:Number=-1,isEndingSoon:Boolean=false,acceptedTimestamp:Number=-1,lastUpdatedTimestamp:Number=-1,state:int=-1,milestone:Milestone=null)
		{
			_questId = questId;
			_name = name;
			_description = description;
			_iconImageUrl = iconImageUrl;
			_bannerImageUrl = bannerImageUrl;
			_iconImageUri = iconImageUri;
			_bannerImageUri = bannerImageUri;			
			_startTimestamp = startTimestamp;
			_endTimestamp = endTimestamp;
			_isEndingSoon = isEndingSoon;	
			_acceptedTimestamp = acceptedTimestamp;
			_lastUpdatedTimestamp = lastUpdatedTimestamp;
			_state = state;
			_milestone=milestone;
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Quest {
			
			if( jsonObject.questId == null ) return null;

			var milestone:Milestone = Milestone.fromJSONObject( jsonObject.milestone );

			return new Quest( jsonObject.questId,jsonObject.name,jsonObject.description,
				jsonObject.iconImageUrl,jsonObject.bannerImageUrl,jsonObject.iconImageUri,jsonObject.bannerImageUri,
				jsonObject.startTimestamp,jsonObject.endTimestamp,jsonObject.isEndingSoon,jsonObject.acceptedTimestamp,jsonObject.lastUpdatedTimestamp,jsonObject.state,milestone);
		}
		//---------------------------------------------------
		public function get questId():String { return _questId; }
		public function get name():String { return _name; }
		public function get description():String { return _description; }	
		public function get iconImageUrl():String { return _iconImageUrl; }	
		public function get bannerImageUrl():String { return _bannerImageUrl; }
		public function get iconImageUri():String { return _iconImageUri; }	
		public function get bannerImageUri():String { return _bannerImageUri; }		
		public function get startTimestamp():Number { return _startTimestamp; }
		public function get endTimestamp():Number { return _endTimestamp; }	
		public function get isEndingSoon():Boolean { return _isEndingSoon; }	
		public function get acceptedTimestamp():Number { return _acceptedTimestamp; }
		public function get lastUpdatedTimestamp():Number { return _lastUpdatedTimestamp; }	
		public function get state():int { return _state; }		
		public function get milestone():Milestone { return _milestone; }	

		//---------------------------------------------------
	}
}