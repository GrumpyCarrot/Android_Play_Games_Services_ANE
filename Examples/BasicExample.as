package example {
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	
	import starling.display.Image;
	import starling.textures.Texture;
	
	import com.grumpycarrot.ane.playgameservices.PlayGamesServices;
	import com.grumpycarrot.ane.playgameservices.PlayGamesServicesEvent;
	import com.grumpycarrot.ane.playgameservices.PlayerStats;
	import com.grumpycarrot.ane.playgameservices.Player;
	import com.grumpycarrot.ane.playgameservices.achievements.Achievement;
	import com.grumpycarrot.ane.playgameservices.leaderboards.Leaderboard;
	import com.grumpycarrot.ane.playgameservices.leaderboards.LeaderboardScore;

	//---------------------------------------------------------		
	// https://developers.google.com/games/services/android/achievements
	// https://developers.google.com/games/services/android/leaderboards
	//---------------------------------------------------------		
    public class BasicExample {

		public var _playGamesServices:PlayGamesServices;

		private var LEADERBOARD_ID:String = "xxxxxx_xxxxxxxx";
		private var ACHIEVEMENT_ID:String = "xxxxxx_xxxxxxxx";

		private var player:Player;
		
		//---------------------------------------------------------	
		public function BasicExample() {
			
			init();
		}
		//---------------------------------------------------------	
		private function init():void {

			_playGamesServices = PlayGamesServices.getInstance();
			_playGamesServices.addEventListener(PlayGamesServicesEvent.GOOGLE_PLAY_GAMES_EVENT, onGooglePlayGames);

			_playGamesServices.initAPI(false,false,true,3,true);
			
		}
		//---------------------------------------------------------	
		private function onGooglePlayGames(event:PlayGamesServicesEvent):void
		{
			
			if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_IN_SUCCESS) {
				trace("Sign In Success");
			} 
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_OUT_SUCCESS) {
				trace("Sign Out Success");
			}
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_SIGN_IN_FAIL) {
				trace("Sign In Failed");
			}
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_URI_IMAGE_LOADED) {
				
				var  bitmapData:BitmapData = _playGamesServices.utils.getCurrentLoadedUriImage();
				 
				//use the Bitmapdata with Starling for example :
				var texture:Texture = Texture.fromBitmap(new Bitmap(bitmapData));
				var image:Image = new Image(texture);
				 
			}
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_PLAYERSTATS_LOADED) {
				
				trace("Player Stats Loaded Success");
					
				var playerStats:PlayerStats = event.playerStats;
				
				trace("averageSessionLength : ", playerStats.averageSessionLength);
				trace("daysSinceLastPlayed : ", playerStats.daysSinceLastPlayed);
				trace("numberOfPurchases : ", playerStats.numberOfPurchases);
				trace("numberOfSessions : ", playerStats.numberOfSessions);
				trace("sessionPercentile : ", playerStats.sessionPercentile);
				trace("spendPercentile : ", playerStats.spendPercentile);

			}			
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_ACHIEVEMENTS_LOADED) {
				
				var achivArray:Vector.<Achievement> = event.achievements;
				
				trace("Achievements Loaded, found : ", achivArray.length);
				
				if (achivArray.length > 0 ) {
					
					var achi:Achievement = achivArray[0];
					
					trace("name : ", achi.name);
					trace("description : ", achi.description);
					trace("state : ", achi.state);
					trace("totalSteps : ", achi.totalSteps);

					//load the achievement Image with android URI
					_playGamesServices.utils.loadUriImage(achi.revealedImageUri);					
					
				}
	
			}
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_PLAYER_SCORE_LOADED) {
				
				trace("Player Score Loaded :");
				
				var leaderboardScore:LeaderboardScore = event.leaderboardScore;
				
				trace("leaderboardScore ", leaderboardScore.displayRank);
				trace("leaderboardScore ", leaderboardScore.displayScore);
				trace("leaderboardScore ", leaderboardScore.rank);
				trace("leaderboardScore ", leaderboardScore.rawScore);

			}
			//-----------------
			else if (event.responseCode == PlayGamesServicesEvent.ON_LEADERBOARD_LOADED) {
				
				trace("Leaderboard loaded :");
				
				var  leaderboard:Leaderboard = event.leaderboard;
				
				if (leaderboard != null) {
					
					trace("leaderboard displayName ", leaderboard.displayName);
					trace("leaderboard iconImageUri ", leaderboard.iconImageUri);
					trace("leaderboard leaderboardId ", leaderboard.leaderboardId);	
					
					var leaderboardScores:Vector.<LeaderboardScore> = leaderboard.leaderboardScores;
					
					trace("Number of scores loaded : ", leaderboardScores.length);
					
					if (leaderboardScores.length > 0) {
						
						var firstLeaderboardScore:LeaderboardScore = leaderboardScores[0];
				
						trace("firstLeaderboardScore displayRank ", firstLeaderboardScore.displayRank);
						trace("firstLeaderboardScore displayScore ", firstLeaderboardScore.displayScore);
						trace("firstLeaderboardScore rank ", firstLeaderboardScore.rank);
						trace("firstLeaderboardScore rawScore ", firstLeaderboardScore.rawScore);
						trace("firstLeaderboardScore displayName", firstLeaderboardScore.player.displayName);	
					}
				}

			}
			
		}
		//---------------------------------------------------------
		// Sign In / Sign Out Functions
		//---------------------------------------------------------
		public function signIn():void { 

			_playGamesServices.signIn();
		}
		//---------------------------------------------------------
		public function signOut():void { 
		
			_playGamesServices.signOut();
		}
		//---------------------------------------------------------
		// Current Player Functions
		//---------------------------------------------------------
		public function getActivePlayerInfo():void { 
			
			var player:Player = _playGamesServices.getActivePlayer();
			
			trace("displayName ", player.id);
			trace("displayName ", player.displayName);
			trace("iconImageUri ", player.iconImageUri);
			trace("iconImageUrl ", player.iconImageUrl);
			
		}
		//---------------------------------------------------------
		// https://developers.google.com/games/services/android/stats
		//---------------------------------------------------------
		public function getPlayerStats():void { 

			_playGamesServices.getPlayerStats();
		}
		//---------------------------------------------------------
		// Achievements Functions
		//---------------------------------------------------------
		public function showAchievementsUI():void { 
	
			_playGamesServices.achievements.showAchievements();
		}
		//---------------------------------------------------------
		public function getAchievementsList():void { 

			_playGamesServices.achievements.loadAchievements();
		}
		//---------------------------------------------------------
		public function unlockAchievement():void { 

			_playGamesServices.achievements.unlockAchievement(ACHIEVEMENT_ID);
		}
		//---------------------------------------------------------
		public function incrementAchievement(numSteps:int):void { 

			_playGamesServices.achievements.incrementAchievement(ACHIEVEMENT_ID, numSteps);
		}
		//---------------------------------------------------------
		public function setStepsAchivement(numSteps:int):void { 
			
			_playGamesServices.achievements.setStepsAchivement(ACHIEVEMENT_ID, numSteps);
		}
		//---------------------------------------------------------
		// Leaderboards Functions
		//---------------------------------------------------------
		public function showAllLeaderboards():void { 

			_playGamesServices.leaderboards.showLeaderboards();
		}
		//---------------------------------------------------------
		public function showLeaderboard():void { 

			_playGamesServices.leaderboards.showLeaderboard(LEADERBOARD_ID);
		}
		//---------------------------------------------------------
		public function getCurrentPlayerLeaderboardScore():void { 

			_playGamesServices.leaderboards.getCurrentPlayerLeaderboardScore(LEADERBOARD_ID,Leaderboard.TIME_SPAN_ALL_TIME,Leaderboard.COLLECTION_SOCIAL);
		}
		//---------------------------------------------------------
		public function getPlayerCenteredLeaderboard():void { 

			_playGamesServices.leaderboards.getPlayerCenteredLeaderboard(LEADERBOARD_ID, 5,Leaderboard.TIME_SPAN_ALL_TIME, Leaderboard.COLLECTION_PUBLIC);
		}
		//---------------------------------------------------------
		public function getTopLeaderboard():void { 

			_playGamesServices.leaderboards.getTopLeaderboard(LEADERBOARD_ID, 10,Leaderboard.TIME_SPAN_ALL_TIME, Leaderboard.COLLECTION_PUBLIC);
		}
		//---------------------------------------------------------			

		}
}