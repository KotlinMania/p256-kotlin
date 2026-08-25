// port-lint: source lib.rs
package io.github.kotlinmania.p256

/**
 * NIST P-256 elliptic curve.
 *
 * This curve is also known as prime256v1 (ANSI X9.62) and secp256r1 (SECG)
 * and is specified in NIST SP 800-186: Recommendations for Discrete
 * Logarithm-based Cryptography: Elliptic Curve Domain Parameters.
 *
 * It's included in the US National Security Agency's "Suite B" and is widely
 * used in protocols like TLS and the associated X.509 PKI.
 *
 * Its equation is `y² = x³ - 3x + b` over a ~256-bit prime field where `b` is
 * the "verifiably random" constant:
 *
 *     b = 41058363725152142129326129780047268409114441015993725554835256314039467401291
 *
 * The specific origins of this constant have never been fully disclosed
 * (it is the SHA-1 digest of an unknown NSA-selected constant).
 */

object NistP256 {
    /** 32-byte serialized field elements. */
    const val FIELD_BYTES_SIZE: Int = 32

    /**
     * Field modulus p = 2^{224}(2^{32} − 1) + 2^{192} + 2^{96} − 1
     *
     * Serialized as hexadecimal:
     *     p = FFFFFFFF 00000001 00000000 00000000 00000000 FFFFFFFF FFFFFFFF FFFFFFFF
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
     *
     *     n = FFFFFFFF 00000000 FFFFFFFF FFFFFFFF BCE6FAAD A7179E84 F3B9CAC2 FC632551
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
}
