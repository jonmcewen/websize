package com.jonmcewen.apps.websize.internal;

import java.net.URISyntaxException;
import java.net.URL;

/**
 * Represents the result of a web size request. Includes toString implementation
 * that formats the result for output
 * 
 */
public class WebSizeResult {

	private final URL url;

	private final Long sizeInBytes;

	/**
	 * Static helper to create a new WebSizeResult with unknown size
	 * 
	 * @param url
	 * @return a new WebSizeResult
	 */
	public static WebSizeResult newUnknownSizeResult(URL url) {
		return new WebSizeResult(url, null);
	}

	/**
	 * @param url
	 * @param size
	 *            in Bytes
	 */
	public WebSizeResult(URL url, Long size) {
		this.sizeInBytes = size;
		this.url = url;
	}

	@Override
	public String toString() {
		// TODO consider moving this formatting logic out to the UI?
		StringBuilder sb = new StringBuilder();
		String address;
		try {
			address = this.url.toURI().getSchemeSpecificPart().substring(2);
		} catch (URISyntaxException e) {
			address = url.toString();
		}
		sb.append(address).append(" ");
		if (this.sizeInBytes != null) {
			sb.append(this.sizeInBytes);
		} else {
			sb.append("*");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sizeInBytes == null) ? 0 : sizeInBytes.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WebSizeResult other = (WebSizeResult) obj;
		if (sizeInBytes == null) {
			if (other.sizeInBytes != null) {
				return false;
			}
		} else if (!sizeInBytes.equals(other.sizeInBytes)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}
}
