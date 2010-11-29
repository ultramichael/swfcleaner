package fr.epsi.csii3.secu.ui.generic;

public abstract class AbstractController {
	protected AbstractView view;
	protected AbstractModel model;
	
	public AbstractController(AbstractView view, AbstractModel model) {
		this.view = view;
		this.model = model;
	}
	
	public AbstractView getView() {
		return this.view;
	}
}
