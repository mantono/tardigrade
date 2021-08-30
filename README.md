# tardigrade
Simple retry and back off handling of requests or actions that may fail for some reason.

## Example
### Blocking
This code would make 5 attempts to requested URL. The returned value is the Kotlin
[Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) type 
```kotlin
val result: Result<String> = attempt {
    // Request that may fail (as in throwing an exception)
    Unirest.get("https://github.com/mantono").asString
}
```

More advanced example, here we create a `BlockingGuard` object which allows us to configure the
back-off algorithm used (how much the delay should be after each attempt).

```kotlin
val guard: BlockingGuard<String> = BlockingGuard(
    /**
     * Give up after 20 attempts, and return the last exception that was caught
     */
    maxAttempts = 20,
    /**
     * Custom back-off algorithm;
     * Attempt ---> Wait time (ms)
     *       1 ---> 1_000
     *       2 ---> 4_000
     *       3 ---> 9_000
     *       4 ---> 16_000
     */
    backOff = { it * it * 1_000L }
)

val result: Result<String> = guard {
    Unirest.get("https://github.com/mantono").asString
}
```
### Async/Suspending
Equivalent functionality of the blocking function and guard can be done with Kotlin
[coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and suspend functions.
- Instead of `attempt` we have `attemptAsync`
- Instead of `BlockingGuard` we have `AsyncGuard`

```kotlin
val result: Deferred<Result<String>> = attemptAsync(maxAttempts = 10) {
    // Suspending function that may fail
}
```

With AsyncGuard
```kotlin
val guard = AsyncGuard<String>()
val result: Deferred<Result<String>> = guard {
    // Suspending function that may fail
}
```

Both of these will return `Deferred<Result<T>>` instead of just `Result<T>`. See Kotlin
documentation on
[Deferred](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-deferred/)
for more information.
