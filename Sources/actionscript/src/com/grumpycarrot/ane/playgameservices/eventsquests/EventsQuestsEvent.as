//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests
{

	import flash.events.Event;
	
	public class EventsQuestsEvent extends Event
	{
		public static const EVENTS_AND_QUESTS_EVENT:String = "EVENTS_AND_QUESTS_EVENT";
		
		public static const ON_RETRIEVE_EVENT_SUCCESS:String = "ON_RETRIEVE_EVENT_SUCCESS";
		public static const ON_RETRIEVE_EVENT_FAILED:String = "ON_RETRIEVE_EVENT_FAILED";
		public static const ON_QUESTS_UI_CANCEL:String = "ON_QUESTS_UI_CANCEL";
		public static const ON_QUESTS_UI_ACCEPTED:String = "ON_QUESTS_UI_ACCEPTED";
		public static const ON_QUESTS_UI_CLAIM_REWARD:String = "ON_QUESTS_UI_CLAIM_REWARD";
		public static const ON_LOAD_QUESTS_SUCCESS:String = "ON_LOAD_QUESTS_SUCCESS";
		public static const ON_LOAD_QUESTS_FAILED:String = "ON_LOAD_QUESTS_FAILED";		
		public static const ON_QUEST_ACCEPT_SUCCESS:String = "ON_QUEST_ACCEPT_SUCCESS";	
		public static const ON_QUEST_ACCEPT_FAILED:String = "ON_QUEST_ACCEPT_FAILED";	
		public static const ON_CLAIM_REWARD_SUCCESS:String = "ON_CLAIM_REWARD_SUCCESS";
		public static const ON_CLAIM_REWARD_FAILED:String = "ON_CLAIM_REWARD_FAILED";
		
		private var _responseCode:String;
		private var _gameEventsList:Vector.<GameEvent>;
		private var _questsList:Vector.<Quest>;
		private var _quest:Quest;
		private var _rewardData:String;

		
		public function EventsQuestsEvent(type:String, responseCode:String, gameEventsList:Vector.<GameEvent>=null,questsList:Vector.<Quest>=null,quest:Quest=null,rewardData:String=null)
		{
			super(type);
			_responseCode = responseCode;
			_gameEventsList=gameEventsList;
			_questsList=questsList;
			_quest=quest;
			_rewardData=rewardData;

		}
		//---------------------------------------------------------------------------------------------------
		public function get responseCode():String { return _responseCode; }
		public function get gameEventsList():Vector.<GameEvent> { return _gameEventsList; }
		public function get questsList():Vector.<Quest> { return _questsList; }
		public function get quest():Quest { return _quest; }
		public function get rewardData():String { return _rewardData; }
		//---------------------------------------------------------------------------------------------------
	}
}