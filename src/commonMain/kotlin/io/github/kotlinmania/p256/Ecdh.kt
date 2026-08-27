// port-lint: source p256/src/ecdh.rs
package io.github.kotlinmania.p256

/**
 * Ephemeral Diffie-Hellman secret for NIST P-256.
 */
class EphemeralSecret(
    val secretScalar: NonZeroScalar,
) {
    constructor(scalar: Scalar) : this(NonZeroScalar(scalar))
}

/**
 * Shared secret computed via ECDH.
 */
class SharedSecret(
    val rawBytes: ByteArray,
) {
    init {
        require(rawBytes.size == 32) { "Shared secret must be 32 bytes" }
    }

    fun rawSecretBytes(): ByteArray = rawBytes
}
