// port-lint: source p256/src/ecdh.rs
package io.github.kotlinmania.p256

/**
 * Elliptic Curve Diffie-Hellman (Ephemeral) Support.
 *
 * The upstream Rust module provides ephemeral Diffie-Hellman key exchange using
 * secp256r1. It delegates entirely to `elliptic_curve::ecdh`, which requires the
 * `elliptic-curve` trait ecosystem (no `elliptic-curve-kotlin` kotlinmania sibling
 * exists). The ECDH computation also requires point arithmetic from `primeorder`,
 * which is likewise absent.
 *
 * The field and scalar arithmetic needed for a future ECDH implementation are
 * fully ported in [FieldElement] and [Scalar]. Once `elliptic-curve-kotlin` and
 * `primeorder-kotlin` are created, the ECDH types can be wired here.
 */