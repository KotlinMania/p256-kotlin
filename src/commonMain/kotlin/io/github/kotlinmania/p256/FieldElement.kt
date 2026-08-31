// port-lint: source p256/src/arithmetic/field.rs
package io.github.kotlinmania.p256

/**
 * Field modulus serialized as hex.
 * p = 2^{224}(2^{32} − 1) + 2^{192} + 2^{96} − 1
 *
 *     p = FFFFFFFF 00000001 00000000 00000000 00000000 FFFFFFFF FFFFFFFF FFFFFFFF
 */
internal val FIELD_MODULUS: ULongArray =
    ulongArrayOf(
        0xFFFFFFFFFFFFFFFFuL,
        0x00000000FFFFFFFFuL,
        0x0000000000000000uL,
        0xFFFFFFFF00000001uL,
    )

/** R^2 = 2^512 mod p */
internal val R_2: ULongArray =
    ulongArrayOf(
        0x0000000000000003uL,
        0xFFFFFFFBFFFFFFFFuL,
        0xFFFFFFFFFFFFFFFEuL,
        0x00000004FFFFFFFDuL,
    )

/** Raw field element: 4 x 64-bit limbs in little-endian order. */
internal typealias Fe = ULongArray

/**
 * An element in the finite field modulo p = 2^{224}(2^{32} − 1) + 2^{192} + 2^{96} − 1.
 *
 * The internal representation is in little-endian order. Elements are always in
 * Montgomery form; i.e., FieldElement(a) = aR mod p, with R = 2^256.
 */
class FieldElement(
    val value: ULongArray,
) : Comparable<FieldElement> {
    /** Zero field element. */
    constructor() : this(ulongArrayOf(0uL, 0uL, 0uL, 0uL))

    companion object {
        /** Additive identity (0 in Montgomery form). */
        val ZERO: FieldElement = FieldElement(ulongArrayOf(0uL, 0uL, 0uL, 0uL))

        /** Multiplicative identity (1 in Montgomery form = R mod p). */
        val ONE: FieldElement = FieldElement(feToMontgomery(ulongArrayOf(1uL, 0uL, 0uL, 0uL)))

        /** 2 in Montgomery form. */
        val TWO: FieldElement = FieldElement(feToMontgomery(ulongArrayOf(2uL, 0uL, 0uL, 0uL)))

        /**
         * Creates a field element from a big-endian byte array (32 bytes).
         * The value is converted to Montgomery form.
         */
        fun fromBytes(bytes: ByteArray): FieldElement {
            require(bytes.size == 32) { "Field element must be 32 bytes, got ${bytes.size}" }
            val limbs = bytesToLimbs(bytes)
            if (cmpLimbs(limbs, FIELD_MODULUS) >= 0) {
                throw IllegalArgumentException("Field element not in range [0, p)")
            }
            return FieldElement(feToMontgomery(limbs))
        }

        /**
         * Creates a field element from a hex string (big-endian, 64 hex chars).
         */
        fun fromHex(hex: String): FieldElement = fromBytes(hexToBytes(hex))

        /**
         * Creates a field element from a u64 value (not in Montgomery form).
         */
        fun fromU64(value: ULong): FieldElement {
            val limbs = ulongArrayOf(value, 0uL, 0uL, 0uL)
            return FieldElement(feToMontgomery(limbs))
        }

        /** Translates a field element into the Montgomery domain. */
        internal fun feToMontgomery(w: Fe): Fe = feMul(w, R_2)

        /** Translates a field element out of the Montgomery domain. */
        internal fun feFromMontgomery(w: Fe): Fe =
            montgomeryReduce(
                ulongArrayOf(w[0], w[1], w[2], w[3], 0uL, 0uL, 0uL, 0uL),
            )
    }

    /** Returns the SEC1 encoding of this field element (big-endian 32 bytes). */
    fun toBytes(): ByteArray {
        val regular = feFromMontgomery(value)
        val bytes = ByteArray(32)
        for (i in 0 until 4) {
            for (j in 0 until 8) {
                // j=0 is least significant byte, j=7 is most significant
                bytes[31 - i * 8 - j] = (regular[i] shr (j * 8)).toByte()
            }
        }
        return bytes
    }

    /** Returns self + rhs mod p. */
    fun add(rhs: FieldElement): FieldElement = FieldElement(feAdd(value, rhs.value))

    /** Returns self - rhs mod p. */
    fun sub(rhs: FieldElement): FieldElement = FieldElement(feSub(value, rhs.value))

    /** Returns self * rhs mod p. */
    fun multiply(rhs: FieldElement): FieldElement = FieldElement(feMul(value, rhs.value))

    /** Returns self * self mod p. */
    fun square(): FieldElement = FieldElement(feMul(value, value))

    /** Returns -self mod p. */
    fun negate(): FieldElement = FieldElement(feNeg(value))

    /** Returns self doubled (self + self mod p). */
    fun double(): FieldElement = add(this)

    /** Returns true if self is zero. */
    fun isZero(): Boolean = value.all { it == 0uL }

    /** Returns true if self is odd. */
    fun isOdd(): Boolean {
        val regular = feFromMontgomery(value)
        return (regular[0] and 1uL) != 0uL
    }

    /** Returns self^(2^n) mod p. */
    private fun sqn(n: Int): FieldElement {
        var x = this
        repeat(n) { x = x.square() }
        return x
    }

    /**
     * Returns the multiplicative inverse of self.
     *
     * Uses Fermat's Little Theorem: a^(p-2) ≡ a^(-1) mod p.
     * Does not check that self is non-zero.
     */
    fun invertUnchecked(): FieldElement {
        val t111 = multiply(multiply(square()).square())
        val t111111 = t111.multiply(t111.sqn(3))
        val x15 =
            t111111
                .sqn(6)
                .multiply(t111111)
                .sqn(3)
                .multiply(t111)
        val x16 = x15.square().multiply(this)
        val i53 = x16.sqn(16).multiply(x16).sqn(15)
        val x47 = x15.multiply(i53)
        return x47
            .multiply(
                i53
                    .sqn(17)
                    .multiply(this)
                    .sqn(143)
                    .multiply(x47)
                    .sqn(47),
            ).sqn(2)
            .multiply(this)
    }

    /**
     * Returns the multiplicative inverse of self, or null if self is zero.
     */
    fun invert(): FieldElement? = if (isZero()) null else invertUnchecked()

    /**
     * Returns the square root of self mod p, or null if no square root exists.
     *
     * For secp256r1, p ≡ 3 mod 4, so:
     *     alpha = ± beta^((p + 1) / 4) mod p
     */
    fun sqrt(): FieldElement? {
        val t11 = multiply(square())
        val t1111 = t11.multiply(t11.sqn(2))
        val t11111111 = t1111.multiply(t1111.sqn(4))
        val x16 = t11111111.sqn(8).multiply(t11111111)
        val sqrt =
            x16
                .sqn(16)
                .multiply(x16)
                .sqn(32)
                .multiply(this)
                .sqn(96)
                .multiply(this)
                .sqn(94)
        return if (sqrt.square() == this) sqrt else null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldElement) return false
        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()

    override fun compareTo(other: FieldElement): Int = cmpLimbs(feFromMontgomery(value), feFromMontgomery(other.value))

    override fun toString(): String {
        val regular = feFromMontgomery(value)
        return "FieldElement(0x${limbsToHex(regular)})"
    }
}

// --- Free functions for Montgomery field arithmetic ---

/** Returns `a + b mod p`. */
internal fun feAdd(a: Fe, b: Fe): Fe {
    val (w0, c0) = Util.adc(a[0], b[0], 0uL)
    val (w1, c1) = Util.adc(a[1], b[1], c0)
    val (w2, c2) = Util.adc(a[2], b[2], c1)
    val (w3, w4) = Util.adc(a[3], b[3], c2)
    return subInner(
        ulongArrayOf(w0, w1, w2, w3, w4),
        ulongArrayOf(FIELD_MODULUS[0], FIELD_MODULUS[1], FIELD_MODULUS[2], FIELD_MODULUS[3], 0uL),
    )
}

/** Returns `a - b mod p`. */
internal fun feSub(a: Fe, b: Fe): Fe =
    subInner(ulongArrayOf(a[0], a[1], a[2], a[3], 0uL), ulongArrayOf(b[0], b[1], b[2], b[3], 0uL))

/** Returns `a * b mod p`. */
internal fun feMul(a: Fe, b: Fe): Fe {
    val (w0, c0) = Util.mac(0uL, a[0], b[0], 0uL)
    val (w1, c1) = Util.mac(0uL, a[0], b[1], c0)
    val (w2, c2) = Util.mac(0uL, a[0], b[2], c1)
    val (w3, w4) = Util.mac(0uL, a[0], b[3], c2)

    val (w1n, c3) = Util.mac(w1, a[1], b[0], 0uL)
    val (w2n, c4) = Util.mac(w2, a[1], b[1], c3)
    val (w3n, c5) = Util.mac(w3, a[1], b[2], c4)
    val (w4n, w5) = Util.mac(w4, a[1], b[3], c5)

    val (w2m, c6) = Util.mac(w2n, a[2], b[0], 0uL)
    val (w3m, c7) = Util.mac(w3n, a[2], b[1], c6)
    val (w4m, c8) = Util.mac(w4n, a[2], b[2], c7)
    val (w5m, w6) = Util.mac(w5, a[2], b[3], c8)

    val (w3f, c9) = Util.mac(w3m, a[3], b[0], 0uL)
    val (w4f, c10) = Util.mac(w4m, a[3], b[1], c9)
    val (w5f, c11) = Util.mac(w5m, a[3], b[2], c10)
    val (w6f, w7) = Util.mac(w6, a[3], b[3], c11)

    return montgomeryReduce(ulongArrayOf(w0, w1n, w2m, w3f, w4f, w5f, w6f, w7))
}

/** Returns `-w mod p`. */
internal fun feNeg(w: Fe): Fe = feSub(ulongArrayOf(0uL, 0uL, 0uL, 0uL), w)

/** Returns `w * w mod p`. */
internal fun feSquare(w: Fe): Fe = feMul(w, w)

/**
 * Montgomery Reduction.
 *
 * For secp256r1, the following simplifications apply:
 * - `p'` is 1, so our multiplicand is simply the first limb of the intermediate A.
 * - The first limb of p is 2^64 - 1; multiplications by this limb can be simplified
 *   to a shift and subtraction.
 * - The third limb of p is zero, so we can ignore any multiplications by it.
 */
internal fun montgomeryReduce(r: ULongArray): Fe {
    val r0 = r[0]
    val r1 = r[1]
    val r2 = r[2]
    val r3 = r[3]
    val r4 = r[4]
    val r5 = r[5]
    val r6 = r[6]
    val r7 = r[7]
    val m = FIELD_MODULUS

    // Round 0: k = r0, multiply by modulus limbs, add to r[1..4]
    val (r1a, c0) = Util.mac(r1, r0, m[1], r0)
    val (r2a, c1) = Util.adc(r2, 0uL, c0)
    val (r3a, c2) = Util.mac(r3, r0, m[3], c1)
    val (r4a, c2b) = Util.adc(r4, 0uL, c2)

    // Round 1: k = r1a
    val (r2b, c3) = Util.mac(r2a, r1a, m[1], r1a)
    val (r3b, c4) = Util.adc(r3a, 0uL, c3)
    val (r4b, c5) = Util.mac(r4a, r1a, m[3], c4)
    val (r5b, c5b) = Util.adc(r5, c2b, c5)

    // Round 2: k = r2b
    val (r3c, c6) = Util.mac(r3b, r2b, m[1], r2b)
    val (r4c, c7) = Util.adc(r4b, 0uL, c6)
    val (r5c, c8) = Util.mac(r5b, r2b, m[3], c7)
    val (r6c, c8b) = Util.adc(r6, c5b, c8)

    // Round 3: k = r3c
    val (r4d, c9) = Util.mac(r4c, r3c, m[1], r3c)
    val (r5d, c10) = Util.adc(r5c, 0uL, c9)
    val (r6d, c11) = Util.mac(r6c, r3c, m[3], c10)
    val (r7d, r8) = Util.adc(r7, c8b, c11)

    return subInner(
        ulongArrayOf(r4d, r5d, r6d, r7d, r8),
        ulongArrayOf(m[0], m[1], m[2], m[3], 0uL),
    )
}

private fun subInner(l: ULongArray, r: ULongArray): Fe {
    val (w0, b0) = Util.sbb(l[0], r[0], 0uL)
    val (w1, b1) = Util.sbb(l[1], r[1], b0)
    val (w2, b2) = Util.sbb(l[2], r[2], b1)
    val (w3, b3) = Util.sbb(l[3], r[3], b2)
    val (_, b4) = Util.sbb(l[4], r[4], b3)

    val m = FIELD_MODULUS
    val borrow = b4
    val (w0a, ca0) = Util.adc(w0, m[0] and borrow, 0uL)
    val (w1a, ca1) = Util.adc(w1, m[1] and borrow, ca0)
    val (w2a, ca2) = Util.adc(w2, m[2] and borrow, ca1)
    val (w3a, _) = Util.adc(w3, m[3] and borrow, ca2)

    return ulongArrayOf(w0a, w1a, w2a, w3a)
}

// --- Utility functions ---

/** Compares two limb arrays (little-endian). Returns -1, 0, or 1. */
internal fun cmpLimbs(a: ULongArray, b: ULongArray): Int {
    for (i in 3 downTo 0) {
        if (a[i] < b[i]) return -1
        if (a[i] > b[i]) return 1
    }
    return 0
}

/** Converts little-endian limbs to a big-endian hex string. */
internal fun limbsToHex(limbs: ULongArray): String {
    val sb = StringBuilder()
    for (i in 3 downTo 0) {
        sb.append(limbs[i].toString(16).padStart(16, '0'))
    }
    return sb.toString()
}

/** Converts a big-endian hex string to a byte array. */
internal fun hexToBytes(hex: String): ByteArray {
    val clean = if (hex.startsWith("0x") || hex.startsWith("0X")) hex.substring(2) else hex
    val padded = clean.padStart(64, '0')
    return ByteArray(padded.length / 2) { i ->
        val hi = padded[i * 2].hexDigitToInt()
        val lo = padded[i * 2 + 1].hexDigitToInt()
        ((hi shl 4) or lo).toByte()
    }
}

private fun Char.hexDigitToInt(): Int =
    when (this) {
        in '0'..'9' -> this - '0'
        in 'a'..'f' -> this - 'a' + 10
        in 'A'..'F' -> this - 'A' + 10
        else -> 0
    }

/** Converts a big-endian byte array to little-endian limbs. */
internal fun bytesToLimbs(bytes: ByteArray): ULongArray {
    require(bytes.size == 32) { "Expected 32 bytes, got ${bytes.size}" }
    val limbs = ULongArray(4)
    for (i in 0 until 4) {
        var limb = 0uL
        // bytes[31 - i*8] is the least significant byte of limb i (big-endian input)
        // bytes[24 - i*8] is the most significant byte of limb i
        for (j in 0 until 8) {
            // j=0 reads the least significant byte, j=7 reads the most significant
            val b = bytes[31 - i * 8 - j].toInt() and 0xFF
            limb = limb or (b.toULong() shl (j * 8))
        }
        limbs[i] = limb
    }
    return limbs
}
