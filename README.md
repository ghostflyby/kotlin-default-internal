# Kotlin Default Internal Compiler Plugin

Make `internal` the default visibility modifier for classes, functions, and properties in Kotlin.

## Special cases
- constructors default to `public`
- companion objects default to `public`
- properties in primary constructors follow the visibility of the class, but at least `internal`
- members of interfaces default to `public`
- property accessors default to the visibility of the property
