package example {

	import com.grumpycarrot.ane.playgameservices.PlayGamesServices;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobBanner;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobEvent;
	import com.grumpycarrot.ane.playgameservices.ads.AdmobInterstitial;

	//---------------------------------------------------------		
	// https://developers.google.com/admob/android/interstitial
	//---------------------------------------------------------		
    public class AdsExample {
		
		private var ADMOB_INTER_AD_ID:String = "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx";
		private var ADMOB_BANNER_AD_ID:String = "ca-app-pub-xxxxxxxxxxxxxxxx/xxxxxxxxxx";

		public var _playGamesServices:PlayGamesServices;

		private var admobBanner:AdmobBanner;
		private var isBannerLoaded:Boolean = false;
		private var admobInterstitial:AdmobInterstitial;
		
		//---------------------------------------------------------	
		public function AdsExample() {
			
			init();
		}
		//---------------------------------------------------------	
		private function init():void {
			
			_playGamesServices = PlayGamesServices.getInstance();
			_playGamesServices.addEventListener(AdmobEvent.ADMOB_EVENT, onAds);

			admobInterstitial.init(ADMOB_INTER_AD_ID);
			admobInterstitial.load();
			
			admobBanner.init(ADMOB_BANNER_AD_ID, AdmobBanner.ADSIZE_SMART_BANNER, AdmobBanner.POSITION_BOTTOM_CENTER);
			admobBanner.load();

		}
		//---------------------------------------------------------	
		private function onAds(event:AdmobEvent):void
		{
			if (event.responseCode == AdmobEvent.ON_INTERSTITIAL_LOADED) {
				
				trace("Interstitial Ad was loaded");
			}
			//-----------------
			else if (event.responseCode == AdmobEvent.ON_BANNER_LOADED) {
				
				trace("banner Ad was loaded");
				isBannerLoaded = true;
			}			

		}
		//---------------------------------------------------------
		// Interstitial Ads Functions
		//---------------------------------------------------------
		public function showInterstitialAd():void { 
			
			if(admobInterstitial.isLoaded()) admobInterstitial.show();	
		}
		//---------------------------------------------------------
		// Banner Ads Functions
		//---------------------------------------------------------
		public function showBannerAd():void { 
			
			if (isBannerLoaded) admobBanner.show();
		}
		//---------------------------------------------------------
		public function removeBannerAd():void { 
			
			if (admobBanner.isShown()) admobBanner.hide();
		}		
		//---------------------------------------------------------
		}
}