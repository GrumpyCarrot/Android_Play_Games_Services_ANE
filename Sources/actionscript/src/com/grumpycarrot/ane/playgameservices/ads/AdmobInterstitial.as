//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.ads
{
	import flash.external.ExtensionContext;


	public class AdmobInterstitial
	{
		private var _context:ExtensionContext;
		
		public static const RESOLUTION_POLICY_HIGHEST_PROGRESS:int = 4;
		public static const RESOLUTION_POLICY_LAST_KNOWN_GOOD:int = 2;
		public static const RESOLUTION_POLICY_LONGEST_PLAYTIME:int = 1;
		public static const RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED:int = 3;

		//---------------------------------------------------
		public function AdmobInterstitial(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//----------------------------------------------------------------------------------------------
		public function  init(adMobId:String,deviceID:String="",isTestMode:Boolean=false):void
		{
			_context.call("interstitialInit",adMobId,deviceID,isTestMode);
		}		
		//----------------------------------------------------------------------------------------------
		public function  isLoaded():Boolean
		{
			return _context.call("interstitialIsLoaded");
		}		
		//----------------------------------------------------------------------------------------------
		public function  load():void
		{
			_context.call("interstitialLoad");
		}		
		//----------------------------------------------------------------------------------------------
		public function  show():void
		{
			_context.call("interstitialShow");
		}	
		//---------------------------------------------------
	}
}