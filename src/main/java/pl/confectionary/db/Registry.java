package pl.confectionary.db;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

import pl.confectionary.domain.Choclate;
import pl.confectionary.domain.ChoclateBox;

public class Registry { 

	public static Optional<Choclate> getChoclate(long id) {
		return getInstance().getInternalChoclate(id);
	}
	
	public static Optional<ChoclateBox> getChoclateBox(long id) {
		return getInstance().getInternalChoclateBox(id);
	}

	public static void addChoclate(Choclate choclate) {
		Choclate tmp = checkNotNull(choclate);
		getInstance().addInternalChoclate(tmp);
	}
	
	public static void addChoclateBox(ChoclateBox choclateBox) {
		ChoclateBox tmp = checkNotNull(choclateBox);
		getInstance().addInternalChoclate(tmp);
	}

	private static Registry getInstance() {
		return instance;
	}
	
	private void addInternalChoclate(Choclate tmp) {
		cholatesRegistry.put(tmp.getId(), tmp);
	}
	
	private void addInternalChoclate(ChoclateBox tmp) {
		cholateBoxesRegistry.put(tmp.getId(), tmp);
	}

	private Optional<Choclate> getInternalChoclate(long id) {
		return Optional.fromNullable(cholatesRegistry.get(id));
	}
	
	private Optional<ChoclateBox> getInternalChoclateBox(long id) {
		return Optional.fromNullable(cholateBoxesRegistry.get(id));
	}
	
	private static Registry instance =  new Registry();
	private Map<Long,Choclate> cholatesRegistry = new HashMap<>();
	private Map<Long,ChoclateBox> cholateBoxesRegistry = new HashMap<>();

}
