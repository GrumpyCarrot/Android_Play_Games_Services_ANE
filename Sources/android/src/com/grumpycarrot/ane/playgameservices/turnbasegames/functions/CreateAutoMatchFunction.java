package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class CreateAutoMatchFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		int minPlayersToSelect = 1;
		int maxPlayersToSelect = 1;

		try
		{
			minPlayersToSelect = arg1[0].getAsInt();
			maxPlayersToSelect = arg1[1].getAsInt();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		

		Extension.context.turnBaseMulti.createAutoMatch(minPlayersToSelect, maxPlayersToSelect);

		return null;
	}

}
