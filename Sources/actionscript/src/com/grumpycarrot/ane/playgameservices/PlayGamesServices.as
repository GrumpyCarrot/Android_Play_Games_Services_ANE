//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices
{
	import com.grumpycarrot.ane.playgameservices.achievements.Achievement;
	import com.grumpycarrot.ane.playgameservices.achievements.Achievements;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobBanner;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobEvent;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobInterstitial;
	import com.grumpycarrot.ane.playgameservices.eventsquests.EventsQuests;
	import com.grumpycarrot.ane.playgameservices.eventsquests.EventsQuestsEvent;
	import com.grumpycarrot.ane.playgameservices.eventsquests.GameEvent;
	import com.grumpycarrot.ane.playgameservices.eventsquests.Quest;
	import com.grumpycarrot.ane.playgameservices.leaderboards.Leaderboard;
	import com.grumpycarrot.ane.playgameservices.leaderboards.LeaderboardScore;
	import com.grumpycarrot.ane.playgameservices.leaderboards.Leaderboards;
	import com.grumpycarrot.ane.playgameservices.multiplayer.TurnBasedEvent;
	import com.grumpycarrot.ane.playgameservices.multiplayer.TurnBasedMatch;
	import com.grumpycarrot.ane.playgameservices.multiplayer.TurnBasedMultiplayer;
	import com.grumpycarrot.ane.playgameservices.savedgames.SavedGame;
	import com.grumpycarrot.ane.playgameservices.savedgames.SavedGameEvent;
	import com.grumpycarrot.ane.playgameservices.savedgames.SavedGames;
	import com.grumpycarrot.ane.playgameservices.utils.Utils;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import com.grumpycarrot.ane.playgameservices.multiplayer.Invitation;

	
	public class PlayGamesServices extends EventDispatcher
	{

		private static const EXTENSION_ID : String = "com.grumpycarrot.ane.playgameservices";
		private static var _instance : PlayGamesServices;
		private var _context : ExtensionContext;
		public var turnBasedMultiplayer:TurnBasedMultiplayer;
		public var savedGames:SavedGames;
		public var admobBanner:AdmobBanner;
		public var admobInterstitial:AdmobInterstitial;
		public var eventsQuests:EventsQuests;
		public var utils:Utils;
		public var achievements:Achievements;
		public var leaderboards:Leaderboards;

		// -----------------------------------------------------------------------------------------
		public function PlayGamesServices()
		{ 
			if (!_instance)
			{
				_context = ExtensionContext.createExtensionContext(EXTENSION_ID, null);
				if (!_context)
				{
					throw Error("ERROR - Extension context is null. Please check if extension.xml is setup correctly.");
					return;
				}
				_context.addEventListener(StatusEvent.STATUS, onStatus);
				_instance = this;
				turnBasedMultiplayer=new TurnBasedMultiplayer(_context);
				savedGames=new SavedGames(_context);
				admobBanner=new AdmobBanner(_context);
				admobInterstitial=new AdmobInterstitial(_context);
				eventsQuests=new EventsQuests(_context);
				utils=new Utils(_context);
				achievements=new Achievements(_context);
				leaderboards=new Leaderboards(_context);
				
			}
			else
			{
				throw Error("This is a singleton, use getInstance(), do not call the constructor directly.");
			}
		}
		// -----------------------------------------------------------------------------------------
		public static function getInstance() : PlayGamesServices
		{
			return _instance ? _instance : new PlayGamesServices();
		}
		//---------------------------------------------------
		public function initAPI( enableSavedGames:Boolean=false, enableTurnBaseMulti:Boolean=false,connectOnStart:Boolean=true, maxAutoSignInAttempts:int=3,showPopUps:Boolean=true):void {
			_context.call("initAPI",enableSavedGames,enableTurnBaseMulti,connectOnStart,maxAutoSignInAttempts,showPopUps); 
		}		
		// -----------------------------------------------------------------------------------------
		public function signIn():void { 
			_context.call("signIn"); 
		}
		// -----------------------------------------------------------------------------------------
		public function signOut():void {
			_context.call("signOut"); 
		}
		// -----------------------------------------------------------------------------------------
		public function getPlayerStats():void { 
			_context.call("checkPlayerStats"); 
		}
		// -----------------------------------------------------------------------------------------
		public function getActivePlayer():Player { 
			
			var playerStr:String=_context.call("getActivePlayer") as String;
			var jsonPlayer:Object=JSON.parse(playerStr) as Object;
			
			return Player.fromJSONObject(jsonPlayer); 
		}
		// -----------------------------------------------------------------------------------------
		// Events to Dispatch
		// -----------------------------------------------------------------------------------------
		private function onStatus( event : StatusEvent ) : void
		{
			trace("[AirGooglePlayGames]");
			var e:Event=null;
			var i:int;

			// -----------------------------------------------------------------------------------------
			// GameServices Events
			// -----------------------------------------------------------------------------------------
			if (event.code == "ON_SIGN_IN_SUCCESS")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_SIGN_IN_SUCCESS);
			} else if (event.code == "ON_SIGN_IN_FAIL")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_SIGN_IN_FAIL);
			} else if (event.code == "ON_SIGN_OUT_SUCCESS")	
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_SIGN_OUT_SUCCESS);
			} 
			else if (event.code == "ON_URI_IMAGE_LOADED")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_URI_IMAGE_LOADED );
			}	
			else if (event.code == "ON_PLAYERSTATS_LOADED")
			{
				var jsonPlayerStats:Object=JSON.parse(event.level) as Object;
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_PLAYERSTATS_LOADED,null,null, PlayerStats.fromJSONObject(jsonPlayerStats));
			}			
			// -----------------------------------------------------------------------------------------
			// Achievements Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_ACHIEVEMENTS_LOADING_FAILED")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_ACHIEVEMENTS_LOADING_FAILED );
			}  else if (event.code == "ON_ACHIEVEMENTS_LOADED")
			{
				var jsonAchievArray:Array = JSON.parse(event.level) as Array;
				var jsonjsonAchievLength:int=jsonAchievArray.length;
				
				var achievements:Vector.<Achievement>= new <Achievement>[];
				
				for(i=0; i < jsonjsonAchievLength;i++) {
					achievements.push(Achievement.fromJSONObject(Object(jsonAchievArray[i])));
				}							
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_ACHIEVEMENTS_LOADED,null,achievements);				
			} 				
			// -----------------------------------------------------------------------------------------
			// Leaderboards Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_LEADERBOARD_LOADED")
			{
				
				var jsonLeaderboard:Object=JSON.parse(event.level) as Object;
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_LEADERBOARD_LOADED,Leaderboard.fromJSONObject(jsonLeaderboard) );

			} else if (event.code == "ON_LEADERBOARD_FAILED")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_LEADERBOARD_LOADING_FAILED );
			}
			else if (event.code == "ON_PLAYER_SCORE_UNKNOWN")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_PLAYER_SCORE_UNKNOWN );
			}				
			else if (event.code == "ON_PLAYER_SCORE_LOAD_FAILED")
			{
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_PLAYER_SCORE_LOAD_FAILED );
			}	
			else if (event.code == "ON_PLAYER_SCORE_LOADED")
			{
				var jsonLeaderboardScore:Object=JSON.parse(event.level) as Object;
				e = new PlayGamesServicesEvent(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT,PlayGamesServicesEvent.ON_PLAYER_SCORE_LOADED,null,null, null,LeaderboardScore.fromJSONObject(jsonLeaderboardScore));
			}				
			// -----------------------------------------------------------------------------------------
			// Admobs Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_INTERSTITIAL_LOADED")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_INTERSTITIAL_LOADED);
			} else if (event.code == "ON_INTERSTITIAL_FAILED_TO_LOAD")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_INTERSTITIAL_FAILED_TO_LOAD,String(event.level));
			}
			else if (event.code == "ON_INTERSTITIAL_OPEN")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_INTERSTITIAL_OPEN);
			}			
			else if (event.code == "ON_INTERSTITIAL_LEFT_APP")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_INTERSTITIAL_LEFT_APP);
			}				
			else if (event.code == "ON_INTERSTITIAL_CLOSED")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_INTERSTITIAL_CLOSED);
			}			
			else if (event.code == "ON_BANNER_LOADED")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_BANNER_LOADED);
			}				
			else if (event.code == "ON_BANNER_FAILED_TO_LOAD")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_BANNER_FAILED_TO_LOAD,String(event.level));
			}			
			else if (event.code == "ON_BANNER_OPENED")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_BANNER_OPENED);
			}				
			else if (event.code == "ON_BANNER_LEFT_APP")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_BANNER_LEFT_APP);
			}			
			else if (event.code == "ON_BANNER_CLOSED")
			{
				e = new AdmobEvent(AdmobEvent.ADMOB_EVENT,AdmobEvent.ON_BANNER_CLOSED);
			}						
			// -----------------------------------------------------------------------------------------
			// Saved Games Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_LOAD_SAVEDGAMES_FAILED")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_LOAD_SAVEDGAMES_FAILED );
			} else if (event.code == "ON_LOAD_SAVEDGAMES_SUCCESS")
			{
				var jsonSavedGamesArray:Array = JSON.parse(event.level) as Array;
				var jsonSavedGamesLength:int=jsonSavedGamesArray.length;

				var savedGames:Vector.<SavedGame>= new <SavedGame>[];
				
				for(i=0; i < jsonSavedGamesLength;i++) {
					savedGames.push(SavedGame.fromJSONObject(Object(jsonSavedGamesArray[i])));
				}							
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_LOAD_SAVEDGAMES_SUCCESS,savedGames);				
			} 
			else if (event.code == "ON_OPEN_FAILED")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_OPEN_FAILED );
			} else if (event.code == "OPEN_SUCCESS")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_OPEN_SUCCESS,null, event.level );
			}
			
			else if (event.code == "WRITE_SUCCESS")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_WRITE_SUCCESS );
			}			
			else if (event.code == "WRITE_FAILED")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_WRITE_FAILED );
			}	
			else if (event.code == "WRITE_ERROR_FILE_NOT_OPEN")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_WRITE_ERROR_FILE_NOT_OPEN );
			}	
			else if (event.code == "ON_UI_CANCEL")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_UI_CANCEL );
			}
			else if (event.code == "ON_UI_CREATE_GAME")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_UI_CREATE_GAME );
			}	
			else if (event.code == "ON_UI_LOAD_GAME")
			{
				var jsonSavedGame:Object=JSON.parse(event.level) as Object;
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_UI_LOAD_GAME,null,null,SavedGame.fromJSONObject(jsonSavedGame) );
			}	
			else if (event.code == "ON_DELETE_SUCCESS")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_DELETE_SUCCESS );
			}			
			else if (event.code == "ON_DELETE_FAILED")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_DELETE_FAILED );
			}			
			else if (event.code == "ON_DELETE_ERROR_FILE_NOT_OPEN")
			{
				e = new SavedGameEvent(SavedGameEvent.SAVEDGAMES_EVENT,SavedGameEvent.ON_DELETE_ERROR_FILE_NOT_OPEN );
			}	
			// -----------------------------------------------------------------------------------------
			// Events Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_RETRIEVE_EVENT_FAILED")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_RETRIEVE_EVENT_FAILED );
			} else if (event.code == "ON_RETRIEVE_EVENT_SUCCESS")
			{
				var jsongameEventsArray:Array = JSON.parse(event.level) as Array;
				var jsongameEventsLength:int=jsongameEventsArray.length;

				var gameEventsList:Vector.<GameEvent> = new <GameEvent>[];
				
				for(i=0; i < jsongameEventsLength;i++) {
					gameEventsList.push(GameEvent.fromJSONObject(Object(jsongameEventsArray[i])));
				}
				
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_RETRIEVE_EVENT_SUCCESS,gameEventsList);				
			} 
			// -----------------------------------------------------------------------------------------
			// Quests Events
			// -----------------------------------------------------------------------------------------			
			else if (event.code == "ON_QUESTS_UI_CANCEL")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_QUESTS_UI_CANCEL );
			} else if (event.code == "ON_QUESTS_UI_ACCEPTED")
			{
				var jsonQuest:Object=JSON.parse(event.level) as Object;
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_QUESTS_UI_ACCEPTED,null,null,Quest.fromJSONObject(jsonQuest));				
			}  else if (event.code == "ON_QUESTS_UI_CLAIM_REWARD")
			{
				var jsonRewardQuest:Object=JSON.parse(event.level) as Object;
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_QUESTS_UI_CLAIM_REWARD,null,null,Quest.fromJSONObject(jsonRewardQuest));				
			} 				
			else if (event.code == "ON_LOAD_QUESTS_FAILED")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_LOAD_QUESTS_FAILED );
			} else if (event.code == "ON_LOAD_QUESTS_SUCCESS")
			{
				var jsonQuestsArray:Array = JSON.parse(event.level) as Array;
				var jsonQuestsLength:int=jsonQuestsArray.length;
				
				var questList:Vector.<Quest> = new <Quest>[];
				
				for(i=0; i < jsonQuestsLength;i++) {
					questList.push(Quest.fromJSONObject(Object(jsonQuestsArray[i])));
				}

				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_LOAD_QUESTS_SUCCESS,null,questList);				
			} 			
			else if (event.code == "ON_QUEST_ACCEPT_SUCCESS")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_QUEST_ACCEPT_SUCCESS );
			}			
			else if (event.code == "ON_QUEST_ACCEPT_FAILED")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_QUEST_ACCEPT_FAILED );
			}			
			else if (event.code == "ON_CLAIM_REWARD_FAILED")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_CLAIM_REWARD_FAILED );
			}				
			else if (event.code == "ON_CLAIM_REWARD_SUCCESS")
			{
				e = new EventsQuestsEvent(EventsQuestsEvent.EVENTS_AND_QUESTS_EVENT,EventsQuestsEvent.ON_CLAIM_REWARD_SUCCESS,null,null,null,String(event.level) );
			}			
			// -----------------------------------------------------------------------------------------
			// TurnBasedMultiplayer Events
			// -----------------------------------------------------------------------------------------
			else if (event.code == "ON_LOOK_AT_MATCH_UI_CANCELED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOOK_AT_MATCH_UI_CANCELED );
			}				
			else if (event.code == "ON_CREATEMATCH_UI_CANCELED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_CREATE_MATCH_UI_CANCELED );
			}				
			else if (event.code == "ON_INITIATE_MATCH_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_INITIATE_MATCH_FAILED );
			}			
			else if (event.code == "ON_INITIATE_MATCH_SUCCESS")
			{
				var jsonNewMatch:Object=JSON.parse(event.level) as Object;
				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_INITIATE_MATCH_SUCCESS,null,TurnBasedMatch.fromJSONObject(jsonNewMatch) );
			}			
		
			else if (event.code == "ON_LOAD_INVITATIONS_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_INVITATIONS_FAILED );
			}			
			else if (event.code == "ON_LOAD_INVITATIONS_SUCCESS") {
				
				var jsonInvArray:Array = JSON.parse(event.level) as Array;
				var jsonInvLength:int=jsonInvArray.length;

				var invList:Vector.<Invitation>= new <Invitation>[];

				for( i=0; i < jsonInvLength;i++) {
					invList.push(Invitation.fromJSONObject(Object(jsonInvArray[i])));
				}	
				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_INVITATIONS_SUCCESS,null,null,null,invList);
			}			
			else if (event.code == "ON_LOAD_MATCHES_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_MATCHES_FAILED );
			}			
			else if (event.code == "ON_LOAD_MATCHES_SUCCESS")
			{
				
				var jsonMatchArray:Array = JSON.parse(event.level) as Array;
				var jsonMatchLength:int=jsonMatchArray.length;
				
				var matchList:Vector.<TurnBasedMatch>= new <TurnBasedMatch>[];
				
				for( i=0; i < jsonMatchLength;i++) {
					matchList.push(TurnBasedMatch.fromJSONObject(Object(jsonMatchArray[i])));
				}					
				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_MATCHES_SUCCESS,null,null,matchList);
			}			
			else if (event.code == "ON_LOAD_MATCH_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_MATCH_FAILED );
			}			
			
			else if (event.code == "ON_LOAD_MATCH_SUCCESS")
			{
				var jsonLoadMatch:Object=JSON.parse(event.level) as Object;
				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LOAD_MATCH_SUCCESS,null,TurnBasedMatch.fromJSONObject(jsonLoadMatch) );
			}			
			else if (event.code == "ON_UPDATE_MATCH_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_UPDATE_MATCH_FAILED );
			}			
				
			else if (event.code == "ON_UPDATE_MATCH_SUCCESS")
			{
				var jsonUpdateMatch:Object=JSON.parse(event.level) as Object;
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_UPDATE_MATCH_SUCCESS,null,TurnBasedMatch.fromJSONObject(jsonUpdateMatch) );
			}					
			else if (event.code == "ON_CANCEL_MATCH_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_CANCEL_MATCH_FAILED );
			}			
				
			else if (event.code == "ON_CANCEL_MATCH_SUCCESS")
			{				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_CANCEL_MATCH_SUCCESS,event.level);
			}				
			else if (event.code == "ON_LEAVE_MATCH_FAILED")
			{
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LEAVE_MATCH_FAILED );
			}			
				
			else if (event.code == "ON_LEAVE_MATCH_SUCCESS")
			{
				var jsonLeaveMatch:Object=JSON.parse(event.level) as Object;
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_LEAVE_MATCH_SUCCESS,null,TurnBasedMatch.fromJSONObject(jsonLeaveMatch) );
			}		
			else if (event.code == "ON_NOTIFICATION_TBM_RECEIVED")
			{
				var jsonNotifMatch:Object=JSON.parse(event.level) as Object;
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_NOTIFICATION_TBM_RECEIVED,null,TurnBasedMatch.fromJSONObject(jsonNotifMatch) );
			}			
			else if (event.code == "ON_NOTIFICATION_TBM_REMOVED")
			{				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_NOTIFICATION_TBM_REMOVED,event.level);
			}	
			else if (event.code == "ON_NOTIFICATION_INVITATION_RECEIVED")
			{
				var jsonNotifInvitation:Object=JSON.parse(event.level) as Object;
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_NOTIFICATION_INVITATION_RECEIVED,null,null,null,null,null,Invitation.fromJSONObject(jsonNotifInvitation));
			}	
			else if (event.code == "ON_NOTIFICATION_INVITATION_REMOVED")
			{				
				e = new TurnBasedEvent(TurnBasedEvent.MULTIPLAYER_TBM_EVENT,TurnBasedEvent.ON_NOTIFICATION_INVITATION_REMOVED,null,null,null,null,event.level);
			}			

			// -----------------------------------------------------------------------------------------
			
			if (e) dispatchEvent(e);
			
		}
	}
	// -----------------------------------------------------------------------------------------
}