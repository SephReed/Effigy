package seph.reed.effigy;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import scott.thumbz.jaromin.OOPject.OOmject;

public class KeyboardListener 
extends OOmject implements KeyListener{
	
//	int [] KEY_TO_FINGER = 
//		{ 	KeyEvent.VK_A, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_G, KeyEvent.VK_D,  //left hand
//			KeyEvent.VK_K, KeyEvent.VK_H, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_SEMICOLON };
	
	int [] KEY_TO_FINGER = //wonderplan
		{ 	KeyEvent.VK_T, KeyEvent.VK_D, KeyEvent.VK_G, KeyEvent.VK_M, KeyEvent.VK_L, KeyEvent.VK_R,   //left hand
			KeyEvent.VK_I, KeyEvent.VK_F, KeyEvent.VK_U, KeyEvent.VK_B, KeyEvent.VK_H, KeyEvent.VK_E };
	
	
	protected Hands m_hands;
	
	public KeyboardListener(OOmject i_mother, Hands i_hands) {
		super(i_mother);  
		m_hands = i_hands;
		}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		interpretKey(e, true);  }

	@Override
	public void keyReleased(KeyEvent e) {
		interpretKey(e, false);  }
	
	
	
	private void interpretKey(KeyEvent e, boolean pressed) {
//		System.out.println("fire ");
		
		int key = e.getKeyCode();
		int fireNum = -1;
		
		for(int i = 0; i < KEY_TO_FINGER.length; i++) {
			if(KEY_TO_FINGER[i] == key) {
				fireNum = i;
				i = KEY_TO_FINGER.length;
			}
		}
		
		if(fireNum != -1)  {
			m_hands.modFire(fireNum, pressed);  }
	}
	
	
}
