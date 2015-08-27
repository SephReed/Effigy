package seph.reed.effigy.sequenceEditor.generators;

import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.EditModeManager;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;




public class HandSymbolGenerator 
extends Generator {

	public Pattern[] m_patterns;
	public int m_currentPattern;
	
	public static final String BOTH = "Both Hands";
	public static final String LEFT = "Left Hand";
	public static final String RIGHT = "Right Hand";
	public static final String[] m_handModes = {BOTH, LEFT, RIGHT};
	public int m_currentHandMode = 0;
	
	public HandSymbolGenerator(EditModeManager i_mother) {
		super(i_mother);  

		m_name = "Hand Symbols";
		m_patterns = new Pattern[10];
		m_patterns[0] = new Pattern("Peace", new int[]{2,3});
		m_patterns[1] = new Pattern("Lazy Bird", new int[]{1, 3});
		m_patterns[2] = new Pattern("Fuck You", new int[]{3});
		m_patterns[3] = new Pattern("Love", new int[]{1,2,5});
		m_patterns[4] = new Pattern("Shocker", new int[]{2,3,5});
		m_patterns[5] = new Pattern("Classy", new int[]{5});
		m_patterns[6] = new Pattern("Claw", new int[]{1,3,5});
		m_patterns[7] = new Pattern("Nail's Did", new int[]{1,2,3,4,5});
		m_patterns[8] = new Pattern("The Goku", new int[]{0,1,2,3,4,5});
		m_patterns[9] = new Pattern("Palm", new int[]{0});

		m_gui = new HandSymbolGeneratorGUI<HandSymbolGenerator>(this, this);
	}

	
	@Override
	public void generateNotes(Sequencer addToMe) {
		GenerateAction userAction = new GenerateAction(addToMe);
		
		for(int i_s =  0; i_s < m_patterns[m_currentPattern].m_steps.length; i_s++) {
			if(m_handModes[m_currentHandMode] != LEFT) {
				MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
				addMe.setBeat(addToMe.m_selectionPos);
				addMe.setNote(m_patterns[m_currentPattern].m_steps[i_s]);
				addMe.setLength(m_genLength);
				addToMe.addEntity(addMe);
					//
				userAction.m_addedEnts.add(addMe);
			}
			if(m_handModes[m_currentHandMode] != RIGHT) {
				MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
				addMe.setBeat(addToMe.m_selectionPos);
				addMe.setNote(11 - m_patterns[m_currentPattern].m_steps[i_s]);
				addMe.setLength(m_genLength);
				addToMe.addEntity(addMe);
					//
				userAction.m_addedEnts.add(addMe);
			}
		}
		
		Effigy.HISTORY.addAction(userAction);
	}
	
	
	public class Pattern {

		public int[] m_steps;
		public String m_name;
		
		public Pattern(String i_name, int[] i_steps) {
			m_name = i_name;
			m_steps = i_steps;  }
		
		public String toString() {
			return m_name;  }
	}

}
