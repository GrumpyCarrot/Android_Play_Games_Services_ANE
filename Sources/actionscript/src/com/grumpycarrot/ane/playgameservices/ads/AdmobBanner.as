//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.ads
{
	import flash.external.ExtensionContext;


	public class AdmobBanner
	{
		private var _context:ExtensionContext;
		
		public static const ADSIZE_BANNER:int = 0;
		public static const ADSIZE_FLUID:int = 1;
		public static const ADSIZE_FULL_BANNER:int = 2;
		public static const ADSIZE_LARGE_BANNER:int = 3;
		public static const ADSIZE_LEADERBOARD:int = 4;
		public static const ADSIZE_MEDIUM_RECTANGLE:int = 5;
		public static const ADSIZE_SMART_BANNER:int = 6;

		public static const POSITION_TOP_LEFT:int = 0;
		public static const POSITION_TOP_CENTER:int = 1;
		public static const POSITION_TOP_RIGHT:int = 2;
		public static const POSITION_MIDDLE_LEFT:int = 3;
		public static const POSITION_MIDDLE_CENTER:int = 4;
		public static const POSITION_MIDDLE_RIGHT:int = 5;
		public static const POSITION_BOTTOM_LEFT:int = 6;
		public static const POSITION_BOTTOM_CENTER:int = 7;
		public static const POSITION_BOTTOM_RIGHT:int = 8;

		//---------------------------------------------------
		public function AdmobBanner(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//----------------------------------------------------------------------------------------------
		public function  init(adMobId:String,bannerSize:int=ADSIZE_SMART_BANNER,bannerPosition:int=POSITION_TOP_CENTER,deviceID:String="", isTestMode:Boolean=false ):void
		{
			_context.call("bannerAdInit",adMobId, deviceID, isTestMode, bannerSize, bannerPosition);
		}		
		//----------------------------------------------------------------------------------------------
		public function  show():void
		{
			_context.call("bannerAdShow");
		}		
		//----------------------------------------------------------------------------------------------
		public function  hide():void
		{
			_context.call("bannerAdHide");
		}		
		//----------------------------------------------------------------------------------------------
		public function  remove():void
		{
			_context.call("bannerAdRemove");
		}		
		//----------------------------------------------------------------------------------------------
		public function  isShown():Boolean
		{
			return _context.call("bannerIsShown");
		}		
		//----------------------------------------------------------------------------------------------
		public function  isActivated():Boolean
		{
			return _context.call("bannerIsActivated");
		}		
		//----------------------------------------------------------------------------------------------
		public function load():void
		{
			_context.call("bannerAdLoad");
		}		
		//----------------------------------------------------------------------------------------------
	}
}