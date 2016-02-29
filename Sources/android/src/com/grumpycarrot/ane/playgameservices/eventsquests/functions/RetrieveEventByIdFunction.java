package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class RetrieveEventByIdFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

        String eventId;
        boolean forceReload;

        try
        {
        	eventId = arg1[0].getAsString();
        	forceReload = arg1[1].getAsBool();

        	Extension.context.eventsQuests.retrieveEventById(eventId,forceReload);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }           

		return null;

    }

}