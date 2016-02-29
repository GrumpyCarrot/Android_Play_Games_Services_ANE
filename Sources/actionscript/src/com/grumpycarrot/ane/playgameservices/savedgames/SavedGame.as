//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.savedgames
{

	public class SavedGame
	{
		public static const PLAYED_TIME_UNKNOWN:int = -1;
		public static const PROGRESS_VALUE_UNKNOWN:int = -1;
		
		private var _uniqueName:String;
		private var _description:String;
		private var _lastModifiedTimestamp:Number;
		private var _playedTime:Number;
		private var _progressValue:Number;
		private var _data:String;

		public function SavedGame( uniqueName:String, description:String="",playedTime:Number=PLAYED_TIME_UNKNOWN, progressValue:Number=PROGRESS_VALUE_UNKNOWN, lastModifiedTimestamp:Number=-1  )
		{
			_uniqueName = uniqueName;
			_description = description;
			_lastModifiedTimestamp = lastModifiedTimestamp;
			_playedTime = playedTime;
			_progressValue = progressValue;			
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):SavedGame {
			
			if( jsonObject.uniqueName == null ) return null;
			
			return new SavedGame( jsonObject.uniqueName, jsonObject.description, jsonObject.playedTime, jsonObject.progressValue, jsonObject.lastModifiedTimestamp);
		}
		//---------------------------------------------------
		public function get uniqueName():String { return _uniqueName; }
		public function get lastModifiedTimestamp():Number { return _lastModifiedTimestamp; }

		public function get description():String { return _description; }
		public function setDescription(description:String=""):void { _description=description; }

		public function get playedTime():Number { return _playedTime; }
		public function setPlayedTime(playedTime:Number=PLAYED_TIME_UNKNOWN):void { _playedTime=playedTime; }

		public function get progressValue():Number { return _progressValue; }
		public function setProgressValue(progressValue:Number=PROGRESS_VALUE_UNKNOWN):void { _progressValue=progressValue; }
		
		public function get data():String { if(_data==null) return ""; else return _data; }
		public function setData(data:String):void { _data=data; }
		//---------------------------------------------------
	}
}