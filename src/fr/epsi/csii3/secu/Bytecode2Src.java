package fr.epsi.csii3.secu;

import javax.swing.SwingUtilities;

import fr.epsi.csii3.secu.ui.MainFrame;

public class Bytecode2Src {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
