package com.viewpagerindicator.view;

import java.lang.ref.SoftReference;
import java.util.Hashtable;

import com.viewpagerindicator.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CustomFontHelper {
	public static final String TAG = "CustomFontHelper";

	public static void setCustomFont(View textViewOrButton, Context ctx, AttributeSet attrs, int[] attributeSet, int fontId, int typeFaceId) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, attributeSet);
        String customFont = a.getString(fontId);
        String customTypeFace = a.getString(typeFaceId);
        setCustomFont(textViewOrButton, ctx, customFont, customTypeFace);
        a.recycle();
    }
	
	public static boolean setCustomFont(View textViewOrButton, Context ctx, String asset, String typeFace) {
		if(TextUtils.isEmpty(asset))
			return false;
		Typeface tf = null;
		try {
			tf = getFont(ctx, asset);
			String bold = ctx.getResources().getString(R.string.typeface_bold);
			if(textViewOrButton instanceof CustomFontView) {
				CustomFontView cfv = (CustomFontView) textViewOrButton;
				cfv.setTypeface(tf, (typeFace != null && typeFace.equals(bold)) ? Typeface.BOLD : Typeface.NORMAL);
			}
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + asset, e);
			return false;
		}

		return true;
	}
	
	private static final Hashtable<String, SoftReference<Typeface>> fontCache = new Hashtable<String, SoftReference<Typeface>>();

	public static Typeface getFont(Context c, String name) {
		synchronized (fontCache) {
			if(fontCache.get(name) != null) {
				SoftReference<Typeface> ref = fontCache.get(name);
				if(ref.get() != null) {
					return ref.get();
				}
			}

			Typeface typeface = Typeface.createFromAsset(c.getAssets(), "fonts/" + name);
			fontCache.put(name, new SoftReference<Typeface>(typeface));

			return typeface;
		}
	}
}
