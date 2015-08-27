package seph.reed.effigy;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class AutoPlay 
extends OOmject {

	public SequenceList m_list;
	public Timer m_timer;
	public SequenceRandomizerTask m_randTask;
	
	public AutoPlay(OOmject i_mother, SequenceList i_list) {
		super(i_mother);  
		m_list = i_list;  }

	public void playRandom() {
		m_list.getSelectedSequencer().stop();
		ANCESTOR(Effigy.class).m_editorManager.m_scene.stop();
		
		m_timer = new Timer();
		m_randTask = new SequenceRandomizerTask(null);
		m_timer.schedule(m_randTask, 0);
	}
	
	public void stop() {
		if(m_randTask != null) { m_randTask.cancel();  }
		if(m_timer != null)  { m_timer.cancel();  }
	}
	
	
	
	public class SequenceRandomizerTask extends TimerTask {
		public Sequencer m_lastPlayed;
		
		public SequenceRandomizerTask(Sequencer i_lastPlayed) {
			m_lastPlayed = i_lastPlayed;  }

		
		@Override
		public void run() {
			ArrayList<Sequencer> seqs = m_list.m_sequencers;
			
			
			if(seqs.size() > 0) {
				int seqIndex = (int) (Math.random() * seqs.size());
				Sequencer playMe = seqs.get(seqIndex);
				
				sysOut("playing "+playMe.m_name);
				
				m_list.setSelectedClip(seqIndex, false);
				playMe.reset(Math.random()*playMe.m_size);
				
				if(m_lastPlayed != null && m_lastPlayed != playMe) {
					double overlap = Math.random()*m_lastPlayed.m_size;
					long dMS = (long) (overlap * (60.0/Effigy.TEMPO) * 4000.0);
					m_timer.schedule(new TimerTask() {
						@Override
						public void run() {
							m_lastPlayed.stop();  }
					}, dMS);
				}
				
				double length = Math.random()+Math.random();
				length *= playMe.getSize();
				long dMS = (long) (length * (60.0/Effigy.TEMPO) * 4000.0);
				
				m_randTask = new SequenceRandomizerTask(playMe);
				m_timer.schedule(m_randTask, dMS);
			}
		}
		
	}
	
}
