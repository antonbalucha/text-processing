package sk.yss.textprocessor.utilities;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDProcessor {

	private static final Pattern UUID_PATTERN = Pattern.compile("^[0-9a-f]{8}-?[0-9a-f]{4}-?[0-5][0-9a-f]{3}-?[089ab][0-9a-f]{3}-?[0-9a-f]{12}$");

	public static String generateUuid() {
		return UUID.randomUUID().toString();
	}

	public static boolean isValidUuid(String uuid) {
		return ((uuid != null) && (uuid.trim().length() > 31) && (UUID_PATTERN.matcher(uuid).matches()));
	}
}
