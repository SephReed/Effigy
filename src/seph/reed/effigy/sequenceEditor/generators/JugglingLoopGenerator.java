package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;

public class JugglingLoopGenerator 
extends JugglingGenerator {

	public double m_rate = 1.0/16.0;
//	public double m_noteLength = 4.0/8.0;

	
	public JugglingLoopGenerator(OOmject i_mother) {
		super(i_mother);
		m_name = "Juggling Looper";
		m_gui = new JugglingLoopGeneratorGUI<JugglingLoopGenerator>(this, this);   }
	
	

	@Override
	public void generateNotes(Sequencer addToMe) {
		int fireNum = nextFireNumAfter(-1);
		GenerateAction userAction = new GenerateAction(addToMe);
		
		for(double i_p = 0; i_p < m_genLength; i_p += m_rate) {
			int note = m_patterns[m_currentPattern].m_steps[fireNum];
			double beat = i_p + addToMe.m_selectionPos;

			MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
			addMe.setBeat(beat);
			addMe.setNote(note);
			addMe.setLength(m_noteLength * m_rate);
			addToMe.addEntity(addMe);  
			
			userAction.m_addedEnts.add(addMe);
			fireNum = nextFireNumAfter(fireNum);
		}		///////
		Effigy.HISTORY.addAction(userAction);
	}
	

	

}
