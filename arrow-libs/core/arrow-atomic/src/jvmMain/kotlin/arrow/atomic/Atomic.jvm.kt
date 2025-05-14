package arrow.atomic

import java.util.concurrent.atomic.AtomicReference

public actual class Atomic<V> actual constructor(initialValue: V) {
  private val atomic = AtomicReference(initialValue)
  public actual fun get(): V = atomic.get()
  public actual fun set(value: V): Unit = atomic.set(value)
  public actual fun getAndSet(value: V): V = atomic.getAndSet(value)
  public actual fun compareAndSet(expected: V, new: V): Boolean =
    atomic.compareAndSet(expected, new)
}
