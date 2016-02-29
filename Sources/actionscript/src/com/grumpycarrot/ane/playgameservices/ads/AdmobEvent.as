//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.ads
{

	import flash.events.Event;
	
	public class AdmobEvent extends Event
	{
		public static const ADMOB_EVENT:String = "ADMOB_EVENT";
		
		public static const ON_INTERSTITIAL_LOADED:String = "ON_INTERSTITIAL_LOADED";	
		public static const ON_INTERSTITIAL_FAILED_TO_LOAD:String = "ON_INTERSTITIAL_FAILED_TO_LOAD";	
		public static const ON_INTERSTITIAL_OPEN:String = "ON_INTERSTITIAL_OPEN";
		public static const ON_INTERSTITIAL_LEFT_APP:String = "ON_INTERSTITIAL_LEFT_APP";	
		public static const ON_INTERSTITIAL_CLOSED:String = "ON_INTERSTITIAL_CLOSED";	

		public static const ON_BANNER_LOADED:String = "ON_BANNER_LOADED";	
		public static const ON_BANNER_FAILED_TO_LOAD:String = "ON_BANNER_FAILED_TO_LOAD";	
		public static const ON_BANNER_OPENED:String = "ON_BANNER_OPENED";
		public static const ON_BANNER_LEFT_APP:String = "ON_BANNER_LEFT_APP";
		public static const ON_BANNER_CLOSED:String = "ON_BANNER_CLOSED";	

		private var _errorCode:String;
		private var _responseCode:String;

		public function AdmobEvent(type:String, responseCode:String, errorCode:String=null)
		{
			super(type);
			_responseCode = responseCode;
			_errorCode = errorCode;
		}
		//---------------------------------------------------------------------------------------------------
		public function get errorCode():String { return _errorCode; }
		//---------------------------------------------------------------------------------------------------
		public function get responseCode():String { return _responseCode; }
		//---------------------------------------------------------------------------------------------------
	}
}