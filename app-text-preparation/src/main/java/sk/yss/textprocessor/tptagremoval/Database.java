package sk.yss.textprocessor.tptagremoval;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static sk.yss.textprocessor.utilities.connectors.DatabaseConnectionCloser.close;
import static sk.yss.textprocessor.utilities.connectors.DatabaseConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Database {

	private static final Logger logger = LogManager.getLogger(Database.class);

	public static String selectContent(String uuid) {

		if (isNotBlank(uuid)) {
			logger.info("Content identified by uuid='" + uuid + " will be selected from database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String content = null;

			try {
				ps = connection.prepareStatement("SELECT content FROM web_pages WHERE uuid = ?");
				ps.setString(1, uuid);
				rs = ps.executeQuery();

				if (rs != null && rs.next()) {
					content = rs.getString(1);
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				close(rs);
				close(ps);
			}

			logger.info("Content identified by uuid='" + uuid + "' was selected from database.");

			return content;

		} else {
			logger.error("Entered uuid is null or empty!");
			return null;
		}
	}

	public static void insertRemovedTags(String uuid, String removedTags) {

		if (isNotBlank(uuid) && isNotBlank(removedTags)) {
			logger.info("Content with removed tags of record with uuid='" + uuid + "' will be inserted to database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;

			try {
				ps = connection.prepareStatement("UPDATE web_pages SET removed_tags = ? WHERE uuid = ?");
				ps.setString(1, removedTags);
				ps.setString(2, uuid);
				int numberOfUpdatedRecords = ps.executeUpdate();

				if (numberOfUpdatedRecords == 1) {
					logger.info("Content with removed tags of record with uuid='" + uuid + "' was inserted to database.");
				} else {
					logger.error("Problem with update of record with uuid='" + uuid + "'. Number of updated records is '" + numberOfUpdatedRecords + "'!");
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				close(ps);
			}
		} else {
			logger.error("One of parameters uuid or content is null or empty!");
		}
	}
}
