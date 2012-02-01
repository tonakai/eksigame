package aykutsoysal.server;

import java.text.Normalizer;

public class StringUtility {
	static String RemoveDiacritics(String s) {
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		return s;
	}
	
	static boolean equalsIgnoreCaseAndDiacritics(String string1, String string2) {
		return RemoveDiacritics(string1).equalsIgnoreCase(RemoveDiacritics(string2));
	}
}
