//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices
{
	public class PlayerStats
	{

		private var _averageSessionLength:Number;
		private var _daysSinceLastPlayed:int;
		private var _numberOfPurchases:int;
		private var _numberOfSessions:int;
		private var _sessionPercentile:Number;
		private var _spendPercentile:Number;		
		
		//---------------------------------------------------------------------------------------------------
		public function PlayerStats(averageSessionLength:Number=-1,daysSinceLastPlayed:int=-1, numberOfPurchases:int=-1,numberOfSessions:int=-1,sessionPercentile:Number=-1,spendPercentile:Number=-1)
		{
			
			_averageSessionLength = averageSessionLength;
			_daysSinceLastPlayed = daysSinceLastPlayed;
			_numberOfPurchases = numberOfPurchases;
			_numberOfSessions = numberOfSessions;
			_sessionPercentile = sessionPercentile;
			_spendPercentile = spendPercentile;			

		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):PlayerStats {

			return new PlayerStats( parseFloat(jsonObject.averageSessionLength),jsonObject.daysSinceLastPlayed,jsonObject.numberOfPurchases,
				jsonObject.numberOfSessions,parseFloat(jsonObject.sessionPercentile),parseFloat(jsonObject.spendPercentile));
			
		}
		//---------------------------------------------------
		public function get averageSessionLength():Number { return _averageSessionLength; }
		public function get daysSinceLastPlayed():int { return _daysSinceLastPlayed; }
		public function get numberOfPurchases():int { return _numberOfPurchases; }	
		public function get numberOfSessions():int { return _numberOfSessions; }	
		public function get sessionPercentile():Number { return _sessionPercentile; }	
		public function get spendPercentile():Number { return _spendPercentile; }			
		//---------------------------------------------------
	}
}