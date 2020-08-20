package sk.yss.textprocessor.webdownloader;

import static sk.yss.textprocessor.utilities.connectors.DatabaseConnectionCloser.close;
import static sk.yss.textprocessor.utilities.connectors.DatabaseConnector.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sk.yss.textprocessor.apiclasses.TfIdf;
import sk.yss.textprocessor.apiclasses.Word;

public class Database {

	private static final Logger logger = LogManager.getLogger(Database.class);

	private static int minId = 0;

	private static int maxId = 0;

	public static int getMinId() {
		return minId;
	}

	public static int getMaxId() {
		return maxId;
	}

	public static void selectWordMetadata() {

		Connection connection = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = connection.prepareStatement("SELECT MIN(id) as min_id, MAX(id) as max_id FROM words;");
			rs = ps.executeQuery();

			if (rs != null && rs.next()) {
				minId = rs.getInt("min_id");
				maxId = rs.getInt("max_id");
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
		} finally {
			close(rs);
			close(ps);
			rs = null;
			ps = null;
			connection = null;
		}
	}

	/** Unfortunately, not working as expected. RAND() in MySQL is not random enough. */
	public static Word selectRandomWord() {

		Connection connection = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		Word word = null;

		try {
			ps = connection
					.prepareStatement("SELECT w.id, w.uuid, w.number_of_word_occurences_in_text, w.term_frequency, w.stemmed_word_lower_case_no_diacritics"
							+ " FROM words AS w, (SELECT ROUND(RAND() * (SELECT MAX(id) FROM words)) AS id) AS x WHERE w.id >= x.id LIMIT 1;");
			rs = ps.executeQuery();

			if (rs != null && rs.next()) {
				word = new Word().setId(rs.getLong("id")).setUuid(rs.getString("uuid"))
						.setNumberOfWordOccurencesInText(rs.getInt("number_of_word_occurences_in_text"))
						.setTermFrequency(Double.parseDouble(rs.getString("term_frequency").replace(",", ".")))
						.setStemmedWordLowerCaseNoDiacritics(rs.getString("stemmed_word_lower_case_no_diacritics"));
			}

			connection.commit();
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
		} finally {
			close(rs);
			close(ps);
			rs = null;
			ps = null;
			connection = null;
		}

		logger.info("Random word identified by uuid='" + word.getUuid() + "' was selected from database.");

		return word;
	}

	public static Word selectWord(int id) {

		Connection connection = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		Word word = null;

		if (id >= 0) {
			try {
				ps = connection.prepareStatement(
						"SELECT w.id, w.uuid, w.number_of_word_occurences_in_text, w.term_frequency, w.stemmed_word_lower_case_no_diacritics FROM words AS w WHERE w.id = ?");
				ps.setInt(1, id);
				rs = ps.executeQuery();

				if (rs != null && rs.next()) {
					word = new Word().setId(rs.getLong("id")).setUuid(rs.getString("uuid"))
							.setNumberOfWordOccurencesInText(rs.getInt("number_of_word_occurences_in_text"))
							.setTermFrequency(Double.parseDouble(rs.getString("term_frequency").replace(",", ".")))
							.setStemmedWordLowerCaseNoDiacritics(rs.getString("stemmed_word_lower_case_no_diacritics"));
				}

				connection.commit();

				logger.info("Random word identified by id='" + id + "' was selected from database.");
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				close(rs);
				close(ps);
				rs = null;
				ps = null;
				connection = null;
			}
		} else {
			logger.info("Entered id is lower than zero! id='" + id + "'");
		}

		return word;
	}

	public static TfIdf selectIdfInfo(Word word) {

		if (word != null && StringUtils.isNotBlank(word.getStemmedWordLowerCaseNoDiacritics())) {

			logger.debug("Information based on word='" + word.toString() + "' will be selected from database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			TfIdf tfidf = null;

			try {
				ps = connection
						.prepareStatement("SELECT t1.number_of_documents_with_word, t2.number_of_all_documents, t3.number_of_words_in_examined_document FROM "
								+ "(SELECT COUNT(*) AS number_of_documents_with_word FROM words WHERE stemmed_word_lower_case_no_diacritics = ?) AS t1, "
								+ "(SELECT COUNT(DISTINCT uuid) AS number_of_all_documents FROM words) AS t2, "
								+ "(SELECT COUNT(*) AS number_of_words_in_examined_document FROM words WHERE uuid = ?) as t3; ");

				ps.setString(1, word.getStemmedWordLowerCaseNoDiacritics());
				ps.setString(2, word.getUuid());
				rs = ps.executeQuery();

				if (rs != null && rs.next()) {
					tfidf = new TfIdf().setNumberOfAllDocuments(rs.getInt("number_of_all_documents"))
							.setNumberOfDocumentsWithWord(rs.getInt("number_of_documents_with_word"))
							.setNumberOfWordsInExaminedDocument(rs.getInt("number_of_words_in_examined_document"));

					logger.debug("Information retrieved for stemmedWordLowerCaseNoDiacritics='" + word.getStemmedWordLowerCaseNoDiacritics() + "' with values: "
							+ tfidf.toString() + " was selected from database.");
				}

				connection.commit();
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				close(rs);
				close(ps);
				// DatabaseConnector.close(connection);
				rs = null;
				ps = null;
				connection = null;
			}

			return tfidf;
		} else {
			logger.error("Entered word is null or some of its parts are null or empty for selection of information for tfidf computation!");
			return null;
		}
	}

	public static void updateTfIdf(Word word) {

		if (word != null && StringUtils.isNotBlank(word.getUuid()) && StringUtils.isNotBlank(word.getStemmedWordLowerCaseNoDiacritics())) {

			logger.debug("Information about tfidf='" + word.getTfIdf() + "' of word '" + word.toString() + "' will be inserted to database.");

			Connection connection = getConnection();
			PreparedStatement ps = null;

			try {
				ps = connection.prepareStatement("UPDATE words SET tfidf = ? WHERE uuid = ? AND stemmed_word_lower_case_no_diacritics = ?");
				ps.setString(1, String.format("%.16f", word.getTfIdf()));
				ps.setString(2, word.getUuid());
				ps.setString(3, word.getStemmedWordLowerCaseNoDiacritics());
				int numberOfUpdatedRecords = ps.executeUpdate();

				if (numberOfUpdatedRecords == 1) {
					logger.debug("Update of information about tfidf for word='" + word.toString() + "' was processed.");
				} else {
					logger.error("Problem with update of tfidf for word='" + word + "'. Number of updated records is '" + numberOfUpdatedRecords + "'!");
				}

				connection.commit();
			} catch (SQLException e) {
				logger.error("SQLException: " + e.getMessage() + "; SQL state='" + e.getSQLState() + "'", e);
			} finally {
				close(ps);
				// DatabaseConnector.close(connection);
				ps = null;
				connection = null;
			}
		} else {
			logger.error("Entered word is null or some of its parts for updating tfidf are null or empty!");
		}
	}
}
