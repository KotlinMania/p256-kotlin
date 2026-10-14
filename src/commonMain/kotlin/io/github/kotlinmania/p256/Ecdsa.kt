// port-lint: source ecdsa.rs
package io.github.kotlinmania.p256

import io.github.kotlinmania.constanttimeeq.constantTimeEq32
import io.github.kotlinmania.digest.Update
import io.github.kotlinmania.sha2.Sha256
import io.github.kotlinmania.sha2.Sha384
import io.github.kotlinmania.zeroize.Zeroize
import io.github.kotlinmania.zeroize.ZeroizeOnDrop

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
        return constantTimeEq32(r, other.r) && constantTimeEq32(s, other.s)
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
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    constructor(scalar: Scalar) : this(NonZeroScalar(scalar))

    fun toBytes(): ByteArray = secretScalar.scalar.toBytes()

    override fun zeroize() {
        secretScalar.zeroize()
    }

    override fun close() {
        zeroize()
    }

    companion object {
        /** Generates a random ECDSA/P-256 signing key using `getrandom-kotlin`. */
        fun random(): SigningKey = SigningKey(NonZeroScalar.random())

        /** Parses a signing key from 32 big-endian bytes, or returns null if invalid or zero. */
        fun fromBytes(bytes: ByteArray): SigningKey? {
            val s = Scalar.fromBytes(bytes) ?: return null
            if (s.isZero()) return null
            return SigningKey(NonZeroScalar(s))
        }

        /** Computes the default SHA-256 message digest for ECDSA/P-256 signing/verification. */
        fun sha256Digest(message: ByteArray): ByteArray = Sha256.digest(message)

        /** Computes a SHA-384 message digest for ECDSA/P-256 signing/verification. */
        fun sha384Digest(message: ByteArray): ByteArray = Sha384.digest(message)

        /** Creates a new streaming SHA-256 digest updater (`digest-kotlin` [Update]). */
        internal fun newSha256Updater(): Update =
            object : Update {
                private val hasher = Sha256.new()

                override fun update(data: ByteArray) {
                    hasher.update(data)
                }
            }
    }
}

/**
 * ECDSA/P-256 verifying key (public key).
 */
class VerifyingKey(
    val affinePoint: AffinePoint,
)
