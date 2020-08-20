package sk.yss.textprocessor;

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

	public static String selectUuid() {

		Connection connection = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		String uuid = null;

		try {
			ps = connection.prepareStatement("SELECT uuid FROM web_pages WHERE content IS NOT NULL AND removed_tags IS NULL ORDER BY id LIMIT 1;");
			rs = ps.executeQuery();

			if (rs != null && rs.next()) {
				uuid = rs.getString(1);
			}
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
		} finally {
			close(rs);
			close(ps);
		}

		logger.info("Content identified by uuid='" + uuid + "' was selected from database.");

		return uuid;
	}
}
