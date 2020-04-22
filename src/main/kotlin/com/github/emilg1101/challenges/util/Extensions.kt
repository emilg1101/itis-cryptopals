package com.github.emilg1101.challenges.util

import java.math.BigInteger
import java.security.MessageDigest

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
