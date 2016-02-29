package com.grumpycarrot.ane.playgameservices.savedgames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class DeleteGameFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		String gameName = null;

		try
		{
			gameName = arg1[0].getAsString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.savedGames.deleteGame(gameName);
		
		return null;
	}

}
