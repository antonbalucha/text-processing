package technology.tonyb.textprocessor.tptextprocessor.stopwords;

import technology.tonyb.textprocessor.tptextprocessor.processors.TextProcessor;

public class CzechSW {

	public static final String[] sw = {
		"a", 
		"aby", 
		"aj", 
		"ale", 
		"ani", 
		"aniž", 
		"ano", 
		"asi", 
		"až", 
		"b", 
		"bez", 
		"bude", 
		"budem", 
		"budeš", 
		"by", 
		"byl", 
		"byla", 
		"byli", 
		"bylo", 
		"být", 
		"c", 
		"co", 
		"což", 
		"cz", 
		"či", 
		"d", 
		"dnes", 
		"do", 
		"e", 
		"f", 
		"g", 
		"h", 
		"ho", 
		"ch", 
		"i", 
		"j", 
		"já", 
		"jak", 
		"jako", 
		"je", 
		"jeho", 
		"jej", 
		"její", 
		"jejich", 
		"jen", 
		"jenž", 
		"ještě", 
		"ji", 
		"jiné", 
		"již", 
		"jsem", 
		"jseš", 
		"jsme", 
		"jsou", 
		"jšte", 
		"k", 
		"kam", 
		"každý", 
		"kde", 
		"kdo", 
		"když", 
		"ke", 
		"která", 
		"které", 
		"kterou", 
		"který", 
		"kteři", 
		"ku", 
		"l", 
		"m", 
		"ma", 
		"máte", 
		"me", 
		"mě", 
		"mezi", 
		"mi", 
		"mít", 
		"mně", 
		"mnou", 
		"můj", 
		"může", 
		"my", 
		"n", 
		"na", 
		"ná", 
		"nad", 
		"nám", 
		"náš", 
		"naši", 
		"ne", 
		"nebo", 
		"nechť", 
		"nejsou", 
		"není", 
		"než", 
		"ní", 
		"nic", 
		"nové", 
		"nový", 
		"o", 
		"od", 
		"ode", 
		"on", 
		"p", 
		"pak", 
		"po", 
		"pod", 
		"podle", 
		"pokud", 
		"pouze", 
		"práve", 
		"pro", 
		"proč", 
		"proto", 
		"protože", 
		"první", 
		"před", 
		"přede", 
		"přes", 
		"při", 
		"pta", 
		"q", 
		"r", 
		"re", 
		"s", 
		"se", 
		"si", 
		"sice", 
		"strana", 
		"své", 
		"svůj", 
		"svých", 
		"svým", 
		"svými", 
		"t", 
		"ta", 
		"tak", 
		"také", 
		"takže", 
		"tato", 
		"te", 
		"tě", 
		"tedy", 
		"těma", 
		"ten", 
		"tento", 
		"této ", 
		"tím", 
		"tímto", 
		"tipy", 
		"to", 
		"tohle", 
		"toho", 
		"tohoto", 
		"tom", 
		"tomto", 
		"tomuto", 
		"toto", 
		"tu", 
		"tuto", 
		"tvůj", 
		"ty", 
		"tyto", 
		"u", 
		"už", 
		"v", 
		"vám", 
		"váš", 
		"vaše", 
		"ve", 
		"více", 
		"však", 
		"vy", 
		"w", 
		"x", 
		"y", 
		"z", 
		"za", 
		"zda", 
		"zde", 
		"ze", 
		"zpět", 
		"že"
	};
	
	public static String[] swNoDiacritics = removeDiacritics();
	
	private static String[] removeDiacritics() {
		
		String[] removedDiacritics = new String[sw.length];
		
		for (int i = 0; i < sw.length; i++) {
			removedDiacritics[i] = TextProcessor.removeDiacritics(sw[i]);
		}
		
		return removedDiacritics;
	}
}
