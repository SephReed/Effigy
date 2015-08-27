package seph.reed.effigy.undo;

import java.util.ArrayList;

import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class GenerateAction
extends UserAction {
	
	public Sequencer m_sequencer;
	public ArrayList<MidiEventEntity> m_addedEnts;
	
	public GenerateAction(Sequencer i_sequencer) {
		m_sequencer = i_sequencer;  
		m_addedEnts = new ArrayList<MidiEventEntity>();  }
	
	
	@Override
	public boolean redo() {
//		if(current == true) {
			for(int i = 0; i < m_addedEnts.size(); i++) {
				m_sequencer.addEntity(m_addedEnts.get(i));  }
			return true;
//		}
//		else return false;
	}

	
	
	@Override
	public boolean undo() {
//		if(current == true) {
			for(int i = 0; i < m_addedEnts.size(); i++) {
				m_sequencer.removeEntity(m_addedEnts.get(i));  }
			return true;
//		}
//		else return false;
	}
	
}