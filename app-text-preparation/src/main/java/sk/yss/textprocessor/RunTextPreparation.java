package sk.yss.textprocessor;

import static sk.yss.textprocessor.utilities.connectors.DatabaseConnector.youMayClose;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.tptagremoval.RunTpTagRemoval;
import sk.yss.textprocessor.tptextprocessor.RunTpTextProcessor;

public class RunTextPreparation {

	private static final Logger logger = LogManager.getLogger(RunTextPreparation.class);

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
			youMayClose();
		}
	}
}
