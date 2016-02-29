//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests
{
	import flash.external.ExtensionContext;

	
	
	public class EventsQuests
	{

		public static const SELECT_ACCEPTED:int = 3;
		public static const SELECT_COMPLETED:int = 4;
		public static const SELECT_COMPLETED_UNCLAIMED:int = 101;
		public static const SELECT_ENDING_SOON:int = 102;
		public static const SELECT_EXPIRED:int = 5;
		public static const SELECT_FAILED:int = 6;
		public static const SELECT_OPEN:int = 2;
		public static const SELECT_RECENTLY_FAILED:int = 103;
		public static const SELECT_UPCOMING:int = 1;
		
		private var _context:ExtensionContext;

		//---------------------------------------------------
		public function EventsQuests(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//---------------------------------------------------
		public function submitEvent(eventId:String,incrementAmount:int=1):void
		{
			_context.call("submitEvent", eventId,incrementAmount);	
		}	
		//---------------------------------------------------
		public function retrieveEvent(forceReload:Boolean=true):void
		{
			_context.call("retrieveEvent",forceReload);	
		}	
		//---------------------------------------------------
		public function retrieveEventById(eventId:String,forceReload:Boolean=true):void
		{
			_context.call("RetrieveEventById",eventId,forceReload);	
		}	
		//---------------------------------------------------
		public function showQuestsUI(questSelector:Array):void
		{			
			_context.call("showQuestsUI",questSelector);	
		}	
		//---------------------------------------------------
		public function loadQuests(questSelector:Array):void
		{
			_context.call("loadQuests",questSelector);	
		}	
		//---------------------------------------------------
		public function acceptQuest(questId:String):void
		{
			_context.call("acceptQuest",questId);	
		}	
		//---------------------------------------------------
		public function claimReward(questId:String,milestoneId:String):void
		{
			_context.call("claimReward",questId,milestoneId);	
		}	
		//---------------------------------------------------
		public function registerQuestUpdateListener():void
		{
			_context.call("registerQuestUpdateListener");	
		}	
		//---------------------------------------------------
		public function unregisterQuestUpdateListener():void
		{
			_context.call("unregisterQuestUpdateListener");	
		}	
		//---------------------------------------------------		

		//---------------------------------------------------
	}
}