package aykutsoysal.server;

import java.util.logging.Logger;

import aykutsoysal.client.CoreService;
import aykutsoysal.shared.FieldVerifier;
import aykutsoysal.shared.SozlukEntry;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CoreServiceImpl extends RemoteServiceServlet implements CoreService {

	private static final EntryFetcher fetcher = new EksisozlukEntryFetcher();

	private static final Logger log = Logger.getLogger(CoreServiceImpl.class.getName());
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public SozlukEntry fetchSozlukEntry() throws IllegalArgumentException {
		//EntryFetcher fetcher = new DummyEntryFetcher();
		
		 SozlukEntry sozlukEntry = fetcher.fetchNonEmptySozlukEntry();
		 EntryData data=EntryData.getById(sozlukEntry.getId());
		 if ( data== null) {
			 data = new EntryData(sozlukEntry);
			 data.save();
			 log.info("EntryData saved: " + data.getId() + ", " + data.getKey().toString());
		 }
		 
		 sozlukEntry.setKey(Long.toString(data.getKey().getId()));
		 //post-processing
		 sozlukEntry.setHeader(sozlukEntry.getHeader().replaceAll("[A-Za-z0-9ıİğĞüÜşŞöÖçÇ]", "_"));
		 return sozlukEntry;
	}

	@Override
	public SozlukEntry answerSozlukEntry(String key, String answer) throws IllegalArgumentException {
		
		answer = answer.trim();
		
		EntryData entryData = EntryData.getByKey(key);
		AnsweredEntry answeredEntry = new AnsweredEntry();
		answeredEntry.setAnswer(answer);
		answeredEntry.setEntryKey(entryData.getKey());
		log.info("Cevaplandi: " + entryData.getHeader() + ", " + answer);
		SozlukEntry sozlukEntry = entryData.convertToSozlukEntry();
		sozlukEntry.setUrl(String.format(fetcher.getBaseUriShow(), sozlukEntry.getId()));
		
		if (answer.isEmpty()) {
			sozlukEntry.setStatus("empty");
		}
		else if (StringUtility.equalsIgnoreCaseAndDiacritics(entryData.getHeader(), answer))
		{
			sozlukEntry.setStatus("correct");
			answeredEntry.setStatus(true);
		}
		else {
			sozlukEntry.setStatus("false");
			answeredEntry.setStatus(false);
		}
		
		answeredEntry.save();
		
		//AnsweredEntry fake = AnsweredEntry.getByKey(Long.toString( answeredEntry.getKey().getId()));
		//log.info("Cevaplanan entry: " + fake.getEntry().getHeader());
		return sozlukEntry;
	}

	
}
