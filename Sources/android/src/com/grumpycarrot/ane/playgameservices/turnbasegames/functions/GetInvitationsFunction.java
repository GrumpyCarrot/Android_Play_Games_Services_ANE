package com.grumpycarrot.ane.playgameservices.turnbasegames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class GetInvitationsFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		int invitationSortOrder;

		try
		{
			invitationSortOrder = arg1[0].getAsInt();
		}
		catch (Exception e)
		{
			return null;
		}			

		Extension.context.turnBaseMulti.loadInvitations(invitationSortOrder);
		return null;
	}

}
