package seph.reed.effigy;

import java.awt.Color;
import java.io.InputStream;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.EditModeManager;
import seph.reed.effigy.undo.EffigyUndoRedo;

public class Effigy 
extends OOmject{
	
	public static boolean QUNEO_OUTPUT = false;
	public static final int[] QUNEO_MAPPING = {20, 0, 32, 50, 52, 38, 40, 58, 60, 46, 14, 26};
	
//	public static final Color COLOR_1 = new Color(55, 20, 60);
	public static final Color COLOR_1 = new Color(55, 90, 20);
	public static final Color COLOR_2 = new Color(30, 30, 30);
	public static final Color COLOR_3 = Color.BLACK;
	public static final Color COLOR_4 = Color.ORANGE;
	public static final Color COLOR_5 = new Color(220, 220, 220);
	public static final Color COLOR_6 = new Color(30, 0, 35);

	public static double TEMPO = 110;
	
	public Hands m_hands;
	public MidiRouter m_router;
	public static EffigyUndoRedo HISTORY;
	public static AutoPlay AUTOPLAY;
	
	public EffigyGUI m_gui;
	public EffigyLoadSave m_loader;
	public EditModeManager m_editorManager;
	public SequenceList m_sequencerList;

	public static void main(String[] args) {	
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		   System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Effigy Fire SHIT");
		new Effigy(null);  }
	
	
	public Effigy(OOmject i_mother) {
		super(i_mother);
		
		m_sequencerList = new SequenceList(this);
		m_hands = new Hands(this);
		m_router = new MidiRouter(this);
		HISTORY = new EffigyUndoRedo(this);
		AUTOPLAY = new AutoPlay(this, m_sequencerList);
		m_editorManager = new EditModeManager(this);
		
		m_gui = new EffigyGUI(this, this);
		m_gui.getComponent().pack();
		m_gui.getComponent().setVisible(true);
		
	}
	
	
	
	public static InputStream easyload(String name) {
		InputStream input = Effigy.class.getResourceAsStream(name);

		if(input == null) {
			 input = Effigy.class.getResourceAsStream("/"+name); }
		
		if(input == null) {
			System.out.println("no input stream for "+name);  
			System.out.println("in "+Hands.class.getResource("").getPath());  }
		return input;
	}


	public void setTempo(double i_tempo) {
		TEMPO = i_tempo;
		
		for(int i = 0; i < m_sequencerList.m_sequencers.size(); i++) {
			m_sequencerList.m_sequencers.get(i).setTempo(i_tempo);  }
		
		m_editorManager.m_scene.setTempo(i_tempo);
	}	

}
