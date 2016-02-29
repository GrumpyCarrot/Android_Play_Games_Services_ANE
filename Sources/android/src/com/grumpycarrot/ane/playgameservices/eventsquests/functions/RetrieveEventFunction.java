package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class RetrieveEventFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

        boolean forceReload;

        try
        {
        	forceReload = arg1[0].getAsBool();
        	Extension.context.eventsQuests.retrieveEvent(forceReload);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }           

		return null;

    }

}