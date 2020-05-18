package com.github.emilg1101.challenges.util

import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

fun BigInteger.hash(): ByteArray {
    var digest = ByteArray(0)
    try {
        val m = MessageDigest.getInstance("SHA-1")
        val x = toByteArray()
        m.update(x)
        digest = m.digest()
    } catch (e: Exception) {
        e.printStackTrace(System.err)
    }
    return digest.take(16).toByteArray()
}

fun ByteArray.toHex() = BigInteger(this).toString(16)

fun ByteArray.toBigInt() = BigInteger(this)

fun String.toBase64() = Base64.getDecoder().decode(this)