// port-lint: source p256/src/ecdsa.rs
package io.github.kotlinmania.p256

/**
 * Elliptic Curve Digital Signature Algorithm (ECDSA) for NIST P-256.
 *
 * The upstream Rust module provides ECDSA signing and verification via the
 * `ecdsa` crate (re-exported as `ecdsa_core`). It requires:
 * - `ecdsa_core::Signature<NistP256>` — the signature type
 * - `ecdsa_core::SigningKey<NistP256>` / `ecdsa_core::VerifyingKey<NistP256>`
 * - `ecdsa_core::hazmat::SignPrimitive` / `VerifyPrimitive` trait impls on
 *   [Scalar] and [AffinePoint]
 * - `sha2::Sha256` for the digest
 *
 * No `ecdsa-kotlin` or `elliptic-curve-kotlin` kotlinmania sibling repos exist,
 * so the ECDSA signing/verification pipeline is not ported. The [Scalar] and
 * [FieldElement] arithmetic that ECDSA depends on IS fully implemented.
 *
 * The RFC 6979 deterministic nonce generation and signature computation require
 * HMAC-SHA256 (from `sha2-kotlin` / `hmac-kotlin`, both published on Maven Central)
 * plus point arithmetic from `primeorder-kotlin` (not yet created). Once the
 * dependency chain is complete, the ECDSA types and sign/verify operations can
 * be wired here.
 */