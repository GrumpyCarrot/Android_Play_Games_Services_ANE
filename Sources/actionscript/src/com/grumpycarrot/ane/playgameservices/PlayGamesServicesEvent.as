//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices
{
	import com.grumpycarrot.ane.playgameservices.achievements.Achievement;
	import com.grumpycarrot.ane.playgameservices.leaderboards.Leaderboard;
	import com.grumpycarrot.ane.playgameservices.leaderboards.LeaderboardScore;
	
	import flash.events.Event;
	
	public class PlayGamesServicesEvent extends Event
	{
		public static const GOOGLE_PLAY_GAMES_EVENT:String = "GOOGLE_PLAY_GAMES_EVENT";
		
		public static const ON_SIGN_IN_SUCCESS:String = "ON_SIGN_IN_SUCCESS";
		public static const ON_SIGN_IN_FAIL:String = "ON_SIGN_IN_FAIL";
		public static const ON_SIGN_OUT_SUCCESS:String = "ON_SIGN_OUT_SUCCESS";

		public static const ON_LEADERBOARD_LOADED:String = "ON_LEADERBOARD_LOADED";
		public static const ON_LEADERBOARD_LOADING_FAILED:String = "ON_LEADERBOARD_LOADING_FAILED";
		public static const ON_PLAYER_SCORE_LOADED:String = "ON_PLAYER_SCORE_LOADED";
		public static const ON_PLAYER_SCORE_LOAD_FAILED:String = "ON_PLAYER_SCORE_LOAD_FAILED";		
		public static const ON_PLAYER_SCORE_UNKNOWN:String = "ON_PLAYER_SCORE_UNKNOWN";			
		
		public static const ON_ACHIEVEMENTS_LOADED:String = "ON_ACHIEVEMENTS_LOADED";
		public static const ON_ACHIEVEMENTS_LOADING_FAILED:String = "ON_ACHIEVEMENTS_LOADING_FAILED";

		public static const ON_URI_IMAGE_LOADED:String = "ON_URI_IMAGE_LOADED";
		public static const ON_PLAYERSTATS_LOADED:String = "ON_PLAYERSTATS_LOADED";

		private var _responseCode:String;
		private var _leaderboard:Leaderboard;
		private var _achievements:Vector.<Achievement>;
		private var _playerStats:PlayerStats;
		private var _leaderboardScore:LeaderboardScore;
		
		public function PlayGamesServicesEvent(type:String, responseCode:String,leaderboard:Leaderboard=null,achievements:Vector.<Achievement>=null,playerStats:PlayerStats=null,leaderboardScore:LeaderboardScore=null)
		{
			super(type);
			_responseCode = responseCode;
			_leaderboard = leaderboard;
			_achievements = achievements;
			_playerStats = playerStats;
			_leaderboardScore=leaderboardScore;
		}
		//---------------------------------------------------------------------------------------------------
		public function get responseCode():String { return _responseCode; }
		public function get leaderboard():Leaderboard { return _leaderboard; }
		public function get achievements():Vector.<Achievement> { return _achievements; }
		public function get playerStats():PlayerStats { return _playerStats; }
		public function get leaderboardScore():LeaderboardScore { return _leaderboardScore; }
		//---------------------------------------------------------------------------------------------------
	}
}