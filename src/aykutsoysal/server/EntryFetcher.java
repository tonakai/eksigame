package aykutsoysal.server;

import aykutsoysal.shared.SozlukEntry;

public interface EntryFetcher {

	public abstract SozlukEntry fetchNonEmptySozlukEntry();
	public abstract String getBaseUriShow();

}