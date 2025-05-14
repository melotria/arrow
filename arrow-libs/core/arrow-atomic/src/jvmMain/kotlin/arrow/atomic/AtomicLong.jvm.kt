package arrow.atomic

public actual class AtomicLong actual constructor(initialValue: Long) {
  private val atomic = AtomicLong(initialValue)
  public actual fun get(): Long = atomic.get()

  public actual fun set(newValue: Long) {
    atomic.getAndSet(newValue)
  }

  public actual fun getAndSet(value: Long): Long {
    return atomic.getAndSet(value)
  }

  public actual fun incrementAndGet(): Long {
    return atomic.incrementAndGet()
  }

  public actual fun decrementAndGet(): Long {
    return atomic.decrementAndGet()
  }

  public actual fun addAndGet(delta: Long): Long {
    return atomic.addAndGet(delta)
  }

  public actual fun compareAndSet(expected: Long, new: Long): Boolean {
    return atomic.compareAndSet(expected, new)
  }
}
