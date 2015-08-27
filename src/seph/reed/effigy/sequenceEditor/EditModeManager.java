package seph.reed.effigy.sequenceEditor;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.generators.AdvancedJugglingGenerator;
import seph.reed.effigy.sequenceEditor.generators.Generator;
import seph.reed.effigy.sequenceEditor.generators.HandSymbolGenerator;
import seph.reed.effigy.sequenceEditor.generators.HandSymbolLoopGenerator;
import seph.reed.effigy.sequenceEditor.generators.JugglingGenerator;
import seph.reed.effigy.sequenceEditor.generators.JugglingLoopGenerator;
import seph.reed.effigy.sequenceEditor.generators.JugglingSpeedDropGenerator;
import seph.reed.effigy.sequenceEditor.generators.ToneGenerator;

public class EditModeManager 
extends OOmject{
	
	public static final int NOTE_MODE = 0;
	public static final int GEN_MODE = 2;
	public static final int CLIP_MODE = 4;
	public static final int SCENE_MODE = 8;
	public int m_mode;
	
//	public Clip m_currentClip;
	
	public EditModeManagerGUI m_gui;
	public Generator [] m_generators = { 
			new JugglingGenerator(this),
			new JugglingLoopGenerator(this),
			new JugglingSpeedDropGenerator(this),
			new HandSymbolGenerator(this),
			new HandSymbolLoopGenerator(this),
			new AdvancedJugglingGenerator(this),
			new ToneGenerator(this)  };
	public int m_currentGenerator = 0;
	
	public ClipSequencer m_scene;
	public double lastNoteLength = 1.0/4.0;
//	public double m_selectionPos = 0;
	

	public EditModeManager(OOmject i_mother) {
		super(i_mother);  
		m_mode = CLIP_MODE;
		m_scene = new ClipSequencer(this);
		m_scene.m_name = "scene";
//		m_currentClip = ANCESTOR(Effigy.class).m_clipList.getSelectedClip();
		m_gui = new EditModeManagerGUI(this, this);  }  
	
	public void setMode(int i_mode) {
		m_mode = i_mode;
		m_gui.updateEditPanel();;
	}

	public void setGenerator(int i_currentGenerator) {
		m_currentGenerator = i_currentGenerator;
		m_gui.updateGeneratorPanel();
	}

	public Generator currentGenerator() {
		if(m_generators != null) return m_generators[m_currentGenerator];
		else return null;
	}

	public void setSelectionPos(double x) {
		getCurrentSequencer().m_selectionPos = x;  }
	
	public double getSelectionPos() {
		return getCurrentSequencer().m_selectionPos;  }

	public Sequencer getCurrentSequencer() {
		return ANCESTOR(Effigy.class).m_sequencerList.getSelectedSequencer();  }


}
