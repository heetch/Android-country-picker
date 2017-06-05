Android country picker
====================

## Features
This country picker is a simple dialog window presenting a list of countries
translated in the user's phone language. It return an object containing the flag
resource, the iso and dialing code of the selected country.
See the example to see more detail.


<img src="https://raw.githubusercontent.com/heetch/Android-country-picker/master/screenshots/screenshot_1.png" width="250">

## How to install

`compile 'io.xsor:countrypicker:0.3'`

## How to use

See [sample app main activity](https://github.com/xsorifc28/CountryPicker/blob/master/sample/src/main/java/io/xsor/countrypickersample/MainActivity.java).
```

If you want to retrieve a flag res outside of the country picker dialog
use `Utils.getMipmapResId(Context context, String drawableName)` where
the drawable have to be formatted as follow: `countryIsoCodeInLowerCase`.
For example:
    `Utils.getMipmapResId(mContext, "fr")`
