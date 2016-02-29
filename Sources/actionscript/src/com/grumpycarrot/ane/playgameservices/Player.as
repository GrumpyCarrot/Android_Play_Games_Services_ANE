//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices
{
	public class Player
	{
		
		private var _id:String;
		private var _displayName:String;
		private var _iconImageUri:String;
		private var _iconImageUrl:String;
		private var _hiResImageUri:String;
		private var _hiResImageUrl:String;		
		
		//---------------------------------------------------------------------------------------------------
		public function Player( id:String, displayName:String=null, iconImageUri:String = null, iconImageUrl:String = null, hiResImageUri:String = null, hiResImageUrl:String = null)
		{

			_id = id;
			_displayName = displayName;
			_iconImageUri = iconImageUri;
			_iconImageUrl = iconImageUrl;
			_hiResImageUri = hiResImageUri;
			_hiResImageUrl = hiResImageUrl;		
		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Player {

			if( jsonObject.id == null ) return null;
			return new Player( jsonObject.id, jsonObject.displayName, jsonObject.iconImageUri,  jsonObject.iconImageUrl, jsonObject.hiResImageUri,  jsonObject.hiResImageUrl);
		}
		//---------------------------------------------------
		public function get id():String { return _id; }
		public function get displayName():String { return _displayName; }
		public function get iconImageUri():String { return _iconImageUri; }	
		public function get iconImageUrl():String { return _iconImageUrl; }	
		public function get hiResImageUri():String { return _hiResImageUri; }	
		public function get hiResImageUrl():String { return _hiResImageUrl; }			
		//---------------------------------------------------
	}
}