package seph.reed.effigy;

import java.util.ArrayList;

import scott.thumbz.jaromin.DataStructures.ThumbzArrayList;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.EditModeManager;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class SequenceList 
extends OOmject{

	public SequencerListGUI m_gui;
	public ArrayList<Sequencer> m_sequencers;
	public int m_selectedClip = 0;
	public int clipIdCounter = 1;

	
	public SequenceList(OOmject i_mother) {
		super(i_mother);
		m_gui = new SequencerListGUI(this, this);
		m_sequencers = new ThumbzArrayList<Sequencer>();  
		createSequencer().reset();  
		createSequencer();   }

	
	public Sequencer createSequencer() {
		return createSequencer("Clip #"+clipIdCounter);  }
	
	
	public Sequencer createSequencer(String name) {
		Sequencer out = new Sequencer(this, name);
		clipIdCounter++;
		m_sequencers.add(out);
		m_gui.updateClipList();
		return out;  }

	public Sequencer getSelectedSequencer() {
		return m_sequencers.get(m_selectedClip);
	}


	public void setSelectedClip(int selectedIndex) {
		setSelectedClip(selectedIndex, true);  }
	
	public void setSelectedClip(int selectedIndex, boolean stopCurrent) {
		if(stopCurrent == true) {  
			getSelectedSequencer().stop();  }
		m_selectedClip = selectedIndex;
		if(ANCESTOR(Effigy.class).m_editorManager.m_mode != EditModeManager.SCENE_MODE){
			getSelectedSequencer().reset(); }
		ANCESTOR(Effigy.class).m_editorManager.m_gui.updateEditPanel();	
	}


	public void removeClip(int i_clipNum) {
		if(m_sequencers.size() != 1)  {
			m_sequencers.get(i_clipNum).stop();
			m_sequencers.remove(i_clipNum);  }
		m_selectedClip = Math.min(m_selectedClip, m_sequencers.size()-1);
		m_gui.updateClipList();
		setSelectedClip(m_selectedClip);
	}

	public void clearSequencers() {
		for(int i = 0; i < m_sequencers.size(); i++) {
			m_sequencers.get(i).stop();  }
		m_sequencers.clear();
	}


	

}
