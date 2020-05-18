package com.github.emilg1101.challenges.set_six

import com.github.emilg1101.challenges.set_five.RSA
import com.github.emilg1101.challenges.set_five.cbrt
import com.github.emilg1101.challenges.set_five.toBigInt
import com.github.emilg1101.challenges.util.hash
import com.github.emilg1101.challenges.util.toBigInt
import java.math.BigInteger

val ASN_SHA1 = byteArrayOf(30, 21, 30, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 14).toBigInt()

operator fun Int.plus(a: BigInteger) =
    a.toByteArray().toMutableList().also { it.add(0, this.toByte()) }.toByteArray().toBigInt()

class RSADigitalSignature(private val rsa: RSA) {

    fun verify(encryptedSignature: BigInteger, message: BigInteger): Boolean {
        val signature = 0x00 + rsa.encrypt(encryptedSignature)
        val r = Regex("0001ff+?00.{15}.{20}]", RegexOption.DOT_MATCHES_ALL)

        if (!r.matches(signature.toString(16))) {
            return false
        }
        val firstMatched = r.matchEntire(signature.toString(16))!!.groupValues.first()
        val firstMatchedBigInt = BigInteger(firstMatched, 16)
        return firstMatchedBigInt == message.toByteArray().toBigInt()
    }
}

fun forgeSignature(message: BigInteger, keyLength: Int): BigInteger {
    var block = byteArrayOf(0, 1, (255).toByte(), 0).toBigInt().shiftLeft(ASN_SHA1.bitCount()) + ASN_SHA1
    block = block.shiftLeft(message.bitCount()) + message
    val garbageZeros = BigInteger("00".repeat(((keyLength - block.bitCount() + 7) / 8)), 16)
    block = block.shiftLeft(garbageZeros.bitCount()) + garbageZeros
    return block.cbrt()
}

fun main() {
    val message = "Cryptopals challenge 42".toBigInt()
    val forgedSignature = forgeSignature(message, 1024)
    println(RSADigitalSignature(RSA(1024)).verify(forgedSignature, message))
}
