package fr.epsi.csii3.secu.ui.text;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import fr.epsi.csii3.secu.ui.generic.AbstractController;

public class TextController extends AbstractController implements KeyListener {
	public TextController() {
		super(new TextView(), new TextModel());
		
		((TextView)view).getTextArea().addKeyListener(this);
	}
	
	@Override
	public void keyPressed(KeyEvent kEvt) {	
	}
	
	@Override
	public void keyReleased(KeyEvent kEvt) {
	}
	
	@Override
	public void keyTyped(KeyEvent kEvt) {
		((TextModel)model).setText(((TextModel)model).getText()+kEvt.getKeyChar());
	}
}
