// port-lint: source p256/src/ecdsa.rs
package io.github.kotlinmania.p256

/**
 * ECDSA/P-256 fixed-size signature (r, s as 32-byte arrays).
 */
data class Signature(
    val r: ByteArray,
    val s: ByteArray,
) {
    init {
        require(r.size == 32) { "r must be 32 bytes" }
        require(s.size == 32) { "s must be 32 bytes" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Signature) return false
        return r.contentEquals(other.r) && s.contentEquals(other.s)
    }

    override fun hashCode(): Int {
        var result = r.contentHashCode()
        result = 31 * result + s.contentHashCode()
        return result
    }

    fun toBytes(): ByteArray = r + s

    companion object {
        fun fromBytes(bytes: ByteArray): Signature {
            require(bytes.size == 64) { "Signature must be 64 bytes" }
            val r = bytes.copyOfRange(0, 32)
            val s = bytes.copyOfRange(32, 64)
            return Signature(r, s)
        }
    }
}

/**
 * ECDSA/P-256 signing key.
 */
class SigningKey(
    val secretScalar: NonZeroScalar,
) {
    constructor(scalar: Scalar) : this(NonZeroScalar(scalar))

    fun toBytes(): ByteArray = secretScalar.scalar.toBytes()
}

/**
 * ECDSA/P-256 verifying key (public key).
 */
class VerifyingKey(
    val affinePoint: AffinePoint,
)
