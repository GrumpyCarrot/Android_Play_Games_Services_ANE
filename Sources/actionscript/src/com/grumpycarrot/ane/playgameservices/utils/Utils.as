//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.utils
{
	import flash.display.BitmapData;
	import flash.external.ExtensionContext;

	
	
	public class Utils
	{
		private var _context:ExtensionContext;

		//---------------------------------------------------
		public function Utils(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//---------------------------------------------------
		public function loadUriImage(uriString:String):void
		{
			_context.call("loadUriImage", uriString);	
		}	
		//---------------------------------------------------
		public function getCurrentLoadedUriImage():BitmapData
		{
			return _context.call("getCurrentLoadedUriImage") as BitmapData;
		}			
		//---------------------------------------------------
	}
}