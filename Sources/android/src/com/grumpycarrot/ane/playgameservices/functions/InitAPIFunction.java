package com.grumpycarrot.ane.playgameservices.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class InitAPIFunction implements FREFunction {
	

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		boolean enableSavedGames;
		boolean enableTurnBaseMulti;
		boolean connectOnStart;
		int maxAutoSignInAttempts;
		boolean showPopUps;
		
		try
		{
			enableSavedGames = arg1[0].getAsBool();
			enableTurnBaseMulti = arg1[1].getAsBool();
			connectOnStart = arg1[2].getAsBool();
			maxAutoSignInAttempts = arg1[3].getAsInt();
			showPopUps = arg1[4].getAsBool();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}	
		
		Extension.context.initAPI(enableSavedGames, enableTurnBaseMulti,connectOnStart,maxAutoSignInAttempts,showPopUps);
		return null;		
	}

}
