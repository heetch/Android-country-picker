Android country picker
====================

## Features
This country picker is a simple dialog window presenting a list of countries
translated in the user's phone language. It return an object containing the flag
resource, the iso and dialing code of the selected country.
See the example to see more detail.


<img src="https://raw.githubusercontent.com/heetch/Android-country-picker/master/screenshots/screenshot_1.png" width="250">

## How to use

To show CountryPicker as a dialog:

```java
CountryPickerDialog countryPicker =
    new CountryPickerDialog(mContext, new CountryPickerCallbacks() {
        @Override
        public void onCountrySelected(Country country, int flagResId) {
            // TODO handle callback
        }
    });
countryPicker.show();
```

If you want to retrieve a flag res outside of the country picker dialog
use `Utils.getMipmapResId(Context context, String drawableName)` where
the drawable have to be formatted as follow: `countryIsoCodeInLowerCase_flag`.
For example:
    `Utils.getMipmapResId(mContext, "fr_flag")`
