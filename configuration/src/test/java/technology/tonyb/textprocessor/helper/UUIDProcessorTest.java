package technology.tonyb.textprocessor.helper;

import org.junit.Assert;
import org.junit.Test;

import technology.tonyb.textprocessor.tpconfiguration.helper.UUIDProcessor;

public class UUIDProcessorTest {

	@Test
	public void testGenerateUuid() {
		String uuid = UUIDProcessor.generateUuid();
		System.out.println(uuid);
		Assert.assertNotNull(uuid);
	}
	
	@Test
	public void testIsValidUuid() {
		String uuid = UUIDProcessor.generateUuid();
		System.out.println(uuid);

		Assert.assertEquals(false, UUIDProcessor.isValidUuid(null));
		Assert.assertEquals(false, UUIDProcessor.isValidUuid(""));
		Assert.assertEquals(true, UUIDProcessor.isValidUuid(UUIDProcessor.generateUuid()));
	}
}
