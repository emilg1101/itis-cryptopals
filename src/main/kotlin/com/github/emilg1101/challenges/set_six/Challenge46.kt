package com.github.emilg1101.challenges.set_six

import com.github.emilg1101.challenges.set_five.*
import com.github.emilg1101.challenges.util.toBase64
import com.github.emilg1101.challenges.util.toBigInt
import java.math.BigDecimal
import java.math.BigInteger

class RSAParityOracle(private val rsa: RSA) : Encrypter by rsa, Decrypter by rsa {

    val publicKey: Pair<BigInteger, BigInteger>
        get() = rsa.e to rsa.n

    fun isOddParity(encryptedData: BigInteger): Boolean {
        return encryptedData.modPow(rsa.e, rsa.n).mod(TWO) != ZERO
    }
}

fun parityOracleAttack(cipherText: BigInteger, rsaParity: RSAParityOracle): BigInteger {
    val (e, n) = rsaParity.publicKey
    val multiplier = TWO.modPow(e, n)

    var lowerBound = BigDecimal(0)
    var upperBound = BigDecimal(n)
    var _cipherText = cipherText
    while (lowerBound < upperBound - BigDecimal.ONE) {
        _cipherText = (_cipherText * multiplier) % n

        val mid = (lowerBound + upperBound) / (BigDecimal(2))

        if (rsaParity.isOddParity(_cipherText)) {
            lowerBound = mid
        } else {
            upperBound = mid
        }
    }
    return upperBound.toBigInteger()
}

fun main() {
    val message =
        "VGhhdCdzIHdoeSBJIGZvdW5kIHlvdSBkb24ndCBwbGF5IGFyb3VuZCB3aXRoIHRoZSBGdW5reSBDb2xkIE1lZGluYQ==".toBase64()
            .toBigInt()
    val rsaParity = RSAParityOracle(RSA(1024))
    val cipherText = rsaParity.encrypt(message)
    val plainText = parityOracleAttack(cipherText, rsaParity)

    println(plainText == message)
}
