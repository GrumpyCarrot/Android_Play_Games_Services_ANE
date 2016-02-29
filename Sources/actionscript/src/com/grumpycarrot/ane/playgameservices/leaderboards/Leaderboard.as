//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.leaderboards
{

	public class Leaderboard
	{
		public static const COLLECTION_PUBLIC:int = 0;
		public static const  COLLECTION_SOCIAL:int = 1;
		
		public static const TIME_SPAN_ALL_TIME:int = 2;
		public static const TIME_SPAN_DAILY:int = 1;
		public static const TIME_SPAN_WEEKLY:int = 0;
		
		private var _leaderboardId:String;
		private var _displayName:String;
		private var _iconImageUri:String;
		private var _iconImageUrl:String;
		private var _leaderboardScores:Vector.<LeaderboardScore>;


		public function Leaderboard(leaderboardId:String,displayName:String=null,iconImageUri:String=null,iconImageUrl:String=null,leaderboardScores:Vector.<LeaderboardScore>=null)
		{
			
			_leaderboardId = leaderboardId;
			_displayName = displayName;
			_iconImageUri = iconImageUri;
			_iconImageUrl = iconImageUrl;
			_leaderboardScores=leaderboardScores;

		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Leaderboard {

			var scores:Vector.<LeaderboardScore> = new <LeaderboardScore>[];
			
			for each ( var scoreObject:Object in jsonObject.leaderboardScores ) {
				scores.push( LeaderboardScore.fromJSONObject( scoreObject ) );
			}

			return new Leaderboard( jsonObject.leaderboardId, jsonObject.displayName,jsonObject.iconImageUri, jsonObject.iconImageUrl, scores );
		}
		//---------------------------------------------------
		public function get leaderboardId():String { return _leaderboardId; }
		public function get displayName():String { return _displayName; }
		public function get iconImageUri():String { return _iconImageUri; }	
		public function get iconImageUrl():String { return _iconImageUrl; }		
		public function get leaderboardScores():Vector.<LeaderboardScore> { return _leaderboardScores; }
		//---------------------------------------------------
		
	}
}