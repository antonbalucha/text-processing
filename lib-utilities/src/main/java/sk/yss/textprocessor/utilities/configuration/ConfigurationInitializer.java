package sk.yss.textprocessor.utilities.configuration;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.utilities.OSIdentifier;

/**
 * Class contains methods used for initialization of configuration loader. <br>
 */
public class ConfigurationInitializer {

	private static final Logger logger = LogManager.getLogger(ConfigurationInitializer.class);

	public static final String UTF_8 = "UTF-8";

	public static final String WINDOWS_CONFIG_LOCATION = "C:\\devel\\textprocessor\\configuration\\config.properties";
	public static final String LINUX_CONFIG_LOCATION = "/etc/textprocessor/configuration/config.properties";

	public static final String CONFIGURATION_PROPERTIES;
	public static final String PROPERTY_PREFIX;

	private static Configuration config;

	static {

		if (OSIdentifier.isWindows()) {
			CONFIGURATION_PROPERTIES = WINDOWS_CONFIG_LOCATION;
			PROPERTY_PREFIX = "windows";
		} else {
			CONFIGURATION_PROPERTIES = LINUX_CONFIG_LOCATION;
			PROPERTY_PREFIX = "linux";
		}

		try {
			Parameters parameters = new Parameters();
			PropertiesBuilderParameters parametersSet = parameters.properties() //
					.setFileName(CONFIGURATION_PROPERTIES) //
					.setEncoding(UTF_8) //
					.setThrowExceptionOnMissing(false) //
					.setReloadingRefreshDelay(300000L); // 5 minutes

			FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class).configure(parametersSet);

			config = configurationBuilder.getConfiguration();
		} catch (ConfigurationException e) {
			logger.error("ConfigurationException: " + e.getMessage(), e);
		}
	}

	static final String loadAsString(String propertyName) {
		return isBlank(propertyName) ? null : config.getString(PROPERTY_PREFIX + "." + propertyName, null);
	}

	static final Integer loadAsInteger(String propertyName) {
		return isBlank(propertyName) ? null : config.getInteger(PROPERTY_PREFIX + "." + propertyName, null);
	}
}
