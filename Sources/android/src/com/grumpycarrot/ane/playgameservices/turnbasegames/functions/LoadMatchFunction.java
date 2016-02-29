package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class LoadMatchFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		String matchId = null;

		try
		{
			matchId = arg1[0].getAsString();

		}
		catch (Exception e)
		{
			return null;
		}	
		
		Extension.context.turnBaseMulti.loadMatch(matchId);
		return null;
	
	}

}
