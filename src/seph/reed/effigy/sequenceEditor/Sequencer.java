package seph.reed.effigy.sequenceEditor;

import java.awt.Color;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import scott.thumbz.jaromin.DataStructures.ThumbzArrayList;
import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Science.BasicVisualSpace2D;
import scott.thumbz.jaromin.Science.Rectangle;
import scott.thumbz.jaromin.extras.music.midi.MidiToolBox;
import seph.reed.effigy.Effigy;

public class Sequencer 
extends BasicVisualSpace2D<MidiEventEntity>{
	
	public static final int ALLOW_OVERLAP = -3;
	public static final int TRUNCATE_AT_OVERLAP = 0;
	public static final int SKIP_OVERLAP = 2;
	public static final int DEMOLISH_OVERLAP = 4;
	public static final int SKIP_THEN_MERGE_OVERLAP = 8;
	public int overlapMode = TRUNCATE_AT_OVERLAP;
	
//	public static final int IMPULSE_OVERLAP = -3;
//	public static final int IMPULSE_CUT = 0;
//	public static final int IMPULSE_DELAY = 2;
//	public int impulseOverMode = IMPULSE_CUT;
	
	public String m_name;
	public double m_selectionPos = 0;
	public double m_focus;
	public double m_pos = 0;
	public int m_posMS = 0;
	public Timer m_timer;
	public final int PROGRESS_RATE_MS = 24;
	public int LOOP_LENGTH_MS;
	public double PROGRESS_RATE_DPOS;
	public MidiEventEntity nextNote;
	public int nextNoteOff = -1;
	public double m_tempo;
	
	public double m_size = 4;
	public MidiEventEntity m_head;
	
	public double[] noteOffTimes = new double[18];
	public MidiEventEntity[] noteOffEventHolders = new MidiEventEntity[18];
	public SequencerTimer m_task;
	public DrawTimer m_drawTask;
	
	public SequencerGUI m_gui;
	

	public Sequencer(OOmject i_mother, String i_name) {
		super(i_mother);  
		Arrays.fill(noteOffTimes, -1);
		m_name = i_name;
		
		this.m_bounds = new Rectangle(m_size, 18);
		this.borderColor = Color.BLACK;
		this.fillColor = Color.BLACK;
		
		m_gui = new SequencerGUI(this, this);
		m_timer = new Timer();
		
		setTempo(Effigy.TEMPO);
		setFocus(1.0/4.0);
	}
	
	
	@Override
	public void addEntityAt(MidiEventEntity addMe, int layerNum)  {
		if(overlapMode != ALLOW_OVERLAP) {
			ThumbzArrayList<MidiEventEntity> collisions = detectCollisions(addMe);
//			if(collisions.size() != 0) { sysOut("collision");  }
			
			if(overlapMode == TRUNCATE_AT_OVERLAP) {
				for(int i_c = 0; i_c < collisions.size(); i_c++) {
					MidiEventEntity touched = collisions.get(i_c);
					if(touched.getBeat() < addMe.getBeat()) {
						touched.setEndBeat(addMe.getBeat());  }  
					else if(touched.getBeat() == addMe.getBeat()) {
						entityLayers.get(0).remove(touched);  }
					else if(addMe.getEndBeat() > touched.getBeat()) {
						addMe.setEndBeat(touched.getBeat());  
					}
				}
			}
		}
		addToLinkedList(addMe);
		
		super.addEntityAt(addMe, layerNum);
	}
	
	
	
	public void removeEntity(MidiEventEntity target)  {
		if(entityLayers.get(0).remove(target) == true)  {
			if(m_head == null) return;
			
			if(m_head == target) {
				m_head = target.m_next;  }
			
			else for(MidiEventEntity ptr = m_head; ptr.m_next != null; ptr = ptr.m_next)  {
				if(ptr.m_next == target) {
					ptr.m_next = ptr.m_next.m_next;
					return;  }
			}
		}
	}


	private void addToLinkedList(MidiEventEntity addMe) {
		
		MidiEventEntity putAfter = null;
		if(m_head != null && m_head.m_bounds.getX() < addMe.m_bounds.getX()) {
			for(MidiEventEntity ptr = m_head; ptr != null && putAfter == null; ptr = ptr.m_next) {
//				System.out.println(ptr.m_bounds.getX());
				if(ptr.m_next == null || ptr.m_next.m_bounds.getX() > addMe.m_bounds.getX()) {
					putAfter = ptr;  
		}	}	}
		
		if(putAfter == null) {
			addMe.m_next = m_head;
			m_head = addMe;  }
		else {
			if(addMe.getBeat() > m_pos && nextNote!= null && addMe.getBeat() < nextNote.getBeat()) {
				nextNote = addMe;  }
			
			addMe.m_next = putAfter.m_next;
			putAfter.m_next = addMe;
		}
	}
	
	
	
	private void start(double startPos) {
		
		m_pos = startPos;
		m_posMS = (int) ((startPos/m_size)*LOOP_LENGTH_MS);
		nextNote = null;
		
		for(MidiEventEntity ptr = m_head; nextNote == null && ptr != null; ptr = ptr.m_next) {
			if(ptr.getBeat() >= m_pos) {
				nextNote = ptr;  }
		}	
		
		
		System.out.println("start");
		m_timer = new Timer();
		if(nextNote != null && nextNote.getBeat() == m_pos)
		{  m_task = new SequencerTimer(SequencerTimer.NOTE_ON);  }
		else
		{  m_task = new SequencerTimer(SequencerTimer.NO_NOTE);  }
		m_timer.schedule(m_task, 0);
		
		m_drawTask = new DrawTimer();
		m_timer.scheduleAtFixedRate(m_drawTask, 0, 1000/24);
	}

	
	public void reset(double startPos) {
		stop();
		start(startPos); 
	}
	
	public void reset() {
		reset(0);  }
	
	
	public void stop() {
		if(m_task != null){  m_task.cancel();  }
		if(m_drawTask != null){  m_drawTask.cancel();  }
		m_pos = 0;
		m_posMS = 0;
		nextNote = m_head;  
		m_timer.cancel();  
		for(int i = 0; i < noteOffEventHolders.length; i++)  {
			if(noteOffEventHolders[i] != null) {
				noteOffEvent(noteOffEventHolders[i]);
				noteOffTimes[i] = -1;
				noteOffEventHolders[i] = null;
		}	}
	}

	
	
	
	public class DrawTimer extends TimerTask  {
		@Override
		public void run() {
			if(m_gui.hasComponent()) {
				m_gui.getComponent().repaint();
				m_gui.getComponent().revalidate();  }
		}
	}
	
	
	public class SequencerTimer extends TimerTask  {
		//
		public int delayType;
		public final static int NO_NOTE = 0;
		public final static int NOTE_ON = 2;
		public final static int NOTE_OFF = 4;
		
		public SequencerTimer(int i_delayType) {
			delayType = i_delayType;  }
	
		@Override
		public void run() {
				//
			if(delayType == NOTE_ON) {
				m_pos = nextNote.getBeat();  }
			else if(delayType == NO_NOTE){
				m_pos += PROGRESS_RATE_DPOS;  }
			else if(delayType == NOTE_OFF){
				m_pos = noteOffTimes[nextNoteOff];  }
			
			m_pos = checkForLoopEvent(m_pos);
			m_posMS %= LOOP_LENGTH_MS;
				
			
			if(delayType != NO_NOTE) {
				for(int i = 0; i < noteOffTimes.length; i++) {
					if(noteOffTimes[i] != -1 && noteOffTimes[i]% m_size == m_pos){
						noteOffEvent(noteOffEventHolders[i]);
						ANCESTOR(Effigy.class).m_hands.modFire(i, false);
						noteOffTimes[i] = -1;
						noteOffEventHolders[i] = null;
//						System.out.println("----");
					}
				}
			}
			
			if(delayType == NOTE_ON) {
				for(; nextNote != null && nextNote.getBeat() == m_pos; nextNote = nextNote.m_next) {
					int midiNum = (int) nextNote.m_bounds.getY();
					noteOffTimes[midiNum] = nextNote.getEndBeat();
					noteOffEventHolders[midiNum] = nextNote;
					noteOnEvent(nextNote);
				}
			}
			
			
			//Everything from here on deals with the exact position in time, not a rounded one
			m_pos = ((double)m_posMS)/LOOP_LENGTH_MS;  
			m_pos *= m_size;
			
			
			//figure out the delay and whether or not a note will be playing at the end of it
			long dMS = PROGRESS_RATE_MS;
			int nextDelay = NO_NOTE;
			if(m_head != null) {
				if(nextNote == null || nextNote.m_bounds.getX() >= m_size) {
//					sysOut(nextNote.m_bounds.getX() + "<"+m_size+" "+m_pos);
					nextNote = m_head;  }
				
				double dPosOn = nextNote.m_bounds.getX() - m_pos;
				if(dPosOn < 0) { dPosOn += m_size; }
				
				double dPosOff = -1;
				for(int i = 0; i < noteOffTimes.length; i++) {
					if(noteOffTimes[i] != -1) {
						double tryMe = noteOffTimes[i] - m_pos;
						if(tryMe < dPosOff || dPosOff == -1) {
							dPosOff = tryMe;  
							nextNoteOff = i;  
						}
					}
				}
				
				if(dPosOff == -1) {
					dPosOff = PROGRESS_RATE_DPOS * 2; }
				
				if(dPosOn > 0 && dPosOn <= dPosOff && dPosOn <= PROGRESS_RATE_DPOS) {
					dMS = (long) (((m_pos+dPosOn)/m_size) * LOOP_LENGTH_MS) - m_posMS ;
					nextDelay = NOTE_ON;  }
				else if(dPosOff > 0 && dPosOff <= PROGRESS_RATE_DPOS) {
					dMS = (long) (((m_pos+dPosOff)/m_size) * LOOP_LENGTH_MS) - m_posMS ;
					nextDelay = NOTE_OFF;  }
			}
			
			
			//ERROR HANDLING
			if(dMS < 0) {  dMS = PROGRESS_RATE_MS;  sysOut("ERROR :: dMS < 0");  }
				//
//			if(nextNote != null && nextNote != m_head && nextNote.getBeat() < m_pos) {
//				while(nextNote != null && nextNote.getBeat() < m_pos) {
//					sysOut("skipping note "+nextNote.getBeat());
//					nextNote = nextNote.m_next;  }
//				
//				nextDelay = NOTE_ON;
//				if(nextNote == null) {  nextNote = m_head;  }
//				dMS = (long) (((nextNote.getBeat()/m_size)*LOOP_LENGTH_MS) - m_posMS);
//				dMS += LOOP_LENGTH_MS;
//				dMS %= LOOP_LENGTH_MS;
//			}	
				//
//			if(nextNote != null) { sysOut(nextNote.getLength() + " " + nextNote.getBeat() + " " + m_head.getBeat());  }
//			if(nextDelay != NO_NOTE) { sysOut("note");  }
			
			
			m_task = new SequencerTimer(nextDelay);
			m_timer.schedule(m_task, dMS);
			m_posMS += dMS;
		}
		
		//xena1107

		private double checkForLoopEvent(double checkMe) {
			if(checkMe >= m_size || checkMe == 0)  {
				sysOut("loop");
				for(int i = 0; i < noteOffTimes.length; i++) {
					if(noteOffTimes[i] != -1) {
						noteOffEvent(noteOffEventHolders[i]);
						noteOffTimes[i] = -1;
						noteOffEventHolders[i] = null;
				}	}
				nextNote = m_head;
				return checkMe %= m_size;  
			}
			return checkMe;
		}

		
		
	}

	public void noteOnEvent(MidiEventEntity note) {
//		sysOut("playNote "+note.getBeat()+" "+note.getLength());
		ANCESTOR(Effigy.class).m_hands.modFire(note.getNote(), true);
		try {
			ShortMessage sendMe;
			if(Effigy.QUNEO_OUTPUT == true && note.getNote() < 12) {
				sendMe =  new ShortMessage(MidiToolBox.NOTE_ON+2, Effigy.QUNEO_MAPPING[note.getNote()], 80);  }
			else {
				sendMe = new ShortMessage(MidiToolBox.NOTE_ON, note.getNote(), 127);  }
			ANCESTOR(Effigy.class).m_router.sendMidiOut(sendMe);  }
		catch (InvalidMidiDataException e) {  e.printStackTrace();  }
	}
	
	


	public void noteOffEvent(MidiEventEntity note) {
		ANCESTOR(Effigy.class).m_hands.modFire(note.getNote(), false);
		try {
			ShortMessage sendMe;
			if(Effigy.QUNEO_OUTPUT == true && note.getNote() < 12) {
				sendMe =  new ShortMessage(MidiToolBox.NOTE_ON+2, Effigy.QUNEO_MAPPING[note.getNote()], 0);  }
			else {
				sendMe = new ShortMessage(MidiToolBox.NOTE_ON, note.getNote(), 0);  }
			ANCESTOR(Effigy.class).m_router.sendMidiOut(sendMe);  }
		catch (InvalidMidiDataException e) {  e.printStackTrace();  }
	}
		
	
	

	public void setTempo(double i_tempo) {
		m_tempo = i_tempo;
		PROGRESS_RATE_DPOS = (PROGRESS_RATE_MS/1000.0)/(60.0/m_tempo);
		PROGRESS_RATE_DPOS /= 4.0;     //conversion from 1/4 note to whole note
		LOOP_LENGTH_MS = (int) (m_size*(60.0/m_tempo)*4.0*1000);
	}


//	public void setFocus(int splitVal) {
//		m_focus = splitVal;
//		if(m_gui.m_gridDrawer != null) {
//			m_gui.m_gridDrawer.gridIncrements.setX(4.0/m_focus);  }
//	}
	
	public void setFocus(double splitVal) {
		m_focus = splitVal;
		if(m_gui.m_gridDrawer != null) {
			m_gui.m_gridDrawer.gridIncrements.setX(m_focus);  }
	}
	
	public double getSize() {
		return m_bounds.getWidth();  }

	
	public String toString() {
		String out = m_name;
		out += "-- " + m_size+" measures";
		return out;
	}


	public void setSize(double i_size) {
		m_size = i_size;
		if(m_gui != null && m_gui.m_lens != null) {
			m_gui.m_lens.bounds.setWidth(m_size);  }
		LOOP_LENGTH_MS = (int) (m_size*(60.0/m_tempo)*4.0*1000);
	}


	public void removeAllNullEntities() {
		for(MidiEventEntity ptr = m_head; ptr != null;) {
			MidiEventEntity next = ptr.m_next;
			if(ptr.getLength() <= 0 || ptr.getBeat() < 0 ) {
				sysOut("removed Ent @"+ptr.getBeat());
				this.removeEntity(ptr);  }
			ptr = next;
		}
		
	}





	

}
