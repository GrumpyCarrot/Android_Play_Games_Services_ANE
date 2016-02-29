package com.grumpycarrot.ane.playgameservices.achievements.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class UnlockAchievementFunction implements FREFunction {

	public UnlockAchievementFunction() {}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		String achievementId = null;

		try
		{
			achievementId = arg1[0].getAsString();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.achievements.unlockAchivement(achievementId);


		return null;
	}

}
