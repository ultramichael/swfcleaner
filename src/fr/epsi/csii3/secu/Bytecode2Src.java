package fr.epsi.csii3.secu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import javax.swing.SwingUtilities;

import flash.swf.TagDecoder;
import flash.swf.tools.SwfxParser;
import flash.swf.tools.SwfxPrinter;
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
