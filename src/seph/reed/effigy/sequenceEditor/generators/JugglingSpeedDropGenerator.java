package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;
import seph.reed.effigy.undo.GenerateAction;

public class JugglingSpeedDropGenerator 
extends JugglingGenerator{

	public static final String LINEAR = "Linear";
	public static final String CURVED = "Curved";
	public static final String[] rateChangeModes = { LINEAR, CURVED };
	
	public double m_startRate = 1.0/4.0;
	public double m_endRate = 1.0/16.0;
	public double m_loopOverlap = 1.0/1.0;
	public int m_rateChangeMode = 0;
	
	public JugglingSpeedDropGenerator(OOmject i_mother) {
		super(i_mother);  
		m_name = "Juggling Speed Drop";  
		m_genLength = 2.0;  
		m_gui = new JugglingSpeedDropGeneratorGUI<JugglingSpeedDropGenerator>(this, this);  }

	@Override
	public void generateNotes(Sequencer addToMe) {
		double dRate = m_endRate - m_startRate;
		
		double dExpRatio = Math.log(m_endRate)/Math.log(m_startRate);
		dExpRatio -= 1.0;
		
		GenerateAction userAction = new GenerateAction(addToMe);
		
		int fireNum = nextFireNumAfter(-1);
		Pattern pat = m_patterns[m_currentPattern];
		
		for(double pos = 0; pos < m_genLength; ) {
			double rGen = pos/m_genLength;
			
			double dPos;
			if(rateChangeModes[m_rateChangeMode] == CURVED) { 
				sysOut("curve");
//				dPos = m_startRate * (1.0+(rGen * dExpRatio));
				double exp = 1.0 + (rGen * dExpRatio);
				dPos = Math.pow(m_startRate, exp);
			}
			else { 
				sysOut("line");
				dPos = m_startRate + (rGen * dRate);  }
			
			MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
			addMe.setBeat(pos + addToMe.m_selectionPos);
			addMe.setNote(pat.m_steps[fireNum]);
			addMe.setLength(m_noteLength * dPos);
			
			userAction.m_addedEnts.add(addMe);
			addToMe.addEntity(addMe);
			
			pos += dPos;
			fireNum = nextFireNumAfter(fireNum);
		}
		
		
		Effigy.HISTORY.addAction(userAction);
		
	}
}
