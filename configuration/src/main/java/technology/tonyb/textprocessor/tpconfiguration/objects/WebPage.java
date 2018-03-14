package technology.tonyb.textprocessor.tpconfiguration.objects;

import java.util.List;

public class WebPage {

	private String uuid;
	
	private String url;
	
	private String content;

	private List<Word> words;
	
	private String language;
	
	public List<String> links;
	
	public String getUuid() {
		return this.uuid;
	}
	
	public WebPage setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUrl() {
		return this.url;
	}

	public WebPage setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getContent() {
		return this.content;
	}

	public WebPage setContent(String content) {
		if (content == null) {
			content = "<html><head></head><body></body</html>";
		}
		
		this.content = content;
		return this;
	}

	public List<Word> getWords() {
		return this.words;
	}

	public WebPage setWords(List<Word> words) {
		this.words = words;
		return this;
	}

	public String getLanguage() {
		return this.language;
	}

	public WebPage setLanguage(String language) {
		this.language = language;
		return this;
	}
	
	public List<String> getLinks() {
		return this.links;
	}

	public WebPage setLinks(List<String> links) {
		this.links = links;
		return this;
	}
}
