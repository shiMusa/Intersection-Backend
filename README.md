# Intersection - Backend

This is the frontend of the _Intersection_ application.

The tech stack is [Kotlin](https://kotlinlang.org/)
with [Spring-Boot](https://kotlinlang.org/docs/jvm-get-started-spring-boot.html).

## Dev

You can run the `IntersectionApplication` either directly from your IDE (
e.g. [Intellij](https://www.jetbrains.com/idea/)),
or via the [gradle](https://gradle.org/) command

```bash
gradlew bootRun
```

## Build

Build the project via
```bash
gradlew build
```

## Test

The unit tests can be run via

```bash
gradlew test
```

The test reports can be found under [`/build/reports/tests/test/index.html`](/build/reports/tests/test/index.html).

## Documentation

The documentation for this project can be built using [dokka](https://kotlinlang.org/docs/dokka-introduction.html) via

```bash
gradlew dokkaGenerateHtml
```

The resulting HTML documentation can be found under [`/buid/dokka/html/index.html`](/build/dokka/html/index.html).