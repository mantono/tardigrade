package com.mantono.tardigrade

import java.util.concurrent.TimeUnit


interface WriteQueue<in E> {
	/**
	 * Inserts the specified element into this queue if it is possible to do so
	 * immediately without violating capacity restrictions, returning true upon
	 * success and false if no space is currently available.
	 */
	fun offer(e: E): Boolean

	/**
	 * Inserts the specified element into this queue, waiting up to the
	 * specified wait time if necessary for space to become available.
	 */
	fun offer(e: E, timeout: Long, unit: TimeUnit): Boolean

	/**
	 * Inserts the specified element into this queue, waiting if necessary for
	 * space to become available.
	 */
	fun send(e: E)

	/**
	 * Returns the number of additional elements that this queue can ideally
	 * (in the absence of memory or resource constraints) accept without
	 * blocking, or Integer.MAX_VALUE if there is no intrinsic limit.
	 */
	fun remainingCapacity(): Int
}

interface ReadQueue<out E> {
	/**
	 * Retrieves and removes the head of this queue, waiting up to the
	 * specified wait time if necessary for an element to become available.
	 */
	fun poll(timeout: Long, unit: TimeUnit): E?

	/**
	 * Retrieves and removes the head of this queue, waiting if necessary
	 * until an element becomes available.
	 */
	fun take(): E
}

interface Consumer<in E>: WriteQueue<E> {
	fun consume(e: E)
}

interface Producer<out E>: ReadQueue<E> {
	fun produce(): E
}

/**
 * An interface for classes using the producer / consumer pattern,
 * which consumes event of type [I] and produces event of type [O]
 */
interface Worker<in I, out O>: WriteQueue<I>, ReadQueue<O> {
	fun transform(i: I): O
}