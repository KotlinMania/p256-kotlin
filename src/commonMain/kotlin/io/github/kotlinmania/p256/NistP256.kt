// port-lint: source lib.rs
package io.github.kotlinmania.p256

import io.github.kotlinmania.zeroize.Zeroize
import io.github.kotlinmania.zeroize.ZeroizeOnDrop

/**
 * NIST P-256 elliptic curve.
 *
 * This curve is also known as prime256v1 (ANSI X9.62) and secp256r1 (SECG)
 * and is specified in NIST SP 800-186: Recommendations for Discrete
 * Logarithm-based Cryptography: Elliptic Curve Domain Parameters.
 */
object NistP256 {
    /** 32-byte serialized field elements. */
    const val FIELD_BYTES_SIZE: Int = 32

    /**
     * Field modulus p = 2^{224}(2^{32} − 1) + 2^{192} + 2^{96} − 1
     */
    val MODULUS: ULongArray =
        ulongArrayOf(
            0xFFFFFFFFFFFFFFFFu,
            0x00000000FFFFFFFFu,
            0x0000000000000000u,
            0xFFFFFFFF00000001u,
        )

    /**
     * Order of NIST P-256's elliptic curve group (i.e. scalar modulus).
     */
    val ORDER: ULongArray =
        ulongArrayOf(
            0xF3B9CAC2FC632551u,
            0xBCE6FAADA7179E84u,
            0xFFFFFFFFFFFFFFFFu,
            0xFFFFFFFF00000000u,
        )

    /** NIST P-256 points are typically uncompressed. */
    const val COMPRESS_POINTS: Boolean = false

    /** NIST P-256 points are typically uncompressed. */
    const val COMPACT_POINTS: Boolean = false

    /** OID for NIST P-256 in PKCS#8: 1.2.840.10045.3.1.7 */
    const val OID: String = "1.2.840.10045.3.1.7"

    /** JWK curve identifier. */
    const val CRV: String = "P-256"

    /** Decodes field bytes into a FieldElement. */
    fun decodeFieldBytes(bytes: ByteArray): FieldElement = FieldElement.fromBytes(bytes)

    /** Encodes a FieldElement into field bytes (32 bytes big-endian). */
    fun encodeFieldBytes(fe: FieldElement): ByteArray = fe.toBytes()
}

/** Non-zero scalar in NIST P-256 group. */
class NonZeroScalar(
    val scalar: Scalar,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    init {
        require(!scalar.isZero()) { "Scalar must not be zero" }
    }

    override fun zeroize() {
        scalar.zeroize()
    }

    override fun close() {
        zeroize()
    }

    companion object {
        /** Generates a random non-zero scalar using `getrandom-kotlin`. */
        fun random(): NonZeroScalar {
            while (true) {
                val s = Scalar.random()
                if (!s.isZero()) {
                    return NonZeroScalar(s)
                }
            }
        }
    }
}

/** Public key represented by an AffinePoint. */
class PublicKey(
    val point: AffinePoint,
)

/** Secret key represented by a NonZeroScalar. */
class SecretKey(
    val scalar: NonZeroScalar,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    /** Serializes this secret key as 32 big-endian bytes. */
    fun toBytes(): ByteArray = scalar.scalar.toBytes()

    override fun zeroize() {
        scalar.zeroize()
    }

    override fun close() {
        zeroize()
    }

    companion object {
        /** Generates a random secret key using `getrandom-kotlin`. */
        fun random(): SecretKey = SecretKey(NonZeroScalar.random())

        /** Parses a secret key from 32 big-endian bytes, or returns null if invalid or zero. */
        fun fromBytes(bytes: ByteArray): SecretKey? {
            val s = Scalar.fromBytes(bytes) ?: return null
            if (s.isZero()) return null
            return SecretKey(NonZeroScalar(s))
        }
    }
}

/** Encoded point bytes. */
class EncodedPoint(
    val bytes: ByteArray,
)
