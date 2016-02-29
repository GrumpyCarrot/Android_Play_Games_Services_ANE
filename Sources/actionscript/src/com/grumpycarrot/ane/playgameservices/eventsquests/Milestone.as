//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.eventsquests
{
	public class Milestone
	{
		public static const STATE_CLAIMED:int = 4;
		public static const STATE_COMPLETED_NOT_CLAIMED:int = 3;
		public static const STATE_NOT_COMPLETED:int = 2;
		public static const STATE_NOT_STARTED:int = 1;

		private var _milestoneId:String;
		private var _eventId:String;
		private var _state:int;
		private var _currentProgress:Number;
		private var _targetProgress:Number;
		private var _rewardData:String;

		//---------------------------------------------------------------------------------------------------
		public function Milestone(milestoneId:String,eventId:String=null,state:int=-1, currentProgress:Number=-1,targetProgress:Number=-1,rewardData:String=null)
		{
			_milestoneId = milestoneId;
			_eventId = eventId;
			_state = state;
			_currentProgress = currentProgress;
			_targetProgress = targetProgress;
			_rewardData = rewardData;

		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Milestone {

			if( jsonObject.milestoneId == null ) return null;
			
			return new Milestone( jsonObject.milestoneId,jsonObject.eventId,jsonObject.state,jsonObject.currentProgress,jsonObject.targetProgress,jsonObject.rewardData);
		}
		//---------------------------------------------------
		public function get milestoneId():String { return _milestoneId; }
		public function get eventId():String { return _eventId; }
		public function get state():int { return _state; }	
		public function get currentProgress():Number { return _currentProgress; }	
		public function get targetProgress():Number { return _targetProgress; }
		public function get rewardData():String { return _rewardData; }
		//---------------------------------------------------
	}
}