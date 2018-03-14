package technology.tonyb.textprocessor.tpconfiguration.objects;

public class Word {

	private long id;

	private String uuid;

	private String word;

	private String wordLowerCase;

	private String wordLowerCaseNoDiacritics;

	private String stemmedWordLowerCase;

	private String stemmedWordLowerCaseNoDiacritics;

	private int numberOfWordOccurencesInText = 0;

	private double termFrequency = 0.0d;

	private double tfIdf = 0.0d;

	public Word() {}

	public Word(String word) {
		this.word = word;
	}

	public long getId() {
		return this.id;
	}

	public Word setId(long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return this.uuid;
	}

	public Word setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getWord() {
		return this.word;
	}

	public Word setWord(String word) {
		this.word = word;
		return this;
	}

	public String getWordLowerCase() {
		return this.wordLowerCase;
	}

	public Word setWordLowerCase(String wordLowerCase) {
		this.wordLowerCase = wordLowerCase;
		return this;
	}

	public String getWordLowerCaseNoDiacritics() {
		return this.wordLowerCaseNoDiacritics;
	}

	public Word setWordLowerCaseNoDiacritics(String wordLowerCasenoDiacritics) {
		this.wordLowerCaseNoDiacritics = wordLowerCasenoDiacritics;
		return this;
	}

	public String getStemmedWordLowerCase() {
		return this.stemmedWordLowerCase;
	}

	public Word setStemmedWordLowerCase(String stemmedWordLowerCase) {
		this.stemmedWordLowerCase = stemmedWordLowerCase;
		return this;
	}

	public String getStemmedWordLowerCaseNoDiacritics() {
		return this.stemmedWordLowerCaseNoDiacritics;
	}

	public Word setStemmedWordLowerCaseNoDiacritics(String stemmedWordLowerCaseNoDiacritics) {
		this.stemmedWordLowerCaseNoDiacritics = stemmedWordLowerCaseNoDiacritics;
		return this;
	}

	public int getNumberOfWordOccurencesInText() {
		return this.numberOfWordOccurencesInText;
	}

	public Word setNumberOfWordOccurencesInText(int numberOfWordOccurencesInText) {
		this.numberOfWordOccurencesInText = numberOfWordOccurencesInText;
		return this;
	}

	public Word incrementNumberOfWordOccurenceInText() {
		this.numberOfWordOccurencesInText = this.numberOfWordOccurencesInText + 1;
		return this;
	}

	public double getTermFrequency() {
		return this.termFrequency;
	}

	public Word setTermFrequency(double termFrequency) {
		this.termFrequency = termFrequency;
		return this;
	}

	public double getTfIdf() {
		return this.tfIdf;
	}

	public Word setTfIdf(double tfIdf) {
		this.tfIdf = tfIdf;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=");
		sb.append(this.id);
		sb.append(", uuid=");
		sb.append(this.uuid);
		sb.append(", w=");
		sb.append(this.word);
		sb.append(", wlc=");
		sb.append(this.wordLowerCase);
		sb.append(", wlcnd=");
		sb.append(this.wordLowerCaseNoDiacritics);
		sb.append(", swlc=");
		sb.append(this.stemmedWordLowerCase);
		sb.append(", swlcnd=");
		sb.append(this.stemmedWordLowerCaseNoDiacritics);
		sb.append(", numberOfWordOccurencesInText=");
		sb.append(this.numberOfWordOccurencesInText);
		sb.append(", termFrequency=");
		sb.append(String.format("%.16f", this.termFrequency));
		sb.append(", tfIdf=");
		sb.append(String.format("%.16f", this.tfIdf));
		return sb.toString();
	}
}
