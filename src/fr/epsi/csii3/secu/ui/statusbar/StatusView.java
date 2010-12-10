package fr.epsi.csii3.secu.ui.statusbar;

import fr.epsi.csii3.secu.ui.generic.AbstractView;

@SuppressWarnings("serial")
public class StatusView extends AbstractView {
	public StatusView() {
		this.model = new StatusModel();
		this.model.addModelChangedListener(this);
	}

	@Override
	public void modelHasChanged() {
		
	}
}
