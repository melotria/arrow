import arrow.core.Either
import arrow.core.raise.ExperimentalTraceApi
import arrow.core.raise.Raise
import arrow.core.raise.fold
import arrow.core.raise.traced
import kotlin.coroutines.cancellation.CancellationException

@ExperimentalTraceApi
inline fun <Failure, Success> failOnRaise(block: Raise<Failure>.() -> Success): Success {
    // Instead of using either, which catches and wraps CancellationException,
    // we'll use fold directly with a custom handler that rethrows CancellationException
    return fold(
        block = { 
            traced(block) { trace, error ->
                throw AssertionError("An operation raised $error\n${trace.stackTraceToString()}")
                    .apply {
                        for (suppressed in trace.suppressedExceptions())
                            addSuppressed(suppressed)
                    }
            }
        },
        catch = { throwable -> 
            // Rethrow CancellationException to avoid the warning
            if (throwable is CancellationException) throw throwable
            throw throwable // Rethrow other exceptions as well
        },
        recover = { error -> 
            throw AssertionError("Impossible situation: recover was called with $error")
        },
        transform = { value -> value }
    )
}
