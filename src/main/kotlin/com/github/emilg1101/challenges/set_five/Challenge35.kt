package com.github.emilg1101.challenges.set_five

import com.github.emilg1101.challenges.util.hash
import com.github.emilg1101.challenges.util.toHex
import java.math.BigInteger

val ZERO
    get() = BigInteger.valueOf(0L)

val ONE
    get() = BigInteger.valueOf(1L)

val TWO
    get() = BigInteger.valueOf(2L)

fun main() {
    val p = BigInteger(
        "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74" +
                "020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f1437" +
                "4fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7ed" +
                "ee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf05" +
                "98da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb" +
                "9ed529077096966d670c354e4abc9804f1746c08ca237327ffffffffffffffff", 16
    )
    for ((g, sk) in listOf(ONE to ONE, p to ZERO, (p - ONE) to (p - ONE))) {
        val aliceDH = DiffieHellman(p, g)
        val bobDH = DiffieHellman(p, g)

        val alicePublicKey = aliceDH.publicKeyGen()
        val bobPublicKey = bobDH.publicKeyGen()

        val aliceSharedKey = aliceDH.sharedKeyGen(bobPublicKey)
        val bobSharedKey = bobDH.sharedKeyGen(alicePublicKey)

        val msg = "Hello!".toByteArray()

        val iv = generateRandomArray()
        val msgFromAliceEncrypted = aesEncrypt(aliceSharedKey.hash(), iv, msg) + iv

        var eKey = sk.hash()

        var eMsg: ByteArray

        try {
            eMsg = aesDecrypt(eKey, iv, msgFromAliceEncrypted.msg())
        } catch (e: Exception) {
            val _sk = modPow(p - ONE, TWO, p)
            eKey = _sk.hash()
            eMsg = aesDecrypt(eKey, iv, msgFromAliceEncrypted.msg())
        }

        if (eMsg.toHex() != msg.toHex()) {
            println("Intercepted Traffic Incorrectly Decrypted")
        }

        val ivBob = generateRandomArray()
        val bMsg = aesDecrypt(bobSharedKey.hash(), iv, msgFromAliceEncrypted.msg())
        val msgFromBobEncrypted = aesEncrypt(bobSharedKey.hash(), ivBob, bMsg) + ivBob

        eMsg = aesDecrypt(eKey, ivBob, msgFromBobEncrypted.msg())

        if (eMsg.toHex() != bMsg.toHex()) {
            println("Intercepted Traffic Incorrectly Decrypted")
        }
    }
}