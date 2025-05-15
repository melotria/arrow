import arrow.core.Either
import arrow.core.raise.ExperimentalTraceApi
import arrow.core.raise.Raise
import arrow.core.raise.fold
import arrow.core.raise.traced
import kotlin.coroutines.cancellation.CancellationException

/**
 * A helper function for Arrow Core that simplifies writing tests by creating a Raise context
 * where failures throw AssertionError instead of returning an Either.
 *
 * This function properly handles CancellationException by rethrowing it directly instead of
 * swallowing it, which avoids the warning from Arrow's tracing system.
 *
 * @param block The code block to execute within the Raise context
 * @param Failure The type of failure that can be raised
 * @param Success The return type of the block
 * @return The successful result of the block execution
 * @throws AssertionError If the block raises a failure, with the failure value and trace information
 * @throws CancellationException If a CancellationException occurs during execution
 */
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
