/**
 * [OneShotEvent] is event which can be executed only once. It wraps data and return them only once.
 * This class is thread safe - can be used from multiple threads
 * @param value Event data of type [T]. Can be null.
 */
class OneShotEvent<T> constructor(value: T?) {
    private var data: Any? = value
    private val tombstone = Any()
    private val lock = Any()

    /**
     * Fires the event.
     * @return given data or null if data has been already fired.
     *
     * @see isFired to determine whether event is still fresh
     */
    fun fire(): T? {
        synchronized(lock) {
            if (data != tombstone) {
                @Suppress("UNCHECKED_CAST")
                val value = data as? T
                data = tombstone
                return value
            }
            return null
        }
    }

    /**
     * Returns whether event has been already fired or not.
     * @return true in case when event has been already fired, false otherwise
     */
    fun isFired(): Boolean = synchronized(lock) {
        return data == tombstone
    }
}
