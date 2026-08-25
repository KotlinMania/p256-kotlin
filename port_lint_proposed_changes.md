# port-lint Proposed Changes

**Generated:** 2026-08-25
**Source:** tmp
**Target:** src/commonMain/kotlin

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/p256/Scalar.kt` | `// port-lint: source arithmetic/scalar.rs` | `// port-lint: source arithmetic/scalar.rs` | `arithmetic/scalar.rs` | `port-lint provenance header matched only after fallback normalization: 'arithmetic/scalar.rs' vs expected 'arithmetic/scalar.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/p256/ScalarTest.kt` | `// port-lint: tests arithmetic/scalar.rs` | `// port-lint: tests arithmetic/scalar.rs` | `arithmetic/scalar.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:arithmetic/scalar.rs' vs expected 'arithmetic/scalar.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/NistP256.kt` | `// port-lint: source lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/p256/NistP256Test.kt` | `// port-lint: tests lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/Ecdsa.kt` | `// port-lint: source ecdsa.rs` | `// port-lint: source ecdsa.rs` | `ecdsa.rs` | `port-lint provenance header matched only after fallback normalization: 'ecdsa.rs' vs expected 'ecdsa.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/FieldElement.kt` | `// port-lint: source arithmetic/field.rs` | `// port-lint: source arithmetic/field.rs` | `arithmetic/field.rs` | `port-lint provenance header matched only after fallback normalization: 'arithmetic/field.rs' vs expected 'arithmetic/field.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/p256/FieldElementTest.kt` | `// port-lint: tests arithmetic/field.rs` | `// port-lint: tests arithmetic/field.rs` | `arithmetic/field.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:arithmetic/field.rs' vs expected 'arithmetic/field.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/Arithmetic.kt` | `// port-lint: source arithmetic.rs` | `// port-lint: source arithmetic.rs` | `arithmetic.rs` | `port-lint provenance header matched only after fallback normalization: 'arithmetic.rs' vs expected 'arithmetic.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/Ecdh.kt` | `// port-lint: source ecdh.rs` | `// port-lint: source ecdh.rs` | `ecdh.rs` | `port-lint provenance header matched only after fallback normalization: 'ecdh.rs' vs expected 'ecdh.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/p256/Util.kt` | `// port-lint: source arithmetic/util.rs` | `// port-lint: source arithmetic/util.rs` | `arithmetic/util.rs` | `port-lint provenance header matched only after fallback normalization: 'arithmetic/util.rs' vs expected 'arithmetic/util.rs'` |
