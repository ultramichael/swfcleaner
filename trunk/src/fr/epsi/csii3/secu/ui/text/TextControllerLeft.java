package fr.epsi.csii3.secu.ui.text;

import fr.epsi.csii3.secu.business.flux.Node;
import fr.epsi.csii3.secu.ui.listeners.CurrentMethodChangedListener;


public class TextControllerLeft extends TextController implements CurrentMethodChangedListener {
	@Override
	public void newCurrentMethod(String abc) {
		((TextModel)view.getModel()).setText(abc);
		Node root = Node.createFromAbc(abc.split("\n"));
		root = root.toSyntheticTree();
		System.out.println(root);
	}
}
