package sk.yss.textprocessor.configuration.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamConnector {

	private static final Logger logger = LoggerFactory.getLogger(StreamConnector.class);
	
	public static void close(BufferedReader br) {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				logger.error("IOException: " + e.getMessage(), e);
			} finally {
				br = null;
			}
		}
	}

	public static void close(Reader r) {
		if (r != null) {
			try {
				r.close();
			} catch (IOException e) {
				logger.error("IOException: " + e.getMessage(), e);
			} finally {
				r = null;
			}
		}
	}
	
	public static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				logger.error("IOException: " + e.getMessage(), e);
			} finally {
				is = null;
			}
		}
	}
}
