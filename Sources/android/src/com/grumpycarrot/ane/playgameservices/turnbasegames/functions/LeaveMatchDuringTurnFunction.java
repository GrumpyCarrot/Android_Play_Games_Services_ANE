package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class LeaveMatchDuringTurnFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		String matchId = null;
		String pendingParticipantId = null;

		try
		{
			matchId = arg1[0].getAsString();
			pendingParticipantId = arg1[1].getAsString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}	
		
		Extension.context.turnBaseMulti.leaveMatchDuringTurn(matchId, pendingParticipantId);
		return null;
	}

}
