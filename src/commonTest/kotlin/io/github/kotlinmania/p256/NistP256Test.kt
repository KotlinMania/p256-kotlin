// port-lint: tests lib.rs
package io.github.kotlinmania.p256

import kotlin.test.Test
import kotlin.test.assertEquals

class NistP256Test {
    @Test
    fun modulusIsCorrect() {
        val expected = "ffffffff00000001000000000000000000000000ffffffffffffffffffffffff"
        assertEquals(expected, limbsToHex(NistP256.MODULUS))
    }

    @Test
    fun orderIsCorrect() {
        val expected = "ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551"
        assertEquals(expected, limbsToHex(NistP256.ORDER))
    }

    @Test
    fun fieldBytesSize() {
        assertEquals(32, NistP256.FIELD_BYTES_SIZE)
    }

    @Test
    fun compressPoints() {
        assertEquals(false, NistP256.COMPRESS_POINTS)
        assertEquals(false, NistP256.COMPACT_POINTS)
    }

    @Test
    fun oid() {
        assertEquals("1.2.840.10045.3.1.7", NistP256.OID)
    }

    @Test
    fun crv() {
        assertEquals("P-256", NistP256.CRV)
    }

    @Test
    fun curveParamsGenerator() {
        val expectedGx = "6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296"
        val expectedGy = "4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5"
        assertEquals(expectedGx, CurveParams.GENERATOR_X.toBytes().toHexString())
        assertEquals(expectedGy, CurveParams.GENERATOR_Y.toBytes().toHexString())
    }

    @Test
    fun curveParamsB() {
        val expectedB = "5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b"
        assertEquals(expectedB, CurveParams.EQUATION_B.toBytes().toHexString())
    }

    @Test
    fun curveParamsAIsMinusThree() {
        // a = -3 mod p = p - 3
        val expectedA = "ffffffff00000001000000000000000000000000fffffffffffffffffffffffc"
        assertEquals(expectedA, CurveParams.EQUATION_A.toBytes().toHexString())
    }

    @Test
    fun fieldBytesCodec() {
        val fe = FieldElement.ONE
        val bytes = NistP256.encodeFieldBytes(fe)
        val decoded = NistP256.decodeFieldBytes(bytes)
        assertEquals(fe, decoded)
    }

    @Test
    fun signatureRoundtrip() {
        val r = ByteArray(32) { (it + 1).toByte() }
        val s = ByteArray(32) { (it + 33).toByte() }
        val sig = Signature(r, s)
        val raw = sig.toBytes()
        val parsed = Signature.fromBytes(raw)
        assertEquals(sig, parsed)
    }

    @Test
    fun ecdhSecret() {
        val secret = EphemeralSecret.random()
        secret.close()
        assertEquals(Scalar.ZERO, secret.secretScalar.scalar)
        val sharedA = SharedSecret(ByteArray(32) { 0x42 })
        val sharedB = SharedSecret(ByteArray(32) { 0x42 })
        assertEquals(sharedA, sharedB)
        sharedA.close()
        assertEquals(ByteArray(32).toList(), sharedA.rawSecretBytes().toList())
    }

    @Test
    fun ecdsaSha2DigestsAndKeyZeroize() {
        val message = "abc".encodeToByteArray()
        val sha256Hex = SigningKey.sha256Digest(message).toHexString()
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", sha256Hex)

        val sha384Hex = SigningKey.sha384Digest(message).toHexString()
        assertEquals(
            "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed8086072ba1e7cc2358baeca134c825a7",
            sha384Hex,
        )

        val updater = SigningKey.newSha256Updater()
        updater.update(message)

        val signingKey = SigningKey.random()
        val keyBytes = signingKey.toBytes()
        val parsed = SigningKey.fromBytes(keyBytes)
        assertEquals(keyBytes.toList(), parsed?.toBytes()?.toList())
        signingKey.close()
        assertEquals(ByteArray(32).toList(), signingKey.toBytes().toList())
    }
}
