package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;

public class JugglingGenerator 
extends Generator  {
	
	public Pattern[] m_patterns;
	public int m_currentPattern;
	public boolean directionForward = true;
	public double m_noteLength = 4.0/8.0;

	public JugglingGenerator(OOmject i_mother) {
		super(i_mother);  

		m_name = "Juggler";
		m_patterns = new Pattern[17];
		m_patterns[0] = new Pattern("Big Circle", new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11});
		m_patterns[1] = new Pattern("Right Hand Circle", new int[]{0, 1, 2, 3, 4, 5});
		m_patterns[2] = new Pattern("Left Hand Circle", new int[]{11, 10, 9, 8, 7, 6});
		m_patterns[3] = new Pattern("Figure 8", new int[]{0, 1, 2, 3, 4, 5, 11, 10, 9, 8, 7, 6});
		m_patterns[4] = new Pattern("T/T Symmetry", new int[]{0, 11, 1, 10, 2, 9, 3, 8, 4, 7, 5, 6});
		m_patterns[5] = new Pattern("T/T Asymetry", new int[]{0, 6, 1, 7, 2, 8, 3, 9, 4, 10, 5, 11});
		m_patterns[6] = new Pattern("T/T Sway Symmetry", new int[]{0, 11, 2, 9, 1, 10, 3, 8, 2, 9, 4, 7, 3, 8, 5, 6});
		m_patterns[7] = new Pattern("T/T Sway Asymetry", new int[]{0, 6, 2, 8, 1, 7, 3, 9, 2, 8, 4, 10, 3, 9, 5, 11});
		m_patterns[8] = new Pattern("T/T Sway Right Hand", new int[]{0, 2, 1, 3, 2, 4, 3, 5});
		m_patterns[9] = new Pattern("T/T Sway Left Hand", new int[]{11, 9, 10, 8, 9, 7, 8, 6});
		m_patterns[10] = new Pattern("T/T Shrink Symmetry", new int[]{0, 11, 5, 6, 1, 10, 4, 7, 2, 9, 3, 8});
		m_patterns[11] = new Pattern("T/T Shrink Asymetry", new int[]{0, 6, 5, 11, 1, 7, 4, 10, 2, 8, 3, 9});
		m_patterns[12] = new Pattern("Middle Fingers", new int[]{3, 8});
		m_patterns[13] = new Pattern("Guns", new int[]{1, 2, 3, 10, 9, 8});
		m_patterns[14] = new Pattern("Palms", new int[]{0, 11});
		m_patterns[15] = new Pattern("Chunky Circles", new int[]{0, 1, 2, 11, 10, 9, 3, 4, 5, 8, 7, 6});
		m_patterns[16] = new Pattern("Chunky Shrinks", new int[]{0, 1, 2, 11, 10, 9, 5, 4, 3, 6, 7, 8});
		
		m_gui = new JugglingGeneratorGUI<JugglingGenerator>(this, this);
	}

	@Override
	public void generateNotes(Sequencer addToMe) {
		double rate = m_genLength / m_patterns[m_currentPattern].m_steps.length;
		generatePatternAtRate(addToMe, rate);  }	
	
	
	protected void generatePatternAtRate(Sequencer addToMe, double rate) {
		GenerateAction userAction = new GenerateAction(addToMe);
		
		int fireNum = nextFireNumAfter(-1);
		Pattern pat = m_patterns[m_currentPattern];
		for(double i_f = 0; i_f < pat.m_steps.length; i_f++) {
			int note = pat.m_steps[fireNum];
			double beat = i_f*rate + addToMe.m_selectionPos;

			MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
			addMe.setBeat(beat);
			addMe.setNote(note);
			addMe.setLength(rate * m_noteLength);
			addToMe.addEntity(addMe);  
			
			userAction.m_addedEnts.add(addMe);
			
			fireNum = nextFireNumAfter(fireNum);
		}
		
		Effigy.HISTORY.addAction(userAction);
	}	

	public int nextFireNumAfter(int lastNum)  {
		if(lastNum == -1) {
			if(directionForward) return 0;
			else return m_patterns[m_currentPattern].m_steps.length -1;  
		}
		
		if(directionForward) {
			lastNum++;
			lastNum %=  m_patterns[m_currentPattern].m_steps.length; }
		else {
			lastNum--;
			if(lastNum<0) { lastNum =  m_patterns[m_currentPattern].m_steps.length -1;  } }
		return lastNum;
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
