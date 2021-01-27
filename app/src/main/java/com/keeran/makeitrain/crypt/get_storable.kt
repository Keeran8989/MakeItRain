package com.keeran.makeitrain

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken


fun getStorable(context: Context): Storable? {
    val prefs = context.getSharedPreferences(
        "database",
        Context.MODE_PRIVATE
    )
    val serialized = prefs.getString("key", null)
    if (serialized.isNullOrBlank()) {
        return null
    }
    return try {
        Gson().fromJson(
            serialized,
            object : TypeToken<Storable>() {}.type
        )

    } catch (ex: JsonSyntaxException) {
        null
    }
}
