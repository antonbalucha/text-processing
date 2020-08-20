package sk.yss.textprocessor.utilities.configuration;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Class contains methods used for loading of configuration properties.
 * It encapsulates direct calls of {@linkplain org.apache.commons.configuration.PropertiesConfiguration PropertiesConfiguration}
 * and simplifies selection of correct configuration property. <br>
 */
public class ConfigurationReader {

	public static String getPropertyAsString(String property) {
		return ConfigurationInitializer.loadAsString(property);
	}

	public static Integer getPropertyAsInteger(String property) {
		return ConfigurationInitializer.loadAsInteger(property);
	}
	
	public static String[] getPropertiesAsStringField(String property) {

		String properties = ConfigurationInitializer.loadAsString(property);

		if (isNotBlank(properties)) {
			return properties.split(",");
		} else {
			return null;
		}
	}
}