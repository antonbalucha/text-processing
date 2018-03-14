package technology.tonyb.textprocessor.tpconfiguration.configuration;

import org.apache.commons.lang3.StringUtils;

/**
 * Class contains methods used for loading of configuration properties. It encapsulates direct calls of
 * {@linkplain org.apache.commons.configuration.PropertiesConfiguration PropertiesConfiguration} and simplifies selection of correct configuration property. <br>
 */
public class ConfigurationReader {

	public static String getPropertyAsString(String property) {
		return ConfigurationInitializer.loadAsString(property);
	}
	
	public static String[] getPropertiesAsStringField(String property) {
		String properties = ConfigurationInitializer.loadAsString(property);
		
		if (StringUtils.isNotBlank(properties)) {
			return properties.split(",");	
		} else {
			return null;
		}
	}

	public static Integer getPropertyAsInteger(String property) {
		return ConfigurationInitializer.loadAsInteger(property);
	}
}