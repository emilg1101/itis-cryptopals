package com.github.emilg1101.challenges.set_six

import com.github.emilg1101.challenges.set_five.*
import java.math.BigInteger
import java.util.*

infix fun BigInteger.mod(a: BigInteger) = this.mod(a)

fun randint(min: BigInteger, max: BigInteger): BigInteger {
    val a = max.subtract(min)
    var res = BigInteger(max.bitLength(), Random())
    if (res < min) res = res.add(min)
    if (res >= a) res = res.mod(a).add(min)
    return res
}

fun modexp(base: BigInteger, exponent: BigInteger, module: BigInteger) = base.modPow(exponent, module)

fun main() {
    val plaintext = "Cryptopals challenge 41".toBigInt()
    val rsa = RSA(1024)
    val rsaServer = RSAServer(rsa)

    val result = attack(rsa.encrypt(plaintext), rsaServer)
    println(result == plaintext)
}

fun attack(ciphertext: BigInteger, rsaServer: RSAServer): BigInteger {
    val e = rsaServer.publicKey.first
    val n = rsaServer.publicKey.second
    var s: BigInteger

    while (true) {
        s = randint(TWO, n - ONE)
        if (s mod n > ONE) break
    }
    val r = (modexp(s, e, n) * ciphertext) mod n
    val newMessage = rsaServer.decrypt(r)
    return (newMessage * (s modinv n)) mod n
}

class RSAServer(private val rsa: RSA) {

    val messages = mutableSetOf<BigInteger>()
    val publicKey = rsa.e to rsa.n

    fun decrypt(msg: BigInteger) = rsa.decrypt(msg).also { messages.add(msg) }
}
