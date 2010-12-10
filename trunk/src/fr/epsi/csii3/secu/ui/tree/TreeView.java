package fr.epsi.csii3.secu.ui.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.epsi.csii3.secu.ui.generic.AbstractView;

@SuppressWarnings("serial")
public class TreeView extends AbstractView {
	private JTree tree;
	
	public TreeView() {
		this.model = new TreeModel();
		this.model.addModelChangedListener(this);
		this.tree = new JTree(new Object[0]);
		this.add(this.tree);
	}

	@Override
	public void modelHasChanged() {
		DefaultTreeModel m = (DefaultTreeModel) tree.getModel();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		for(Object o : ((TreeModel)model).getMethodsNames())
			root.add(new DefaultMutableTreeNode(o));
		m.setRoot(root);
		this.tree.setModel(m);
		tree.invalidate();
		tree.repaint();
	}
	
	public void addTreeSelectionListener(TreeSelectionListener l) {
		tree.addTreeSelectionListener(l);
	}
}
