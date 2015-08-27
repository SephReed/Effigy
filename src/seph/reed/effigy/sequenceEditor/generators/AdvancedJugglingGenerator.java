package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class AdvancedJugglingGenerator 
extends JugglingLoopGenerator  {

	public double rateModMax = 1.0/2.0;
	public double rateModRate = 32;
	
	public double lengthModMax = 1.0/4.0;
	public double lengthModRate = 16;
	
	
	public AdvancedJugglingGenerator(OOmject i_mother) {
		super(i_mother);  
		m_genLength = 64;  }
		
	
	@Override
	public void generateNotes(Sequencer addToMe) {
			//
		int fireNum = 0;
		if(!directionForward) { fireNum = m_patterns[m_currentPattern].m_steps.length -1;  }
		
		for(double i_p = addToMe.m_selectionPos; i_p < m_genLength; i_p += getRateAtPos(i_p, addToMe)) {
			MidiEventEntity addMe = new MidiEventEntity(this, addToMe);
			addMe.setBeat(i_p);
			addMe.setNote(m_patterns[m_currentPattern].m_steps[fireNum]);
			addMe.setLength(getLengthAtPos(i_p, addToMe));
			addToMe.addEntity(addMe);  
		
			if(directionForward) {
				fireNum++;
				fireNum %=  m_patterns[m_currentPattern].m_steps.length; }
			else {
				fireNum--;
				if(fireNum<0) { fireNum =  m_patterns[m_currentPattern].m_steps.length -1;  } 
	}	}	}

	private double getRateAtPos(double i_p, Sequencer clip) {
		double rateModWidth = rateModMax - m_rate;
		double dPos =  i_p - clip.m_selectionPos;
		if(dPos < 0) { dPos += clip.m_size;  }
		return ((dPos%rateModRate)/rateModRate*rateModWidth) + m_rate;
	}
	
	private double getLengthAtPos(double i_p, Sequencer clip) {
		double lengthModWidth = rateModMax - m_rate;
		double dPos =  i_p - clip.m_selectionPos;
		if(dPos < 0) { dPos += clip.m_size;  }
		return ((dPos%lengthModRate)/lengthModRate*lengthModWidth) + m_noteLength;
	}
}
