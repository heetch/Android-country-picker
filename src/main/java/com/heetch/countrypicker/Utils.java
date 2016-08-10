package com.heetch.countrypicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {

    public static int getMipmapResId(Context context, String drawableName) {
        return context.getResources().getIdentifier(
                drawableName.toLowerCase(Locale.ENGLISH), "mipmap", context.getPackageName());
    }

    public static int getRoundMipmapResId(Context context, String drawableName) {
        return getMipmapResId(context, drawableName + "_r");
    }


    public static JSONObject getCountriesJSON(Context context) {
        String resourceName = "countries_dialing_code";
        int resourceId = context.getResources().getIdentifier(
                resourceName, "raw", context.getApplicationContext().getPackageName());

        if (resourceId == 0)
            return null;

        InputStream stream = context.getResources().openRawResource(resourceId);

        try {
            return new JSONObject(convertStreamToString(stream));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Country> parseCountries(JSONObject jsonCountries) {
        List<Country> countries = new ArrayList<>();
        Iterator<String> iter = jsonCountries.keys();

        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String value = (String) jsonCountries.get(key);
                countries.add(new Country(key, value));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return countries;
    }

    public static Map<String,String> getCountryAndIsoHashMap(Context context) {
        Map<String,String> map = new HashMap<>();

        List<Country> countries = Utils.parseCountries(Utils.getCountriesJSON(context));

        for(Country c : countries) {
            map.put(c.getCountryName(), c.getIsoCode());
        }

        return map;
    }

    public static String getCountryCodeFromName(Context context, String countryName) {
        Map<String, String> countriesMap = getCountryAndIsoHashMap(context);

        String code = countriesMap.get(countryName);

        return code == null ? null : code.toLowerCase();
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static Bitmap getCircleCroppedBitmap(Bitmap bitmap, float radius) {
        int targetWidth = (int) radius;
        int targetHeight = (int) radius;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        int tWidth = bitmap.getWidth()/2;
        int tHeight = bitmap.getHeight()/2;
        canvas.drawBitmap(bitmap,
                new Rect(tWidth - (int)radius/2, tHeight - (int)radius/2,
                        tWidth + (int)radius/2, tHeight + (int)radius/2),
                new Rect(0, 0, targetWidth,
                        targetHeight), null);
        return targetBitmap;
    }


    public static Bitmap getCircleCroppedBitmap(Context mContext, int resId, float radius) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
        return getCircleCroppedBitmap(bitmap,radius);
    }

    public static float dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
