package com.grumpycarrot.ane.playgameservices.achievements.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class IncrementAchievementFunction implements FREFunction {

	public IncrementAchievementFunction() {}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		String achievementId = null;
		int numSteps;

		try
		{
			achievementId = arg1[0].getAsString();
			numSteps = arg1[1].getAsInt();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.achievements.incrementAchivement(achievementId, numSteps);

		return null;
	}

}
