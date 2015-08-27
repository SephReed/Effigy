package seph.reed.drawers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import scott.thumbz.jaromin.Components.Animation;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.Fire;
import seph.reed.effigy.Hands;
import Helpers.Painter;

public class FireHandsAnimation 
extends Animation  {

	public Hands m_hands;
	public BufferedImage m_background;
	
	public FireHandsAnimation(OOmject i_mother, Hands i_hands) {
		super(i_mother);  
		m_hands = i_hands;  

		try 
		{  m_background = ImageIO.read(Effigy.easyload("RealBackGround.png")); } 
		catch (IOException e) {
			System.out.println("bad path background.png");  }
	}
	

	@Override
	public void update() {
		
	}

	@Override
	public void paint(Painter joe) {
		joe.drawImage(m_background, 0, 0, m_canvas);
		
		
		Fire[] fires = m_hands.m_fires;
		for(int i = 0; i < fires.length; i++) {
			Fire fire = fires[i];
			if(fire.fireAmount > 0) {
				joe.drawImage(fire.getSprite(), fire.m_x, fire.m_y, m_canvas);
				if(fire.sparkleAmount > 0) {
					joe.drawImage(fire.getSparkle(), fire.m_x, fire.m_y, m_canvas);  }
			}
			
		}
		
	}

}
