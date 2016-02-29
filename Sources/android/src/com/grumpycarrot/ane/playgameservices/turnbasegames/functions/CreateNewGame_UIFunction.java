package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class CreateNewGame_UIFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		int minPlayersToSelect = 1;
		int maxPlayersToSelect = 1;
		boolean allowAutomatch = true;

		try
		{
			minPlayersToSelect = arg1[0].getAsInt();
			maxPlayersToSelect = arg1[1].getAsInt();
			allowAutomatch = arg1[2].getAsBool();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		

		Extension.context.turnBaseMulti.selectOpponents(minPlayersToSelect,maxPlayersToSelect,allowAutomatch);
		return null;
	}

}
