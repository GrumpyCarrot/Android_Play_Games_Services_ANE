package com.grumpycarrot.ane.playgameservices.savedgames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class ShowSavedGame_UIFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		
		String title;
		boolean allowAddButton;
		boolean allowDelete;
		int maxSnapshotsToShow;

		try
		{
			title = arg1[0].getAsString();
			allowAddButton = arg1[1].getAsBool();
			allowDelete = arg1[2].getAsBool();
			maxSnapshotsToShow = arg1[3].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		Extension.context.savedGames.showSavedGamesUI(title, allowAddButton, allowDelete, maxSnapshotsToShow);
		
		return null;
	}

}
