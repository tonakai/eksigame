package aykutsoysal.server;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilityTest {

	@Test
	public void testEqual() {
		String string1 = "Çocuk";
		String string2 = "coCuk";
		Assert.assertTrue(StringUtility.equalsIgnoreCaseAndDiacritics(string1, string2));
		string2 = "cocuk2";
		Assert.assertFalse(StringUtility.equalsIgnoreCaseAndDiacritics(string1, string2));
	}
}
