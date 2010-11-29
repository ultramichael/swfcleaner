package fr.epsi.csii3.secu.ui.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import flash.swf.Header;
import flash.swf.TagDecoder;
import flash.swf.TagHandler;
import flash.swf.tags.DoABC;
import flash.swf.tools.SwfxPrinter;
import fr.epsi.csii3.secu.Bytecode2Src;
import fr.epsi.csii3.secu.business.dump.AbcLoader;
import fr.epsi.csii3.secu.business.dump.DeobfuscationTagHandler;
import fr.epsi.csii3.secu.business.dump.AbcLoader.MethodInfo;

public class MenuBarComponent extends JMenuBar {
	public MenuBarComponent() {
		JMenu fileMenu = new JMenu("Fichier");
		JMenuItem openItem = new JMenuItem("Ouvrir");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new ExtensionFileFilter("SWF", new String[] {"SWF"}));
				int status = fileChooser.showOpenDialog(null);
				if(status == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					loadFile(selectedFile);
				}*/
				// Testing
				loadFile2(null);
			}
		});
		fileMenu.add(openItem);
		this.add(fileMenu);
	}
	
	private void loadFile(File f) {
		try {
			OutputStream out = new FileOutputStream(new File("C:\\dump.txt"));
			//OutputStream out = System.out;
			URL url = Bytecode2Src.class.getClass().getResource("/fr/epsi/csii3/secu/resources/Tetris.swf");
			
			String tmpFilename = "tmp.disass";
			SwfxPrinter.main(new String[] {"-abc", "-out", tmpFilename, url.getPath()});
			File tmpFile = new File(tmpFilename);
			tmpFile.deleteOnExit();
			InputStream tmpStream = new FileInputStream(tmpFile);
			BufferedReader tmpReader = new BufferedReader(new InputStreamReader(tmpStream));
			String disass = new String("");
			String line = null;
			while((line = tmpReader.readLine()) != null) {
				disass += line + "\r\n";
			}
			tmpReader.close();
			tmpStream.close();
			
			BufferedWriter outWriter = new BufferedWriter(new OutputStreamWriter(out));
			outWriter.write(disass);
			outWriter.flush();
			outWriter.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadFile2(File f) {
		try {
			URL url = Bytecode2Src.class.getClass().getResource("/fr/epsi/csii3/secu/resources/Tetris.swf");
			new DeobfuscationTagHandler().parseSwf(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
