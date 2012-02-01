package aykutsoysal.client;

import aykutsoysal.shared.SozlukEntry;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface CoreServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void fetchSozlukEntry(AsyncCallback<SozlukEntry> callback) throws IllegalArgumentException;
	void answerSozlukEntry(String key, String answer, AsyncCallback<SozlukEntry> callback) throws IllegalArgumentException;

}
