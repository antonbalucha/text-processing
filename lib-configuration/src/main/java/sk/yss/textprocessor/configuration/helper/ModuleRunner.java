package sk.yss.textprocessor.configuration.helper;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.yss.textprocessor.configuration.ConfigurationReader;

public class ModuleRunner {

	private static final Logger logger = LoggerFactory.getLogger(ModuleRunner.class);
	
	public static void callNextModule(String moduleStartupKey, String parameter) {
		
		String nextModuleStartupCmd = ConfigurationReader.getPropertyAsString(moduleStartupKey);
		
		if (StringUtils.isNotBlank(nextModuleStartupCmd)) {
			
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
