package com.grumpycarrot.ane.playgameservices.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.grumpycarrot.ane.playgameservices.Extension;

public class SignOutFunction implements FREFunction {

	public SignOutFunction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		Extension.context.signOut();
		return null;
	}

}
