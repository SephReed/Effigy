package seph.reed.effigy;

import scott.thumbz.jaromin.OOPject.OOmject;

public class Hands 
extends OOmject{

	public HandsGUI m_gui = null;
	public Fire [] m_fires = new Fire[14];
	
	
	public Hands(OOmject i_mother)  {
		super(i_mother);  
		
		m_gui = new HandsGUI(this, this);
		
		m_fires[0] = new Fire(this, 111, 236);
		m_fires[1] = new Fire(this, 44, 190);
		m_fires[2] = new Fire(this, 74, 185);
		m_fires[3] = new Fire(this, 81, 186);
		m_fires[4] = new Fire(this, 117, 195);
		m_fires[5] = new Fire(this, 166, 213);
			//
		m_fires[6] = new Fire(this, 621, 193);
		m_fires[7] = new Fire(this, 670, 178);
		m_fires[8] = new Fire(this, 710, 177);
		m_fires[9] = new Fire(this, 730, 178);
		m_fires[10] = new Fire(this, 750, 180);
		m_fires[11] = new Fire(this, 660, 227);
		
		//big boosh
		m_fires[12] = new Fire(this, 80, 80);
		m_fires[12].largeMode = true;
		m_fires[13] = new Fire(this, 640, 80);
		m_fires[13].largeMode = true;
	}
	
	
	
	public void modFire(int fireNum, boolean pressed) {
//		Toolkit.getDefaultToolkit().beep();
		if(fireNum<14) {
			if(pressed) {  m_fires[fireNum].setFire(127);  }
			else {  m_fires[fireNum].setFire(0);  }
		}
		else if(pressed == true) {
			if(fireNum == 14) {
				m_fires[0].addRed();  }
			else if(fireNum == 15) {
				m_fires[0].addSparkle();  }
			else if(fireNum == 16) {
				m_fires[11].addRed();  }
			else if(fireNum == 17) {
				m_fires[11].addSparkle();  }
		}
		
	}


	

	public void allOff() {
		for(int i = 0; i < m_fires.length; i++) {
			modFire(i, false);
		}
	}

	
//	
//	public static synchronized void playSound() {
//		 new Thread(new Runnable() {
//		  // The wrapper thread is unnecessary, unless it blocks on the
//		  // Clip finishing; see comments.
//		    public void run() {
//		      try {
//		        
//		        clip.start(); 
//		      } catch (Exception e) {
//		        e.printStackTrace();
//		      }
//		    }
//		  }).start();
//		}
	
	

}
