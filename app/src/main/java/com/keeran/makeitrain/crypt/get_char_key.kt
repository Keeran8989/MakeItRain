package com.keeran.makeitrain

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun getCharKey(passcode: CharArray, context: Context): CharArray {
    if (dbCharKey == null) {
        dbCharKey = initKey(passcode, context)
    }
    return dbCharKey ?: error("Failed to decrypt database key")
}

@RequiresApi(Build.VERSION_CODES.O)
private fun initKey(passcode: CharArray, context: Context): CharArray {
    val storable = getStorable(context)
    if (storable == null) {
        createNewKey()
        persistRawKey(context, passcode)
    } else {
        rawByteKey = getRawByteKey(passcode, storable)
        dbCharKey = rawByteKey.toHex().toCharArray()
    }
    return dbCharKey!!
}