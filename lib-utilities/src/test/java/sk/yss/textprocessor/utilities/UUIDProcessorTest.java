package sk.yss.textprocessor.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UUIDProcessorTest {

	@Test
	public void testGenerateUuid() {
		String uuid = UUIDProcessor.generateUuid();
		System.out.println(uuid);
		Assertions.assertNotNull(uuid);
	}

	@Test
	public void testIsValidUuid() {
		String uuid = UUIDProcessor.generateUuid();
		System.out.println(uuid);

		Assertions.assertEquals(false, UUIDProcessor.isValidUuid(null));
		Assertions.assertEquals(false, UUIDProcessor.isValidUuid(""));
		Assertions.assertEquals(true, UUIDProcessor.isValidUuid(UUIDProcessor.generateUuid()));
	}
}
