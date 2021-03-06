package com.jonmcewen.apps.websize.internal;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests for WebSizeService
 * 
 */
public class WebSizeServiceTest {

	private static final Logger logger = LoggerFactory
			.getLogger(WebSizeServiceTest.class);

	private static final String HTTP_PREFIX = "http://";
	private static final String ADDRESS1 = "bill.com";
	private static final String ADDRESS2 = "ben.com";
	private static final String ERROR_ADDRESS = "error.com";

	UrlRetriever retriever = mock(UrlRetriever.class);
	BytesCounter counter = mock(BytesCounter.class);
	InputStream stream1 = mock(InputStream.class);
	InputStream stream2 = mock(InputStream.class);
	// construct SUT with mocks and single thread executor
	WebSizeService service = new WebSizeService(
			Executors.newSingleThreadExecutor(), counter, retriever);

	@Test
	public final void testMeasurePageSizes() throws IOException,
			UrlRetrievalException {

		// given that we are getting a 1 Byte page and a 2 Byte page
		given(retriever.getUrlStream(urlFor(ADDRESS1))).willReturn(stream1);
		given(retriever.getUrlStream(urlFor(ADDRESS2))).willReturn(stream2);
		given(retriever.getUrlStream(urlFor(ERROR_ADDRESS))).willThrow(
				new UrlRetrievalException("TEST ERROR"));
		given(counter.countBytes(stream1)).willReturn(1L);
		given(counter.countBytes(stream2)).willReturn(2L);

		// when we pass the addresses to the service
		List<String> addresses = new ArrayList<String>();
		addresses.add(ADDRESS1);
		addresses.add(ADDRESS2);
		addresses.add(ERROR_ADDRESS);
		List<WebSizeResult> actual = service.measurePageSizes(addresses);

		// then the list should contain these results
		List<WebSizeResult> expected = new ArrayList<WebSizeResult>();
		expected.add(new WebSizeResult(urlFor(ADDRESS1), 1L));
		expected.add(new WebSizeResult(urlFor(ADDRESS2), 2L));
		expected.add(WebSizeResult.newUnknownSizeResult(urlFor(ERROR_ADDRESS)));
		Collections.sort(expected, new WebSiteResultComparator());
		Collections.sort(actual, new WebSiteResultComparator());

		assertNotNull("Null list returned", actual);
		logger.debug("Expected: {}", expected);
		logger.debug("Actual: {}", actual);
		assertArrayEquals(expected.toArray(), actual.toArray());

	}

	@Test
	public void shouldThrowExceptionIfNullExecutor() {
		try {
			new WebSizeService(null, counter, retriever);
			fail("Should check for null executor");
		} catch (IllegalArgumentException e) {
			// pass
			// TODO check message
		}
	}

	@Test
	public void shouldThrowExceptionIfNullRetriever() {
		try {
			new WebSizeService(Executors.newCachedThreadPool(), counter, null);
			fail("Should check for null retriever");
		} catch (IllegalArgumentException e) {
			// pass
			// TODO check message
		}
	}

	@Test
	public void shouldThrowExceptionIfNullCounter() {
		try {
			new WebSizeService(Executors.newCachedThreadPool(), null, retriever);
			fail("Should check for null counter");
		} catch (IllegalArgumentException e) {
			// pass
			// TODO check message
		}
	}

	private URL urlFor(String address) throws MalformedURLException {
		return new URL(HTTP_PREFIX + address);
	}

	public static class WebSiteResultComparator implements
			Comparator<WebSizeResult> {

		@Override
		public int compare(WebSizeResult o1, WebSizeResult o2) {
			return String.valueOf(o1).compareTo(String.valueOf(o2));
		}

	}
}
