package aykutsoysal.server;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class AnsweredEntry {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String answer;

	@Persistent
	private Key entryKey;

	@Persistent
	private boolean status;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void save() {

		PersistenceManager pm = Datastore.getPersistenceManager();
		pm.makePersistent(this);
	}

	public static AnsweredEntry getByKey(String key) {
		PersistenceManager pm = Datastore.getPersistenceManager();
		// Key k = KeyFactory.createKey(EntryData.class.getSimpleName(),
		// key);
		AnsweredEntry e = pm.getObjectById(AnsweredEntry.class, key);
		return e;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Key getEntryKey() {
		return entryKey;
	}

	public void setEntryKey(Key entryKey) {
		this.entryKey = entryKey;
	}
}
