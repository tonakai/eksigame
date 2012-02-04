package aykutsoysal.server;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import aykutsoysal.shared.SozlukEntry;

public class EksisozlukEntryFetcher implements EntryFetcher {
	private static final int RETRY_COUNT = 5;

	private static final Logger log = Logger.getLogger(EksisozlukEntryFetcher.class.getName());

	private static final String SELECTOR_BODY_STRING = "html body ol#el li";

	private static final String BASE_URI_SHOW = "http://www.eksisozluk.com/show.asp?id=%d";
	private static final String BASE_URI = "http://www.eksisozluk.com/";

	public EksisozlukEntryFetcher() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see aykutsoysal.server.EntryFetcher#fetchNonEmptySozlukEntry()
	 */
	@Override
	public SozlukEntry fetchNonEmptySozlukEntry() {
		// TODO needs clean up, network connection can be moved out of function
		SozlukEntry sozlukEntry = new SozlukEntry();

		Elements ampul = null;
		Document document = null;

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {

				@Override
				public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
					return new InputSource(new StringReader(""));
				}
			});

			do {

				// sukela
				String urlString = "http://www.eksisozluk.com/pick.asp?p=g";

				URL url = new URL(urlString);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "text/html");
				connection.setRequestProperty("Accept-Charset", "utf-8");
				connection
						.setRequestProperty("User-Agent",
								"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.58 Safari/535.2");
				connection.setRequestProperty("Content-Encoding", "utf-8");
				connection.setRequestProperty("Content-Type", "text/html; charset=utf-8");
				log.info("Content Type is " + connection.getContentType());

				document = Jsoup.parse(connection.getInputStream(), "utf-8", "http://www.eksisozluk.com");

				ampul = document.select("li.ampul");
				if (ampul.isEmpty()) {
					log.info("Entry bos: ");
				}

			} while (!ampul.isEmpty());

			Elements links = document.select("a");
			for (Element link : links) {
				//not a good solution
				if (!link.attr("href").startsWith("http")) {
					link.attr("href", BASE_URI + link.attr("href"));
				}
				link.attr("target", "_blank");
			}
			String header = document.select("h1.title").first().text();
			log.info(header);
			sozlukEntry.setHeader(header);

			sozlukEntry.setEntry(extractEntry(document));

			sozlukEntry.setYazar(extractYazar(document));
			sozlukEntry.setId(extractId(document));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sozlukEntry;
	}

	public String getBaseUriShow() {
		return BASE_URI_SHOW;
	}

	private String extractEntry(Document document) {
		StringBuilder builder = new StringBuilder();
		// entry = document.select(SELECTOR_BODY_STRING).first().html();
		for (Node node : document.select(SELECTOR_BODY_STRING).first().childNodes()) {
			if (node.attr("class").equalsIgnoreCase("aul")) {
				break;
			}
			builder.append(node.toString());
		}
		return builder.toString();
	}

	private String extractYazar(Document document) {
		String yazar = null;
		yazar = document.select(SELECTOR_BODY_STRING + " div.aul a").first().text();
		return yazar;
	}

	private long extractId(Document document) {
		return Long.parseLong(document.select(".entrymenu").attr("id").substring(1));
	}
}
