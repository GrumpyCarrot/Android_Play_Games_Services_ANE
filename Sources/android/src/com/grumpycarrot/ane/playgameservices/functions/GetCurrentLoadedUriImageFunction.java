package com.grumpycarrot.ane.playgameservices.functions;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.adobe.fre.FREASErrorException;
import com.adobe.fre.FREBitmapData;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.grumpycarrot.ane.playgameservices.Extension;

public class GetCurrentLoadedUriImageFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {

		Extension.logEvent("GetCurrentLoadedUriImageFunction");
		Bitmap bitmap=Extension.context.currentLoadedUriImage;
		
		if(bitmap==null) return null;

		try {
			
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int[] pixels = new int[width * height];
			boolean srcAlpha = bitmap.hasAlpha();

			bitmap.getPixels(pixels, 0, width, 0, 0, width, height);


		    for(int i = 0; i < pixels.length; i++) {
		        int red = Color.red(pixels[i]);
		        int green = Color.green(pixels[i]);
		        int blue = Color.blue(pixels[i]);
		        int alpha=Color.alpha(pixels[i]);
		        
		        if(srcAlpha) pixels[i] =Color.argb(alpha, red, green, blue);
		        else pixels[i] = Color.rgb(blue, green, red);
		    }
		
		    bitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);	

		    Byte[] fillcolor = {0, 0, 0, 0};
		    FREBitmapData resImgData =FREBitmapData.newBitmapData(bitmap.getWidth(), bitmap.getHeight(),srcAlpha, fillcolor);
		    resImgData.acquire();
		    bitmap.copyPixelsToBuffer(resImgData.getBits());
		    resImgData.release();
		
		    bitmap.recycle();
		    Extension.context.currentLoadedUriImage.recycle();
		    Extension.context.currentLoadedUriImage=null;

		    return resImgData;
		    
		} catch (FREWrongThreadException e) {
			return null;
		} catch (FREInvalidObjectException e) {
			return null;
		} catch (FREASErrorException e) {
			return null;
		}

    }
	//-------------------------------------------------------------------
}
