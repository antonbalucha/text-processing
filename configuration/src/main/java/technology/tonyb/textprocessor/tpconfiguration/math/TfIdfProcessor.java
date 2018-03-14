package technology.tonyb.textprocessor.tpconfiguration.math;

public class TfIdfProcessor {

	public static final double countTF(int numberOfWordOccurencesInDocument, int numberOfAllWordsInExaminedDocument) {
		
		if (numberOfWordOccurencesInDocument == 0 || numberOfAllWordsInExaminedDocument == 0) {
			return 0.0d;
		} else {
			return (numberOfWordOccurencesInDocument + 0.0d) / (numberOfAllWordsInExaminedDocument + 0.0d);
		}
	}

	public static final double countIDF(int numberOfAllDocuments, int numberOfDocumentsWhereWordAppears) {

		if (numberOfDocumentsWhereWordAppears < 0) {
			numberOfDocumentsWhereWordAppears = 1;
		}

		return Math.log(numberOfAllDocuments / numberOfDocumentsWhereWordAppears);
	}

	public static final double countTFIDF(int numberOfWordOccurencesInDocument, int numberOfAllWordsInExaminedDocument, int numberOfAllDocuments, int numberOfDocumentsWhereWordAppears) {
		return countTFIDF(countTF(numberOfWordOccurencesInDocument, numberOfAllWordsInExaminedDocument), countIDF(numberOfAllDocuments, numberOfDocumentsWhereWordAppears));
	}
	
	public static final double countTFIDF(Double tf, Double idf) {
		return tf * idf;
	}
}
