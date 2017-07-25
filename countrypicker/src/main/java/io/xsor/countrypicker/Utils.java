package io.xsor.countrypicker;

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
import android.graphics.RectF;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {

  // Crop radius
  private static int CROP_RADIUS = dpToPx(15);

  public static int getFlagResId(Context context, String drawableName) {
    int resId = context.getResources().getIdentifier(
        drawableName.toLowerCase(Locale.ENGLISH) + "_flag", "mipmap", context.getPackageName());
    if (resId != 0) {
      return resId;
    } else {
      return  R.mipmap.unknown_flag;
    }
  }

  public static JSONObject getCountriesJSON(Context context) {
    String resourceName = "countries_dialing_code";
    int resourceId = context.getResources().getIdentifier(
        resourceName, "raw", context.getApplicationContext().getPackageName());

    if (resourceId == 0) {
      return null;
    }

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

  public static Map<String, String> getCountryAndIsoHashMap(Context context) {
    Map<String, String> map = new HashMap<>();

    List<Country> countries = Utils
        .parseCountries(Utils.getCountriesJSON(context));

    for (Country c : countries) {
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


  public static Bitmap getCircleCroppedBitmap(Bitmap bitmap) {

    int tWidth = bitmap.getWidth();
    int tHeight = bitmap.getHeight();

    int targetSize = Math.min(tWidth, tHeight);

    int targetWidth = CROP_RADIUS * 2;
    int targetHeight = CROP_RADIUS * 2;

    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
        targetHeight, Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(targetBitmap);

    Path path = new Path();
    path.addCircle(targetWidth / 2,
        targetHeight / 2,
        CROP_RADIUS,
        Path.Direction.CW);

    canvas.clipPath(path);

    Paint mPaint = new Paint();
    mPaint.setAntiAlias(true);

    tWidth = bitmap.getWidth() / 2;
    tHeight = bitmap.getHeight() / 2;

    canvas.drawBitmap(bitmap,
        new Rect(tWidth - CROP_RADIUS, tHeight - CROP_RADIUS,
            tWidth + CROP_RADIUS, tHeight + CROP_RADIUS),
        new Rect(0, 0,
            targetWidth, targetHeight),
        mPaint);

    bitmap.recycle();

    return targetBitmap;
  }

  public static RoundedBitmapDrawable getRoundedDrawable(Context c, int resId) {
    RoundedBitmapDrawable img = RoundedBitmapDrawableFactory
        .create(c.getResources(), BitmapFactory.decodeResource(c.getResources(), resId));
    img.setCornerRadius(Math.min(img.getMinimumWidth(), img.getMinimumHeight()) / 2.0f);
    return img;
  }

  public static Bitmap getCircleCroppedBitmap(Context c, int resId) {
    return getCircleCroppedBitmap(BitmapFactory.decodeResource(c.getResources(), resId));
  }


  public static int dpToPx(int dp) {
    return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
  }
}
