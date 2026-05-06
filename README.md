# maven-manual-versioning-java11

## Feature exercised

Every compile-scope dependency version is declared as an explicit, hardcoded string in `pom.xml` with no Maven version properties, no `dependencyManagement`, no BOM imports, and no version ranges. The build is pinned to Temurin JDK 11.0.20+8 via `maven-toolchains-plugin`.

## Toolchain requirement

This build requires **Eclipse Temurin 11.0.20+8** to be registered in `~/.m2/toolchains.xml`. Without this registration `mvn compile` will fail with "No toolchain found for type jdk". The dependency resolution itself (`mvn dependency:tree`) does not require the toolchain.

### Register the toolchain in ~/.m2/toolchains.xml

```xml
<toolchains>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>11</version>
      <vendor>temurin</vendor>
      <id>temurin-11.0.20+8</id>
    </provides>
    <configuration>
      <jdkHome>/path/to/temurin-11.0.20+8</jdkHome>
    </configuration>
  </toolchain>
</toolchains>
```

Common paths:
- macOS with SDKMAN: `~/.sdkman/candidates/java/11.0.20-tem`
- Linux: `/usr/lib/jvm/temurin-11.0.20+8`
- Windows: `C:\Program Files\Eclipse Adoptium\jdk-11.0.20.8-hotspot`

## Dependencies

| Declared in pom.xml | Mend artifactId (filename) | Version | Direct | Scope |
|---|---|---|---|---|
| `com.fasterxml.jackson.core:jackson-databind` | `jackson-databind-2.15.2.jar` | 2.15.2 | yes | compile |
| `org.apache.commons:commons-lang3` | `commons-lang3-3.13.0.jar` | 3.13.0 | yes | compile |
| `com.google.guava:guava` | `guava-32.1.2-jre.jar` | 32.1.2-jre | yes | compile |
| (transitive of jackson-databind) | `jackson-annotations-2.15.2.jar` | 2.15.2 | no | compile |
| (transitive of jackson-databind) | `jackson-core-2.15.2.jar` | 2.15.2 | no | compile |
| (transitive of guava) | `failureaccess-1.0.1.jar` | 1.0.1 | no | compile |
| (transitive of guava) | `listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar` | 9999.0-empty-to-avoid-conflict-with-guava | no | compile |
| (transitive of guava) | `jsr305-3.0.2.jar` | 3.0.2 | no | compile |
| (transitive of guava) | `checker-qual-3.33.0.jar` | 3.33.0 | no | compile |
| (transitive of guava) | `error_prone_annotations-2.18.0.jar` | 2.18.0 | no | compile |
| (transitive of guava) | `j2objc-annotations-2.8.jar` | 2.8 | no | compile |

Total: 11 packages. Direct: 3. Transitive: 8.

Note: `total_packages` in `expected-tree.json` is 11 (corrected from initial draft). The unique_packages list has 11 entries.

## Expected dependency tree

```
com.fasterxml.jackson.core:jackson-databind:2.15.2  (compile, direct, registry, main)
  com.fasterxml.jackson.core:jackson-annotations:2.15.2  (compile, transitive, registry, main)
  com.fasterxml.jackson.core:jackson-core:2.15.2  (compile, transitive, registry, main)
org.apache.commons:commons-lang3:3.13.0  (compile, direct, registry, main)
com.google.guava:guava:32.1.2-jre  (compile, direct, registry, main)
  com.google.guava:failureaccess:1.0.1  (compile, transitive, registry, main)
  com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava  (compile, transitive, registry, main)
  com.google.code.findbugs:jsr305:3.0.2  (compile, transitive, registry, main)
  org.checkerframework:checker-qual:3.33.0  (compile, transitive, registry, main)
  com.google.errorprone:error_prone_annotations:2.18.0  (compile, transitive, registry, main)
  com.google.j2objc:j2objc-annotations:2.8  (compile, transitive, registry, main)
```

All versions reported exactly as declared. No eviction, no override, no range expansion.

## Key detection failure modes exercised

- guava compile-scope transitives missing -- Mend does not traverse guava children
- jackson-annotations or jackson-core missing -- jackson-databind transitives not traversed
- `listenablefuture` version string `9999.0-empty-to-avoid-conflict-with-guava` truncated or mangled
- `error_prone_annotations` artifactId corrupted due to underscore character
- Version reported as range or as latest release instead of verbatim hardcoded string
- `group` classified as `test` instead of `main` (compile scope misclassified)
- `source` classified as `local` instead of `registry`

## Probe metadata

```json
{
  "probe_name": "maven-manual-versioning-java11-probe",
  "pattern": "manual-versioning-java11",
  "pm": "maven",
  "generated": "2026-05-06",
  "target": "remote",
  "remote_org": "mend-detection-qa",
  "remote_repo": "maven-manual-versioning-java11",
  "java_version": "11.0.20+8",
  "java_vendor": "temurin",
  "compiler_source": "11",
  "compiler_target": "11",
  "compiler_release": "11"
}
```