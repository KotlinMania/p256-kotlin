# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/23 (30.4%)
- **Function parity:** 29/143 matched (target 110) — 20.3%
- **Class/type parity:** 9/27 matched (target 14) — 33.3%
- **Combined symbol parity:** 38/170 matched (target 124) — 22.4%
- **Average inline-code cosine:** 0.34 (function body across 6 matched files)
- **Average documentation cosine:** 0.61 (doc text across 6 matched files)
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

### 2. arithmetic.field

- **Target:** `p256.FieldElement`
- **Similarity:** 0.56
- **Dependents:** 0
- **Priority Score:** 61704.4
- **Functions:** 10/15 matched (target 51)
- **Missing functions:** `from_repr`, `to_repr`, `fmt`, `from_bytes`, `to_bytes`
- **Types:** 1/2 matched (target 3)
- **Missing types:** `Repr`
- **Tests:** 5/7 matched

### 3. p256.ecdsa

- **Target:** `p256.Ecdsa`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 60910.0
- **Functions:** 0/4 matched (target 5)
- **Missing functions:** `rfc6979`, `prehash_signer_signing_with_sha384`, `prehash_signer_verification_with_sha384`, `scalar_blinding`
- **Types:** 3/5 matched (target 3)
- **Missing types:** `DerSignature`, `Digest`
- **Tests:** 0/4 matched

### 4. p256.arithmetic

- **Target:** `p256.Arithmetic`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 40600.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 2/6 matched (target 3)
- **Missing types:** `Scalar`, `CurveGroup`, `FieldElement`, `PointArithmetic`

### 5. arithmetic.util

- **Target:** `p256.Util`
- **Similarity:** 0.34
- **Dependents:** 0
- **Priority Score:** 306.6
- **Functions:** 3/3 matched (target 4)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_

### 6. p256.ecdh

- **Target:** `p256.Ecdh [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 210.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 2/2 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `p256.lib` | `p256.NistP256` | `p256/src/lib` |

