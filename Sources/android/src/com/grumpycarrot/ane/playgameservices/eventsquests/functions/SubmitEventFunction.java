package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class SubmitEventFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

        String eventId = null;
        int incrementAmount;

        try
        {
        	eventId = arg1[0].getAsString();
        	incrementAmount = arg1[1].getAsInt();
        	Extension.context.eventsQuests.submitEvent(eventId,incrementAmount);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }           

		return null;

    }

}