package fr.epsi.csii3.secu.ui.generic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModel {
	private List<ModelChangedListener> listeners = new ArrayList<ModelChangedListener>();
	
	public void addModelChangedListener(ModelChangedListener l) {
		listeners.add(l);
	}
	
	public void notifyChanges() {
		for(ModelChangedListener l : listeners)
			l.modelHasChanged();
	}
}
