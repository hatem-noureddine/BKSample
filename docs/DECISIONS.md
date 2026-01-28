# Architecture Decisions

## JVM Target For Test Coverage
**Date:** 2026-02-08

We added a `jvm()` target in `composeApp` to run `commonTest` on the JVM. Kover only measures JVM bytecode, so this target enables reliable coverage reporting and allows enforcing a 90% minimum without relying on Android or native test tasks.

**Consequences**
- `:composeApp:jvmTest` is the canonical unit-test task for coverage.
- Kover verification is now meaningful and reflects common business logic coverage.
