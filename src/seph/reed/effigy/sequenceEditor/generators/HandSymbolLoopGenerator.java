package seph.reed.effigy.sequenceEditor.generators;

import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.EditModeManager;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;

public class HandSymbolLoopGenerator 
extends HandSymbolGenerator{
	
	public double m_rate = 1.0/8.0;
	public double m_noteLength = 1.0/2.0;

	public HandSymbolLoopGenerator(EditModeManager i_mother) {
		super(i_mother);  
		m_name = "Symbol Looper";
		m_gui = new HandSymbolLoopGeneratorGUI<HandSymbolLoopGenerator>(this, this); }

	
	@Override
	public void generateNotes(Sequencer addToMe) {
		GenerateAction userAction = new GenerateAction(addToMe);
		
		Pattern pat = m_patterns[m_currentPattern];
		double length = m_rate * m_noteLength;
		
		for(double pos = 0; pos < m_genLength; ) {
			for(int i_s =  0; i_s < m_patterns[m_currentPattern].m_steps.length; i_s++) {
				if(m_handModes[m_currentHandMode] != LEFT) {
					MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
					addMe.setBeat(addToMe.m_selectionPos + pos);
					addMe.setNote(pat.m_steps[i_s]);
					addMe.setLength(length);
					addToMe.addEntity(addMe);
						//
					userAction.m_addedEnts.add(addMe);
				}
				if(m_handModes[m_currentHandMode] != RIGHT) {
					MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
					addMe.setBeat(addToMe.m_selectionPos + pos);
					addMe.setNote(11 - pat.m_steps[i_s]);
					addMe.setLength(length);
					addToMe.addEntity(addMe);
						//
					userAction.m_addedEnts.add(addMe);
				}
			}
			
			pos += m_rate;
		}
		
		
		Effigy.HISTORY.addAction(userAction);
		
	}
	
}
