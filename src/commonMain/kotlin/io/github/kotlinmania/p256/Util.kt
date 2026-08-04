// port-lint: source p256/src/arithmetic/util.rs
package io.github.kotlinmania.p256

/**
 * Helper functions for multi-precision arithmetic on 64-bit limbs.
 *
 * These mirror the inline Rust helpers in the upstream p256 crate's
 * `arithmetic/util.rs`. Since Kotlin does not have a native 128-bit
 * unsigned integer type in commonMain, carry and borrow are detected
 * via unsigned overflow comparisons on [ULong].
 */
internal object Util {
    /**
     * Computes `a + b + carry`, returning the result along with the new carry.
     * The carry can be any 64-bit value (not limited to 0 or 1), though the
     * output carry is always 0 or 1 when inputs are at most MAX.
     */
    fun adc(a: ULong, b: ULong, carry: ULong): Pair<ULong, ULong> {
        // Add a + b (may overflow)
        val sum = a + b
        val overflow1 = sum < a
        // Add carry to sum (may overflow)
        val result = sum + carry
        val overflow2 = result < sum
        // Total carry: if a+b overflowed, that's 1. If sum+carry overflowed, that's 1.
        // But the actual carry is (a + b + carry) >> 64.
        // When carry > 1, we need to account for the full carry.
        // a + b can overflow by at most 1 (carry1 = 0 or 1).
        // sum + carry: if carry is small, this can overflow by at most 1.
        // But if carry is large (e.g. 0xFFFFFFFE), then sum + carry might not overflow
        // but the true result a + b + carry exceeds 2^64.
        // The correct carry = (a + b + carry) >> 64 = overflow1 + overflow2
        // (since a + b + carry <= 3 * MAX = 3 * (2^64 - 1) < 2^66, carry fits in 2 bits)
        val totalCarry = (if (overflow1) 1uL else 0uL) + (if (overflow2) 1uL else 0uL)
        return Pair(result, totalCarry)
    }

    /**
     * Computes `a - (b + borrow)`, returning the result along with the new borrow.
     * The borrow is a mask: all 1s (0xFFFF...FFFF) on underflow, all 0s otherwise.
     */
    fun sbb(a: ULong, b: ULong, borrow: ULong): Pair<ULong, ULong> {
        val borrowBit = borrow shr 63
        val diff = a - b
        val underflowed1 = a < b
        val result = diff - borrowBit
        val underflowed2 = diff < borrowBit
        val totalUnderflow = (if (underflowed1) 1uL else 0uL) + (if (underflowed2) 1uL else 0uL)
        val mask = 0xFFFFFFFFFFFFFFFFuL
        return Pair(result, if (totalUnderflow > 0uL) mask else 0uL)
    }

    /**
     * Computes `a + (b * c) + carry`, returning the result along with the new carry.
     * The carry can be any 64-bit value (not limited to 0 or 1).
     */
    fun mac(a: ULong, b: ULong, c: ULong, carry: ULong): Pair<ULong, ULong> {
        val (mulHi, mulLo) = mulWide(b, c)
        // Add mulLo + a
        val (lo1, c1) = adc(a, mulLo, 0uL)
        // Add lo1 + carry
        val (lo, c2) = adc(lo1, carry, 0uL)
        // Total low carry = c1 + c2 (at most 2)
        val lowCarry = c1 + c2
        // Add mulHi + lowCarry (high 64 bits)
        val (hi, _) = adc(mulHi, lowCarry, 0uL)
        return Pair(lo, hi)
    }

    /**
     * Multiplies two [ULong]s returning (high, low) as a pair of [ULong]s.
     * This represents the 128-bit product split into two 64-bit halves.
     */
    private fun mulWide(a: ULong, b: ULong): Pair<ULong, ULong> {
        val aLo = a and 0xFFFFFFFFu
        val aHi = a shr 32
        val bLo = b and 0xFFFFFFFFu
        val bHi = b shr 32

        val ll = aLo * bLo
        val lh = aLo * bHi
        val hl = aHi * bLo
        val hh = aHi * bHi

        val mid = (lh and 0xFFFFFFFFu) + (hl and 0xFFFFFFFFu) + (ll shr 32)
        val midLo = mid and 0xFFFFFFFFu
        val midHi = mid shr 32

        val lo = (ll and 0xFFFFFFFFu) or (midLo shl 32)
        val hi = hh + (lh shr 32) + (hl shr 32) + midHi

        return Pair(hi, lo)
    }
}