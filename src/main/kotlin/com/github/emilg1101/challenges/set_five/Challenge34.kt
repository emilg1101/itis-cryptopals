package com.github.emilg1101.challenges.set_five

import com.github.emilg1101.challenges.util.hash
import com.github.emilg1101.challenges.util.toHex
import java.math.BigInteger
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private fun generateRandomArray(): ByteArray =
    ByteArray(16).apply {
        Random().nextBytes(this)
    }

private fun aesEncrypt(key: ByteArray, iv: ByteArray, msg: ByteArray): ByteArray =
    Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }.doFinal(msg)

private fun aesDecrypt(key: ByteArray, iv: ByteArray, msg: ByteArray): ByteArray =
    Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
        init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }.doFinal(msg)

private fun ByteArray.iv() = this.takeLast(16).toByteArray()

private fun ByteArray.msg() = this.dropLast(16).toByteArray()

fun main() {
    val p = BigInteger(
        "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024e088a67cc74" +
                "020bbea63b139b22514a08798e3404ddef9519b3cd3a431b302b0a6df25f1437" +
                "4fe1356d6d51c245e485b576625e7ec6f44c42e9a637ed6b0bff5cb6f406b7ed" +
                "ee386bfb5a899fa5ae9f24117c4b1fe649286651ece45b3dc2007cb8a163bf05" +
                "98da48361c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552bb" +
                "9ed529077096966d670c354e4abc9804f1746c08ca237327ffffffffffffffff", 16
    )
    val g = BigInteger.valueOf(2L)

    val aliceDH = DiffieHellman(p, g)
    val bobDH = DiffieHellman(p, g)

    // val alicePublicKey = aliceDH.publicKeyGen()
    // val bobPublicKey = bobDH.publicKeyGen()

    val aliceSharedKey = aliceDH.sharedKeyGen(p)
    val bobSharedKey = bobDH.sharedKeyGen(p)

    val msg = "Hello!".toByteArray()

    var iv = generateRandomArray()
    val msgFromAliceEncrypted = aesEncrypt(aliceSharedKey.hash(), iv, msg) + iv

    val ivFromAlice = msgFromAliceEncrypted.iv()
    val msgFromAliceDecrypted = aesDecrypt(bobSharedKey.hash(), ivFromAlice, msgFromAliceEncrypted.msg())

    iv = generateRandomArray()
    val msgFromBobEncrypted = aesEncrypt(bobSharedKey.hash(), iv, msgFromAliceDecrypted) + iv

    val mitmKey = BigInteger.valueOf(0L)

    val ivAlice = msgFromAliceEncrypted.iv()
    val msgFromAliceHacked = aesDecrypt(mitmKey.hash(), ivAlice, msgFromAliceEncrypted.msg())

    val ivBob = msgFromBobEncrypted.iv()
    val msgFromBobHacked = aesDecrypt(mitmKey.hash(), ivBob, msgFromBobEncrypted.msg())

    println(msg.toHex() == msgFromAliceHacked.toHex() && msg.toHex() == msgFromBobHacked.toHex()) // true
}
