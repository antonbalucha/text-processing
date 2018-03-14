package technology.tonyb.textprocessor.webdownloader.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.tonyb.textprocessor.tpconfiguration.helper.StreamConnector;

public class HttpsDownloader {

	private static final Logger logger = LoggerFactory.getLogger(HttpsDownloader.class);
	
	public static String download(String urlS) {

		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
		}};
		
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		
		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException: " + e.getMessage(), e);
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException: " + e.getMessage(), e);
		}

		InputStream is = null;
		Reader r = null;
		BufferedReader br = null;
		
		StringBuilder content = new StringBuilder();
		
		try {
			URL url = new URL(urlS);
			URLConnection urlConnection = url.openConnection();
			
			is = urlConnection.getInputStream();
			r = new InputStreamReader(is);
			br = new BufferedReader(r);
			
			String line = "";
			
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (MalformedURLException e) {
			logger.error("MalformedURLException: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage(), e);
		} finally {
			StreamConnector.close(br);
			StreamConnector.close(r);
			StreamConnector.close(is);
			br = null;
			r = null;
			is = null;
		}

		return content.toString();
	}
}
