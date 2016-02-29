package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class TakeTurnFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		String matchId = null;
		String nextParticipantId = null;
		String dataToSend = null;

		try
		{
			matchId = arg1[0].getAsString();
			nextParticipantId = arg1[1].getAsString();
			dataToSend = arg1[2].getAsString();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}		

		Extension.context.turnBaseMulti.takeTurn(matchId, nextParticipantId, dataToSend);
		return null;
	}

}
