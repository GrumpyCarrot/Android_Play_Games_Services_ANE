package com.grumpycarrot.ane.playgameservices.leaderboards.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ShowLeaderboardFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		String leaderboardId = null;
		try
		{
			leaderboardId = arg1[0].getAsString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.leaderboards.showLeaderboard(leaderboardId);
		return null;
	}

}
