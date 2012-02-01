package aykutsoysal.server;

import aykutsoysal.shared.SozlukEntry;

public class DummyEntryFetcher implements EntryFetcher {

	@Override
	public SozlukEntry fetchNonEmptySozlukEntry() {
		SozlukEntry sozlukEntry = new SozlukEntry();
		sozlukEntry.setEntry("Doktor bune");
		sozlukEntry.setId(1337);
		sozlukEntry.setHeader("Doktor bune");
		
		return sozlukEntry;
	}

	@Override
	public String getBaseUriShow() {
		return "%s";
	}

}
