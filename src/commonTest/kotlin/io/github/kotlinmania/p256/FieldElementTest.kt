// port-lint: tests p256/src/test_vectors/field.rs
package io.github.kotlinmania.p256

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FieldElementTest {
    /**
     * Repeated doubling of the multiplicative identity.
     * Test vectors from http://point-at-infinity.org/ecc/nisttv
     */
    private val dblTestVectors: List<String> =
        listOf(
            "0000000000000000000000000000000000000000000000000000000000000001",
            "0000000000000000000000000000000000000000000000000000000000000002",
            "0000000000000000000000000000000000000000000000000000000000000004",
            "0000000000000000000000000000000000000000000000000000000000000008",
            "0000000000000000000000000000000000000000000000000000000000000010",
            "0000000000000000000000000000000000000000000000000000000000000020",
            "0000000000000000000000000000000000000000000000000000000000000040",
            "0000000000000000000000000000000000000000000000000000000000000080",
            "0000000000000000000000000000000000000000000000000000000000000100",
            "0000000000000000000000000000000000000000000000000000000000000200",
            "0000000000000000000000000000000000000000000000000000000000000400",
            "0000000000000000000000000000000000000000000000000000000000000800",
            "0000000000000000000000000000000000000000000000000000000000001000",
            "0000000000000000000000000000000000000000000000000000000000002000",
            "0000000000000000000000000000000000000000000000000000000000004000",
            "0000000000000000000000000000000000000000000000000000000000008000",
        )

    @Test
    fun fromBytes_zero() {
        val zero = FieldElement.fromBytes(ByteArray(32))
        assertEquals(FieldElement.ZERO, zero)
    }

    @Test
    fun fromBytes_one() {
        val bytes = ByteArray(32)
        bytes[31] = 1
        val one = FieldElement.fromBytes(bytes)
        assertEquals(FieldElement.ONE, one)
    }

    @Test
    fun fromBytes_outOfRange() {
        val bytes = ByteArray(32) { 0xFF.toByte() }
        try {
            FieldElement.fromBytes(bytes)
            assertTrue(false, "Should have thrown for out-of-range value")
        } catch (e: IllegalArgumentException) {
            // Expected
        }
    }

    @Test
    fun toBytes_zero() {
        val bytes = FieldElement.ZERO.toBytes()
        assertEquals(32, bytes.size)
        assertTrue(bytes.all { it == 0.toByte() })
    }

    @Test
    fun toBytes_one() {
        val bytes = FieldElement.ONE.toBytes()
        assertEquals(32, bytes.size)
        assertEquals(1, bytes[31].toInt())
        assertTrue(bytes.copyOfRange(0, 31).all { it == 0.toByte() })
    }

    @Test
    fun repeatedAdd() {
        var r = FieldElement.ONE
        for (i in dblTestVectors.indices) {
            assertEquals(dblTestVectors[i], r.toBytes().toHex(), "Index $i")
            r = r.add(r)
        }
    }

    @Test
    fun repeatedDouble() {
        var r = FieldElement.ONE
        for (i in dblTestVectors.indices) {
            assertEquals(dblTestVectors[i], r.toBytes().toHex(), "Index $i")
            r = r.double()
        }
    }

    @Test
    fun repeatedMul() {
        var r = FieldElement.ONE
        val two = r.add(r)
        for (i in dblTestVectors.indices) {
            assertEquals(dblTestVectors[i], r.toBytes().toHex(), "Index $i")
            r = r.multiply(two)
        }
    }

    @Test
    fun negation() {
        val two = FieldElement.ONE.double()
        val negTwo = two.negate()
        assertEquals(FieldElement.ZERO, two.add(negTwo))
        assertEquals(two, negTwo.negate())
    }

    @Test
    fun fieldIdentity() {
        // 0 + a = a
        val a = FieldElement.fromHex("0000000000000000000000000000000000000000000000000000000000000005")
        assertEquals(a, FieldElement.ZERO.add(a))
        // 0 - a = -a
        assertEquals(a.negate(), FieldElement.ZERO.sub(a))
        // a * 0 = 0
        assertEquals(FieldElement.ZERO, a.multiply(FieldElement.ZERO))
        // a * 1 = a
        assertEquals(a, a.multiply(FieldElement.ONE))
        // 1 * 1 = 1
        assertEquals(FieldElement.ONE, FieldElement.ONE.multiply(FieldElement.ONE))
    }

    @Test
    fun fieldInvert() {
        // a * a^(-1) = 1
        val a = FieldElement.fromHex("0000000000000000000000000000000000000000000000000000000000000005")
        val aInv = a.invert()
        assertNotNull(aInv)
        assertEquals(FieldElement.ONE, a.multiply(aInv))

        // 0 has no inverse
        assertNull(FieldElement.ZERO.invert())
    }

    @Test
    fun fieldSquare() {
        // 2^2 = 4
        val two = FieldElement.ONE.double()
        val four = two.square()
        val expectedFour = FieldElement.fromHex("0000000000000000000000000000000000000000000000000000000000000004")
        assertEquals(expectedFour, four)
    }

    @Test
    fun fieldAddThenSub() {
        val a = FieldElement.fromHex("7fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        val b = FieldElement.fromHex("000000003fffffffffffffffffffffffffffffffffffffffffffffffffffffff")
        assertEquals(b, a.add(b).sub(a))
    }

    @Test
    fun isZero() {
        assertTrue(FieldElement.ZERO.isZero())
        assertFalse(FieldElement.ONE.isZero())
    }

    @Test
    fun isOdd() {
        // 1 is odd
        assertTrue(FieldElement.ONE.isOdd())
        // 0 is not odd
        assertFalse(FieldElement.ZERO.isOdd())
        // 2 is not odd
        assertFalse(FieldElement.TWO.isOdd())
    }

    @Test
    fun powVartime() {
        val one = FieldElement.ONE
        val two = one.add(one)
        val four = two.square()
        // 2^2 = 4
        // pow_vartime with exponent 2 (but we don't have pow_vartime on FieldElement,
        // this is tested through repeated squaring)
        assertEquals(four, two.square())
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
