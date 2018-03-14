package technology.tonyb.textprocessor.tptextprocessor.stemmer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Downloaded and slightly modified from http://vi.ikt.ui.sav.sk/Projekty/Projekty_2008%2F%2F2009/Hana_Pifkov%C3%A1_-_Stemer
 */
public class SlovakStemmer {

	private static ArrayList<String[]> pripony = initZoznamPripon();

	private static String[] ei = new String[] { "e", "i", "iam", "iach", "iami", "í", "ia", "ie", "iu", "ím" };

	private static HashMap<String, String> dtnl = createDTNL();

	private static HashMap<String, String> dlheKratke = createLongShort();

	private static String[] cudzieSlovaPredIa = new String[] { "c", "z", "g" };

	private static String[] samohlasky = new String[] { "a", "á", "ä", "e", "é", "i", "í", "o", "ó", "u", "ú", "y", "ý", "ô", "ia", "ie", "iu" };

	private static String[] lr = new String[] { "r", "ŕ", "l", "ĺ" };

	/**
	 * Method which will stem entered word.
	 * 
	 * @param slovo - word which will be stemmed
	 * @return stemmed word
	 */
	public static String stem(String slovo) {
		return odstranPripony(slovo);
	}

	private static String odstranPripony(String slovo) {

		for (String[] pripony : pripony) {
			
			for (String pripona : pripony) {

				// nasli sme priponu
				if (slovo.endsWith(pripona)) {

					// detenele, ditinili
					if (ei(pripona)) {
						return changeDTNL(odstranPriponu(slovo, pripona));
					}

					// cudzie -cia, -gia...
					if (pripona.startsWith("i")) {
						if (cudzie(slovo, pripona)) {
							return odstranPriponu(slovo, pripona).concat("i");
						}
					}

					// ci nepride k overstemmingu
					if (overstemming(slovo, pripona))
						return slovo;

					// inak odstranime priponu
					return odstranPriponu(slovo, pripona);
				}
			}
		}

		// konci na er -> peter, sveter....
		if (slovo.endsWith("er")) {
			return (odstranPriponu(slovo, "er")).concat("r");
		}

		// konci na ok -> sviatok, odpadok....
		if (slovo.endsWith("ok")) {
			return (odstranPriponu(slovo, "ok")).concat("k");
		}

		// konci na zen -> podobizen, bielizen....
		if (slovo.endsWith("zeň")) {
			return (odstranPriponu(slovo, "eň")).concat("ň");
		}

		// konci na ol -> kotol....
		if (slovo.endsWith("ol")) {
			return (odstranPriponu(slovo, "ol")).concat("l");
		}

		// konci na ic -> matematic (matematik, matematici)... (pracovnici vs slnecnic)
		if (slovo.endsWith("ic")) {
			return (odstranPriponu(slovo, "c")).concat("k");
		}

		// konci na ec -> tanec, obec....
		if (slovo.endsWith("ec")) {
			return (odstranPriponu(slovo, "ec")).concat("c");
		}

		// konci na um -> studium, stadium....
		if (slovo.endsWith("um")) {
			return (odstranPriponu(slovo, "um"));
		}

		// genitiv pluralu pre vzory zena, ulica, gazdina, mesto, srdce ???
		return poriesGenitivPluralu(slovo);
	}

	// problem = napriklad pes - psa, den - dna a podobne
	private static boolean overstemming(String slovo, String pripona) {

		// overstemming zrejme vtedy, ked nam ostane koren bez samohlasky / bez l/r v strede slova

		String odstranenaPripona = odstranPriponu(slovo, pripona);

		for (String samohlaska : samohlasky) {
			if (odstranenaPripona.contains(samohlaska)) {
				return false;
			}
		}

		for (String rl : lr) {
			if (odstranenaPripona.contains(rl) && !odstranenaPripona.endsWith(rl)) {
				return false;
			}
		}

		return true;
	}

	// problem = ako rozoznat, ci je to zensky/stredny rod. pr: lama - lam vs. pan - panov TODO

	/**
	 * 
	 * @param in
	 * @return ak je to genitiv pluralu, vrat spravny tvar, ak nie je, vrat in
	 */
	private static String poriesGenitivPluralu(String in) {

		// v poslednej slabike musi byt dlha samohlaska / dlhe r/l

		Set<String> dlhe = dlheKratke.keySet();
		for (String dlha : dlhe) {

			if (in.contains(dlha)) {

				if (poslednaSlabika(in, dlha)) {

					in = nahradPosledne(in, dlha, dlheKratke.get(dlha));

					break;
				}
			}

		}

		return in;
	}

	/**
	 * posledna slabika - ak sa za danym substringom uz nenaxadza uz ziadna samohlaska
	 * 
	 * @param s
	 *            string
	 * @param t
	 *            substring
	 * @return
	 */
	private static boolean poslednaSlabika(String s, String t) {

		int pokial = s.lastIndexOf(t);
		String koniec = s.substring(pokial);
		koniec = koniec.substring(t.length());

		for (String samohlaska : samohlasky) {

			if (koniec.contains(samohlaska))
				return false;

		}

		return true;
	}

	/**
	 * nahradi posledny vyskyt podretazca v retazci inym podretazcom
	 * 
	 * @param s
	 * @param co
	 * @param cim
	 * @return
	 */
	private static String nahradPosledne(String s, String co, String cim) {

		int pokial = s.lastIndexOf(co);

		String koniec = s.substring(pokial);

		koniec = koniec.substring(co.length());

		koniec = cim + koniec;

		s = s.substring(0, pokial) + koniec;

		return s;
	}

	// problem - srdcia TODO
	private static boolean cudzie(String in, String pripona) {

		String s = odstranPriponu(in, pripona);

		for (String koncovka : cudzieSlovaPredIa) {
			if (s.endsWith(koncovka)) {
				return true;
			}
		}

		return false;
	}

	private static String odstranPriponu(String slovo, String pripona) {
		return slovo.endsWith(pripona) ? (slovo.substring(0, slovo.length() - pripona.length())) : slovo; 
	}

	// TODO problem - napr sused - sudedia vs. priatel - priatelia
	private static boolean ei(String slovo) {
		for (String s2 : ei) {
			if (slovo.equals(s2)) {
				return true;
			}
		}
		return false;
	}

	private static String changeDTNL(String slovo) {

		Set<String> tvrde = dtnl.keySet();

		for (String tvrdy : tvrde) {
			if (slovo.endsWith(tvrdy)) {
				slovo = slovo.substring(0, slovo.length() - 1);
				slovo = slovo.concat(dtnl.get(tvrdy));
			}
		}

		return slovo;
	}

	/**
	 * Vytvori zoznam pripon podstatnych mien od najdlhsich po najkratsie tak, ze ak je nejaka pripona obsiahnuta v inej, je kratsia.
	 * 
	 * Pripony pre vzory: chlap, hrdina, dub, stroj, hostinsky zena, ulica, dlan, kost, gazdina mesto, srdce, vysvedcenie, dievca (+ holuba)
	 */
	private static ArrayList<String[]> initZoznamPripon() {

		ArrayList<String[]> p = new ArrayList<String[]>();

		// od najdlhsich po najkratsie tak, ze ak je nejaka pripona obsiahnuta v inej, je kratsia

		// najdlhsie (nie su v ziadnej inej obsiahnute)
		p.add(new String[] { "encami", "atami", "ätami", "iami", "ými", "ovi", "ati", "äti", "eniec", "ence", "ie", "aťom", "äťom", "encom", "atám", "ätám",
				"iam", "ím", "ým", "encoch", "atách", "ätách", "iach", "ých", "aťa", "äťa", "ovia", "atá", "ätá", "aťu", "äťu", "ému", "iu", "iou", "ov", "at",
				"ät", "ä", "ého", "ý", "y", "ií", "ej", "ú", "é" });

		p.add(new String[] { "e", "om", "ami", "ám", "och", "ach", "ách", "ia", "á", "ou", "o", "ii", "í" });

		p.add(new String[] { "mi", "a", "u" });

		p.add(new String[] { "i" });

		return p;
	}

	/**
	 * V ramci zenskeho a stredneho rodu - ideme riesit genitiv pluralu
	 */
	private static HashMap<String, String> createLongShort() {

		HashMap<String, String> h = new HashMap<String, String>();

		h.put("á", "a");
		h.put("ie", "e");
		h.put("ŕ", "r");
		h.put("ĺ", "l");
		h.put("í", "i");
		h.put("ú", "u");
		h.put("ŕ", "r");
		h.put("ô", "o");

		return h;
	}

	/**
	 * d -> ď, t -> ť, ...
	 */
	private static HashMap<String, String> createDTNL() {

		HashMap<String, String> h = new HashMap<String, String>();

		h.put("d", "ď");
		h.put("t", "ť");
		h.put("n", "ň");
		h.put("l", "ľ");

		return h;
	}
}
