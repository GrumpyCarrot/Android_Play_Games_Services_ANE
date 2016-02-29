package com.grumpycarrot.ane.playgameservices.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class LoadUriImageFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
        String uriString = null;

        try
        {
        	uriString = arg1[0].getAsString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Extension.context.loadImageFromUriString(uriString);

		return null;

    }

}
