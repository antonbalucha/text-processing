package technology.tonyb.textprocessor.tptextprocessor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import technology.tonyb.textprocessor.tpconfiguration.objects.Word;
import technology.tonyb.textprocessor.tptextprocessor.processors.TextProcessor;
import technology.tonyb.textprocessor.tptextprocessor.stopwords.SlovakSW;

public class TextProcessorTest {

	@Test
	public void testRemoveStopWords() {

		List<Word> words = new ArrayList<Word>();
		words.add(new Word("Ahoj").setWordLowerCase("ahoj"));
		words.add(new Word("ako").setWordLowerCase("ako"));
		words.add(new Word("sa").setWordLowerCase("sa"));
		words.add(new Word("máš?").setWordLowerCase("mas"));
		words.add(new Word("Čo").setWordLowerCase("co"));
		words.add(new Word("také").setWordLowerCase("take"));
		words.add(new Word("zaujímavé").setWordLowerCase("zaujimave"));
		words.add(new Word("robíš?").setWordLowerCase("robis"));
		words.add(new Word("Z").setWordLowerCase("z"));
		words.add(new Word("Z").setWordLowerCase("z"));
		
		String[] sw = SlovakSW.swNoDiacritics;
		
		List<Word> removedStopWords = TextProcessor.removeStopWords(words, sw);
		
		for (int i = 0; i < removedStopWords.size(); i++) {
			System.out.println(removedStopWords.get(i).getWord());
		}
	}
	
	@Test
	public void testIdentifyLanguage() {

		List<Word> wordsSK = new ArrayList<Word>();
		wordsSK.add(new Word("Ahoj").setWordLowerCaseNoDiacritics("ahoj"));
		wordsSK.add(new Word("ako").setWordLowerCaseNoDiacritics("ako"));
		wordsSK.add(new Word("sa").setWordLowerCaseNoDiacritics("sa"));
		wordsSK.add(new Word("máš?").setWordLowerCaseNoDiacritics("mas"));
		wordsSK.add(new Word("Čo").setWordLowerCaseNoDiacritics("co"));
		wordsSK.add(new Word("také").setWordLowerCaseNoDiacritics("take"));
		wordsSK.add(new Word("zaujímavé").setWordLowerCaseNoDiacritics("zaujimave"));
		wordsSK.add(new Word("robíš?").setWordLowerCaseNoDiacritics("robis"));

		RunTpTextProcessor.print(wordsSK);
		
		List<Word> wordsCZ = new ArrayList<Word>();
		wordsCZ.add(new Word("Nazdar").setWordLowerCaseNoDiacritics("nazdar"));
		wordsCZ.add(new Word("Jak").setWordLowerCaseNoDiacritics("jak"));
		wordsCZ.add(new Word("se").setWordLowerCaseNoDiacritics("se"));
		wordsCZ.add(new Word("máš?").setWordLowerCaseNoDiacritics("mas"));
		wordsCZ.add(new Word("Co").setWordLowerCaseNoDiacritics("co"));
		wordsCZ.add(new Word("děláš?").setWordLowerCaseNoDiacritics("delas"));
		
		RunTpTextProcessor.print(wordsCZ);
		
		List<Word> wordsEN = new ArrayList<Word>();
		wordsEN.add(new Word("Hello").setWordLowerCaseNoDiacritics("hello"));
		wordsEN.add(new Word("how").setWordLowerCaseNoDiacritics("how"));
		wordsEN.add(new Word("are").setWordLowerCaseNoDiacritics("are"));
		wordsEN.add(new Word("you?").setWordLowerCaseNoDiacritics("you"));
		wordsEN.add(new Word("What").setWordLowerCaseNoDiacritics("what"));
		wordsEN.add(new Word("are").setWordLowerCaseNoDiacritics("are"));
		wordsEN.add(new Word("you").setWordLowerCaseNoDiacritics("you"));
		wordsEN.add(new Word("doing?").setWordLowerCaseNoDiacritics("doing"));
		
		RunTpTextProcessor.print(wordsEN);
	}
	
	@Test
	public void testRemoveStopWords2() {

		// Test for slovak language
		List<Word> wordsSK = new ArrayList<Word>();
		wordsSK.add(new Word("Ahoj").setWordLowerCase("ahoj").setWordLowerCaseNoDiacritics("ahoj"));
		wordsSK.add(new Word("ako").setWordLowerCase("ako").setWordLowerCaseNoDiacritics("ako"));
		wordsSK.add(new Word("sa").setWordLowerCase("sa").setWordLowerCaseNoDiacritics("sa"));
		wordsSK.add(new Word("máš?").setWordLowerCase("máš?").setWordLowerCaseNoDiacritics("mas"));
		wordsSK.add(new Word("Čo").setWordLowerCase("čo").setWordLowerCaseNoDiacritics("co"));
		wordsSK.add(new Word("také").setWordLowerCase("také").setWordLowerCaseNoDiacritics("take"));
		wordsSK.add(new Word("zaujímavé").setWordLowerCase("zaujímavé").setWordLowerCaseNoDiacritics("zaujimave"));
		wordsSK.add(new Word("robíš?").setWordLowerCase("robíš").setWordLowerCaseNoDiacritics("robis"));

		String language = TextProcessor.identifyLanguage(wordsSK);
		System.out.println(language);
		List<Word> removedStopWords = TextProcessor.removeStopWords(wordsSK, language);
		RunTpTextProcessor.print(removedStopWords);
		
		// Test for Czech language
		List<Word> wordsCZ = new ArrayList<Word>();
		wordsCZ.add(new Word("Nazdar").setWordLowerCase("nazdar").setWordLowerCaseNoDiacritics("nazdar"));
		wordsCZ.add(new Word("Jak").setWordLowerCase("jak").setWordLowerCaseNoDiacritics("jak"));
		wordsCZ.add(new Word("se").setWordLowerCase("se").setWordLowerCaseNoDiacritics("se"));
		wordsCZ.add(new Word("máš?").setWordLowerCase("máš?").setWordLowerCaseNoDiacritics("mas"));
		wordsCZ.add(new Word("Co").setWordLowerCase("co").setWordLowerCaseNoDiacritics("co"));
		wordsCZ.add(new Word("děláš?").setWordLowerCase("děláš?").setWordLowerCaseNoDiacritics("delas"));
		
		language = TextProcessor.identifyLanguage(wordsCZ);
		System.out.println(language);
		removedStopWords = TextProcessor.removeStopWords(wordsCZ, language);
		RunTpTextProcessor.print(removedStopWords);

		
		// Test for English language
		List<Word> wordsEN = new ArrayList<Word>();
		wordsEN.add(new Word("Hello").setWordLowerCase("hello").setWordLowerCaseNoDiacritics("hello"));
		wordsEN.add(new Word("how").setWordLowerCase("how").setWordLowerCaseNoDiacritics("how"));
		wordsEN.add(new Word("are").setWordLowerCase("are").setWordLowerCaseNoDiacritics("are"));
		wordsEN.add(new Word("you?").setWordLowerCase("you?").setWordLowerCaseNoDiacritics("you"));
		wordsEN.add(new Word("What").setWordLowerCase("what").setWordLowerCaseNoDiacritics("what"));
		wordsEN.add(new Word("are").setWordLowerCase("are").setWordLowerCaseNoDiacritics("are"));
		wordsEN.add(new Word("you").setWordLowerCase("you").setWordLowerCaseNoDiacritics("you"));
		wordsEN.add(new Word("doing?").setWordLowerCase("doing?").setWordLowerCaseNoDiacritics("doing"));
		
		language = TextProcessor.identifyLanguage(wordsEN);
		System.out.println(language);
		removedStopWords = TextProcessor.removeStopWords(wordsEN, language);
		RunTpTextProcessor.print(removedStopWords);

	}
	
	@Test
	public void removeWordsMadeOnlyFromSpecificChars() {
		List<Word> wordsEN = new ArrayList<Word>();
		wordsEN.add(new Word("Hello").setWordLowerCaseNoDiacritics("hello"));
		wordsEN.add(new Word("-").setWordLowerCaseNoDiacritics("-"));
		wordsEN.add(new Word(".").setWordLowerCaseNoDiacritics("."));
		wordsEN.add(new Word("--?").setWordLowerCaseNoDiacritics("--?"));
		wordsEN.add(new Word("...").setWordLowerCaseNoDiacritics("..."));
		wordsEN.add(new Word("12").setWordLowerCaseNoDiacritics("12"));
		wordsEN.add(new Word("1").setWordLowerCaseNoDiacritics("1"));
		wordsEN.add(new Word("000").setWordLowerCaseNoDiacritics("000"));
		
		List<Word> removeWordsMadeOnlyFromSpecificChars = TextProcessor.removeWordsMadeOnlyFromSpecificChars(wordsEN);
		
		RunTpTextProcessor.print(removeWordsMadeOnlyFromSpecificChars);
	}
}
