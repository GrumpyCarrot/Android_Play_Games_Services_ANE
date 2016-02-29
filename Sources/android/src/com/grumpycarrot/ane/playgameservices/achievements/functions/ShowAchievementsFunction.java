package com.grumpycarrot.ane.playgameservices.achievements.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ShowAchievementsFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

        Extension.context.achievements.showAchivements();
		return null;
	}

}
