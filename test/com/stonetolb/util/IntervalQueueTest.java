package com.stonetolb.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IntervalQueueTest {

	IntervalQueue<Integer> testQueue;

	@Before
	public void setup() throws Exception {
		IntervalQueue.Builder<Integer> builder = IntervalQueue.builder();
		testQueue = builder
				.append(Integer.valueOf(1), 10)
				.append(Integer.valueOf(10),10)
				.append(Integer.valueOf(20),10)
				.build();
	}

	@Test
	public void testGetQueueLength() throws Exception {
		Assert.assertEquals("Length Incorrect", testQueue.getQueueLength(), 30);
	}

	@Test
	public void testGetDataAt() throws Exception {
		Assert.assertTrue("Queue Incorrect", testQueue.getDataAt(5).equals(1));
		Assert.assertTrue("Queue Incorrect", testQueue.getDataAt(15).equals(10));
		Assert.assertTrue("Queue Incorrect", testQueue.getDataAt(25).equals(20));
	}
}
