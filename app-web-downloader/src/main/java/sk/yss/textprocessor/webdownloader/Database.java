package sk.yss.textprocessor.webdownloader;

import static sk.yss.textprocessor.utilities.connectors.DatabaseConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.apiclasses.WebPage;
import sk.yss.textprocessor.utilities.UUIDProcessor;
import sk.yss.textprocessor.utilities.connectors.DatabaseConnectionCloser;

public class Database {

	private static final Logger logger = LogManager.getLogger(Database.class);

	public static WebPage selectUrl(String linkMustContains) {

		Connection connection = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		WebPage wp = null;

		try {
			ps = connection.prepareStatement("SELECT uuid, url FROM web_pages WHERE content IS NULL AND LOCATE(?, url) > 0 ORDER BY id LIMIT 1;");
			ps.setString(1, linkMustContains);
			rs = ps.executeQuery();

			if (rs != null && rs.next()) {
				wp = new WebPage().setUuid(rs.getString(1)).setUrl(rs.getString(2));

				logger.debug("Web page info url='" + wp.getUrl() + "', uuid='" + wp.getUuid() + "' was selected from database.");
			}
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
		} finally {
			DatabaseConnectionCloser.close(rs);
			DatabaseConnectionCloser.close(ps);
		}

		return wp;
	}

	public static String insertContent(WebPage wp) {

		if (wp != null && StringUtils.isNotBlank(wp.getUuid()) && StringUtils.isNotBlank(wp.getUrl()) && StringUtils.isNotBlank(wp.getContent())) {

			logger.debug("Content of web page with uuid='" + wp.getUuid() + "' will be inserted to database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;

			String uuid = null;

			try {
				ps = connection.prepareStatement("INSERT INTO web_pages (uuid, url, content) " + "SELECT * FROM (SELECT ?, ?, ?) AS tmp "
						+ "WHERE NOT EXISTS (SELECT url FROM web_pages WHERE url = ?) LIMIT 1;");

				ps.setString(1, wp.getUuid());
				ps.setString(2, wp.getUrl());
				ps.setString(3, wp.getContent());
				ps.setString(4, wp.getUrl());
				int executeUpdate = ps.executeUpdate();

				if (executeUpdate == 1) {
					logger.debug("Content of web page with uuid='" + wp.getUuid() + "' was inserted to database.");
					uuid = wp.getUuid();
				} else {
					logger.error("Url is probably in database, because number of inserted records='" + executeUpdate + "'! (Trying to insert record with uuid='"
							+ wp.getUuid() + "')");
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}

			return uuid;
		} else {
			logger.error("Entered parameter webPage is null!");
			return null;
		}
	}

	public static void updateContent(WebPage wp) {

		if (wp != null && StringUtils.isNotBlank(wp.getUuid())) {

			logger.debug("Content of web page with uuid='" + wp.getUuid() + "' will be updated in database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;

			try {
				ps = connection.prepareStatement("UPDATE web_pages SET content = ? WHERE uuid = ?");
				ps.setString(1, wp.getContent());
				ps.setString(2, wp.getUuid());
				int executeUpdate = ps.executeUpdate();

				if (executeUpdate == 1) {
					logger.debug("Content of web page with uuid='" + wp.getUuid() + "' was inserted to database.");
				} else {
					logger.debug("Some problem occured when updating content of web page with uuid='" + wp.getUuid() + "'");
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}
		} else {
			logger.error("Entered parameter webPage is null!");
		}
	}

	public static void insertLinks(WebPage wp) {

		if (wp != null && wp.getLinks() != null && wp.getLinks().size() > 0) {

			logger.debug("Links of web page with uuid='" + wp.getUuid() + "' will be inserted to database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;

			try {
				for (String link : wp.getLinks()) {
					ps = connection.prepareStatement("INSERT INTO web_pages (uuid, url) " + "SELECT * FROM (SELECT ?, ?) AS tmp "
							+ "WHERE NOT EXISTS (SELECT url FROM web_pages WHERE url = ?) LIMIT 1;");

					ps.setString(1, UUIDProcessor.generateUuid());
					ps.setString(2, link);
					ps.setString(3, link);
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}
		} else {
			logger.error("Entered parameter web page or parameter representing list of links is null or empty!");
		}
	}
}
