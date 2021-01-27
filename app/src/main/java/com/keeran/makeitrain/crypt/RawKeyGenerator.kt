package com.keeran.makeitrain

import android.os.Build
import java.security.SecureRandom


fun generateRandomKey(): ByteArray =
    ByteArray(32).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong().nextBytes(this)
        } else {
            SecureRandom().nextBytes(this)
        }
    }
