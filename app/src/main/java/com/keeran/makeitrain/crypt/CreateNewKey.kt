package com.keeran.makeitrain

lateinit var rawByteKey: ByteArray
var dbCharKey: CharArray? = null

/**
 * Generates a new database key.
 */
fun createNewKey() {

    // This is the raw key that we'll be encrypting + storing
    rawByteKey = generateRandomKey()
    // This is the key that will be used by Room
    dbCharKey = rawByteKey.toHex().toCharArray()
}