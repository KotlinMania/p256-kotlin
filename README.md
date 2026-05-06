# p256-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fp256--kotlin-blue.svg)](https://github.com/KotlinMania/p256-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/p256-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/p256-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/p256-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/p256-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`RustCrypto/elliptic-curves`](https://github.com/RustCrypto/elliptic-curves).

**Original Project:** This port is based on [`RustCrypto/elliptic-curves`](https://github.com/RustCrypto/elliptic-curves). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `RustCrypto/elliptic-curves`

> The text below is reproduced and lightly edited from [`https://github.com/RustCrypto/elliptic-curves`](https://github.com/RustCrypto/elliptic-curves). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## RustCrypto: Elliptic Curves ![Rust Version][rustc-image] [![Project Chat][chat-image]][chat-link] [![dependency status][deps-image]][deps-link]

General purpose Elliptic Curve Cryptography (ECC) support, including types
and traits for representing various elliptic curve forms, scalars, points,
and public/secret keys composed thereof.

All curves reside in the separate crates and implemented using traits from
the [`elliptic-curve`](https://docs.rs/elliptic-curve/) crate.

Crates in this repo do not require the standard library (i.e. `no_std` capable)
and can be easily used for bare-metal or WebAssembly programming.

## Crates

| Name      | Curve              | Crates.io                                                                                 | Documentation                                                              | Build Status                                                                                               |
|-----------|--------------------|-------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| [`bignp256`] | bign-curve256v1  | [![crates.io](https://img.shields.io/crates/v/bignp256.svg)](https://crates.io/crates/bignp256) | [![Documentation](https://docs.rs/bignp256/badge.svg)](https://docs.rs/bignp256) | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/bignp256/badge.svg?branch=master&event=push) |
| [`bp256`] | [brainpoolP256r1]/[brainpoolP256t1] | [![crates.io](https://img.shields.io/crates/v/bp256.svg)](https://crates.io/crates/bp256) | [![Documentation](https://docs.rs/bp256/badge.svg)](https://docs.rs/bp256) | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/bp256/badge.svg?branch=master&event=push) |
| [`bp384`] | [brainpoolP384r1]/[brainpoolP384t1] | [![crates.io](https://img.shields.io/crates/v/bp384.svg)](https://crates.io/crates/bp384) | [![Documentation](https://docs.rs/bp384/badge.svg)](https://docs.rs/bp384) | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/bp384/badge.svg?branch=master&event=push) |
| [`hash2curve`] |               | [![crates.io](https://img.shields.io/crates/v/hash2curve.svg)](https://crates.io/crates/hash2curve)   | [![Documentation](https://docs.rs/hash2curve/badge.svg)](https://docs.rs/hash2curve)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/hash2curve/badge.svg?branch=master&event=push)  |
| [`k256`]  | [secp256k1]        | [![crates.io](https://img.shields.io/crates/v/k256.svg)](https://crates.io/crates/k256)   | [![Documentation](https://docs.rs/k256/badge.svg)](https://docs.rs/k256)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/k256/badge.svg?branch=master&event=push)  |
| [`p192`]  | [NIST P-192]       | [![crates.io](https://img.shields.io/crates/v/p192.svg)](https://crates.io/crates/p192)   | [![Documentation](https://docs.rs/p192/badge.svg)](https://docs.rs/p192)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/p192/badge.svg?branch=master&event=push)  |
| [`p224`]  | [NIST P-224]       | [![crates.io](https://img.shields.io/crates/v/p224.svg)](https://crates.io/crates/p224)   | [![Documentation](https://docs.rs/p224/badge.svg)](https://docs.rs/p224)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/p224/badge.svg?branch=master&event=push)  |
| [`p256`]  | [NIST P-256]       | [![crates.io](https://img.shields.io/crates/v/p256.svg)](https://crates.io/crates/p256)   | [![Documentation](https://docs.rs/p256/badge.svg)](https://docs.rs/p256)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/p256/badge.svg?branch=master&event=push)  |
| [`p384`]  | [NIST P-384]       | [![crates.io](https://img.shields.io/crates/v/p384.svg)](https://crates.io/crates/p384)   | [![Documentation](https://docs.rs/p384/badge.svg)](https://docs.rs/p384)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/p384/badge.svg?branch=master&event=push)  |
| [`p521`]  | [NIST P-521]       | [![crates.io](https://img.shields.io/crates/v/p521.svg)](https://crates.io/crates/p521)   | [![Documentation](https://docs.rs/p521/badge.svg)](https://docs.rs/p521)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/p521/badge.svg?branch=master&event=push)  |
| [`sm2`]   | [SM2]              | [![crates.io](https://img.shields.io/crates/v/sm2.svg)](https://crates.io/crates/sm2)   | [![Documentation](https://docs.rs/sm2/badge.svg)](https://docs.rs/sm2)   | ![build](https://github.com/RustCrypto/elliptic-curves/workflows/sm2/badge.svg?branch=master&event=push)  |

Please see our [tracking issue for additional elliptic curves][other-curves]
if you are interested in curves beyond the ones listed here.

## Minimum Supported Rust Version

All crates in this repository support Rust **1.85** or higher.

Minimum supported Rust version can be changed in the future, but it will be
done with a minor version bump.

## License

All crates licensed under either of

 * [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
 * [MIT license](http://opensource.org/licenses/MIT)

at your option.

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted
for inclusion in the work by you, as defined in the Apache-2.0 license, shall be
dual licensed as above, without any additional terms or conditions.

[//]: # (badges)

[rustc-image]: https://img.shields.io/badge/rustc-1.85+-blue.svg
[chat-image]: https://img.shields.io/badge/zulip-join_chat-blue.svg
[chat-link]: https://rustcrypto.zulipchat.com/#narrow/stream/260040-elliptic-curves
[deps-image]: https://deps.rs/repo/github/RustCrypto/elliptic-curves/status.svg
[deps-link]: https://deps.rs/repo/github/RustCrypto/elliptic-curves

[//]: # (crates)

[`bignp256`]: ./bignp256
[`bp256`]: ./bp256
[`bp384`]: ./bp384
[`hash2curve`]: ./hash2curve
[`k256`]: ./k256
[`p192`]: ./p192
[`p224`]: ./p224
[`p256`]: ./p256
[`p384`]: ./p384
[`p521`]: ./p521
[`sm2`]: ./sm2

[//]: # (curves)

[secp256k1]: https://neuromancer.sk/std/secg/secp256k1
[NIST P-192]: https://neuromancer.sk/std/nist/P-192
[NIST P-224]: https://neuromancer.sk/std/nist/P-224
[NIST P-256]: https://neuromancer.sk/std/nist/P-256
[NIST P-384]: https://neuromancer.sk/std/nist/P-384
[NIST P-521]: https://neuromancer.sk/std/nist/P-521
[SM2]: https://neuromancer.sk/std/oscaa/SM2
[BIGN P-256]: https://apmi.bsu.by/assets/files/std/bign-spec294.pdf
[brainpoolP256r1]: https://neuromancer.sk/std/brainpool/brainpoolP256r1
[brainpoolP384r1]: https://neuromancer.sk/std/brainpool/brainpoolP384r1
[brainpoolP256t1]: https://neuromancer.sk/std/brainpool/brainpoolP256t1
[brainpoolP384t1]: https://neuromancer.sk/std/brainpool/brainpoolP384t1

[//]: # (links)

[other-curves]: https://github.com/RustCrypto/elliptic-curves/issues/114

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:p256-kotlin:0.1.0-SNAPSHOT")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same Apache-2.0 license as the upstream [`RustCrypto/elliptic-curves`](https://github.com/RustCrypto/elliptic-curves). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the elliptic-curves authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`RustCrypto/elliptic-curves`](https://github.com/RustCrypto/elliptic-curves) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
