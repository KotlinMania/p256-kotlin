// port-lint: source arithmetic/scalar.rs
package io.github.kotlinmania.p256

/**
 * Constant representing the scalar modulus (order of the P-256 group).
 * n = FFFFFFFF 00000000 FFFFFFFF FFFFFFFF BCE6FAAD A7179E84 F3B9CAC2 FC632551
 */
internal val SCALAR_MODULUS: ULongArray =
    ulongArrayOf(
        0xF3B9CAC2FC632551uL,
        0xBCE6FAADA7179E84uL,
        0xFFFFFFFFFFFFFFFFuL,
        0xFFFFFFFF00000000uL,
    )

/** MU = floor(2^512 / n) */
internal val MU: ULongArray =
    ulongArrayOf(
        0x012FFD85EEDF9BFEuL,
        0x43190552DF1A6C21uL,
        0xFFFFFFFEFFFFFFFFuL,
        0x00000000FFFFFFFFuL,
        0x0000000000000001uL,
    )

/** MODULUS / 2 as a Scalar */
internal val FRAC_MODULUS_2: ULongArray =
    ulongArrayOf(
        0x79DCE5617E3192A8uL,
        0xDE737D56D38BCF42uL,
        0x7FFFFFFFFFFFFFFFuL,
        0x7FFFFFFF80000000uL,
    )

/**
 * Scalars are elements in the finite field modulo n (the order of the NIST P-256 group).
 *
 * The internal representation is a 256-bit unsigned integer stored as 4 x 64-bit limbs
 * in little-endian order. Unlike [FieldElement], scalars are NOT in Montgomery form —
 * they use Barrett reduction for multiplication.
 */
class Scalar(
    val value: ULongArray,
) : Comparable<Scalar> {
    constructor() : this(ulongArrayOf(0uL, 0uL, 0uL, 0uL))

    companion object {
        /** Zero scalar. */
        val ZERO: Scalar = Scalar(ulongArrayOf(0uL, 0uL, 0uL, 0uL))

        /** Multiplicative identity. */
        val ONE: Scalar = Scalar(ulongArrayOf(1uL, 0uL, 0uL, 0uL))

        /**
         * Creates a scalar from a big-endian byte array (32 bytes).
         * Returns null if the value is not in range [0, n).
         */
        fun fromBytes(bytes: ByteArray): Scalar? {
            require(bytes.size == 32) { "Scalar must be 32 bytes, got ${bytes.size}" }
            val limbs = bytesToLimbs(bytes)
            if (cmpLimbs(limbs, SCALAR_MODULUS) >= 0) return null
            return Scalar(limbs)
        }

        /** Creates a scalar from a hex string (big-endian, 64 hex chars). */
        fun fromHex(hex: String): Scalar? = fromBytes(hexToBytes(hex))

        /** Creates a scalar from a u64 value. */
        fun fromU64(value: ULong): Scalar = Scalar(ulongArrayOf(value, 0uL, 0uL, 0uL))

        /** Creates a scalar from a u32 value. */
        fun fromU32(value: UInt): Scalar = Scalar(ulongArrayOf(value.toULong(), 0uL, 0uL, 0uL))

        /** Creates a scalar from a big-endian byte array without range-checking. */
        fun fromUintUnchecked(bytes: ByteArray): Scalar {
            require(bytes.size == 32) { "Scalar must be 32 bytes, got ${bytes.size}" }
            return Scalar(bytesToLimbs(bytes))
        }
    }

    /** Returns the SEC1 encoding of this scalar (big-endian 32 bytes). */
    fun toBytes(): ByteArray {
        val bytes = ByteArray(32)
        for (i in 0 until 4) {
            for (j in 0 until 8) {
                bytes[31 - i * 8 - j] = (value[i] shr (j * 8)).toByte()
            }
        }
        return bytes
    }

    /** Returns self + rhs mod n. */
    fun add(rhs: Scalar): Scalar = Scalar(addMod(value, rhs.value, SCALAR_MODULUS))

    /** Returns 2*self. */
    fun double(): Scalar = add(this)

    /** Returns self - rhs mod n. */
    fun sub(rhs: Scalar): Scalar = Scalar(subMod(value, rhs.value, SCALAR_MODULUS))

    /** Returns self * rhs mod n (using Barrett reduction). */
    fun multiply(rhs: Scalar): Scalar {
        val (lo, hi) = mulWide(value, rhs.value)
        return Scalar(barrettReduce(lo, hi))
    }

    /** Returns self * self mod n. */
    fun square(): Scalar = multiply(this)

    /** Right shifts the scalar by `shift` bits. */
    fun shrVartime(shift: Int): Scalar {
        if (shift == 0) return Scalar(value.copyOf())
        if (shift >= 256) return ZERO

        val wordShift = shift / 64
        val bitShift = shift % 64
        val result = ULongArray(4)

        if (bitShift == 0) {
            for (i in 0 until (4 - wordShift)) {
                result[i] = value[i + wordShift]
            }
        } else {
            for (i in 0 until (4 - wordShift)) {
                var v = value[i + wordShift] shr bitShift
                if (i + wordShift + 1 < 4) {
                    v = v or (value[i + wordShift + 1] shl (64 - bitShift))
                }
                result[i] = v
            }
        }
        return Scalar(result)
    }

    /** Returns the multiplicative inverse of self, or null if self is zero. */
    fun invert(): Scalar? = if (isZero()) null else invertUnchecked()

    /**
     * Returns the multiplicative inverse of self (Fermat's Little Theorem: a^(n-2) mod n).
     * Does not check that self is non-zero.
     */
    fun invertUnchecked(): Scalar =
        powVartime(
            ulongArrayOf(
                0xF3B9CAC2FC63254FuL,
                0xBCE6FAADA7179E84uL,
                0xFFFFFFFFFFFFFFFFuL,
                0xFFFFFFFF00000000uL,
            ),
        )

    /** Exponentiates self by `exp` using square-and-multiply. */
    fun powVartime(exp: ULongArray): Scalar {
        var res = ONE
        for (i in exp.size - 1 downTo 0) {
            var e = exp[i]
            repeat(64) {
                res = res.square()
                if ((e shr 63) and 1uL != 0uL) {
                    res = res.multiply(this)
                }
                e = e shl 1
            }
        }
        return res
    }

    /** Is this scalar odd? */
    fun isOdd(): Boolean = (value[0] and 1uL) != 0uL

    /** Is this scalar even? */
    fun isEven(): Boolean = !isOdd()

    /** Is this scalar zero? */
    fun isZero(): Boolean = value.all { it == 0uL }

    /** Is this scalar "high" (>= n/2)? */
    fun isHigh(): Boolean = cmpLimbs(value, FRAC_MODULUS_2) > 0

    /**
     * Fast variable-time inversion using Stein's algorithm.
     * Returns null if the scalar is zero.
     * Should not be used with unblinded secret scalars.
     */
    fun invertVartime(): Scalar? {
        if (isZero()) return null

        var u = this
        var v = Scalar(SCALAR_MODULUS.copyOf())
        var a = ONE
        var c = ZERO

        while (!u.isZero()) {
            while (u.isEven()) {
                u = u.shrVartime(1)
                val wasOdd = a.isOdd()
                a = a.shrVartime(1)
                if (wasOdd) {
                    a = a.add(Scalar(FRAC_MODULUS_2.copyOf()))
                    a = a.add(ONE)
                }
            }
            while (v.isEven()) {
                v = v.shrVartime(1)
                val wasOdd = c.isOdd()
                c = c.shrVartime(1)
                if (wasOdd) {
                    c = c.add(Scalar(FRAC_MODULUS_2.copyOf()))
                    c = c.add(ONE)
                }
            }
            if (u >= v) {
                u = u.sub(v)
                a = a.sub(c)
            } else {
                v = v.sub(u)
                c = c.sub(a)
            }
        }
        return c
    }

    /** Negate: returns -self mod n = n - self. */
    fun negate(): Scalar = Scalar.ZERO.sub(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Scalar) return false
        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()

    override fun compareTo(other: Scalar): Int = cmpLimbs(value, other.value)

    override fun toString(): String = "Scalar(0x${limbsToHex(value)})"
}

// --- Low-level scalar arithmetic (from scalar64.rs) ---

/** Computes a + b mod m. */
internal fun addMod(a: ULongArray, b: ULongArray, m: ULongArray): ULongArray {
    val (w0, c0) = Util.adc(a[0], b[0], 0uL)
    val (w1, c1) = Util.adc(a[1], b[1], c0)
    val (w2, c2) = Util.adc(a[2], b[2], c1)
    val (w3, _) = Util.adc(a[3], b[3], c2)
    val result = ulongArrayOf(w0, w1, w2, w3)
    if (cmpLimbs(result, m) >= 0) {
        return subMod(result, m, m)
    }
    return result
}

/** Computes a - b mod m. */
internal fun subMod(a: ULongArray, b: ULongArray, m: ULongArray): ULongArray {
    val (w0, b0) = Util.sbb(a[0], b[0], 0uL)
    val (w1, b1) = Util.sbb(a[1], b[1], b0)
    val (w2, b2) = Util.sbb(a[2], b[2], b1)
    val (w3, b3) = Util.sbb(a[3], b[3], b2)
    val result = ulongArrayOf(w0, w1, w2, w3)
    if (b3 != 0uL) {
        val (r0, ca0) = Util.adc(result[0], m[0], 0uL)
        val (r1, ca1) = Util.adc(result[1], m[1], ca0)
        val (r2, ca2) = Util.adc(result[2], m[2], ca1)
        val (r3, _) = Util.adc(result[3], m[3], ca2)
        return ulongArrayOf(r0, r1, r2, r3)
    }
    return result
}

/** Multiplies two 256-bit integers returning (lo, hi) as 256-bit values. */
internal fun mulWide(a: ULongArray, b: ULongArray): Pair<ULongArray, ULongArray> {
    val result = ULongArray(8)
    for (i in 0 until 4) {
        var carry = 0uL
        for (j in 0 until 4) {
            val (mulHi, mulLo) = mulU64(a[i], b[j])
            // Add mulLo to result[i+j], propagate carry
            val (sum, c1) = Util.adc(result[i + j], mulLo, 0uL)
            result[i + j] = sum
            // Add mulHi + carry + c1 to result[i+j+1]
            val (sum2, c2) = Util.adc(result[i + j + 1], mulHi, c1)
            result[i + j + 1] = sum2
            // Add the accumulated carry from previous j iterations
            val (sum3, c3) = Util.adc(result[i + j + 1], 0uL, carry)
            result[i + j + 1] = sum3
            carry = c2 + c3
            // Propagate carry upward
            var k = i + j + 2
            while (carry != 0uL && k < 8) {
                val (s, c) = Util.adc(result[k], 0uL, carry)
                result[k] = s
                carry = c
                k++
            }
        }
    }
    val lo = ulongArrayOf(result[0], result[1], result[2], result[3])
    val hi = ulongArrayOf(result[4], result[5], result[6], result[7])
    return Pair(lo, hi)
}

/** Multiplies two 64-bit values returning (high, low). */
private fun mulU64(a: ULong, b: ULong): Pair<ULong, ULong> {
    val aLo = a and 0xFFFFFFFFu
    val aHi = a shr 32
    val bLo = b and 0xFFFFFFFFu
    val bHi = b shr 32

    val ll = aLo * bLo
    val lh = aLo * bHi
    val hl = aHi * bLo
    val hh = aHi * bHi

    // mid can overflow 64 bits (sum of three 64-bit values)
    val mid = (lh and 0xFFFFFFFFu) + (hl and 0xFFFFFFFFu) + (ll shr 32)
    val midLo = mid and 0xFFFFFFFFu
    val midHi = mid shr 32

    val lo = (ll and 0xFFFFFFFFu) or (midLo shl 32)
    val hi = hh + (lh shr 32) + (hl shr 32) + midHi

    return Pair(hi, lo)
}

/**
 * Barrett Reduction.
 * Reduces a 512-bit number (given as lo and hi 256-bit halves) modulo n.
 */
internal fun barrettReduce(lo: ULongArray, hi: ULongArray): ULongArray {
    val q1: ULongArray = ulongArrayOf(lo[3], hi[0], hi[1], hi[2], hi[3])
    val q3 = q1TimesMuShiftFive(q1)
    val r1: ULongArray = ulongArrayOf(lo[0], lo[1], lo[2], lo[3], hi[0])
    val r2 = q3TimesNKeepFive(q3)
    val r = subInnerFive(r1, r2)

    val r2a = subtractNIfNecessary(r[0], r[1], r[2], r[3], r[4])
    val r2b = subtractNIfNecessary(r2a[0], r2a[1], r2a[2], r2a[3], r2a[4])
    return ulongArrayOf(r2b[0], r2b[1], r2b[2], r2b[3])
}

private fun q1TimesMuShiftFive(q1: ULongArray): ULongArray {
    // Schoolbook multiplication of q1 (5 limbs) * MU (5 limbs), keeping top 5 limbs
    // The full product is 10 limbs; we return limbs [5..9]

    // q1[0] * MU
    val (_, c0) = Util.mac(0uL, q1[0], MU[0], 0uL)
    val (w1, c1) = Util.mac(0uL, q1[0], MU[1], c0)
    val (w2, c2) = Util.mac(0uL, q1[0], MU[2], c1)
    val (w3, c3) = Util.mac(0uL, q1[0], MU[3], c2)
    val (w4, w5) = Util.mac(0uL, q1[0], MU[4], c3)

    // q1[1] * MU (starts at w[1])
    val (w1a, d0) = Util.mac(w1, q1[1], MU[0], 0uL)
    val (w2a, d1) = Util.mac(w2, q1[1], MU[1], d0)
    val (w3a, d2) = Util.mac(w3, q1[1], MU[2], d1)
    val (w4a, d3) = Util.mac(w4, q1[1], MU[3], d2)
    val (w5a, w6) = Util.mac(w5, q1[1], MU[4], d3)

    // q1[2] * MU (starts at w[2])
    val (w2b, e0) = Util.mac(w2a, q1[2], MU[0], 0uL)
    val (w3b, e1) = Util.mac(w3a, q1[2], MU[1], e0)
    val (w4b, e2) = Util.mac(w4a, q1[2], MU[2], e1)
    val (w5b, e3) = Util.mac(w5a, q1[2], MU[3], e2)
    val (w6a, w7) = Util.mac(w6, q1[2], MU[4], e3)

    // q1[3] * MU (starts at w[3])
    val (w3c, f0) = Util.mac(w3b, q1[3], MU[0], 0uL)
    val (w4c, f1) = Util.mac(w4b, q1[3], MU[1], f0)
    val (w5c, f2) = Util.mac(w5b, q1[3], MU[2], f1)
    val (w6b, f3) = Util.mac(w6a, q1[3], MU[3], f2)
    val (w7a, w8) = Util.mac(w7, q1[3], MU[4], f3)

    // q1[4] * MU (starts at w[4])
    val (w4d, g0) = Util.mac(w4c, q1[4], MU[0], 0uL)
    val (w5d, g1) = Util.mac(w5c, q1[4], MU[1], g0)
    val (w6c, g2) = Util.mac(w6b, q1[4], MU[2], g1)
    val (w7b, g3) = Util.mac(w7a, q1[4], MU[3], g2)
    val (w8a, w9) = Util.mac(w8, q1[4], MU[4], g3)

    return ulongArrayOf(w5d, w6c, w7b, w8a, w9)
}

private fun q3TimesNKeepFive(q3: ULongArray): ULongArray {
    val m = SCALAR_MODULUS

    // q3[0] * modulus
    val (w0, c0) = Util.mac(0uL, q3[0], m[0], 0uL)
    val (w1, c1) = Util.mac(0uL, q3[0], m[1], c0)
    val (w2, c2) = Util.mac(0uL, q3[0], m[2], c1)
    val (w3, c3) = Util.mac(0uL, q3[0], m[3], c2)
    val (w4, _) = Util.mac(0uL, q3[0], 0uL, c3)

    // q3[1] * modulus
    val (w1a, d0) = Util.mac(w1, q3[1], m[0], 0uL)
    val (w2a, d1) = Util.mac(w2, q3[1], m[1], d0)
    val (w3a, d2) = Util.mac(w3, q3[1], m[2], d1)
    val (w4a, _) = Util.mac(w4, q3[1], m[3], d2)

    // q3[2] * modulus
    val (w2b, e0) = Util.mac(w2a, q3[2], m[0], 0uL)
    val (w3b, e1) = Util.mac(w3a, q3[2], m[1], e0)
    val (w4b, _) = Util.mac(w4a, q3[2], m[2], e1)

    // q3[3] * modulus
    val (w3c, f0) = Util.mac(w3b, q3[3], m[0], 0uL)
    val (w4c, _) = Util.mac(w4b, q3[3], m[1], f0)

    // q3[4] * modulus
    val (w4d, _) = Util.mac(w4c, q3[4], m[0], 0uL)

    return ulongArrayOf(w0, w1a, w2b, w3c, w4d)
}

private fun subInnerFive(l: ULongArray, r: ULongArray): ULongArray {
    val (w0, b0) = Util.sbb(l[0], r[0], 0uL)
    val (w1, b1) = Util.sbb(l[1], r[1], b0)
    val (w2, b2) = Util.sbb(l[2], r[2], b1)
    val (w3, b3) = Util.sbb(l[3], r[3], b2)
    val (w4, _) = Util.sbb(l[4], r[4], b3)
    return ulongArrayOf(w0, w1, w2, w3, w4)
}

private fun subtractNIfNecessary(r0: ULong, r1: ULong, r2: ULong, r3: ULong, r4: ULong): ULongArray {
    val m = SCALAR_MODULUS
    val (w0, b0) = Util.sbb(r0, m[0], 0uL)
    val (w1, b1) = Util.sbb(r1, m[1], b0)
    val (w2, b2) = Util.sbb(r2, m[2], b1)
    val (w3, b3) = Util.sbb(r3, m[3], b2)
    val (w4, b4) = Util.sbb(r4, 0uL, b3)

    val borrow = b4
    val (w0a, ca0) = Util.adc(w0, m[0] and borrow, 0uL)
    val (w1a, ca1) = Util.adc(w1, m[1] and borrow, ca0)
    val (w2a, ca2) = Util.adc(w2, m[2] and borrow, ca1)
    val (w3a, ca3) = Util.adc(w3, m[3] and borrow, ca2)
    val (w4a, _) = Util.adc(w4, 0uL, ca3)

    return ulongArrayOf(w0a, w1a, w2a, w3a, w4a)
}
