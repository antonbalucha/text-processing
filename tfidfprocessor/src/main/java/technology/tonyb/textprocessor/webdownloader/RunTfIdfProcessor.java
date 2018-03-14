package technology.tonyb.textprocessor.webdownloader;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.tonyb.textprocessor.tpconfiguration.helper.DatabaseConnector;
import technology.tonyb.textprocessor.tpconfiguration.objects.TfIdf;
import technology.tonyb.textprocessor.tpconfiguration.objects.Word;

public class RunTfIdfProcessor {

	private static final Logger logger = LoggerFactory.getLogger(RunTfIdfProcessor.class);

	private static final SecureRandom sr = new SecureRandom();
	
	private static Word selectRandomWord() {
		Database.selectWordMetadata();
		return Database.selectWord(sr.nextInt(Database.getMaxId() - Database.getMinId() + 1) + Database.getMinId());
	}
	
	private static Word selectWord(int id) {
		return Database.selectWord(id);
	}
	
	private static Word identifyTfIdf(Word word) {
		
		if (word != null && StringUtils.isNotBlank(word.getStemmedWordLowerCaseNoDiacritics())) {
				TfIdf tfIdf = Database.selectIdfInfo(word)
						.setNumberOfWordOccurencesInDocument(word.getNumberOfWordOccurencesInText())
						.countTfIdf();
				
				logger.debug("Identified tfidf numbers: " + tfIdf.toString());
				
				word.setTfIdf(tfIdf.getTfIdf());
				return word;
		} else {
			logger.error("Entered word for processing TF-IDF is null or its parts are null or empty!");
			return null;
		}
	}
	
	private static void process() {

		//while (true) {
			Database.selectWordMetadata();
			
			if (Database.getMinId() < Database.getMaxId()) {
				
				Word word = selectWord(2297);
//				Word word = selectRandomWord();
				word = identifyTfIdf(word);
				Database.updateTfIdf(word);
				word = null;
				
				try {
					Thread.sleep(50);
					System.gc();
				} catch (InterruptedException e) {
					logger.error("InterruptedException: " + e.getMessage(), e);
				}
			} else {
				try {
					Thread.sleep(2000);
					System.gc();
				} catch (InterruptedException e) {
					logger.error("InterruptedException: " + e.getMessage(), e);
				}
			}
		//}
	}
	
	public static void main(String[] args) {
		try {
			process();
		} catch (Throwable e) {
			logger.error("Throwable: " + e.getMessage(), e);
		} finally {
			DatabaseConnector.youMayClose();
		}
	}
}
