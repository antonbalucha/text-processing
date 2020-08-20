package sk.yss.textprocessor.webdownloader.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.webdownloader.RunWebDownloader;

// https://stackoverflow.com/questions/35099509/jsoup-and-character-encoding?rq=1
// http://stackoverflow.com/questions/5131358/access-denied-when-trying-to-get-webpage-from-a-website

public final class WebDocumentDownloader {

	private static final Logger logger = LogManager.getLogger(RunWebDownloader.class);

	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int READ_TIMEOUT = 10000;
	private static final String UTF_8 = "UTF-8";

	private java.util.Map<String, java.util.List<String>> responseHeader = null;
	private String url = null;
	private int responseCode = -1;
	private String mimeType = null;
	private String charset = null;
	private Object content = null;

	private static final String EXCEPTION_MESSAGE = "URL protocol must be HTTP.";

	public WebDocumentDownloader download(String url) {

		this.url = url;

		URL URL = null;
		URLConnection urlConnection = null;
		HttpURLConnection httpUrlConnection = null;

		int length = 0;
		String type = null;
		String[] parts = null;
		String t = null;
		int index = 0;
		InputStream inputStream = null;

		try {

			URL = new URL(url);
			urlConnection = URL.openConnection();

			if (!(urlConnection instanceof HttpURLConnection)) {
				throw new IllegalArgumentException(WebDocumentDownloader.EXCEPTION_MESSAGE);
			}

			httpUrlConnection = (HttpURLConnection) urlConnection;

			httpUrlConnection.setConnectTimeout(WebDocumentDownloader.CONNECTION_TIMEOUT);
			httpUrlConnection.setReadTimeout(WebDocumentDownloader.READ_TIMEOUT);
			httpUrlConnection.setInstanceFollowRedirects(true);

			httpUrlConnection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			httpUrlConnection.setRequestProperty("Accept",
					"text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");
			httpUrlConnection.setRequestProperty("Accept-Language", "en,en-US;q=0.9,sk;q=0.8,cs;q=0.7");
			httpUrlConnection.setRequestProperty("Accept-Charset", "utf-8, windows-1250, iso-8859-1, utf-16, *;q=0.1");
			httpUrlConnection.setRequestProperty("Connection", "Keep-Alive, TE");
			httpUrlConnection.setRequestProperty("TE", "deflate, gzip, chunked, identity, trailers");

			httpUrlConnection.connect();

			this.responseHeader = httpUrlConnection.getHeaderFields();
			this.responseCode = httpUrlConnection.getResponseCode();

			length = httpUrlConnection.getContentLength();
			type = httpUrlConnection.getContentType();

			if (type != null) {

				parts = type.split(";");
				this.mimeType = parts[0].trim();

				for (int i = 1; i < parts.length && this.charset == null; i++) {

					t = parts[i].trim();
					index = t.toLowerCase().indexOf("charset=");

					if (index != -1) {
						this.charset = t.substring(index + 8);
					}
				}
			} else {
				this.charset = UTF_8;
			}

			inputStream = httpUrlConnection.getErrorStream();

			if (inputStream != null) {
				this.content = this.readStream(length, inputStream);
			} else if ((this.content = httpUrlConnection.getContent()) != null && this.content instanceof InputStream) {
				this.content = this.readStream(length, (InputStream) this.content);
			}

			httpUrlConnection.disconnect();
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage(), e);
		}

		return this;
	}

	private Object readStream(int length, InputStream inputStream) throws IOException {

		int bufferLength = 0;
		byte[] buffer = null;
		byte[] bytes = null;
		byte[] newBytes = null;

		bufferLength = Math.max(1024, Math.max(length, inputStream.available()));
		buffer = new byte[bufferLength];
		;

		for (int nRead = inputStream.read(buffer); nRead != -1; nRead = inputStream.read(buffer)) {

			if (bytes == null) {
				bytes = buffer;
				buffer = new byte[bufferLength];
				continue;
			}

			newBytes = new byte[bytes.length + nRead];
			System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
			System.arraycopy(buffer, 0, newBytes, bytes.length, nRead);
			bytes = newBytes;
		}

		if (this.charset == null) {
			return bytes;
		}

		try {
			return new String(bytes, this.charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage(), e);
		}

		return bytes;
	}

	public String getHtmlContent() {

		String page = null;
		String s = null;
		byte[] b = null;

		try {

			if (this.content != null && this.mimeType.equals("text/html") && this.charset == null) {
				b = (byte[]) this.content;
				page = new String(b, UTF_8);
			} else if (this.content != null && this.mimeType.equals("text/html") && this.charset != null) {
				s = (String) this.content;
				page = new String(s.getBytes(this.charset), UTF_8);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException: " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage(), e);
		}

		return page;
	}

	public String getUrl() {
		return this.url;
	}

	public int getResponseCode() {
		return this.responseCode;
	}

	public Map<String, java.util.List<String>> getHeaderFields() {
		return this.responseHeader;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public String getCharset() {
		return this.charset;
	}
}
