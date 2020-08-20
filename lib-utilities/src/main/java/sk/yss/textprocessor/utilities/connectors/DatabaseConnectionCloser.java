package sk.yss.textprocessor.utilities.connectors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DatabaseConnectionCloser {

	private static final Logger logger = LogManager.getLogger(DatabaseConnectionCloser.class);

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				connection = null;
			}
		}
	}

	public static void rollback(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.rollback();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			}
		}
	}

	public static void close(PreparedStatement ps) {
		if (ps != null) {
			try {
				if (!ps.isClosed()) {
					ps.close();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				ps = null;
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				if (!rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				rs = null;
			}
		}
	}
}
