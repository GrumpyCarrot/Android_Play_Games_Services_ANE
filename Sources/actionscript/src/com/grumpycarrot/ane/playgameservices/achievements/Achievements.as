//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.achievements
{
	import flash.external.ExtensionContext;

	
	
	public class Achievements
	{


		private var _context:ExtensionContext;

		//---------------------------------------------------
		public function Achievements(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//---------------------------------------------------
		public function showAchievements():void {
			_context.call("showAchievements");
		}		
		//---------------------------------------------------
		public function revealAchievement(achievementId:String):void { 
			_context.call("revealAchievement", achievementId);
		}			
		//---------------------------------------------------
		public function unlockAchievement(achievementId:String):void { 
			_context.call("unlockAchievement", achievementId);
		}			
		//---------------------------------------------------
		public function incrementAchievement(achievementId:String,numSteps:int):void { 
			_context.call("incrementAchievement", achievementId,numSteps);
		}			
		//---------------------------------------------------
		public function setStepsAchivement(achievementId:String,numSteps:int):void { 
			_context.call("setStepsAchievement", achievementId,numSteps);
		}			
		//---------------------------------------------------
		public function loadAchievements(forceReload:Boolean=true):void
		{
			_context.call("loadAchievements", forceReload);	
		}	
		//---------------------------------------------------

	}
}