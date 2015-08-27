package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;

public class ToneGenerator 
extends JugglingLoopGenerator{
	
	public double m_tone = 80; //hz 

	public ToneGenerator(OOmject i_mother) {
		super(i_mother);  
		m_name = "Tone";
		m_gui = new ToneGeneratorGUI<ToneGenerator>(this, this);
	}

	
	@Override
	public void generateNotes(Sequencer addToMe) {
		int fireNum = nextFireNumAfter(-1);
		int patternLength = m_patterns[m_currentPattern].m_steps.length;
		
		GenerateAction userAction = new GenerateAction(addToMe);
		
		double dPosSec = 60.0/addToMe.m_tempo;
		double dPosRate = dPosSec/m_tone;
		double noteLength = dPosRate*patternLength+ dPosRate;// + (dPosRate/2.0); 
		for(double i_p = 0; i_p < m_genLength; i_p += dPosRate*2) {
				//
			MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
			addMe.setBeat(i_p + addToMe.m_selectionPos);
			addMe.setNote(m_patterns[m_currentPattern].m_steps[fireNum]);
			addMe.setLength(noteLength);
			addToMe.addEntity(addMe);  
			
			userAction.m_addedEnts.add(addMe);
			fireNum = nextFireNumAfter(fireNum);
		}
		
		Effigy.HISTORY.addAction(userAction);
	}		///////
}
