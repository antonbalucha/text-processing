package technology.tonyb.textprocessor.tpconfiguration.objects;

import technology.tonyb.textprocessor.tpconfiguration.math.TfIdfProcessor;

public class TfIdf {

	private int numberOfAllDocuments;
	
	private int numberOfDocumentsWithWord;

	private int numberOfWordsInExaminedDocument;
	
	private int numberOfWordOccurencesInDocument;
	
	private double tfidf;
	
	public int getNumberOfAllDocuments() {
		return this.numberOfAllDocuments;
	}

	public TfIdf setNumberOfAllDocuments(int numberOfAllDocuments) {
		this.numberOfAllDocuments = numberOfAllDocuments;
		return this;
	}

	public int getNumberOfDocumentsWithWord() {
		return this.numberOfDocumentsWithWord;
	}

	public TfIdf setNumberOfDocumentsWithWord(int numberOfDocumentsWithWord) {
		this.numberOfDocumentsWithWord = numberOfDocumentsWithWord;
		return this;
	}

	public int getNumberOfWordsInExaminedDocument() {
		return this.numberOfWordsInExaminedDocument;
	}

	public TfIdf setNumberOfWordsInExaminedDocument(int numberOfWordsInExaminedDocument) {
		this.numberOfWordsInExaminedDocument = numberOfWordsInExaminedDocument;
		return this;
	}

	public int getNumberOfWordOccurencesInDocument() {
		return this.numberOfWordOccurencesInDocument;
	}

	public TfIdf setNumberOfWordOccurencesInDocument(int numberOfWordOccurencesInDocument) {
		this.numberOfWordOccurencesInDocument = numberOfWordOccurencesInDocument;
		return this;
	}
	
	public TfIdf countTfIdf() {
		this.tfidf = TfIdfProcessor.countTFIDF(
				this.numberOfWordOccurencesInDocument,
				this.numberOfWordsInExaminedDocument, 
				this.numberOfAllDocuments,
				this.numberOfDocumentsWithWord);
		return this;
	}
	
	public double getTfIdf() {
		return this.tfidf;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("numberOfAllDocuments=");
		sb.append(this.numberOfAllDocuments);
		sb.append(", numberOfDocumentsWithWord=");
		sb.append(this.numberOfDocumentsWithWord);
		sb.append(", numberOfWordsInExaminedDocument=");
		sb.append(this.numberOfWordsInExaminedDocument);
		sb.append(", numberOfWordOccurencesInDocument=");
		sb.append(this.numberOfWordOccurencesInDocument);
		sb.append(", tfIdf=");
		sb.append(this.tfidf);
		return sb.toString();
	}
}
