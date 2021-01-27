package com.keeran.makeitrain

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

@RequiresApi(Build.VERSION_CODES.O)
fun getRawByteKey(passcode: CharArray, storable: Storable): ByteArray {
    //val passcode = "1234".toCharArray()
    val aesWrappedKey = Base64.getDecoder().decode(storable.key)
    val iv = Base64.getDecoder().decode(storable.iv)
    val salt = Base64.getDecoder().decode(storable.salt)
    val secret: SecretKey = generateSecretKey(passcode, salt)
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
    return cipher.doFinal(aesWrappedKey)
}
