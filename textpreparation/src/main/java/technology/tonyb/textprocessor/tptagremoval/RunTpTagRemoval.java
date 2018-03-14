package technology.tonyb.textprocessor.tptagremoval;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunTpTagRemoval {
	
	private static final Logger logger = LoggerFactory.getLogger(RunTpTagRemoval.class);
	
	/** 
	 * This method add redundant space and fixes situation when many tags is joined together without any space and tag removal 
	 * remove tags and text will be also joined together which is not desired state. 
	 */
	private static String addSpaceToLink(String content) {
		if (StringUtils.isNotBlank(content)) {
			return content.replaceAll("</", " </");
		} else {
			logger.error("Entered content of adding spaces after link is null or empty!");
			return null;
		}
	}
	
	private static String removeTags(String content) {
		
		if (StringUtils.isNotBlank(content)) {
			Document document = Jsoup.parse(content, "UTF-8");
			
			// Remove all script and style elements and those of class "hidden".
			document.select("script, style, .hidden, noscript").remove();
			
			// Remove all style and event-handler attributes from all elements.
			Elements all = document.select("*");
			
			for (Element el : all) { 
			  for (Attribute attribute : el.attributes()) { 
			    String attributeKey = attribute.getKey();
			    if (attributeKey.equals("style") || attributeKey.startsWith("on")) { 
			      el.removeAttr(attributeKey);
			    } 
			  }
			}
			
			return document.text();
		} else {
			logger.error("Entered content is null or empty!");
			return null;
		}
	}
	
	private static void process(String uuid) {
		Database.insertRemovedTags(uuid, removeTags(addSpaceToLink(Database.selectContent(uuid))));
	}
	
	public static void run(String uuid) {
		process(uuid);
	}
	
	public static void main(String[] args) {
		
		if (args != null && args.length == 2 && args[0].equals("-uuid") && StringUtils.isNotBlank(args[1])) {
			logger.info("Valid number of parameters. Entered record with uuid '" + args[1] + "' will be processed.");
			run(args[1]);
		} else {
			logger.error("Invalid number of parameters! Use: -uuid uuid_for_processing");
		}
	}
}
