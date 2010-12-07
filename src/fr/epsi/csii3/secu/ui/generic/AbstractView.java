package fr.epsi.csii3.secu.ui.generic;

import javax.swing.JPanel;

public abstract class AbstractView extends JPanel implements ModelChangedListener {
	protected AbstractModel model;
	
	public AbstractModel getModel() {
		return model;
	}
}
