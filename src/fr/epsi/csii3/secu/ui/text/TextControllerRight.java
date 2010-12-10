package fr.epsi.csii3.secu.ui.text;

import fr.epsi.csii3.secu.business.flux.Node;
import fr.epsi.csii3.secu.ui.listeners.CurrentMethodChangedListener;

public class TextControllerRight extends TextController implements CurrentMethodChangedListener {
	@Override
	public void newCurrentMethod(String abc) {
		Node root = Node.createFromAbc(abc.split("\n"));
		root = root.toSyntheticTree();
		((TextModel)view.getModel()).setText(root.toString());
	}
}
