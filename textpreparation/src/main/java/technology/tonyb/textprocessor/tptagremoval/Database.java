package technology.tonyb.textprocessor.tptagremoval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import technology.tonyb.textprocessor.tpconfiguration.helper.DatabaseConnector;

public class Database {

	private static final Logger logger = LoggerFactory.getLogger(Database.class);
	
	public static String selectContent(String uuid) {
		
		if (StringUtils.isNotBlank(uuid)) {
			logger.info("Content identified by uuid='" + uuid + " will be selected from database.");
			
			Connection connection = DatabaseConnector.getConnection();
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
				DatabaseConnector.close(rs);
				DatabaseConnector.close(ps);
			}

			logger.info("Content identified by uuid='" + uuid + "' was selected from database.");

			return content;

		} else {
			logger.error("Entered uuid is null or empty!");
			return null;
		}
	}
	
	public static void insertRemovedTags(String uuid, String removedTags) {
		
		if (StringUtils.isNotBlank(uuid) && StringUtils.isNotBlank(removedTags)) {
			logger.info("Content with removed tags of record with uuid='" + uuid + "' will be inserted to database.");
			
			Connection connection = DatabaseConnector.getConnection();
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
				DatabaseConnector.close(ps);
			}
		} else {
			logger.error("One of parameters uuid or content is null or empty!");
		}
	}
}
