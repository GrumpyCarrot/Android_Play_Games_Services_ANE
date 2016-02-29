//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.achievements
{
	public class Achievement
	{
		public static const STATE_HIDDEN:int = 2;
		public static const STATE_REVEALED:int = 1;
		public static const STATE_UNLOCKED:int = 0;
		public static const TYPE_INCREMENTAL:int = 1;
		public static const TYPE_STANDARD:int = 0;

		private var _achievementId:String;
		private var _name:String;
		private var _description:String;
		private var _state:int;
		private var _revealedImageUri:String;
		private var _revealedImageUrl:String;
		private var _unlockedImageUri:String;
		private var _unlockedImageUrl:String;
		private var _type:int;
		private var _currentSteps:int;
		private var _totalSteps:int;
		private var _xpValue:Number;

		//---------------------------------------------------------------------------------------------------
		public function Achievement(achievementId:String,name:String=null,description:String=null,state:int=-1,
									revealedImageUri:String=null,revealedImageUrl:String=null,unlockedImageUri:String=null,unlockedImageUrl:String=null,
									type:int=-1,currentSteps:int=-1,totalSteps:int=-1,xpValue:Number=-1)
		{
			_achievementId = achievementId;
			_name = name;
			_description = description;
			_state = state;
			_revealedImageUri = revealedImageUri;
			_revealedImageUrl = revealedImageUrl;
			_unlockedImageUri = unlockedImageUri;
			_unlockedImageUrl = unlockedImageUrl;
			_type = type;
			_currentSteps = currentSteps;
			_totalSteps = totalSteps;
			_xpValue = xpValue;			

		}
		//---------------------------------------------------
		public static function fromJSONObject( jsonObject:Object ):Achievement {

			if( jsonObject.achievementId == null ) return null;
			
			trace("bon alors",jsonObject);
			trace("bon alors",jsonObject.achievementId );
			
			return new Achievement( jsonObject.achievementId,jsonObject.name,jsonObject.description,jsonObject.state,
				jsonObject.revealedImageUri,jsonObject.revealedImageUrl,jsonObject.unlockedImageUri,jsonObject.unlockedImageUrl,
				jsonObject.type,jsonObject.currentSteps,jsonObject.totalSteps,jsonObject.xpValue);
		}
		//---------------------------------------------------
		public function get achievementId():String { return _achievementId; }
		public function get name():String { return _name; }
		public function get description():String { return _description; }
		public function get state():int { return _state; }	
		public function get revealedImageUri():String { return _revealedImageUri; }	
		public function get revealedImageUrl():String { return _revealedImageUrl; }
		public function get unlockedImageUri():String { return _unlockedImageUri; }
		public function get unlockedImageUrl():String { return _unlockedImageUrl; }
		public function get type():int { return _type; }	
		public function get currentSteps():int { return _currentSteps; }
		public function get totalSteps():int { return _totalSteps; }
		public function get xpValue():Number { return _xpValue; }		
		//---------------------------------------------------
	}
}