//////////////////////////////////////////////////////////////////////////////////////
//
//  Created by GrumpyCarrot (http://www.grumpycarrot.com)
//  
//////////////////////////////////////////////////////////////////////////////////////
package com.grumpycarrot.ane.playgameservices.admob;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;
import com.grumpycarrot.ane.playgameservices.Extension;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.FrameLayout.LayoutParams;

public class Banner extends AdListener{

	private AdView adView;
	private RelativeLayout mAdLayout;
	private Boolean mIsTest;
	private String mDeviceId;	

	public Banner() {}
	//----------------------------------------------------------------------------------------------	
	public void dispose() {

		mAdLayout.removeView(adView);
		adView.destroy();
		adView = null;
		mAdLayout = null;
	}
	//----------------------------------------------------------------------------------------------	
	public void init(String adMobId, String devId, Boolean test,int bannerSize,int position) {

		mIsTest=test;
		mDeviceId=devId;

	    adView = new AdView(Extension.context.getActivity());
	    adView.setAdUnitId(adMobId);
	    
	    createLayout(position);	

	    switch(bannerSize) {
	    	case 0: adView.setAdSize(AdSize.BANNER); break; 	
	    	case 1: adView.setAdSize(AdSize.FLUID); break;
	    	case 2: adView.setAdSize(AdSize.FULL_BANNER); break; 
	    	case 3: adView.setAdSize(AdSize.LARGE_BANNER); break; 
	    	case 4: adView.setAdSize(AdSize.LEADERBOARD); break; 
	    	case 5: adView.setAdSize(AdSize.MEDIUM_RECTANGLE); break; 
	    	case 6: adView.setAdSize(AdSize.SMART_BANNER); break; 
	    }

    	if (android.os.Build.VERSION.SDK_INT >= 11) {
    		adView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    	}	    
	 
	    adView.setAdListener(this);	 
	    adView.setVisibility(View.GONE);

		

	}
	//----------------------------------------------------------------------------------------------
	private void createLayout(int position) {

		if(mAdLayout == null)
		{
		    mAdLayout = new RelativeLayout(Extension.context.getActivity());
	    	ViewGroup fl = (ViewGroup)Extension.context.getActivity().findViewById(android.R.id.content);
			fl = (ViewGroup)fl.getChildAt(0);
			fl.addView(mAdLayout, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));	
		}
		
		
		
		RelativeLayout.LayoutParams layoutParams= getRelativeParams(position);	
	    mAdLayout.addView(adView, layoutParams);
	    
	    Extension.logEvent("createLayout FINSIHED");
	}	
	//----------------------------------------------------------------------------------------------	
	private RelativeLayout.LayoutParams getRelativeParams(int position) {
    	
		// Define instances
        int firstVerb= RelativeLayout.ALIGN_PARENT_TOP;
        int secondVerb= RelativeLayout.CENTER_HORIZONTAL;
        int anchor = RelativeLayout.TRUE;
		RelativeLayout.LayoutParams params;
		
		// Create the Parameters
		params = new RelativeLayout.LayoutParams (-2, -2);
		
        switch(position)
        {     
	        case 0: // TOP_LEFT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_TOP;
	        	secondVerb	= RelativeLayout.ALIGN_PARENT_LEFT;
	            break;
	        case 1: // TOP_CENTER
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_TOP;
	        	secondVerb	= RelativeLayout.CENTER_HORIZONTAL;
	            break;
	        case 2: // TOP_RIGHT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_TOP;
	        	secondVerb	= RelativeLayout.ALIGN_PARENT_RIGHT;
	            break;
	        case 3: // MIDDLE_LEFT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_LEFT;
	        	secondVerb	= RelativeLayout.CENTER_VERTICAL;
	            break;
	        case 4: // MIDDLE_CENTER
	        	firstVerb	= RelativeLayout.CENTER_HORIZONTAL;
	        	secondVerb	= RelativeLayout.CENTER_VERTICAL;
	            break;
	        case 5: // MIDDLE_RIGHT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_RIGHT;
	        	secondVerb	= RelativeLayout.CENTER_VERTICAL;
	            break;
	        case 6: // BOTTOM_LEFT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_LEFT;
	        	secondVerb	= RelativeLayout.ALIGN_PARENT_BOTTOM;
	            break;
	        case 7: // BOTTOM_CENTER
	        	firstVerb	= RelativeLayout.CENTER_HORIZONTAL;
	        	secondVerb	= RelativeLayout.ALIGN_PARENT_BOTTOM;
	            break;
	        case 8: // BOTTOM_RIGHT
	        	firstVerb	= RelativeLayout.ALIGN_PARENT_RIGHT;
	        	secondVerb	= RelativeLayout.ALIGN_PARENT_BOTTOM;
	            break;
        }

    	params.addRule(firstVerb,anchor);
    	params.addRule(secondVerb,anchor);
        return params;
	}	
	
	//----------------------------------------------------------------------------------------------	
	public void loadBannerAd() {
		
		AdRequest adRequest;
		
		if(mIsTest) { 

			adRequest = new AdRequest.Builder()
			.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
			.addTestDevice(mDeviceId)
			.build();
		} else {
			adRequest = new AdRequest.Builder().build();
		}

		adView.loadAd(adRequest);
		
	}
	//----------------------------------------------------------------------------------------------	
	public void remove() {

		if(mAdLayout != null)
		{
			mAdLayout.removeView(adView);
			adView.destroy();
			adView = null;
		}
	}	
	//----------------------------------------------------------------------------------------------	
	public void show() {

		adView.setVisibility(View.VISIBLE);
		adView.resume();
	}
	
	//----------------------------------------------------------------------------------------------	
	public void hide() {

		adView.setVisibility(View.GONE);
		adView.pause();
	}
	//----------------------------------------------------------------------------------------------	
	public Boolean isVisible() {

		return adView.isShown();			
	}
	//----------------------------------------------------------------------------------------------	
	public Boolean isActivated() {

		return adView.isActivated();
	}
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdLoaded() {           	
    	Extension.context.sendEventToAir("ON_BANNER_LOADED");
    }
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdFailedToLoad(int error) {
    	Extension.context.sendEventToAir("ON_BANNER_FAILED_TO_LOAD",errorCodeToString(error));
    }
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdOpened() {
    	Extension.context.sendEventToAir("ON_BANNER_OPENED");
    }
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdClosed() {
    	Extension.context.sendEventToAir("ON_BANNER_CLOSED");
    }
	//----------------------------------------------------------------------------------------------
    @Override
    public void onAdLeftApplication() {
    	Extension.context.sendEventToAir("ON_BANNER_LEFT_APP");
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
