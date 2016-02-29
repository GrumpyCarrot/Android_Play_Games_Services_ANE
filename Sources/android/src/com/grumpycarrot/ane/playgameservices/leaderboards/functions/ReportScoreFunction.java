package com.grumpycarrot.ane.playgameservices.leaderboards.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ReportScoreFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		String leaderboardId = null;
		int newScore = 0;
		try
		{
			leaderboardId = arg1[0].getAsString();
			newScore = arg1[1].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		Extension.context.leaderboards.reportScore(leaderboardId, newScore);
		
		return null;
	}

}
