package com.grumpycarrot.ane.playgameservices.savedgames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class WriteGameFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		String uniqueName = null; 
		String newData = null; 
		String newDescription = null;
		long newPlayedTimeMillis; 
		long newProgressValue;

		try
		{
			uniqueName = arg1[0].getAsString();
			newData = arg1[1].getAsString();
			newDescription = arg1[2].getAsString();
			newPlayedTimeMillis = arg1[3].getAsInt();
			newProgressValue = arg1[4].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.savedGames.writeGame(uniqueName, newData, newDescription, newPlayedTimeMillis, newProgressValue);
		
		return null;
	}

}
