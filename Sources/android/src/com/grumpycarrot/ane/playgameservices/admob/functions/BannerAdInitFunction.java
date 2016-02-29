package com.grumpycarrot.ane.playgameservices.admob.functions;


import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;


public class BannerAdInitFunction implements FREFunction {


	@Override
	public FREObject call(FREContext context, FREObject[] args) {

		try {
			
			String adMobId	= args[0].getAsString();
			String devId	= args[1].getAsString();
			Boolean test	= args[2].getAsBool();
			int bannerSize	= args[3].getAsInt();
			int bannerPosition	= args[4].getAsInt();

			Extension.context.banner.init(adMobId, devId, test, bannerSize, bannerPosition);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
		

	}
}