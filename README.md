# Liturgy scraper

## Prerequisites - gradlew --version

```bash
gradle --version
------------------------------------------------------------
Gradle 9.6.1
------------------------------------------------------------

Build time:    ...
Revision:      ...

Kotlin:        2.3.21
Groovy:        4.0.32
Ant:           Apache Ant(TM) version 1.10.17 compiled on April 6 2026
Launcher JVM:  22.0.1 (Oracle Corporation 22.0.1+8-16)
```

## Gradle Dependecies
```kotlin
// build.gradle.kts
dependencies {
    // Use the Kotlin Test integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the JUnit 5 integration.
    testImplementation(libs.junit.jupiter.engine)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)

    implementation("org.jsoup:jsoup:1.22.2")
}
```

## Build and Run

### Linux / macOS

```bash
# Make gradlew executable (first time only)
chmod +x gradlew

# Build the project
./gradlew build

# Run the application
./gradlew run
```

### Windows
```bash
# Build the project
gradlew build

# Run the application
gradlew run
```
