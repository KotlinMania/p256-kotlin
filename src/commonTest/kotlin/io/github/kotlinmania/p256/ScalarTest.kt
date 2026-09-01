// port-lint: tests arithmetic/scalar.rs
package io.github.kotlinmania.p256

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScalarTest {
    @Test
    fun fromToBytesRoundtrip() {
        val k: ULong = 42uL
        val bytes = ByteArray(32)
        // Place 42 in big-endian position (last 8 bytes)
        val kBytes = longToBigEndianBytes(k)
        for (i in 0 until 8) {
            bytes[24 + i] = kBytes[i]
        }

        val scalar = Scalar.fromBytes(bytes)
        assertNotNull(scalar)
        assertEquals(bytes.toList(), scalar.toBytes().toList())
    }

    @Test
    fun multiply() {
        val one = Scalar.ONE
        val two = one.add(one)
        val three = two.add(one)
        val six = three.add(three)

        assertEquals(six, two.multiply(three))

        val minusTwo = Scalar.ZERO.sub(two)
        val minusThree = Scalar.ZERO.sub(three)
        assertEquals(two, minusTwo.negate())
        assertEquals(minusThree.multiply(minusTwo), minusTwo.multiply(minusThree))
        assertEquals(six, minusTwo.multiply(minusThree))
    }

    @Test
    fun scalarIdentity() {
        // 0 + a = a
        val a = Scalar.fromU64(5uL)
        assertEquals(a, Scalar.ZERO.add(a))
        // 0 - a = -a
        assertEquals(Scalar.ZERO.sub(a), a.negate())
        // a * 0 = 0
        assertEquals(Scalar.ZERO, a.multiply(Scalar.ZERO))
        // a * 1 = a
        assertEquals(a, a.multiply(Scalar.ONE))
        // 1 * 1 = 1
        assertEquals(Scalar.ONE, Scalar.ONE.multiply(Scalar.ONE))
    }

    @Test
    fun scalarInvert() {
        val a = Scalar.fromU64(5uL)
        val aInv = a.invert()
        assertNotNull(aInv)
        assertEquals(Scalar.ONE, a.multiply(aInv))

        assertNull(Scalar.ZERO.invert())
    }

    @Test
    fun isZero() {
        assertTrue(Scalar.ZERO.isZero())
        assertFalse(Scalar.ONE.isZero())
    }

    @Test
    fun isOdd() {
        assertTrue(Scalar.ONE.isOdd())
        assertFalse(Scalar.ZERO.isOdd())
        assertFalse(Scalar.fromU64(2uL).isOdd())
    }

    @Test
    fun isEven() {
        assertFalse(Scalar.ONE.isEven())
        assertTrue(Scalar.ZERO.isEven())
        assertTrue(Scalar.fromU64(2uL).isEven())
    }

    @Test
    fun double() {
        val one = Scalar.ONE
        val two = one.double()
        assertEquals(Scalar.fromU64(2uL), two)
        assertEquals(one.add(one), two)
    }

    @Test
    fun negate() {
        val one = Scalar.ONE
        val negOne = one.negate()
        assertEquals(Scalar.ZERO, one.add(negOne))
    }

    @Test
    fun square() {
        val three = Scalar.fromU64(3uL)
        val nine = three.square()
        assertEquals(Scalar.fromU64(9uL), nine)
    }

    @Test
    fun shrVartime() {
        val a = Scalar.fromU64(0xFFuL)
        val shifted = a.shrVartime(4)
        assertEquals(Scalar.fromU64(0x0FuL), shifted)
    }

    @Test
    fun fromBytesOutOfRange() {
        // n (the order itself) should be rejected
        val n = "ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632551"
        val scalar = Scalar.fromBytes(hexToBytes(n))
        assertNull(scalar)
    }

    @Test
    fun fromBytesMaxValid() {
        // n - 1 should be accepted
        val nMinusOne = "ffffffff00000000ffffffffffffffffbce6faada7179e84f3b9cac2fc632550"
        val scalar = Scalar.fromBytes(hexToBytes(nMinusOne))
        assertNotNull(scalar)
    }

    @Test
    fun compareAndOrder() {
        val a = Scalar.fromU64(1uL)
        val b = Scalar.fromU64(2uL)
        assertTrue(a < b)
        assertTrue(b > a)
        assertEquals(0, a.compareTo(a))
    }

    private fun longToBigEndianBytes(value: ULong): ByteArray =
        ByteArray(8) { i ->
            ((value shr ((7 - i) * 8)).toByte())
        }
}
