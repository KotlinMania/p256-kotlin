# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/16 (43.8%)
- **Function parity:** 29/109 matched (target 114) — 26.6%
- **Class/type parity:** 5/39 matched (target 13) — 12.8%
- **Combined symbol parity:** 34/148 matched (target 127) — 23.0%
- **Average inline-code cosine:** 0.37 (function body across 4 matched files)
- **Average documentation cosine:** 0.63 (doc text across 4 matched files)
- **Cheat-zeroed Files:** 3
- **Critical Issues:** 6 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. arithmetic.scalar

- **Target:** `p256.Scalar [PROVENANCE-FALLBACK]`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 375408.6
- **Functions:** 16/48 matched (target 49)
- **Missing functions:** `as_ref`, `random`, `sqrt`, `sqrt_ratio`, `from_repr`, `to_repr`, `to_le_bits`, `char_le_bits`, `shr`, `shr_assign`, `eq`, `partial_cmp`, `cmp`, `from`, `add_assign`, `sub_assign`, `mul`, `mul_assign`, `neg`, `reduce`, `reduce_bytes`, `reduce_nonzero`, `reduce_nonzero_bytes`, `sum`, `product`, `conditional_select`, `ct_eq`, `fmt`, `serialize`, `deserialize`, `from_ec_secret`, `scalar_into_scalarbits`
- **Types:** 1/6 matched (target 2)
- **Missing types:** `Repr`, `ReprBits`, `Uint`, `Output`, `Bytes`
- **Tests:** 1/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:p256/src/arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
- **Proposed provenance header:** `// port-lint: source arithmetic/scalar.rs` (current: `// port-lint: source p256/src/arithmetic/scalar.rs`)
- **Proposed provenance header:** `// port-lint: tests arithmetic/scalar.rs` (current: `// port-lint: tests p256/src/arithmetic/scalar.rs`)
- **Lint issues:** 2

### 2. lib

- **Target:** `p256.NistP256 [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 131410.0
- **Functions:** 0/2 matched (target 10)
- **Missing functions:** `decode_field_bytes`, `encode_field_bytes`
- **Types:** 1/12 matched (target 2)
- **Missing types:** `FieldBytesSize`, `Uint`, `BlindedScalar`, `CompressedPoint`, `EncodedPoint`, `FieldBytes`, `NonZeroScalar`, `PublicKey`, `SecretKey`, `ScalarBits`, `Hash`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:p256/src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source p256/src/lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests p256/src/lib.rs`)
- **Lint issues:** 2

### 3. ecdsa

- **Target:** `p256.Ecdsa [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 90910.0
- **Functions:** 0/4 matched (target 0)
- **Missing functions:** `rfc6979`, `prehash_signer_signing_with_sha384`, `prehash_signer_verification_with_sha384`, `scalar_blinding`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `Signature`, `DerSignature`, `SigningKey`, `VerifyingKey`, `Digest`
- **Tests:** 0/4 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/ecdsa.rs` vs expected `ecdsa.rs`
- **Proposed provenance header:** `// port-lint: source ecdsa.rs` (current: `// port-lint: source p256/src/ecdsa.rs`)
- **Lint issues:** 1

### 4. arithmetic.field

- **Target:** `p256.FieldElement [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 61710.0
- **Functions:** 10/15 matched (target 51)
- **Missing functions:** `from_repr`, `to_repr`, `fmt`, `from_bytes`, `to_bytes`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Repr`
- **Tests:** 5/7 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/arithmetic/field.rs` vs expected `arithmetic/field.rs`
- **Provenance warning:** port-lint provenance header matched only by basename: `tests:p256/src/test_vectors/field.rs` vs expected `arithmetic/field.rs`
- **Proposed provenance header:** `// port-lint: source arithmetic/field.rs` (current: `// port-lint: source p256/src/arithmetic/field.rs`)
- **Proposed provenance header:** `// port-lint: tests arithmetic/field.rs` (current: `// port-lint: tests p256/src/test_vectors/field.rs`)
- **Lint issues:** 2

### 5. arithmetic

- **Target:** `p256.Arithmetic [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 40600.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 2/6 matched (target 3)
- **Missing types:** `Scalar`, `CurveGroup`, `FieldElement`, `PointArithmetic`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/arithmetic.rs` vs expected `arithmetic.rs`
- **Proposed provenance header:** `// port-lint: source arithmetic.rs` (current: `// port-lint: source p256/src/arithmetic.rs`)
- **Lint issues:** 1

### 6. ecdh

- **Target:** `p256.Ecdh [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 20200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/2 matched (target 1)
- **Missing types:** `EphemeralSecret`, `SharedSecret`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/ecdh.rs` vs expected `ecdh.rs`
- **Proposed provenance header:** `// port-lint: source ecdh.rs` (current: `// port-lint: source p256/src/ecdh.rs`)
- **Lint issues:** 1

### 7. arithmetic.util

- **Target:** `p256.Util [PROVENANCE-FALLBACK]`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 306.6
- **Functions:** 3/3 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `p256/src/arithmetic/util.rs` vs expected `arithmetic/util.rs`
- **Proposed provenance header:** `// port-lint: source arithmetic/util.rs` (current: `// port-lint: source p256/src/arithmetic/util.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

