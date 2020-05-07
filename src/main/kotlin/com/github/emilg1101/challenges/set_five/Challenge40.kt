package com.github.emilg1101.challenges.set_five

import com.github.emilg1101.challenges.util.CubeRoot
import java.math.BigInteger

fun BigInteger.cbrt() = CubeRoot.cbrt(this)

fun main() {
    val stringToEncode = "Cryptopals challenge 40".toBigInt()
    val list = arrayListOf<Pair<BigInteger, BigInteger>>()
    for (i in 0 until 3) {
        val rsa = RSA(1024)
        list.add(rsa.encrypt(stringToEncode) to rsa.n)
    }
    println(rsaAttack(list) == stringToEncode)
}

private fun rsaAttack(list: List<Pair<BigInteger, BigInteger>>): BigInteger {
    val c0 = list[0].first
    val c1 = list[1].first
    val c2 = list[2].first

    val n0 = list[0].second
    val n1 = list[1].second
    val n2 = list[2].second

    val m0 = n1 * n2
    val m1 = n0 * n2
    val m2 = n0 * n1

    val t0 = c0 * m0 * (m0 modinv n0)
    val t1 = c1 * m1 * (m1 modinv n1)
    val t2 = c2 * m2 * (m2 modinv n2)

    return (t0 + t1 + t2).mod(n0 * n1 * n2).cbrt()
}
