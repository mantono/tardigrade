package com.mantono.tardigrade

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class ChannelThreadSafetyTest {
	@Test
	fun testChannelConsumptionIsThreadSafe() {
		val channel = Channel<Int>(5000)
		val producer = Runnable {
			runBlocking {
				for(i in 0 until 5000)
					channel.send(i)
			}
		}

		val consumed: MutableList<Int> = ArrayList(5000)

		val consumer1 = Runnable {
			runBlocking {
				for(e in channel) {
					consumed.add(e)
				}
			}
		}

		val consumer2 = Runnable {
			runBlocking {
				for(e in channel) {
					consumed.add(e)
				}
			}
		}

		Thread(producer).start()
		Thread(consumer1).start()
		Thread(consumer2).start()

		Thread.sleep(2200)
		assertTrue(channel.isEmpty)
		assertEquals(5000, consumed.size)
	}
}