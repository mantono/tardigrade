package com.mantono.tardigrade

import kotlinx.coroutines.experimental.channels.Channel
import java.net.URL
import java.time.Duration
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit


interface PartitionedQueue<E>: WriteQueue<E> {
	val partitions: Int
	val capacity: Int

	/**
	 * Transform element of type [E] to an Int value that
	 * allows an element to map to a partition
	 */
	fun hash(e: E): Int = e.toString().hashCode() % partitions
}

sealed class Result {
	object Success: Result()
	class TemporaryFailure(val exception: Throwable?): Result()
	class PermanentFailure: Result()
}
/*

data class Request(val url: URL, val payload: Any)

class RequestConsumer(): Worker<Request, Result> {
	override fun offer(e: Request): Boolean
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun offer(e: Request, timeout: Long, unit: TimeUnit): Boolean
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun put(e: Request)
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun remainingCapacity(): Int
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun poll(timeout: Long, unit: TimeUnit): Result?
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun take(): Result
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun transform(i: Request): Result
	{
		Channel<Int>(40).
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}

data class RequestQueue(
		override val partitions: Int = 4,
		override val capacity: Int = 100,
		val threads: Int = 1,
		val connectionAttempts: Int = 100,
		val failTimePenalty: Duration = Duration.ofSeconds(30)
): Worker<I, O> {
	private val r = WorkRunner(threads, ::consumer)

	private val queues: List<PriorityBlockingQueue<WorkerJob>> = sequenceOf(0 until partitions)
			.map { PriorityBlockingQueue<WorkerJob>(capacity) }
			.toList()

	override fun hash(e: WorkerJob): Int {
		val regexDomainEnd = Regex("(?<![:/])/")
		// Remove everything after http://dn.se in
		// http://dn.se/site/xx?foo=bar so we only get
		// the domain name and protocol as key
		val domain: String = e.url
				.toString()
				.split(regexDomainEnd)
				.first()

		return domain.hashCode() % partitions
	}

	override suspend fun put(e: WorkerJob) {
		val partition: Int = hash(e)
		withContext(DefaultDispatcher) {
			queues[partition].put(e)
		}
	}

	private fun consumer(threadId: Int) {
		val workQueue: PriorityBlockingQueue<WorkerJob> = queues[threadId]
		val client = OkHttpClient()
		while(true) {
			val job: WorkerJob = workQueue.poll()
			runBlocking {
				httpPost(client, job)
				val result: RequestResult = job.await()
				when(result) {
					is RequestResult.Success
				}
			}
		}
	}

	*/
/**
	 * Return the number of threads that were started
	 *//*

	fun start(): Int = r.start()
}

private val log = KotlinLogging.logger("work-queue")

class WorkRunner(val threads: Int, val runner: (Int) -> Unit) {
	private val threadsToStart = Semaphore(threads)

	fun start(): Int {
		var startedThreads: Int = 0
		while(threadsToStart.tryAcquire()) {
			val runnable = Runnable { executeRunner(startedThreads) }
			Thread(runnable, "worker thread $startedThreads").start()
			log.info("Worker thread $startedThreads started")
			startedThreads++
		}
		log.info("$startedThreads worker threads started")
		return startedThreads
	}

	private fun executeRunner(id: Int) {
		runBlocking {
			while(true) {
				sentryDrop(log = KotlinLogging.logger("worker-$id")) {
					runner(id)
				}
			}
		}
	}
}*/
