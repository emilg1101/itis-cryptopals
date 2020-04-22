package com.github.emilg1101.challenges.set_five

import java.math.BigInteger
import java.util.*

class DiffieHellman(private val p: BigInteger, private val g: BigInteger) {

    private val privateKey: BigInteger = BigInteger(1023, Random())

    fun publicKeyGen(): BigInteger {
        return modPow(g, privateKey, p)
    }

    fun sharedKeyGen(pubKey: BigInteger): BigInteger {
        return modPow(pubKey, privateKey, p)
    }

    private fun modPow(_a: BigInteger, _x: BigInteger, _p: BigInteger): BigInteger {
        var x = _x
        var a = _a
        var answer = BigInteger("1")
        val two = BigInteger("2")
        while (x > BigInteger.ZERO) {
            if (x.mod(two) == BigInteger.ONE) answer = answer.multiply(a).mod(_p)
            x = x.shiftRight(1)
            a = a.multiply(a).mod(_p)
        }
        return answer
    }
}

fun main() {
    val p = BigInteger(
        "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74" +
                "020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f1437" +
                "4fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7ed" +
                "ee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf05" +
                "98da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb" +
                "9ed529077096966d670c354e4abc9804f1746c08ca237327ffffffffffffffff", 16
    )
    val g = BigInteger("2", 16)
    val aliceDH = DiffieHellman(p, g)
    val bobDH = DiffieHellman(p, g)

    val alicePublicKey = aliceDH.publicKeyGen()
    val bobPublicKey = bobDH.publicKeyGen()

    val aliceSharedKey = aliceDH.sharedKeyGen(bobPublicKey)
    val bobSharedKey = bobDH.sharedKeyGen(alicePublicKey)

    println(aliceSharedKey == bobSharedKey) // true
}
