package fr.epsi.csii3.secu.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.epsi.csii3.secu.ui.listeners.CurrentMethodChangedListener;
import fr.epsi.csii3.secu.ui.listeners.DataLoadedListener;
import fr.epsi.csii3.secu.ui.menubar.MenuBarComponent;
import fr.epsi.csii3.secu.ui.text.TextController;
import fr.epsi.csii3.secu.ui.text.TextControllerLeft;
import fr.epsi.csii3.secu.ui.tree.TreeController;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private static TreeController treeMenu;
	private static TextController left;
	private static TextController right;
	
	// TODO : generic listener
	private static List<DataLoadedListener> dataListeners = new ArrayList<DataLoadedListener>();
	private static List<CurrentMethodChangedListener> currentMethodListeners = new ArrayList<CurrentMethodChangedListener>();
	
	public MainFrame() {
		super("Bytecode2Src");
		super.setSize(new Dimension(640, 480));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.init();
	}
	
	private void init() {
		this.setJMenuBar(new MenuBarComponent());
		
		treeMenu = new TreeController();
		dataListeners.add(treeMenu);
		
		left = new TextControllerLeft();
		currentMethodListeners.add((CurrentMethodChangedListener) left);
		
		right = new TextController();

		
		JPanel texts = new JPanel();
		texts.add(left.getView());
		texts.add(right.getView());
		texts.setLayout(new GridLayout(1, 2));
		
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, texts, 0, SpringLayout.NORTH, this.getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, texts, 0, SpringLayout.SOUTH, this.getContentPane());
		layout.putConstraint(SpringLayout.NORTH, treeMenu.getView(), 0, SpringLayout.NORTH, this.getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, treeMenu.getView(), 0, SpringLayout.SOUTH, this.getContentPane());
		
		layout.putConstraint(SpringLayout.WEST, treeMenu.getView(), 0, SpringLayout.WEST, this.getContentPane());
		layout.putConstraint(SpringLayout.WEST, texts, 0, SpringLayout.EAST, treeMenu.getView());
		layout.putConstraint(SpringLayout.EAST, texts, 0, SpringLayout.EAST, this.getContentPane());
		this.setLayout(layout);
	
		add(treeMenu.getView());
		add(texts);
	}
	
	public static void loadData(Map<String, String> methods) {
		for(DataLoadedListener l : dataListeners)
			l.dataLoaded(methods);
	}
	
	public static void setABCContent(String abc) {
		for(CurrentMethodChangedListener l : currentMethodListeners)
			l.newCurrentMethod(abc);
	}
}
