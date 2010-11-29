package fr.epsi.csii3.secu.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import fr.epsi.csii3.secu.ui.menubar.MenuBarComponent;
import fr.epsi.csii3.secu.ui.text.TextController;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private TextController left;
	private TextController right;
	
	public MainFrame() {
		super("Bytecode2Src");
		super.setSize(new Dimension(640, 480));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.init();
	}
	
	private void init() {
		this.setJMenuBar(new MenuBarComponent());
		
		this.left = new TextController();
		this.right = new TextController();
		JPanel texts = new JPanel();
		texts.add(this.left.getView());
		texts.add(this.right.getView());
		texts.setLayout(new GridLayout(1, 2));
		
		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, texts, 0, SpringLayout.NORTH, this.getContentPane());
		//layout.putConstraint(SpringLayout.NORTH, this.right.getView(), 0, SpringLayout.NORTH, this.getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, texts, 0, SpringLayout.SOUTH, this.getContentPane());
		//layout.putConstraint(SpringLayout.SOUTH, this.right.getView(), 0, SpringLayout.SOUTH, this.getContentPane());
		layout.putConstraint(SpringLayout.WEST, texts, 0, SpringLayout.WEST, this.getContentPane());
		layout.putConstraint(SpringLayout.EAST, texts, 0, SpringLayout.EAST, this.getContentPane());
		
		//layout.putConstraint(SpringLayout.EAST, this.left.getView(), 0, SpringLayout.WEST, this.right.getView());
		this.setLayout(layout);
		
		//setLayout(new GridLayout(2,2));
		
		//this.add(this.left.getView());
		//this.add(this.right.getView());
		add(texts);
	}
}
