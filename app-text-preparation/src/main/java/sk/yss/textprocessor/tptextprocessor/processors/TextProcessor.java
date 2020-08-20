package sk.yss.textprocessor.tptextprocessor.processors;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.apiclasses.Word;
import sk.yss.textprocessor.calculator.TfIdfProcessor;
import sk.yss.textprocessor.tptextprocessor.stemmer.EnglishStemmer;
import sk.yss.textprocessor.tptextprocessor.stemmer.SlovakStemmer;
import sk.yss.textprocessor.tptextprocessor.stopwords.CzechSW;
import sk.yss.textprocessor.tptextprocessor.stopwords.EnglishSW;
import sk.yss.textprocessor.tptextprocessor.stopwords.SlovakSW;

public class TextProcessor {

	public static final String SLOVAK = "slovak";
	public static final String CZECH = "czech";
	public static final String ENGLISH = "english";

	private static final String lettersWithDiacritic = "áäčďéěíĺľňóôőöŕšťúůűüýřžÁÄČĎÉĚÍĹĽŇÓÔŐÖŔŠŤÚŮŰÜÝŘŽ";
	private static final String lettersWithoutDiacritic = "aacdeeillnoooorstuuuuyrzAACDEEILLNOOOORSTUUUUYRZ";

	private static final Logger logger = LogManager.getLogger(TextProcessor.class);

	public static String removeDiacritics(String text) {

		if (StringUtils.isNotBlank(text)) {
			String textWithoutDiacritic = new String(text);

			for (int i = 0; i < lettersWithDiacritic.length(); i++) {
				textWithoutDiacritic = textWithoutDiacritic.replace(lettersWithDiacritic.charAt(i), lettersWithoutDiacritic.charAt(i));
			}

			return textWithoutDiacritic;
		} else {
			logger.error("Entered text for removing diacritics is null or empty!");
			return null;
		}
	}

	public static List<Word> removeDiacriticsFromWord(List<Word> words) {

		if (words != null && words.size() > 0) {

			for (int i = 0; i < words.size(); i++) {
				words.get(i).setWordLowerCaseNoDiacritics(removeDiacritics(words.get(i).getWordLowerCase()));
			}

			return words;
		} else {
			logger.error("Entered list of words for removing diacritics of words is null or empty!");
			return null;
		}
	}

	public static List<Word> removeDiacriticsFromStem(List<Word> words) {

		if (words != null && words.size() > 0) {

			for (int i = 0; i < words.size(); i++) {
				words.get(i).setStemmedWordLowerCaseNoDiacritics(removeDiacritics(words.get(i).getStemmedWordLowerCase()));
			}

			return words;
		} else {
			logger.error("Entered list of words for removing diacritics of stems is null or empty!");
			return null;
		}
	}

	public static List<Word> removeWordsMadeOnlyFromSpecificChars(List<Word> words) {

		if (words != null && words.size() > 0) {

			List<Word> cleanedList = new ArrayList<Word>(words.size());

			for (int i = 0; i < words.size(); i++) {
				if (!words.get(i).getWord().matches("^[.]{1,}$") && !words.get(i).getWord().matches("^[-]{1,}$") && !words.get(i).getWord().matches("^[–]{1,}$")
						&& !words.get(i).getWord().matches("^[0-9]{1,}$") && !words.get(i).getWord().matches("^[&]{1,}$")) {
					cleanedList.add(words.get(i));
				}
			}

			return cleanedList;
		} else {
			logger.error("Entered list of words for removing words made only from specific charactersis is null or empty!");
			return null;
		}
	}

	public static List<Word> splitTextToWords(String text) {

		if (StringUtils.isNotBlank(text)) {

			String[] splittedWords = text.split(" ");

			if (splittedWords != null && splittedWords.length > 0) {

				List<Word> words = new ArrayList<Word>(splittedWords.length);

				for (int i = 0; i < splittedWords.length; i++) {
					if (StringUtils.isNotBlank(splittedWords[i])) {
						words.add(new Word(splittedWords[i].trim()));
					}
				}

				return words;
			} else {
				logger.error("List of splitted words is null or empty!");
				return null;
			}
		} else {
			logger.error("Entered text for splitting to words is null or empty!");
			return null;
		}
	}

	public static String removeNonTextCharacters(String text) {

		if (StringUtils.isNotBlank(text)) {

			String charsForRemoval = ",;!?©:()✖/*+|'<>›`~{}\"™»[]„“";
			String textWithoutChars = new String(text);

			for (int i = 0; i < charsForRemoval.length(); i++) {
				textWithoutChars = textWithoutChars.replace(charsForRemoval.charAt(i) + "", " ");
			}

			return textWithoutChars;
		} else {
			logger.error("Entered text for removing characters is null or empty!");
			return null;
		}
	}

	public static List<Word> removeEmptyWords(List<Word> words) {

		if (words != null && words.size() > 0) {

			List<Word> cleanedList = new ArrayList<Word>(words.size());

			for (int i = 0; i < words.size(); i++) {
				if (StringUtils.isNotBlank(words.get(i).getWord())) {
					cleanedList.add(words.get(i));
				}
			}

			return cleanedList;
		} else {
			logger.error("Entered list of words for removing empty words is null or empty!");
			return null;
		}
	}

	public static String removeDotsAtTheEnd(String word) {

		if (StringUtils.isNotBlank(word)) {
			while (word.endsWith(".")) {
				word = word.substring(0, word.lastIndexOf("."));
			}

			return word;
		} else {
			logger.error("Entered word for removing dots at the end is null or empty!");
			return null;
		}
	}

	/**
	 * Method removes from the entered list:
	 * <ul>
	 * <li>dots from the end of the word</li>
	 * <li>words made only from dots</li>
	 * </ul>
	 */
	public static List<Word> removeDotsAtTheEnd(List<Word> words) {

		if (words != null && words.size() > 0) {

			List<Word> cleanedList = new ArrayList<Word>(words.size());

			for (int i = 0; i < words.size(); i++) {
				words.get(i).setWord(removeDotsAtTheEnd(words.get(i).getWord()));

				if (StringUtils.isNotBlank(words.get(i).getWord())) {
					cleanedList.add(words.get(i));
				}
			}

			return cleanedList;
		} else {
			logger.error("Entered list of words for removing diacritics is null or empty!");
			return null;
		}
	}

	public static List<Word> toLowerCase(List<Word> words) {

		if (words != null && words.size() > 0) {

			for (int i = 0; i < words.size(); i++) {
				words.get(i).setWordLowerCase(words.get(i).getWord().toLowerCase());
			}

			return words;
		} else {
			logger.error("Entered list of words for lower case is null or empty!");
			return null;
		}
	}

	public static int numberOfAllWords(List<Word> words) {
		if (words != null && words.size() > 0) {
			return words.size();
		} else {
			logger.error("Entered list of words for identification of number of all words is null or empty!");
			return 0;
		}
	}

	public static String identifyLanguage(List<Word> words) {

		if (words != null && words.size() > 0) {

			int slovak = 0;
			int czech = 0;
			int english = 0;

			for (int i = 0; i < words.size(); i++) {
				for (int j = 0; j < SlovakSW.sw.length; j++) {
					if (words.get(i).getWordLowerCaseNoDiacritics().equals(SlovakSW.swNoDiacritics[j])) {
						slovak++;
					}
				}
			}

			for (int i = 0; i < words.size(); i++) {
				for (int j = 0; j < CzechSW.sw.length; j++) {
					if (words.get(i).getWordLowerCaseNoDiacritics().equals(CzechSW.swNoDiacritics[j])) {
						czech++;
					}
				}
			}

			for (int i = 0; i < words.size(); i++) {
				for (int j = 0; j < EnglishSW.sw.length; j++) {
					if (words.get(i).getWordLowerCaseNoDiacritics().equals(EnglishSW.sw[j])) {
						english++;
					}
				}
			}

			return (slovak >= czech && slovak >= english) ? SLOVAK : ((czech >= slovak && czech >= english) ? CZECH : ENGLISH);
		} else {
			logger.error("Entered list of words for language identification is null or empty!");
			return null;
		}
	}

	public static List<Word> stemmer(List<Word> words, String language) {

		if (words != null && words.size() > 0) {

			switch (language) {
			case SLOVAK: {
				for (int i = 0; i < words.size(); i++) {
					words.get(i).setStemmedWordLowerCase(SlovakStemmer.stem(words.get(i).getWordLowerCase()));
				}
				break;
			}
			case CZECH: {
				for (int i = 0; i < words.size(); i++) {
					words.get(i).setStemmedWordLowerCase(SlovakStemmer.stem(words.get(i).getWordLowerCase()));
				}
				break;
			}
			case ENGLISH: {
				for (int i = 0; i < words.size(); i++) {
					words.get(i).setStemmedWordLowerCase(new EnglishStemmer().stem(words.get(i).getWordLowerCase()));
				}
				break;
			}
			default: {
				logger.error("Unknown language when stemming!");
				break;
			}
			}

			return words;
		} else {
			logger.error("Entered list of words for stemming is null or empty!");
			return null;
		}
	}

	public static List<Word> removeStopWords(List<Word> words, String[] stopWords) {

		if (words != null && words.size() > 0 && stopWords != null && stopWords.length > 0) {

			List<Word> removedStopWords = new ArrayList<Word>(words.size());

			for (int i = 0; i < words.size(); i++) {

				boolean isStopWord = false;

				for (int j = 0; j < stopWords.length; j++) {
					if (words.get(i) != null && StringUtils.isNotBlank(words.get(i).getWordLowerCase())
							&& words.get(i).getWordLowerCase().equals(stopWords[j])) {
						isStopWord = true;
						break;
					}
				}

				if (!isStopWord) {
					removedStopWords.add(words.get(i));
				}
			}

			return removedStopWords;
		} else {
			logger.error("Entered list of words or list of stop words for removing stop words is null or empty!");
			return null;
		}
	}

	public static List<Word> removeStopWords(List<Word> words, String language) {

		if (words != null && words.size() > 0) {

			List<Word> removedStopWords = null;

			switch (language) {
			case SLOVAK: {
				removedStopWords = removeStopWords(words, SlovakSW.swNoDiacritics);
				break;
			}

			case CZECH: {
				removedStopWords = removeStopWords(words, CzechSW.swNoDiacritics);
				break;
			}

			case ENGLISH: {
				removedStopWords = removeStopWords(words, EnglishSW.sw);
				break;
			}

			default: {
				logger.error("Unknown error when identifing language and removing stop words!");
				break;
			}
			}

			return removedStopWords;
		} else {
			logger.error("Entered list of words for removing stop words is null or empty!");
			return null;
		}
	}

	public static final List<Word> countWordOccurences(List<Word> words) {

		if (words != null && words.size() > 0) {

			for (int i = 0; i < words.size(); i++) {
				for (int j = 0; j < words.size(); j++) {
					if (StringUtils.isNotBlank(words.get(i).getStemmedWordLowerCaseNoDiacritics())
							&& StringUtils.isNotBlank(words.get(j).getStemmedWordLowerCaseNoDiacritics())
							&& words.get(i).getStemmedWordLowerCaseNoDiacritics().equals(words.get(j).getStemmedWordLowerCaseNoDiacritics())) {
						words.get(i).incrementNumberOfWordOccurenceInText();
					}
				}
			}

			return words;
		} else {
			logger.error("Entered list of words for counting of words is null or empty!");
			return null;
		}
	}

	public static final List<Word> unique(List<Word> words) {

		if (words != null && words.size() > 0) {

			List<Word> cleanedList = new ArrayList<Word>(words.size());

			for (int i = 0; i < words.size(); i++) {
				for (int j = i + 1; j < words.size(); j++) {
					if (words.get(i) != null && words.get(j) != null && StringUtils.isNotBlank(words.get(i).getStemmedWordLowerCaseNoDiacritics())
							&& StringUtils.isNotBlank(words.get(j).getStemmedWordLowerCaseNoDiacritics())
							&& words.get(i).getStemmedWordLowerCaseNoDiacritics().equals(words.get(j).getStemmedWordLowerCaseNoDiacritics())) {
						words.set(j, null);
					}
				}
			}

			for (int i = 0; i < words.size(); i++) {
				if (words.get(i) != null) {
					cleanedList.add(words.get(i));
				}
			}

			return cleanedList;
		} else {
			logger.error("Entered list of words for removal duplicates is null or empty!");
			return null;
		}
	}

	public static final List<Word> countTermFrequency(List<Word> words) {

		if (words != null && words.size() > 0) {

			for (int i = 0; i < words.size(); i++) {
				words.get(i).setTermFrequency(TfIdfProcessor.countTF(words.get(i).getNumberOfWordOccurencesInText(), words.size()));
			}

			return words;
		} else {
			logger.error("Entered list of words for term frequency identification is null or empty!");
			return null;
		}
	}
}
