package sk.yss.textprocessor.tptextprocessor;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.apiclasses.Word;
import sk.yss.textprocessor.utilities.connectors.DatabaseConnectionCloser;

public class Database {

	private static final Logger logger = LogManager.getLogger(Database.class);

	public static String selectTextWithRemovedTags(String uuid) {

		if (isNotBlank(uuid)) {
			logger.info("Text with removed tags identified by uuid='" + uuid + "' will be selected from database.");

			Connection connection = DatabaseConnectionCloser.getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			String removedTags = null;

			try {
				ps = connection.prepareStatement("SELECT removed_tags FROM web_pages WHERE uuid = ?");
				ps.setString(1, uuid);
				rs = ps.executeQuery();

				if (rs != null && rs.next()) {
					removedTags = rs.getString(1);
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(rs);
				DatabaseConnectionCloser.close(ps);
			}

			logger.info("Content identified by uuid='" + uuid + "' was selected from database.");

			return removedTags;

		} else {
			logger.error("Entered uuid is null or empty!");
			return null;
		}
	}

	public static void updateLanguage(String uuid, String language) {

		if (isNotBlank(uuid) && isNotBlank(language)) {
			logger.debug("Information about language '" + language + "' for uuid='" + uuid + "' will be inserted to database.");

			Connection connection = DatabaseConnectionCloser.getConnection();
			PreparedStatement ps = null;

			try {
				ps = connection.prepareStatement("UPDATE web_pages SET language = ? WHERE uuid = ?");
				ps.setString(1, language);
				ps.setString(2, uuid);
				int numberOfUpdatedRecords = ps.executeUpdate();

				if (numberOfUpdatedRecords == 1) {
					logger.info("Information about language of record with uuid='" + uuid + "' was inserted to database.");
				} else {
					logger.error("Problem with update of record with uuid='" + uuid + "'. Number of updated records is '" + numberOfUpdatedRecords + "'!");
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}
		} else {
			logger.error("Entered uuid or language is null or empty!");
		}
	}

	public static void updateNumberOfWordsInText(String uuid, List<Word> words) {

		if (isNotBlank(uuid) && words != null && words.size() > 0) {

			logger.debug("Information about number of words in text with value '" + words.size() + "' for uuid='" + uuid + "' will be inserted to database.");

			Connection connection = DatabaseConnectionCloser.getConnection();
			PreparedStatement ps = null;

			try {
				ps = connection.prepareStatement("UPDATE web_pages SET number_of_words_in_text = ? WHERE uuid = ?");
				ps.setInt(1, words.size());
				ps.setString(2, uuid);
				int numberOfUpdatedRecords = ps.executeUpdate();

				if (numberOfUpdatedRecords == 1) {
					logger.info("Update of information about number of words in text of record with uuid='" + uuid + "' was inserted to database.");
				} else {
					logger.error("Problem with update of record with uuid='" + uuid + "'. Number of updated records is '" + numberOfUpdatedRecords + "'!");
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}
		} else {
			logger.error("Entered uuid is null or empty or numberOfWordsInText is lower than 0!");
		}
	}

	public static void insertWords(String uuid, List<Word> words) {

		if (isNotBlank(uuid) && words != null && words.size() > 0) {
			logger.debug("Processed words for uuid='" + uuid + "' will be inserted to database.");

			Connection connection = DatabaseConnectionCloser.getConnection();
			PreparedStatement ps = null;

			try {
				for (int i = 0; i < words.size(); i++) {
					ps = connection.prepareStatement(
							"INSERT INTO words (uuid, word, word_lower_case, word_lower_case_no_diacritics, stemmed_word_lower_case, stemmed_word_lower_case_no_diacritics, number_of_word_occurences_in_text, term_frequency, tfidf) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
					ps.setString(1, uuid);
					ps.setString(2, words.get(i).getWord());
					ps.setString(3, words.get(i).getWordLowerCase());
					ps.setString(4, words.get(i).getWordLowerCaseNoDiacritics());
					ps.setString(5, words.get(i).getStemmedWordLowerCase());
					ps.setString(6, words.get(i).getStemmedWordLowerCaseNoDiacritics());
					ps.setInt(7, words.get(i).getNumberOfWordOccurencesInText());
					ps.setString(8, String.format("%.16f", words.get(i).getTermFrequency()));
					ps.setString(9, String.format("%.16f", 0.0d));
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				DatabaseConnectionCloser.close(ps);
			}
		} else {
			logger.error("Entered uuid or list of words is null or empty!");
		}
	}
}
