package aykutsoysal.client;

import aykutsoysal.shared.SozlukEntry;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("core")
public interface CoreService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	SozlukEntry fetchSozlukEntry() throws IllegalArgumentException;
	SozlukEntry answerSozlukEntry(String key, String answer) throws IllegalArgumentException;
}
