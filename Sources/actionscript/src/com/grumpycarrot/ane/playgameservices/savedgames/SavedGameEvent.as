//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.savedgames
{
	import flash.events.Event;

	public class SavedGameEvent extends Event
	{
		public static const SAVEDGAMES_EVENT:String = "SAVEDGAMES_EVENT";
		
		public static const ON_UI_CANCEL:String = "ON_UI_CANCEL";	
		public static const ON_UI_LOAD_GAME:String = "ON_UI_LOAD_GAME";	
		public static const ON_UI_CREATE_GAME:String = "ON_UI_CREATE_GAME";	
		public static const ON_LOAD_SAVEDGAMES_SUCCESS:String = "ON_LOAD_SAVEDGAMES_SUCCESS";
		public static const ON_LOAD_SAVEDGAMES_FAILED:String = "ON_LOAD_SAVEDGAMES_FAILED";
		public static const ON_OPEN_FAILED:String = "ON_OPEN_FAILED";
		public static const ON_OPEN_SUCCESS:String = "ON_OPEN_SUCCESS";
		public static const ON_WRITE_SUCCESS:String = "ON_WRITE_SUCCESS";
		public static const ON_WRITE_FAILED:String = "ON_WRITE_FAILED";	
		public static const ON_WRITE_ERROR_FILE_NOT_OPEN:String = "ON_WRITE_ERROR_FILE_NOT_OPEN";	
		public static const ON_DELETE_SUCCESS:String = "ON_DELETE_SUCCESS";	
		public static const ON_DELETE_FAILED:String = "ON_DELETE_FAILED";	
		public static const ON_DELETE_ERROR_FILE_NOT_OPEN:String = "ON_DELETE_ERROR_FILE_NOT_OPEN";	

		private var _responseCode:String;
		private var _savedGame:SavedGame;
		private var _savedGameList:Vector.<SavedGame>;
		private var _savedgameData:String;

		public function SavedGameEvent( type:String, responseCode:String,savedGameList:Vector.<SavedGame>=null,savedgameData:String=null,savedGame:SavedGame=null )
		{
			super( type );
			_responseCode = responseCode;
			_savedGame=savedGame;
			_savedGameList=savedGameList;
			_savedgameData=savedgameData;

		}
		//---------------------------------------------------------------------------------------------------
		public function get responseCode():String { return _responseCode; }
		public function get savedGame():SavedGame { return _savedGame; }
		public function get savedGameList():Vector.<SavedGame> { return _savedGameList; }
		public function get savedgameData():String { return _savedgameData; }
		//---------------------------------------------------------------------------------------------------		
	}
}