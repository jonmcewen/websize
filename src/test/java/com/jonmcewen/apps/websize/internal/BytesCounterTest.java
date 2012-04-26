/**
 * 
 */
package com.jonmcewen.apps.websize.internal;

import static org.mockito.BDDMockito.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * Unit tests for BytesCounter
 * 
 */
public class BytesCounterTest {

	InputStream input = mock(InputStream.class);
	BytesCounter counter = new BytesCounter();

	/**
	 * Check that bytes are read and counted, and that stream is closed.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testCountBytes() throws IOException {
		// given
		given(input.read()).willReturn(97, 98, 99, -1);
		// when
		long size = counter.countBytes(input);
		// then
		verify(input).close();
		assertEquals("Wrong number of bytes counted", 3, size);
	}

	/**
	 * For stream at EOF, check that bytes are read and counted, and that stream
	 * is closed.
	 * 
	 * @throws IOException
	 */
	@Test
	public final void testCountBytesEOF() throws IOException {
		// given
		given(input.read()).willReturn(-1);
		// when
		long size = counter.countBytes(input);
		// then
		verify(input).close();
		assertEquals("Wrong number of bytes counted", 0, size);
	}

	/**
	 * Check that IllegalArgumentException thrown for null input.
	 * @throws IOException 
	 */
	@Test
	public final void testCountBytesIOException() throws IOException {
		given(input.read()).willThrow(IOException.class);
		try {
			counter.countBytes(input);
			fail("Should have thrown an IOException");
		} catch (IOException e) {
			// pass
		}
	}

	/**
	 * Test method for
	 * {@link com.jonmcewen.apps.websize.internal.BytesCounter#countBytes(java.io.InputStream)}
	 * .
	 * @throws IOException 
	 */
	@Test
	public final void testCountBytesNullStream() throws IOException {
		try {
			counter.countBytes(null);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			// pass
		}
	}

}
