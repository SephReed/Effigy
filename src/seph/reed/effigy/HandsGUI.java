package seph.reed.effigy;

import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.drawers.FireHandsAnimation;

public class HandsGUI 
extends GUIManager<Hands, JFancyPanel>{

//	protected GrowShrinkRainbowCircles m_circlesAnimation;
	protected FireHandsAnimation m_animation;
	protected KeyboardListener m_keyInput;
	
	public HandsGUI(OOmject i_holder, Hands i_soul) {
		super(i_holder, i_soul);  }
	

	@Override
	protected void createComponent() {
		m_keyInput = new KeyboardListener(this, m_soul);
		
		m_component = new JFancyPanel();
//		m_component.setBackground(new Color(0,0,0,0));
		m_component.setFocusable(true);
		m_component.setOpaque(false);
		
		m_animation = new FireHandsAnimation(this, m_soul);
		m_animation.setRate(24);
		m_component.addDrawing(m_animation);
		m_animation.start();
		
//		m_circlesAnimation = new GrowShrinkRainbowCircles(this);
//		m_component.addDrawing(m_circlesAnimation);
//		m_circlesAnimation.start();
		
		
		m_component.addKeyListener(m_keyInput);
	}

}
