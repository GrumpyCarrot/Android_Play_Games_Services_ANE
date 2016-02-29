package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class LoadQuestsFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

    	FREArray mFREArray;

        try
        {
        	mFREArray= (FREArray) arg1[0];

        	Extension.context.eventsQuests.loadQuests(Extension.context.FREArraytoIntArray(mFREArray));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }           

		return null;

    }

}