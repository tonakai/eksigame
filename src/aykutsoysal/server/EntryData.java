package aykutsoysal.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import aykutsoysal.shared.SozlukEntry;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class EntryData {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	@Persistent
	private String header;
	@Persistent
	private String yazar;
	@Persistent
	private long id;

	public EntryData() {

	}

	public EntryData(SozlukEntry sozlukEntry) {
		this.id = sozlukEntry.getId();
		this.yazar = sozlukEntry.getYazar();
		this.header = sozlukEntry.getHeader();
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getYazar() {
		return yazar;
	}

	public void setYazar(String yazar) {
		this.yazar = yazar;
	}

	public Key getKey() {
		return key;
	}

	public void save() {

		PersistenceManager pm = Datastore.getPersistenceManager();
		pm.makePersistent(this);

	}

	public static EntryData getById(long id) {
		PersistenceManager pm = Datastore.getPersistenceManager();
		Query query = pm.newQuery(EntryData.class);
		query.setFilter("id == idParam");
		query.declareParameters("long idParam");
		List<EntryData> result = (List<EntryData>) query.execute(id);
		if (result.size() == 0)
			return null;
		return result.get(0);

	}

	public static EntryData getByKey(String key) {
		PersistenceManager pm = Datastore.getPersistenceManager();
		Query query = pm.newQuery(EntryData.class);
		// Key k = KeyFactory.createKey(EntryData.class.getSimpleName(), key);
		EntryData e = pm.getObjectById(EntryData.class, Long.parseLong(key));
		return e;

	}

	public SozlukEntry convertToSozlukEntry() {
		SozlukEntry sozlukEntry = new SozlukEntry();
		sozlukEntry.setHeader(getHeader());
		sozlukEntry.setId(getId());
		// sozlukEntry.setUrl(String.format(EntryFetcher.getBaseUriShow(),getId()));
		sozlukEntry.setYazar(getYazar());
		return sozlukEntry;
	}

}
