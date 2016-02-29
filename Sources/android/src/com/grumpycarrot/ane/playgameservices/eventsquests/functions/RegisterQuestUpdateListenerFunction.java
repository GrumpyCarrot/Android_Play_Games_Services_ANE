package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class RegisterQuestUpdateListenerFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

    	Extension.context.eventsQuests.registerQuestUpdateListener();

		return null;

    }

}