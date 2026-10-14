// port-lint: source ecdh.rs
package io.github.kotlinmania.p256

import io.github.kotlinmania.constanttimeeq.constantTimeEq32
import io.github.kotlinmania.zeroize.Zeroize
import io.github.kotlinmania.zeroize.ZeroizeOnDrop
import io.github.kotlinmania.zeroize.zeroize

/**
 * Ephemeral Diffie-Hellman secret for NIST P-256.
 */
class EphemeralSecret(
    val secretScalar: NonZeroScalar,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    constructor(scalar: Scalar) : this(NonZeroScalar(scalar))

    override fun zeroize() {
        secretScalar.zeroize()
    }

    override fun close() {
        zeroize()
    }

    companion object {
        /** Generates a random ephemeral secret using `getrandom-kotlin`. */
        fun random(): EphemeralSecret = EphemeralSecret(NonZeroScalar.random())
    }
}

/**
 * Shared secret computed via ECDH.
 */
class SharedSecret(
    val rawBytes: ByteArray,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    init {
        require(rawBytes.size == 32) { "Shared secret must be 32 bytes" }
    }

    fun rawSecretBytes(): ByteArray = rawBytes

    override fun zeroize() {
        rawBytes.zeroize()
    }

    override fun close() {
        zeroize()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SharedSecret) return false
        return constantTimeEq32(rawBytes, other.rawBytes)
    }

    override fun hashCode(): Int = rawBytes.contentHashCode()
}
