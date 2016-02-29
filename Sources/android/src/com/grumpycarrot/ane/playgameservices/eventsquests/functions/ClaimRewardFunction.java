package com.grumpycarrot.ane.playgameservices.eventsquests.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ClaimRewardFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

        String questId;
        String milestoneId;

        try
        {
        	questId = arg1[0].getAsString();
        	milestoneId = arg1[1].getAsString();

        	Extension.context.eventsQuests.claimReward(questId, milestoneId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }           

		return null;

    }

}