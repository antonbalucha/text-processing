package sk.yss.textprocessor.webdownloader.downloader;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupDownloader {

	private static final Logger logger = LoggerFactory.getLogger(JsoupDownloader.class);
	
	static {
		InstallAllTrustingCertificates.install();
	}
	
	public static String download(String url, String cookieName, String cookieValue) {
		
		if (StringUtils.isNotBlank(url)) {
			
			String content = null;
			
			try {
				
				Document document = null;
				
				if (StringUtils.isNotBlank(cookieName) && StringUtils.isNotBlank(cookieValue)) {
					document = Jsoup.connect(url)
						.validateTLSCertificates(false)
						.userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
						.cookie(cookieName, cookieValue)
						.get();
				} else {
					document = Jsoup.connect(url)
						.validateTLSCertificates(false)
						.userAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
						.get();
				}
				
				content = document.html();
			} catch (HttpStatusException e) {
				logger.error("HttpStatusException: " + e.getMessage() + ", url='" + e.getUrl() + "', statusCode='" + e.getMessage() + "'", e);
				content = null;
			} catch (UncheckedIOException e) {
				logger.error("UncheckedIOException: " + e.getMessage(), e);
				content = null;
			} catch (IOException e) {
				logger.error("IOException: " + e.getMessage(), e);
				content = null;
			} catch (Exception e) {
				logger.error("Exception: " + e.getMessage(), e);
				content = null;
			}
			
			return content;
		} else {
			logger.error("Entered url is null or empty!");
			return null;
		}
	}
}
