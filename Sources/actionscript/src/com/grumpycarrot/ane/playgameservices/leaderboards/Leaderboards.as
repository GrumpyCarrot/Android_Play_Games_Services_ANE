//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.leaderboards
{
	
	import flash.external.ExtensionContext;

	
	public class Leaderboards
	{

		private var _context:ExtensionContext;

		//---------------------------------------------------
		public function Leaderboards(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//-----------------------------------------------------------------------------------------
		public function showLeaderboards():void {
			_context.call("showAllLeaderboards"); 
		}	
		//-----------------------------------------------------------------------------------------
		public function showLeaderboard(leaderboardId:String):void {
			_context.call("showLeaderboard",leaderboardId); 
		}	
		// -----------------------------------------------------------------------------------------
		public function reportScore(leaderboardId:String, newScore:Number):void { 
			_context.call("reportScore", leaderboardId, newScore);
		}
		//-----------------------------------------------------------------------------------------
		public function getCurrentPlayerLeaderboardScore( leaderboardId:String,span:int=Leaderboard.TIME_SPAN_ALL_TIME, leaderboardCollection:int=Leaderboard.COLLECTION_PUBLIC):void {
			_context.call("getCurrentPlayerLeaderboardScore", leaderboardId,span, leaderboardCollection ); 
		}	
		// -----------------------------------------------------------------------------------------
		public function getTopLeaderboard( leaderboardId:String,maxResults:int=25,span:int=Leaderboard.TIME_SPAN_ALL_TIME, leaderboardCollection:int=Leaderboard.COLLECTION_PUBLIC  ):void {
			_context.call("getTopLeaderboard", leaderboardId,span, leaderboardCollection,maxResults ); 
		}
		// -----------------------------------------------------------------------------------------
		public function getPlayerCenteredLeaderboard( leaderboardId:String,maxResults:int=25 ,span:int=Leaderboard.TIME_SPAN_ALL_TIME, leaderboardCollection:int=Leaderboard.COLLECTION_PUBLIC ):void {
			_context.call("getPlayerCenteredLeaderboard", leaderboardId,span, leaderboardCollection,maxResults ); 
		}
		// -----------------------------------------------------------------------------------------

	}
}