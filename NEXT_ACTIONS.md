# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/16 (43.8%)
- **Function parity:** 29/109 matched (target 114) — 26.6%
- **Class/type parity:** 5/39 matched (target 13) — 12.8%
- **Combined symbol parity:** 34/148 matched (target 127) — 23.0%
- **Average inline-code cosine:** 0.41 (function body across 5 matched files)
- **Average documentation cosine:** 0.66 (doc text across 5 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 6 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. arithmetic.scalar

- **Target:** `p256.Scalar`
- **Similarity:** 0.14
- **Dependents:** 0
- **Priority Score:** 375408.6
- **Functions:** 16/48 matched (target 49)
- **Missing functions:** `as_ref`, `random`, `sqrt`, `sqrt_ratio`, `from_repr`, `to_repr`, `to_le_bits`, `char_le_bits`, `shr`, `shr_assign`, `eq`, `partial_cmp`, `cmp`, `from`, `add_assign`, `sub_assign`, `mul`, `mul_assign`, `neg`, `reduce`, `reduce_bytes`, `reduce_nonzero`, `reduce_nonzero_bytes`, `sum`, `product`, `conditional_select`, `ct_eq`, `fmt`, `serialize`, `deserialize`, `from_ec_secret`, `scalar_into_scalarbits`
- **Types:** 1/6 matched (target 2)
- **Missing types:** `Repr`, `ReprBits`, `Uint`, `Output`, `Bytes`
- **Tests:** 1/3 matched

### 2. lib

- **Target:** `p256.NistP256`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 131410.0
- **Functions:** 0/2 matched (target 10)
- **Missing functions:** `decode_field_bytes`, `encode_field_bytes`
- **Types:** 1/12 matched (target 2)
- **Missing types:** `FieldBytesSize`, `Uint`, `BlindedScalar`, `CompressedPoint`, `EncodedPoint`, `FieldBytes`, `NonZeroScalar`, `PublicKey`, `SecretKey`, `ScalarBits`, `Hash`

### 3. ecdsa

- **Target:** `p256.Ecdsa [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 90910.0
- **Functions:** 0/4 matched (target 0)
- **Missing functions:** `rfc6979`, `prehash_signer_signing_with_sha384`, `prehash_signer_verification_with_sha384`, `scalar_blinding`
- **Types:** 0/5 matched (target 1)
- **Missing types:** `Signature`, `DerSignature`, `SigningKey`, `VerifyingKey`, `Digest`
- **Tests:** 0/4 matched

### 4. arithmetic.field

- **Target:** `p256.FieldElement`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 61704.4
- **Functions:** 10/15 matched (target 51)
- **Missing functions:** `from_repr`, `to_repr`, `fmt`, `from_bytes`, `to_bytes`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Repr`
- **Tests:** 5/7 matched

### 5. arithmetic

- **Target:** `p256.Arithmetic`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 40600.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 2/6 matched (target 3)
- **Missing types:** `Scalar`, `CurveGroup`, `FieldElement`, `PointArithmetic`

### 6. ecdh

- **Target:** `p256.Ecdh [STUB]`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 20200.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/2 matched (target 1)
- **Missing types:** `EphemeralSecret`, `SharedSecret`

### 7. arithmetic.util

- **Target:** `p256.Util`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 306.6
- **Functions:** 3/3 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

