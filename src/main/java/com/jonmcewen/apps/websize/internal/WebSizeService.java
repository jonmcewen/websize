package com.jonmcewen.apps.websize.internal;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service to find the size of web pages. Does not follow links.
 * 
 */
public class WebSizeService {

	private static final int TIMEOUT = 1500;

	private static final String HTTP_PREFIX = "http://";

	private static final Logger logger = LoggerFactory
			.getLogger(WebSizeService.class);

	private final ExecutorService executor;

	private final BytesCounter counter;

	private final UrlRetriever retriever;

	private long timeoutMs;

	public WebSizeService(final ExecutorService executor,

	final BytesCounter counter,

	final UrlRetriever retriever) {
		this.executor = executor;
		this.counter = counter;
		this.retriever = retriever;

		// TODO null checks
	}

	/**
	 * Given a list of addresses, performs an HTTP GET on each address in
	 * parallel and returns a list of WebSizeResult objects.
	 * 
	 * @param addresses
	 *            A list of web addresses, e.g. www.google.com
	 * @return a list of WebSizeResult objects
	 * @throws MalformedURLException
	 */
	public List<WebSizeResult> measurePageSizes(List<String> addresses)
			throws MalformedURLException {

		logger.info("Measuring sizes for: {}", addresses);

		List<WebSizeResult> results = new ArrayList<WebSizeResult>();

		Map<URL, Future<WebSizeResult>> futures = new HashMap<URL, Future<WebSizeResult>>();
		for (String address : addresses) {
			// prepend http:// (could be more lenient and check for scheme, but
			// not required)
			URL url = new URL(HTTP_PREFIX + address);
			// submit
			futures.put(url, executor.submit(new WebSizeCallable(url)));
		}

		// get results within timeout
		long start = System.currentTimeMillis();
		for (Entry<URL, Future<WebSizeResult>> futureEntry : futures.entrySet()) {
			URL url = futureEntry.getKey();
			Future<WebSizeResult> future = futureEntry.getValue();
			// still time?
			long timeLeft = TIMEOUT - (System.currentTimeMillis() - start);
			if (timeLeft > 0) {
				// allow up to timeout to finish
				WebSizeResult result;
				try {
					result = future.get(timeLeft, TimeUnit.MILLISECONDS);
					results.add(result);
				} catch (InterruptedException e) {
					results.add(handleException(e, url));
				} catch (ExecutionException e) {
					results.add(handleException(e, url));
				} catch (TimeoutException e) {
					// out of time
					results.add(handleTimeout(url));
				}
			} else {

				if (future.isDone()) {
					try {
						// done so get will return immediately
						WebSizeResult result = future.get();
						results.add(result);
					} catch (InterruptedException e) {
						results.add(handleException(e, url));
					} catch (ExecutionException e) {
						results.add(handleException(e, url));
					}

				} else {
					// out of time
					results.add(handleTimeout(url));
				}
			}
		}

		return results;
	}

	private WebSizeResult handleException(Exception e, URL url) {
		logger.debug("Could not calculate size of {} due to {}.", url,
				e.toString());
		return WebSizeResult.newUnknownSizeResult(url);
	}

	private WebSizeResult handleTimeout(URL url) {
		logger.debug("Could not calculate size of {} due to timeout.", url);
		return WebSizeResult.newUnknownSizeResult(url);
	}

	public long getTimeoutMs() {
		return timeoutMs;
	}

	public void setTimeoutMs(long timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	private class WebSizeCallable implements Callable<WebSizeResult> {

		private final URL url;

		public WebSizeCallable(final URL url) {
			this.url = url;
		}

		@Override
		public WebSizeResult call() throws Exception {

			try {
				logger.debug("Retrieving {}", url);
				InputStream stream = retriever.getUrlStream(url);
				logger.debug("Measuring {}", url);
				long size = counter.countBytes(stream);
				logger.debug("Measured {} as {} Bytes", url, size);
				return new WebSizeResult(url, size);
			} catch (Exception e) {
				return handleException(e, url);
			}
		}

	}
}
