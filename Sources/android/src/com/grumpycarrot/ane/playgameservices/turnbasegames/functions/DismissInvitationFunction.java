package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class DismissInvitationFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		String invitationId = null;

		try
		{
			invitationId = arg1[0].getAsString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}	
		
		Extension.context.turnBaseMulti.dismissInvitation(invitationId);
		return null;
	}

}
