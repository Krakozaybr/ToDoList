package com.krakozaybr.todolist.presentation.components.bottom_sheet

import androidx.compose.foundation.MutatePriority
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicReference

/**
 * Used in place of the standard Job cancellation pathway to avoid reflective
 * javaClass.simpleName lookups to build the exception message and stack trace collection.
 * Remove if these are changed in kotlinx.coroutines.
 */
private class MutationInterruptedException : CancellationException("Mutation interrupted") {
    override fun fillInStackTrace(): Throwable {
        // Avoid null.clone() on Android <= 6.0 when accessing stackTrace
        stackTrace = emptyArray()
        return this
    }
}

/**
 * Mutual exclusion for UI state mutation over time.
 *
 * [mutate] permits interruptible state mutation over time using a standard [MutatePriority].
 * A [MutatorMutex] enforces that only a single writer can be active at a time for a particular
 * state resource. Instead of queueing callers that would acquire the lock like a traditional
 * [Mutex], new attempts to [mutate] the guarded state will either cancel the current mutator or
 * if the current mutator has a higher priority, the new caller will throw [CancellationException].
 *
 * [MutatorMutex] should be used for implementing hoisted state objects that many mutators may
 * want to manipulate over time such that those mutators can coordinate with one another. The
 * [MutatorMutex] instance should be hidden as an implementation detail. For example:
 *
 * @sample androidx.compose.foundation.samples.mutatorMutexStateObject
 */
@Stable
class MutatorMutex {
    private class Mutator(val priority: MutatePriority, val job: Job) {
        fun canInterrupt(other: Mutator) = priority >= other.priority

        fun cancel() = job.cancel(MutationInterruptedException())
    }

    private val currentMutator = AtomicReference<Mutator?>(null)
    private val mutex = Mutex()

    private fun tryMutateOrCancel(mutator: Mutator) {
        while (true) {
            val oldMutator = currentMutator.get()
            if (oldMutator == null || mutator.canInterrupt(oldMutator)) {
                if (currentMutator.compareAndSet(oldMutator, mutator)) {
                    oldMutator?.cancel()
                    break
                }
            } else throw CancellationException("Current mutation had a higher priority")
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutate] is called while another call to [mutate] or [mutateWith] is in progress, their
     * [priority] values are compared. If the new caller has a [priority] equal to or higher than
     * the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * @param priority the priority of this mutation; [MutatePriority.Default] by default. Higher
     * priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate] or
     * [mutateWith].
     */
    suspend fun <R> mutate(
        priority: MutatePriority = MutatePriority.Default,
        block: suspend () -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutateWith] is called while another call to [mutate] or [mutateWith] is in progress,
     * their [priority] values are compared. If the new caller has a [priority] equal to or
     * higher than the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * This variant of [mutate] calls its [block] with a [receiver], removing the need to create
     * an additional capturing lambda to invoke it with a receiver object. This can be used to
     * expose a mutable scope to the provided [block] while leaving the rest of the state object
     * read-only. For example:
     *
     * @sample androidx.compose.foundation.samples.mutatorMutexStateObjectWithReceiver
     *
     * @param receiver the receiver `this` that [block] will be called with
     * @param priority the priority of this mutation; [MutatePriority.Default] by default. Higher
     * priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate] or
     * [mutateWith].
     */
    suspend fun <T, R> mutateWith(
        receiver: T,
        priority: MutatePriority = MutatePriority.Default,
        block: suspend T.() -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                receiver.block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Attempt to mutate synchronously if there is no other active caller.
     * If there is no other active caller, the [block] will be executed in a lock. If there is
     * another active caller, this method will return false, indicating that the active caller
     * needs to be cancelled through a [mutate] or [mutateWith] call with an equal or higher
     * mutation priority.
     *
     * Calls to [mutate] and [mutateWith] will suspend until execution of the [block] has finished.
     *
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     * @return true if the [block] was executed, false if there was another active caller and the
     * [block] was not executed.
     */
    fun tryMutate(block: () -> Unit): Boolean {
        val didLock = mutex.tryLock()
        if (didLock) {
            try {
                block()
            } finally {
                mutex.unlock()
            }
        }
        return didLock
    }
}