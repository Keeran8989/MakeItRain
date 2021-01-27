package com.keeran.makeitrain

import android.content.Context
import com.google.gson.Gson


fun saveToPrefs(context: Context, storable: Storable) {
    val serialized = Gson().toJson(storable)
    val prefs = context.getSharedPreferences(
        "database",
        Context.MODE_PRIVATE
    )
    prefs.edit().putString("key", serialized).apply()
}