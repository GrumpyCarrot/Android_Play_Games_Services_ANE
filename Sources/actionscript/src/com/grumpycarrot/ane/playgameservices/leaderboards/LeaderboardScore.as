//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.leaderboards
{
	import com.grumpycarrot.ane.playgameservices.Player;

	public class LeaderboardScore
	{

		public static const LEADERBOARD_RANK_UNKNOWN:int = -1;
		
		private var _displayRank:String;
		private var _displayScore:String;
		private var _rank:Number;
		private var _rawScore:Number;		
		private var _player:Player;
		
		
		public function LeaderboardScore( displayRank:String=null,displayScore:String=null,rank:Number=-1,rawScore:Number=-1,player:Player=null )
		{
			_displayRank = displayRank;
			_displayScore = displayScore;
			_rank = rank;
			_rawScore = rawScore;
			_player = player;
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):LeaderboardScore {
			
			var player:Player = Player.fromJSONObject( jsonObject.player );
			
			if( player == null ) return null;
			
			return new LeaderboardScore( jsonObject.displayRank, jsonObject.displayScore,jsonObject.rank, jsonObject.rawScore, player );
			
		}
		//---------------------------------------------------
		public function get displayRank():String { return _displayRank; }
		public function get displayScore():String { return _displayScore; }
		public function get rank():Number { return _rank; }
		public function get rawScore():Number { return _rawScore; }		
		public function get player():Player { return _player; }

		
		//---------------------------------------------------
		
	}
}