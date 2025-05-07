@ExperimentalTraceApi
inline fun <Failure, Success> failOnRaise(block: Raise<Failure>.() -> Success): Success {
    val result = either {
        catch({
            traced(block) { trace, error ->
                throw AssertionError("An operation raised $error\n${trace.stackTraceToString()}").apply {
                    for (suppressed in trace.suppressedExceptions())
                        addSuppressed(suppressed)
                }
            }
        }) { throwable ->
            // Rethrow CancellationException and other fatal exceptions
            throw throwable.nonFatalOrThrow()
        }
    }
    check(result is Either.Right) {
        "Impossible situation: we throw an error when the passed block raises, but it still gave us a failed either: $result"
    }
    return result.value
}
