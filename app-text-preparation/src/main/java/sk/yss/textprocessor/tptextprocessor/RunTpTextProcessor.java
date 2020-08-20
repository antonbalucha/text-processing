package sk.yss.textprocessor.tptextprocessor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.apiclasses.Word;
// import sk.yss.textprocessor.tpinitializer.Logger;
import sk.yss.textprocessor.tptextprocessor.processors.TextProcessor;

public class RunTpTextProcessor {

	private static final Logger logger = LogManager.getLogger(RunTpTextProcessor.class);

	public static void print(List<Word> words) {
		if (words != null && words.size() > 0) {
			for (int i = 0; i < words.size(); i++) {
				logger.debug(words.get(i).toString());
			}
		}
	}

	private static void process(String uuid) {

		String text = Database.selectTextWithRemovedTags(uuid);
		text = TextProcessor.removeNonTextCharacters(text);

		List<Word> words = TextProcessor.splitTextToWords(text);
		words = TextProcessor.removeDotsAtTheEnd(words);
		words = TextProcessor.removeWordsMadeOnlyFromSpecificChars(words);
		words = TextProcessor.toLowerCase(words);
		words = TextProcessor.removeDiacriticsFromWord(words);
		String language = TextProcessor.identifyLanguage(words);
		words = TextProcessor.removeStopWords(words, language);
		words = TextProcessor.stemmer(words, language);
		words = TextProcessor.removeDiacriticsFromStem(words);
		words = TextProcessor.countWordOccurences(words);
		words = TextProcessor.unique(words);
		words = TextProcessor.countTermFrequency(words);

		Database.updateLanguage(uuid, language);
		Database.updateNumberOfWordsInText(uuid, words);
		Database.insertWords(uuid, words);

		print(words);
	}

	public static void run(String uuid) {
		process(uuid);
	}

	public static void main(String[] args) {

		if (args != null && args.length == 2 && args[0].equals("-uuid") && isNotBlank(args[1])) {
			logger.info("Valid number of parameters. Entered record with uuid '" + args[1] + "' will be processed.");
			run(args[1]);
		} else {
			logger.error("Invalid number of parameters! Use: -uuid uuid_for_processing");
		}
	}
}
