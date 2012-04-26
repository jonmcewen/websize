package com.jonmcewen.apps.websize.internal;

import java.io.IOException;
import java.io.InputStream;

/**
 * Counts bytes
 * 
 */
public class BytesCounter {

	/**
	 * 
	 * Reads an input stream completely and closes the stream.
	 * 
	 * @return the number of bytes read from the stream
	 * 
	 */
	public long countBytes(InputStream input) throws IOException {
		if (input == null) {
			throw new IllegalArgumentException("input cannot be null");
		}
		// TODO this could probably be done quicker to use up less of the
		// timeout
		long size = 0;
		int data = input.read();
		while (data != -1) {
			size++;

			data = input.read();
		}
		input.close();
		return size;
	}
}
