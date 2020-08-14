package sk.yss.textprocessor.configuration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.yss.textprocessor.configuration.helper.OSIdentifier;

/** 
 * Class contains methods used for initialization of configuration loader. <br>
 */
public class ConfigurationInitializer {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationInitializer.class);
	
	public static final String UTF_8 = "UTF-8";
	
	public static final String WINDOWS_CONFIG_LOCATION = "C:\\devel\\textprocessor\\configuration\\config.properties";
	public static final String LINUX_CONFIG_LOCATION = "/etc/textprocessor/configuration/config.properties";

	public static final String CONFIGURATION_PROPERTIES;
	public static final String PROPERTY_PREFIX;
	
	private static PropertiesConfiguration config;
	
	static {
		
		if (OSIdentifier.isWindows()) {
			CONFIGURATION_PROPERTIES = WINDOWS_CONFIG_LOCATION;
			PROPERTY_PREFIX = "windows";
		} else {
			CONFIGURATION_PROPERTIES = LINUX_CONFIG_LOCATION;
			PROPERTY_PREFIX = "linux";
		}
		
		try {
			config = new PropertiesConfiguration(CONFIGURATION_PROPERTIES);
			config.setEncoding(UTF_8);
			config.setThrowExceptionOnMissing(false);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			logger.error("ConfigurationException: " + e.getMessage(), e);
		}
	}

	static final String loadAsString(String propertyName) {
		return StringUtils.isBlank(propertyName) ? null : config.getString(PROPERTY_PREFIX + "." + propertyName, null);
	}
	
	static final Integer loadAsInteger(String propertyName) {
		return StringUtils.isBlank(propertyName) ? null : config.getInteger(PROPERTY_PREFIX + "." + propertyName, null);
	}
}
