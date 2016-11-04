package pl.confectionary.db;

import java.util.HashMap;
import java.util.Map;

import pl.confectionary.domain.Choclate;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.*;

public class Registry { 

	public static Optional<Choclate> getChoclate(long id) {
		return getInstance().getInternalChoclate(id);
	}

	public static void addChoclate(Choclate choclate) {
		Choclate tmp = checkNotNull(choclate);
		getInstance().addInternalChoclate(tmp);
	}
	
	private static Registry getInstance() {
		return instance;
	}
	
	private void addInternalChoclate(Choclate tmp) {
		cholatesRegistry.put(tmp.getId(), tmp);
	}

	private Optional<Choclate> getInternalChoclate(long id) {
		return Optional.fromNullable(cholatesRegistry.get(id));
	}
	
	private static Registry instance =  new Registry();
	private Map<Long,Choclate> cholatesRegistry = new HashMap<>();
}
