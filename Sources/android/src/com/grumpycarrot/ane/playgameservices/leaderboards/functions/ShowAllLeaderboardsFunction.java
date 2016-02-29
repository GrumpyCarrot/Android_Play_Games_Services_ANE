package com.grumpycarrot.ane.playgameservices.leaderboards.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ShowAllLeaderboardsFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		Extension.context.leaderboards.showAllLeaderboards();
		return null;
	}

}
