// port-lint: tests p256/src/lib.rs
package io.github.kotlinmania.p256

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        assertEquals(expectedGx, CurveParams.GENERATOR_X.toBytes().toHex())
        assertEquals(expectedGy, CurveParams.GENERATOR_Y.toBytes().toHex())
    }

    @Test
    fun curveParamsB() {
        val expectedB = "5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b"
        assertEquals(expectedB, CurveParams.EQUATION_B.toBytes().toHex())
    }

    @Test
    fun curveParamsAIsMinusThree() {
        // a = -3 mod p = p - 3
        val expectedA = "ffffffff00000001000000000000000000000000fffffffffffffffffffffffc"
        assertEquals(expectedA, CurveParams.EQUATION_A.toBytes().toHex())
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
        val secret = EphemeralSecret(Scalar.ONE)
        val shared = SharedSecret(ByteArray(32) { 0x42 })
        assertEquals(32, shared.rawSecretBytes().size)
    }

    private fun ByteArray.toHex(): String {
        val hexChars = "0123456789abcdef"
        val sb = StringBuilder(size * 2)
        for (b in this) {
            val v = b.toInt() and 0xFF
            sb.append(hexChars[v shr 4])
            sb.append(hexChars[v and 0x0F])
        }
        return sb.toString()
    }
}
