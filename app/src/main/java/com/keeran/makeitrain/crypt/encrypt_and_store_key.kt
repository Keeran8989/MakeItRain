package com.keeran.makeitrain

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@RequiresApi(Build.VERSION_CODES.O)
fun persistRawKey(context: Context, userPasscode: CharArray) {
    val storable = toStorable(rawByteKey, userPasscode)

    saveToPrefs(context, storable)
}

/**
 * Returns a [Storable] instance with the db key encrypted using PBE.
 *
 * @param rawDbKey the raw database key
 * @param userPasscode the user's passcode
 * @return storable instance
 */
@RequiresApi(Build.VERSION_CODES.O)
fun toStorable(rawDbKey: ByteArray, userPasscode: CharArray): Storable {
    //val userPasscode = "1234".toCharArray()
    val salt = ByteArray(8).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong().nextBytes(this)
        } else {
            SecureRandom().nextBytes(this)
        }
    }
    val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val secret: SecretKey = generateSecretKey(userPasscode, salt)
    // Now encrypt the database key with PBE
    val iv = ByteArray(cipher.blockSize)
    val ivParameterSpec = IvParameterSpec(iv)
    cipher.init(Cipher.ENCRYPT_MODE, secret, ivParameterSpec)
    //cipher.init(Cipher.ENCRYPT_MODE, secret)

    //val params: AlgorithmParameters = cipher.parameters
    //val iv: ByteArray = params.getParameterSpec(GCMParameterSpec::class.java).iv
    val ciphertext: ByteArray = cipher.doFinal(rawDbKey)
    // Return the IV and CipherText which can be stored to disk
    return Storable(
        Base64.getEncoder().encodeToString(iv),
        Base64.getEncoder().encodeToString(ciphertext),
        Base64.getEncoder().encodeToString(salt)
    )
}

fun generateSecretKey(passcode: CharArray, salt: ByteArray): SecretKey {
    // Initialize PBE with password
    //val passcode = "1234".toCharArray()
    val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val spec: KeySpec = PBEKeySpec(passcode, salt, 65536, 256)
    val tmp: SecretKey = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}