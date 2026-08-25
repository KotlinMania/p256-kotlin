// port-lint: source arithmetic.rs
package io.github.kotlinmania.p256

/**
 * Pure Kotlin implementation of group operations on secp256r1.
 *
 * Curve parameters can be found in NIST SP 800-186 § G.1.2: Curve P-256.
 *
 * The upstream Rust crate delegates point arithmetic to the `primeorder` crate,
 * which provides generic prime-order curve operations using the `elliptic-curve`
 * trait ecosystem. No `primeorder-kotlin` or `elliptic-curve-kotlin` sibling repo
 * exists in the kotlinmania workspace, so the affine/projective point types and
 * point arithmetic are not ported here. The field and scalar arithmetic
 * ([FieldElement] and [Scalar]) are self-contained and fully implemented.
 */

/**
 * Curve parameters for NIST P-256.
 *
 * The equation is y² = x³ - 3x + b (i.e., a = -3).
 */

object CurveParams {
    /** a = -3 (the curve equation coefficient). */
    val EQUATION_A: FieldElement = FieldElement.fromU64(3uL).negate()

    /** b = 5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b */
    val EQUATION_B: FieldElement =
        FieldElement.fromHex(
            "5ac635d8aa3a93e7b3ebbd55769886bc651d06b0cc53b0f63bce3c3e27d2604b",
        )

    /**
     * Base point (generator) of P-256.
     *
     * Defined in NIST SP 800-186 § G.1.2:
     *
     *     Gx = 6b17d1f2 e12c4247 f8bce6e5 63a440f2 77037d81 2deb33a0 f4a13945 d898c296
     *     Gy = 4fe342e2 fe1a7f9b 8ee7eb4a 7c0f9e16 2bce3357 6b315ece cbb64068 37bf51f5
     */
    val GENERATOR_X: FieldElement =
        FieldElement.fromHex(
            "6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296",
        )

    val GENERATOR_Y: FieldElement =
        FieldElement.fromHex(
            "4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5",
        )
}

/**
 * Elliptic curve point in affine coordinates.
 *
 * The upstream Rust type is `primeorder::AffinePoint<NistP256>`, which requires
 * the `primeorder` and `elliptic-curve` trait ecosystem. Since no kotlinmania
 * sibling repos exist for those crates, affine point operations (point addition,
 * point doubling, scalar multiplication, point compression/decompression) are
 * not ported. The field and scalar arithmetic they depend on IS ported in
 * [FieldElement] and [Scalar].
 *
 * This type is provided as a data holder for coordinates.
 */

data class AffinePoint(
    val x: FieldElement,
    val y: FieldElement,
) {
    companion object {
        /** The point at infinity (identity element). */
        val IDENTITY: AffinePoint = AffinePoint(FieldElement.ZERO, FieldElement.ZERO)
    }
}

/**
 * Elliptic curve point in projective coordinates.
 *
 * The upstream Rust type is `primeorder::ProjectivePoint<NistP256>`. Point
 * arithmetic (add, double, scalar multiplication) requires the `primeorder`
 * crate's generic implementation, which is not ported (no kotlinmania sibling).
 *
 * This type is provided as a data holder for coordinates.
 */

data class ProjectivePoint(
    val x: FieldElement,
    val y: FieldElement,
    val z: FieldElement,
) {
    companion object {
        /** The point at infinity (identity element). */
        val IDENTITY: ProjectivePoint = ProjectivePoint(FieldElement.ZERO, FieldElement.ONE, FieldElement.ZERO)
    }
}
