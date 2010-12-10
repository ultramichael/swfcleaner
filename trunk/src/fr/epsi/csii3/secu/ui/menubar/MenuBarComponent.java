package fr.epsi.csii3.secu.ui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.epsi.csii3.secu.business.dump.DeobfuscationTagHandler;
import fr.epsi.csii3.secu.ui.MainFrame;

@SuppressWarnings("serial")
public class MenuBarComponent extends JMenuBar {
	public MenuBarComponent() {
		JMenu fileMenu = new JMenu("Fichier");
		JMenuItem openItem = new JMenuItem("Ouvrir");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new ExtensionFileFilter("SWF", new String[] {"SWF"}));
				int status = fileChooser.showOpenDialog(null);
				if(status == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					loadFile(selectedFile);
				}
				// Testing
				//loadFile2();
			}
		});
		fileMenu.add(openItem);
		this.add(fileMenu);
	}
	
	private void loadFile(File f) {
		try {
			URL url = f.toURI().toURL();
			MainFrame.loadData(new DeobfuscationTagHandler().parseSwf(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void loadFile2() {
		try {
			URL url = Bytecode2Src.class.getClass().getResource("/fr/epsi/csii3/secu/resources/Tetris_Obfuscated.swf");
			MainFrame.loadData(new DeobfuscationTagHandler().parseSwf(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
