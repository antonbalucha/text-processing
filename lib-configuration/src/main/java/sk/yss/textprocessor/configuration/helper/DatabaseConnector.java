package sk.yss.textprocessor.configuration.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.yss.textprocessor.configuration.ConfigurationReader;

public class DatabaseConnector {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);
	
	private static final String PROPERTY_NAME_DRIVER = "database.connection.driver";
	private static final String PROPERTY_NAME_URL = "database.connection.url";
	private static final String PROPERTY_NAME_USERNAME = "database.connection.username";
	private static final String PROPERTY_NAME_PASSWORD = "database.connection.password";
	
	private static String driver;
	private static String url;
	private static String user;
	private static String password;
	
	private static Connection connection;
	
	static {
		init();
	}
	
	private static void init() {
		initializeProperties();
		initializeConnection();
	}
	
	private static void initializeProperties() {
		driver = ConfigurationReader.getPropertyAsString(PROPERTY_NAME_DRIVER);
		url = ConfigurationReader.getPropertyAsString(PROPERTY_NAME_URL);
		user = ConfigurationReader.getPropertyAsString(PROPERTY_NAME_USERNAME);
		password = ConfigurationReader.getPropertyAsString(PROPERTY_NAME_PASSWORD);
	}
	
	private static void initializeConnection() {

		close(connection);
		
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException: " + e.getMessage(), e);
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
		}
	}
	
	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				init();
			}
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
		}
		
		return connection;
	}
	
	public static void youMayClose() {
		
		close(connection);
		connection = null;
		
		driver = null;
		url = null;
		user = null;
		password = null;
	}
	
	public static void close(Connection connection) {
		if (connection != null) {
			try {
				if (!connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
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
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
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
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
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
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'" , e);
			} finally {
				rs = null;
			}
		}
	}
}
