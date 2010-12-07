package fr.epsi.csii3.secu.ui.tree;

import java.util.Map;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.epsi.csii3.secu.ui.MainFrame;
import fr.epsi.csii3.secu.ui.generic.AbstractController;
import fr.epsi.csii3.secu.ui.listeners.DataLoadedListener;

public class TreeController extends AbstractController implements TreeSelectionListener, DataLoadedListener {

	public TreeController() {
		super(new TreeView());
		((TreeView)getView()).addTreeSelectionListener(this);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		String methodName = (String)((DefaultMutableTreeNode) e.getPath().getLastPathComponent()).getUserObject();
		MainFrame.setABCContent(((TreeModel)view.getModel()).getMethodBody(methodName));
	}

	@Override
	public void dataLoaded(Map<String, String> data) {
		((TreeModel)view.getModel()).setMethods(data);
	}

}
