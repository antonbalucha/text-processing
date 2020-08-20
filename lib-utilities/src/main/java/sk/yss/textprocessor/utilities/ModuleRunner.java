package sk.yss.textprocessor.utilities;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.utilities.configuration.ConfigurationReader;

public class ModuleRunner {

	private static final Logger logger = LogManager.getLogger(ModuleRunner.class);

	public static void callNextModule(String moduleStartupKey, String parameter) {

		String nextModuleStartupCmd = ConfigurationReader.getPropertyAsString(moduleStartupKey);

		if (isNotBlank(nextModuleStartupCmd)) {

			nextModuleStartupCmd = String.format(nextModuleStartupCmd, parameter);
			logger.info("Next module will be called by command='" + nextModuleStartupCmd + "'");

			try {
				Runtime.getRuntime().exec(nextModuleStartupCmd);
			} catch (IOException e) {
				logger.error("IOException: " + e.getMessage(), e);
			}
		} else {
			logger.error("Next module will not be started because information how to run next module is missing!");
		}
	}
}
