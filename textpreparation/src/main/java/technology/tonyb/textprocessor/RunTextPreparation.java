package technology.tonyb.textprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.tonyb.textprocessor.tpconfiguration.helper.DatabaseConnector;
import technology.tonyb.textprocessor.tptagremoval.RunTpTagRemoval;
import technology.tonyb.textprocessor.tptextprocessor.RunTpTextProcessor;

public class RunTextPreparation {

	private static final Logger logger = LoggerFactory.getLogger(RunTextPreparation.class);
	
	private static void process() {
		
		String uuid = Database.selectUuid();
		
		RunTpTagRemoval.run(uuid);
		RunTpTextProcessor.run(uuid);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			logger.error("InterruptedException: " + e.getMessage(), e);
		}
		
		process();
	}
	
	public static void main(String[] args) {
		try {
			process();
		} catch (Throwable e) {
			logger.error("Throwable: " + e.getMessage(), e);
		} finally {
			DatabaseConnector.youMayClose();
		}
	}
}
