//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.admob;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.grumpycarrot.ane.playgameservices.Extension;

public class Interstitial extends AdListener {
	
	private InterstitialAd interstitialAd=null;
	private Boolean isTest;
	private String deviceId;

	public Interstitial() {}
	//----------------------------------------------------------------------------------------------	
	public void dispose() {
		interstitialAd=null;
	}	
	//----------------------------------------------------------------------------------------------	
	public void init(String adMobId, String devId, Boolean test) {

		isTest=test;
		deviceId=devId;

		interstitialAd = new InterstitialAd( Extension.context.getActivity() );
		interstitialAd.setAdUnitId(adMobId);
	    interstitialAd.setAdListener(this);
		
	}
	//----------------------------------------------------------------------------------------------	
	public void loadInterstitial() {
		
		AdRequest adRequest;
		
		if(isTest) { 

			adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(deviceId)
			.build();
		} else {
			adRequest = new AdRequest.Builder().build();
		}

	    interstitialAd.loadAd(adRequest);
	}

	//----------------------------------------------------------------------------------------------	
	public void showInterstitial() {
		
		if (interstitialAd.isLoaded()) {
	      interstitialAd.show();
	    }
	 }	
	//----------------------------------------------------------------------------------------------	
	public Boolean isInterstialLoaded() {
		
		return interstitialAd.isLoaded();

	 }	
	//----------------------------------------------------------------------------------------------
	@Override
	 public void onAdLoaded() {
		Extension.context.sendEventToAir("ON_INTERSTITIAL_LOADED");
	}
	//----------------------------------------------------------------------------------------------
	@Override
	public void onAdFailedToLoad(int errorCode) {
		Extension.context.sendEventToAir("ON_INTERSTITIAL_FAILED_TO_LOAD",errorCodeToString(errorCode));
	}
	//----------------------------------------------------------------------------------------------
	@Override
	public void onAdClosed() {
		Extension.context.sendEventToAir("ON_INTERSTITIAL_CLOSED");
	}
	//----------------------------------------------------------------------------------------------
	@Override
	public void onAdLeftApplication() {
		Extension.context.sendEventToAir("ON_INTERSTITIAL_LEFT_APP");
	}	
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdOpened() {
    	Extension.context.sendEventToAir("ON_INTERSTITIAL_OPEN");
    }
	//----------------------------------------------------------------------------------------------	
	private String errorCodeToString(int errorCode) {
    	
		String errorString="";
		
        switch(errorCode)
        {     
	        case 0:
	        	errorString="ERROR_CODE_INTERNAL_ERROR";
	            break;
	        case 1:
	        	errorString="ERROR_CODE_INVALID_REQUEST";
	            break;
	        case 2:
	        	errorString="ERROR_CODE_NETWORK_ERROR";
	            break;	
	        case 3:
	        	errorString="ERROR_CODE_NO_FILL";
	            break;	            
        }

        return errorString;
	}	
	//----------------------------------------------------------------------------------------------
}
