package sk.yss.textprocessor.helper;

import org.junit.Assert;
import org.junit.Test;

import sk.yss.textprocessor.configuration.helper.UUIDProcessor;

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
