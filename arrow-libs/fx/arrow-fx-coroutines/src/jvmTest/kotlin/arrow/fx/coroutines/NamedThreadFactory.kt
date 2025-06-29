package arrow.fx.coroutines

import arrow.atomic.AtomicInt
import java.util.concurrent.ThreadFactory

private val namedThreadCount = AtomicInt(0)

class NamedThreadFactory(val name: String): ThreadFactory {
  override fun newThread(r: Runnable): Thread? =
    Thread(r, "$name-${/* DOES NOT WORK: namedThreadCount.getAndIncrement()*/ null}").apply {
      uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, e ->
        e.printStackTrace()
      }
    }
}
