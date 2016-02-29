package com.grumpycarrot.ane.playgameservices.savedgames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class OpenGameFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		String openGameName;
		int conflictPolicy;

		try
		{
			openGameName = arg1[0].getAsString();
			conflictPolicy  = arg1[1].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.savedGames.openSnapshot(openGameName,conflictPolicy);
		
		return null;
	}

}
