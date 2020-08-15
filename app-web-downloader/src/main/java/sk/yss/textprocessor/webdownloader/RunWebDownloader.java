package sk.yss.textprocessor.webdownloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.yss.textprocessor.apiclasses.WebPage;
import sk.yss.textprocessor.configuration.helper.DatabaseConnector;
import sk.yss.textprocessor.configuration.helper.UUIDProcessor;
import sk.yss.textprocessor.webdownloader.downloader.JsoupDownloader;

public class RunWebDownloader {

	private static final Logger logger = LoggerFactory.getLogger(RunWebDownloader.class);

	private static WebPage downloadContent(WebPage webPage, String cookieName, String cookieValue) {
		if (webPage != null && StringUtils.isNotBlank(webPage.getUrl())) {
			return webPage.setContent(JsoupDownloader.download(webPage.getUrl(), cookieName, cookieValue));
			// content = new WebDocumentDownloader().download(url).getHtmlContent();
			// return webPage.setContent(HttpsDownloader.download(webPage.getUrl()));
		} else {
			logger.error("Entered web page is null or entered web page url is null or empty!");
			return null;
		}
	}

	private static WebPage identifyLinks(WebPage webPage, String linkMustContains) {

		if (webPage != null && StringUtils.isNotBlank(webPage.getContent()) && StringUtils.isNotBlank(linkMustContains)) {

			Document parse = Jsoup.parse(webPage.getContent());
			Elements elements = parse.getElementsByTag("a");
			List<String> links = new ArrayList<String>(elements.size());

			for (Element element : elements) {
				String link = element.attr("href");
				logger.debug("Identified link: " + link);

				if (StringUtils.isNotBlank(link) && !link.startsWith("whatsapp:") && !link.startsWith("javascript:") && !link.startsWith("#")) {

					if (link.startsWith("//")) {
						try {
							link = new URL(webPage.getUrl()).getProtocol() + ":" + link;
							logger.debug("Fixed protocol: " + link);
						} catch (MalformedURLException e) {
							logger.error("MalformedURLException: " + e.getMessage(), e);
						}
					} else if (link.startsWith("/")) {
						if (webPage.getUrl().endsWith("/")) {
							link = webPage.getUrl().substring(0, webPage.getUrl().length()) + link;
							logger.debug("Updated / in link: " + link);
						} else {
							link = webPage.getUrl() + link;
							logger.debug("Concated link: " + link);
						}
					}

					link = link.trim();

					try {
						if (new URL(link).getHost().indexOf(linkMustContains) >= 0) {
							if (!links.contains(link)) {
								links.add(link);
								logger.debug("Added link: " + link);
							} else {
								logger.debug("Duplicated link: " + link);
							}
						} else {
							logger.debug("Out of condition link: " + link);
						}
					} catch (MalformedURLException e) {
						logger.error("MalformedURLException: " + e.getMessage(), e);
					}
				} else {
					logger.debug("Not supported link: " + link);
				}
			}

			webPage.setLinks(links);
			return webPage;

		} else {
			logger.error("Entered parameter web page is null or its content is null or empty or condition which link must contains is null or empty!");
			return null;
		}
	}

	private static String process(String url, String linkMustContains, boolean downloadNext, String cookieName, String cookieValue) {

		String uuid = downloadSingle(url, linkMustContains, cookieName, cookieValue);

		if (downloadNext) {
			downloadNext(linkMustContains, cookieName, cookieValue);
		}

		DatabaseConnector.youMayClose();
		return uuid;
	}

	private static String downloadSingle(String url, String linkMustContains, String cookieName, String cookieValue) {
		WebPage wp = new WebPage().setUuid(UUIDProcessor.generateUuid()).setUrl(url);

		wp = downloadContent(wp, cookieName, cookieValue);
		wp = identifyLinks(wp, linkMustContains);

		String uuid = Database.insertContent(wp);
		Database.insertLinks(wp);

		return uuid;
	}

	private static void downloadNext(String linkMustContains, String cookieName, String cookieValue) {

		while (true) {
			WebPage wp = Database.selectUrl(linkMustContains);

			if (wp != null && StringUtils.isNotBlank(wp.getUrl())) {
				wp = downloadContent(wp, cookieName, cookieValue);
				wp = identifyLinks(wp, linkMustContains);

				Database.updateContent(wp);
				Database.insertLinks(wp);
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				logger.error("InterruptedException: " + e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args) {

		String uuid = null;

		try {
			if (args != null && args[0].equals("-start_with") && StringUtils.isNotBlank(args[1]) && args[2].equals("-link_must_contains")
					&& StringUtils.isNotBlank(args[3]) && args[4].equals("-download_next") && StringUtils.isNotBlank(args[5])) {
				logger.info("Valid number of parameters. Entered start url '" + args[1] + "' with condition '" + args[3] + "' will be processed.");
				uuid = process(args[1].trim(), args[3].trim(), new Boolean(args[5].trim()), args[7].trim(), args[9].trim());
			} else {
				logger.error(
						"Invalid number of parameters! Use: -start_with url -link_must_contains base_of_url. E.g.: -start_with https://www.sme.sk -link_must_contains sme.sk -download_next true -cookie_name cookieName -cookie_value cookieValue");
			}
		} catch (Throwable e) {
			logger.error("Throwable: " + e.getMessage(), e);
		} finally {
			DatabaseConnector.youMayClose();
			System.out.println(StringUtils.isNotBlank(uuid) ? uuid : "error");
		}
	}
}
