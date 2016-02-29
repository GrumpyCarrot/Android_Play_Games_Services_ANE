//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.savedgames
{
	import flash.external.ExtensionContext;


	public class SavedGames
	{
		private var _context:ExtensionContext;
		
		public static const RESOLUTION_POLICY_HIGHEST_PROGRESS:int = 4;
		public static const RESOLUTION_POLICY_LAST_KNOWN_GOOD:int = 2;
		public static const RESOLUTION_POLICY_LONGEST_PLAYTIME:int = 1;
		public static const RESOLUTION_POLICY_MOST_RECENTLY_MODIFIED:int = 3;

		//---------------------------------------------------
		public function SavedGames(_context:ExtensionContext)
		{
			this._context=_context;
		}
		//---------------------------------------------------
		public function showSavedGames_UI(title:String="My saved games",allowAddButton:Boolean=false,allowDelete:Boolean=false,maxSnapshotsToShow:int=5):void
		{
			_context.call("showSavedGamesUI", title, allowAddButton,allowDelete,maxSnapshotsToShow);	
		}	
		//---------------------------------------------------
		public function getSavedGamesList(forceReload:Boolean=false):void
		{
			_context.call("getSavedGamesList", forceReload);	
		}	
		//---------------------------------------------------
		public function openGame(savedGame:SavedGame,conflictPolicy:int=RESOLUTION_POLICY_LAST_KNOWN_GOOD):void
		{
			_context.call("openGame",savedGame.uniqueName,conflictPolicy);	
		}	
		//---------------------------------------------------
		public function writeGame(openedGame:SavedGame):void
		{		
			_context.call("writeGame",openedGame.uniqueName,openedGame.data,openedGame.description,openedGame.playedTime,openedGame.progressValue);	
		}	
		//---------------------------------------------------
		public function deleteGame(openedGame:SavedGame):void
		{		
			_context.call("deleteGame",openedGame.uniqueName);	
		}	
		//---------------------------------------------------
	}
}