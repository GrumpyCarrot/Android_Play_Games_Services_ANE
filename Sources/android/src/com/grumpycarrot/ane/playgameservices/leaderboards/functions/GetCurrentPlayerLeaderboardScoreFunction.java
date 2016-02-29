package com.grumpycarrot.ane.playgameservices.leaderboards.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class GetCurrentPlayerLeaderboardScoreFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {

        String leaderboardId = null;
        int span;
        int leaderboardCollection;

        try
        {
            leaderboardId = arg1[0].getAsString();
            span = arg1[1].getAsInt();
            leaderboardCollection = arg1[2].getAsInt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        if( leaderboardId != null )
            Extension.context.leaderboards.currentPlayerLeaderboardScore(leaderboardId, span, leaderboardCollection);

		return null;

    }

}