package com.github.emilg1101.challenges.set_five

import java.math.BigInteger
import kotlin.random.Random
import kotlin.random.asJavaRandom

infix fun BigInteger.gcd(second: BigInteger) = this.gcd(second)

infix fun BigInteger.lcm(second: BigInteger) = (this * second).abs() / (this gcd second)

infix fun BigInteger.modinv(second: BigInteger) = this.modInverse(second)

infix fun java.util.Random.probablePrime(len: Int) = BigInteger.probablePrime(len, this)

fun String.toBigInt() = BigInteger(this.toByteArray())

fun main() {
    val stringToEncode = "Cryptopals challenge 39".toBigInt()
    val rsa = RSA(1024)
    val encrypted = rsa.encrypt(stringToEncode)
    val decrypted = rsa.decrypt(encrypted)
    println(decrypted == stringToEncode)
}

class RSA(keyLength: Int) {

    private val random = Random.asJavaRandom()
    var e = BigInteger.valueOf(3)
        private set
    private var phi = ZERO
    private var p = ZERO
    private var q = ZERO
    var n = ZERO
        private set
    private var d = ZERO

    init {
        while (e gcd phi != ONE) {
            p = random probablePrime (keyLength / 2)
            q = random probablePrime (keyLength / 2)
            phi = (p - ONE) lcm (q - ONE)
            n = p * q
        }
        d = e modinv phi
    }

    fun encrypt(base: BigInteger): BigInteger = base.modPow(e, n)

    fun decrypt(base: BigInteger): BigInteger = base.modPow(d, n)
}
