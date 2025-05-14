package arrow.atomic

import java.util.concurrent.atomic.AtomicInteger

public actual class AtomicInt actual constructor(initialValue: Int) {
  private val atomic = AtomicInteger(initialValue)
  public actual fun get(): Int = atomic.get()

  public actual fun set(newValue: Int) {
    atomic.getAndSet(newValue)
  }

  public actual fun getAndSet(value: Int): Int {
    return atomic.getAndSet(value)
  }

  public actual fun incrementAndGet(): Int {
    return atomic.incrementAndGet()
  }

  public actual fun decrementAndGet(): Int {
    return atomic.decrementAndGet()
  }

  public actual fun addAndGet(delta: Int): Int {
    return atomic.addAndGet(delta)
  }

  public actual fun compareAndSet(expected: Int, new: Int): Boolean {
    return atomic.compareAndSet(expected, new)
  }
}
