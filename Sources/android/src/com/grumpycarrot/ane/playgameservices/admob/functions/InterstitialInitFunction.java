package com.grumpycarrot.ane.playgameservices.admob.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class InterstitialInitFunction implements FREFunction {

	@Override
	public FREObject call(FREContext context, FREObject[] args) {

		try {

			String adMobId	= args[0].getAsString();
			String deviceId	= args[1].getAsString();
			Boolean isTest	= args[2].getAsBool();

			Extension.context.interstitial.init(adMobId,deviceId,isTest);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
		
	}
}