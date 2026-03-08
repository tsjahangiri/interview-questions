# Inverter - invert Method: Test Suite (JUnit 4)

## Description

Implement a suite of tests for the `invert` method of the `Inverter` class using the **JUnit 4** framework, covering all the requirements below.

## Method Signature

```java
public static String invert(String input)
```

## Behaviour

| Input | Output |
|---|---|
| `null` | `""` (empty string) |
| `""` | `""` (empty string) |
| `"a"` | `"a"` (single char → same string) |
| `"abcd"` | `"dcba"` (longer than 1 char → reversed) |

## Requirements

- When the string is **empty** it returns an empty string.
- When the argument is **null** it returns an empty string.
- When the string has **exactly one character** the same string is returned.
- When the string is **longer than 1 character** its inverted (reversed) version is returned.

## Examples

```java
Inverter.invert("a");     // returns "a"
Inverter.invert(null);    // returns ""
Inverter.invert("abcd");  // returns "dcba"
```

## Notes

- You do **not** need to implement the `Inverter` class or the `invert` method.
- All tests must **pass** for a correct implementation.
- For a **wrong** implementation, at least one test must **fail**.
- Use static assertion methods from `org.junit.Assert` (e.g. `assertEquals`, `assertNotEquals`).

---