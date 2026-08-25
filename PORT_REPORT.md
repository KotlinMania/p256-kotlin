=== Deep Analysis: tmp (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp (rust)
  Files: 23
  Total imports: 84
  Most depended: tests.pkcs8 (1 dependents)

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 10
  Total imports: 14

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp (23 files)
Target: src/commonMain/kotlin (10 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   7 files
Unmatched: 16 source, 0 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
arithmetic.scalar             p256.Scalar [PROVENANCE-FALLBACK]0.14      0          16/48         1/6         375408.6  
p256.lib                      p256.NistP256 [PROVENANCE-FALLBACK]0.00      0          0/2           1/12        131410.0  
p256.ecdsa                    p256.Ecdsa [STUB] [PROVENANCE-FALLBACK]0.00      0          0/4           0/5         90910.0   
arithmetic.field              p256.FieldElement [PROVENANCE-FALLBACK]0.56      0          10/15         1/2         61704.4   
p256.arithmetic               p256.Arithmetic [PROVENANCE-FALLBACK]1.00      0          0/0           2/6         40600.0   
p256.ecdh                     p256.Ecdh [STUB] [PROVENANCE-FALLBACK]1.00      0          0/0           0/2         20200.0   
arithmetic.util               p256.Util [PROVENANCE-FALLBACK]0.34      0          3/3           0/0         306.6     

=== Function and Symbol Details ===

arithmetic.scalar -> p256.Scalar [PROVENANCE-FALLBACK]
  similarity: 0.14, priority: 375408.6, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
  functions: 16/48 matched (target total: 49, required body score: 0.14)
  missing functions: as_ref, random, sqrt, sqrt_ratio, from_repr, to_repr, to_le_bits, char_le_bits, shr, shr_assign, eq, partial_cmp, cmp, from, add_assign, sub_assign, mul, mul_assign, neg, reduce, reduce_bytes, reduce_nonzero, reduce_nonzero_bytes, sum, product, conditional_select, ct_eq, fmt, serialize, deserialize, from_ec_secret, scalar_into_scalarbits
  types: 1/6 matched (target total: 2)
  missing types: Repr, ReprBits, Uint, Output, Bytes
  tests: 1/3 matched

p256.lib -> p256.NistP256 [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 131410.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
  functions: 0/2 matched (target total: 10, required body score: 0.00)
  missing functions: decode_field_bytes, encode_field_bytes
  types: 1/12 matched (target total: 2)
  missing types: FieldBytesSize, Uint, BlindedScalar, CompressedPoint, EncodedPoint, FieldBytes, NonZeroScalar, PublicKey, SecretKey, ScalarBits, Hash

p256.ecdsa -> p256.Ecdsa [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 90910.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `ecdsa.rs` vs expected `ecdsa.rs`
  functions: 0/4 matched (target total: 0, required body score: 0.00)
  missing functions: rfc6979, prehash_signer_signing_with_sha384, prehash_signer_verification_with_sha384, scalar_blinding
  types: 0/5 matched (target total: 1)
  missing types: Signature, DerSignature, SigningKey, VerifyingKey, Digest
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: no target functions found; report scoring is function-by-function only
  tests: 0/4 matched

arithmetic.field -> p256.FieldElement [PROVENANCE-FALLBACK]
  similarity: 0.56, priority: 61704.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `arithmetic/field.rs` vs expected `arithmetic/field.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:arithmetic/field.rs` vs expected `arithmetic/field.rs`
  functions: 10/15 matched (target total: 51, required body score: 0.56)
  missing functions: from_repr, to_repr, fmt, from_bytes, to_bytes
  types: 1/2 matched (target total: 3)
  missing types: Repr
  tests: 5/7 matched

p256.arithmetic -> p256.Arithmetic [PROVENANCE-FALLBACK]
  similarity: 1.00, priority: 40600.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `arithmetic.rs` vs expected `arithmetic.rs`
  functions: 0/0 matched (target total: 0, required body score: 1.00)
  missing functions: none
  types: 2/6 matched (target total: 3)
  missing types: Scalar, CurveGroup, FieldElement, PointArithmetic

p256.ecdh -> p256.Ecdh [STUB] [PROVENANCE-FALLBACK]
  similarity: 1.00, priority: 20200.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `ecdh.rs` vs expected `ecdh.rs`
  functions: 0/0 matched (target total: 0, required body score: 1.00)
  missing functions: none
  types: 0/2 matched (target total: 1)
  missing types: EphemeralSecret, SharedSecret

arithmetic.util -> p256.Util [PROVENANCE-FALLBACK]
  similarity: 0.34, priority: 306.6, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `arithmetic/util.rs` vs expected `arithmetic/util.rs`
  functions: 3/3 matched (target total: 4, required body score: 0.34)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none


=== Scores Forced To 0 ===

  - p256.ecdsa -> p256.Ecdsa: no target functions found; report scoring is function-by-function only

=== Provenance Header Fallbacks ===

These files were paired only after normalization; fix the port-lint source header.
  - arithmetic.scalar -> p256.Scalar: port-lint provenance header matched only after fallback normalization: `arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
    proposed: // port-lint: source arithmetic/scalar.rs
  - arithmetic.scalar -> p256.Scalar: port-lint provenance header matched only after fallback normalization: `tests:arithmetic/scalar.rs` vs expected `arithmetic/scalar.rs`
    proposed: // port-lint: tests arithmetic/scalar.rs
  - p256.lib -> p256.NistP256: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
    proposed: // port-lint: source lib.rs
  - p256.lib -> p256.NistP256: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
    proposed: // port-lint: tests lib.rs
  - p256.ecdsa -> p256.Ecdsa: port-lint provenance header matched only after fallback normalization: `ecdsa.rs` vs expected `ecdsa.rs`
    proposed: // port-lint: source ecdsa.rs
  - arithmetic.field -> p256.FieldElement: port-lint provenance header matched only after fallback normalization: `arithmetic/field.rs` vs expected `arithmetic/field.rs`
    proposed: // port-lint: source arithmetic/field.rs
  - arithmetic.field -> p256.FieldElement: port-lint provenance header matched only after fallback normalization: `tests:arithmetic/field.rs` vs expected `arithmetic/field.rs`
    proposed: // port-lint: tests arithmetic/field.rs
  - p256.arithmetic -> p256.Arithmetic: port-lint provenance header matched only after fallback normalization: `arithmetic.rs` vs expected `arithmetic.rs`
    proposed: // port-lint: source arithmetic.rs
  - p256.ecdh -> p256.Ecdh: port-lint provenance header matched only after fallback normalization: `ecdh.rs` vs expected `ecdh.rs`
    proposed: // port-lint: source ecdh.rs
  - arithmetic.util -> p256.Util: port-lint provenance header matched only after fallback normalization: `arithmetic/util.rs` vs expected `arithmetic/util.rs`
    proposed: // port-lint: source arithmetic/util.rs

=== Missing from Target (need to port) ===

File                          Deps    Path
------------------------------------------------------------------------------
tests.pkcs8                   1       p256/tests/pkcs8.rs
tests.scalar                  1       p256/tests/scalar.rs
benches.field                 0       p256/benches/field.rs
benches.scalar                0       p256/benches/scalar.rs
field.field32                 0       p256/src/arithmetic/field/field32.rs
field.field64                 0       p256/src/arithmetic/field/field64.rs
arithmetic.hash2curve         0       p256/src/arithmetic/hash2curve.rs
scalar.scalar32               0       p256/src/arithmetic/scalar/scalar32.rs
scalar.scalar64               0       p256/src/arithmetic/scalar/scalar64.rs
p256.test_vectors             0       p256/src/test_vectors.rs
test_vectors.ecdsa            0       p256/src/test_vectors/ecdsa.rs
test_vectors.field            0       p256/src/test_vectors/field.rs
test_vectors.group            0       p256/src/test_vectors/group.rs
tests.affine                  0       p256/tests/affine.rs
tests.ecdsa                   0       p256/tests/ecdsa.rs
tests.projective              0       p256/tests/projective.rs

=== Porting Quality Summary ===

Matched by exact header:          0 / 7
Matched by provenance fallback:   7 / 7
Matched by name:                  0 / 7
Total TODOs in target: 0
Total lint errors:    10
Stub files:           2

=== Big Picture ===

- Missing files: 16
- Incomplete ports (similarity < 60%): 5
- Stub files: 2
- Files missing functions: 4 (total deficit: 43 functions)
- Type definitions missing: 28
- Files missing tests: 3 (total deficit: 8 unported `#[test]` functions)
- Documentation coverage: 242 / 538 lines (45%)

Primary focus: create missing files (highest deps first)

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
p256.Scalar [PROVENANCE-FALL  0.14       0.00       16/48         1/3       0     2     LOW_SIM
  missing functions: `as_ref`, `random`, `sqrt`, `sqrt_ratio`, `from_repr`, `to_repr`, `to_le_bits`, `char_le_bits`, `shr`, `shr_assign`, `eq`, `partial_cmp`, `cmp`, `from`, `add_assign`, `sub_assign`, `mul`, `mul_assign`, `neg`, `reduce`, `reduce_bytes`, `reduce_nonzero`, `reduce_nonzero_bytes`, `sum`, `product`, `conditional_select`, `ct_eq`, `fmt`, `serialize`, `deserialize`, `from_ec_secret`, `scalar_into_scalarbits`
  missing types: `Repr`, `ReprBits`, `Uint`, `Output`, `Bytes`
p256.NistP256 [PROVENANCE-FA  0.00       0.00       0/2           -         0     2     LOW_SIM
  missing functions: `decode_field_bytes`, `encode_field_bytes`
  missing types: `FieldBytesSize`, `Uint`, `BlindedScalar`, `CompressedPoint`, `EncodedPoint`, `FieldBytes`, `NonZeroScalar`, `PublicKey`, `SecretKey`, `ScalarBits`, `Hash`
p256.Ecdsa [STUB] [PROVENANC  0.00       0.00       0/4           0/4       0     1     STUB
  missing functions: `rfc6979`, `prehash_signer_signing_with_sha384`, `prehash_signer_verification_with_sha384`, `scalar_blinding`
  missing types: `Signature`, `DerSignature`, `SigningKey`, `VerifyingKey`, `Digest`
p256.FieldElement [PROVENANC  0.56       0.00       10/15         5/7       0     2     MISSING_FUNCS
  missing functions: `from_repr`, `to_repr`, `fmt`, `from_bytes`, `to_bytes`
  missing types: `Repr`
p256.Arithmetic [PROVENANCE-  1.00       0.00       -             -         0     1     MISSING_TYPES
  missing types: `Scalar`, `CurveGroup`, `FieldElement`, `PointArithmetic`
p256.Ecdh [STUB] [PROVENANCE  1.00       0.00       -             -         0     1     STUB
  missing types: `EphemeralSecret`, `SharedSecret`
p256.Util [PROVENANCE-FALLBA  0.34       0.00       3/3           -         0     1     LOW_SIM

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 5
Missing files: 16

Incomplete ports to complete:
  arithmetic.scalar              similarity=0.14 function_parity=16/48 dependents=0
    missing functions: `as_ref`, `random`, `sqrt`, `sqrt_ratio`, `from_repr`, `to_repr`, `to_le_bits`, `char_le_bits`, `shr`, `shr_assign`, `eq`, `partial_cmp`, `cmp`, `from`, `add_assign`, `sub_assign`, `mul`, `mul_assign`, `neg`, `reduce`, `reduce_bytes`, `reduce_nonzero`, `reduce_nonzero_bytes`, `sum`, `product`, `conditional_select`, `ct_eq`, `fmt`, `serialize`, `deserialize`, `from_ec_secret`, `scalar_into_scalarbits`
    missing types: `Repr`, `ReprBits`, `Uint`, `Output`, `Bytes`
  p256.lib                       similarity=0.00 function_parity=0/2 dependents=0
    missing functions: `decode_field_bytes`, `encode_field_bytes`
    missing types: `FieldBytesSize`, `Uint`, `BlindedScalar`, `CompressedPoint`, `EncodedPoint`, `FieldBytes`, `NonZeroScalar`, `PublicKey`, `SecretKey`, `ScalarBits`, `Hash`
  p256.ecdsa                     similarity=0.00 function_parity=0/4 dependents=0 [STUB]
    missing functions: `rfc6979`, `prehash_signer_signing_with_sha384`, `prehash_signer_verification_with_sha384`, `scalar_blinding`
    missing types: `Signature`, `DerSignature`, `SigningKey`, `VerifyingKey`, `Digest`
  arithmetic.field               similarity=0.56 function_parity=10/15 dependents=0
    missing functions: `from_repr`, `to_repr`, `fmt`, `from_bytes`, `to_bytes`
    missing types: `Repr`
  arithmetic.util                similarity=0.34 function_parity=3/3 dependents=0

=== Missing Files (by Dependents) ===

Source File                   Expected Target                       Dependents Path
-----------------------------------------------------------------------------------------------------------------------
tests.pkcs8                   p256.tests.Pkcs8                      1          p256/tests/pkcs8.rs
tests.scalar                  p256.tests.Scalar                     1          p256/tests/scalar.rs
benches.field                 p256.benches.Field                    0          p256/benches/field.rs
benches.scalar                p256.benches.Scalar                   0          p256/benches/scalar.rs
field.field32                 p256.src.arithmetic.field.Field32     0          p256/src/arithmetic/field/field32.rs
field.field64                 p256.src.arithmetic.field.Field64     0          p256/src/arithmetic/field/field64.rs
arithmetic.hash2curve         p256.src.arithmetic.Hash2curve        0          p256/src/arithmetic/hash2curve.rs
scalar.scalar32               p256.src.arithmetic.scalar.Scalar32   0          p256/src/arithmetic/scalar/scalar32.rs
scalar.scalar64               p256.src.arithmetic.scalar.Scalar64   0          p256/src/arithmetic/scalar/scalar64.rs
p256.test_vectors             p256.src.testvectors.TestVectors      0          p256/src/test_vectors.rs
test_vectors.ecdsa            p256.src.testvectors.Ecdsa            0          p256/src/test_vectors/ecdsa.rs
test_vectors.field            p256.src.testvectors.Field            0          p256/src/test_vectors/field.rs
test_vectors.group            p256.src.testvectors.Group            0          p256/src/test_vectors/group.rs
tests.affine                  p256.tests.Affine                     0          p256/tests/affine.rs
tests.ecdsa                   p256.tests.Ecdsa                      0          p256/tests/ecdsa.rs
tests.projective              p256.tests.Projective                 0          p256/tests/projective.rs

=== Documentation Gaps ===

There is missing documentation that is hurting overall scoring.
Documentation coverage: 242 / 538 lines (45%)
Files with >20% doc gap: 4

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
arithmetic.scalar             154         54          64%       0.64      0.35      0.50      
p256.lib                      132         34          74%       0.76      0.26      0.51      
p256.ecdsa                    88          3           96%       0.49      0.03      0.26      
p256.ecdh                     78          3           96%       0.21      0.04      0.12      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
