package fr.epsi.csii3.secu.ui.generic;

public abstract class AbstractController {
	protected AbstractView view;
	
	public AbstractController(AbstractView view) {
		this.view = view;
	}
	
	public AbstractView getView() {
		return this.view;
	}
}
