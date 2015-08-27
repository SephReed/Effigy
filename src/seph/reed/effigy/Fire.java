package seph.reed.effigy;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import scott.thumbz.jaromin.OOPject.OOmject;

public class Fire 
extends OOmject{

	public int fireAmount = 0;
	public int m_x;
	public int m_y;
	public double m_step = 0;
	public Clip fireOn;
	public Clip fireLoop;
	public boolean largeMode = false;
	public int redAmount = 0;
	public int sparkleAmount = 0;
	
	public static final Image [] m_sprites = dissect("fireBurst.png", 42, 73);
	public static final Image [] m_spritesRed = dissect("fireBurstRed.png", 42, 73);
	public static final Image [] m_spritesSparkle = dissect("fireBurstSparkle.png", 42, 73);
	public static final Image [] m_spritesLarge = dissect("fireBurstLarge.png", 126, 219);
	
	public Fire(OOmject i_mother, int i_x, int i_y) {
		super(i_mother);
		m_x = i_x;
		m_y = i_y;
		
		
//		fireOn = getClip("Fire.wav");
//		fireLoop = getClip("FireLoop.wav");
//		FloatControl volume = (FloatControl) fireLoop.getControl(FloatControl.Type.MASTER_GAIN);
//		volume.setValue(-10f);
	}

//	private Clip getClip(String name) {
//		Clip out = null;
//		try {
//			out = AudioSystem.getClip();
//			AudioInputStream inputStream = AudioSystem.getAudioInputStream(Effigy.easyload(name));
//			out.open(inputStream);
//		} 
//		catch (LineUnavailableException e) {  e.printStackTrace();  }
//		catch (UnsupportedAudioFileException | IOException e) {  e.printStackTrace();  }
//		return out;
//	}

//	public void step() {
//		m_step += .015;
//		m_step /= m_sprites.length;
//	}

	public BufferedImage getSprite() {
		m_step += .45;
		m_step %= m_sprites.length;
		
		if(redAmount > 0) {
			redAmount--; 
			return (BufferedImage) m_spritesRed[(int) m_step];  }
		else if(largeMode) {
			return (BufferedImage) m_spritesLarge[(int) m_step];  }
		else  {
			return (BufferedImage) m_sprites[(int) m_step];  }
	}

	public BufferedImage getSparkle() {
		if(sparkleAmount > 0) {
			sparkleAmount--;
			return (BufferedImage) m_spritesSparkle[(int) m_step];  }
		else return null;
	}
	
	
	
	private static Image[] dissect(String path, int width, int height) {
		BufferedImage crop = null;
		try 
		{  crop = ImageIO.read(Effigy.easyload(path)); } 
		catch (IOException e) {
			System.out.println("bad path "+path);  }
		
		Image[] frames = new Image [14];
		
		for(int x = 0; x < 7; x++) {
			for(int y = 0; y < 2; y++) {
				frames[(y*7)+x] = crop.getSubimage((x*width), y*height, width, height);
			}
		}
		
		return frames;
		
	}

	public void setFire(int i_fireAmount) {
//		if(i_fireAmount != 0) {
//			fireLoop.setFramePosition(0);
//			fireLoop.start();  }
//		else {
//			fireLoop.stop();  }
//		
//		if(i_fireAmount != fireAmount) {
//			fireOn.setFramePosition(0);
//			fireOn.start();   }
		fireAmount = i_fireAmount;
	}

	public void addRed() {
		redAmount += 64;   }

	public void addSparkle() {
		sparkleAmount += 64;  }
	
	
}
