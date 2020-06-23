package com.heetch.countrypicker

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream
import java.util.*

/**
 * Created by GODARD Tuatini on 07/05/15.
 */
object Utils {
    @JvmStatic
    fun getMipmapResId(context: Context, drawableName: String): Int {
        return context.resources.getIdentifier(
                drawableName.toLowerCase(Locale.ENGLISH), "mipmap", context.packageName)
    }

    @JvmStatic
    fun getCountriesJSON(context: Context): JSONObject? {
        val resourceName = "countries_dialing_code"
        val resourceId = context.resources.getIdentifier(
                resourceName, "raw", context.applicationContext.packageName)
        if (resourceId == 0) return null
        val stream = context.resources.openRawResource(resourceId)
        try {
            return JSONObject(convertStreamToString(stream))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun parseCountries(context: Context, jsonCountries: JSONObject): MutableList<Country> {
        val countries: MutableList<Country> = ArrayList()
        val iter = jsonCountries.keys()
        while (iter.hasNext()) {
            val key = iter.next()
            try {
                val value = jsonCountries[key] as String
                val name = Locale(context.getResources()
                        .configuration.locale.language, key).displayCountry
                countries.add(Country(name, key, value))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return countries
    }

    fun convertStreamToString(`is`: InputStream?): String {
        val s = Scanner(`is`).useDelimiter("\\A")
        return if (s.hasNext()) s.next() else ""
    }
}