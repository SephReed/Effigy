package seph.reed.effigy;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.extras.music.midi.MidiToolBox;
import seph.reed.effigy.sequenceEditor.ClipEventEntity;
import seph.reed.effigy.sequenceEditor.ClipSequencer;
import seph.reed.effigy.sequenceEditor.EditModeManager;
import seph.reed.effigy.sequenceEditor.MidiEventEntity;
import seph.reed.effigy.sequenceEditor.Sequencer;


public class EffigyLoadSave 
extends OOmject{
	
	public static final int NOTE_ON = 0x90;
//    public static final int NOTE_OFF = 0x80;
//    public static int TICKS_PER_QUARTER = 64;
	public static int TICKS_PER_QUARTER = 256;
    
    public static ArrayList <MidiEvent> noteOffs = new ArrayList<MidiEvent>();
    
    
    
    public static MidiEventEntity [] lastNoteOn = new MidiEventEntity[127];
	
	public EffigyLoadSave(OOmject i_mother) {
		super(i_mother);  }

	

	public static void loadMidiFromLocal(String file, Sequencer addToMe ) {
		if(!file.endsWith(".mid")) {
			Toolkit.getDefaultToolkit().beep();
			return;  }
		InputStream in = Effigy.easyload(file);
		bufferMidiInputStream(in, addToMe);
	}
	
	
	public static void loadMidiFromFullPath(String absolutePath, Sequencer addToMe) {
		if(!absolutePath.endsWith(".mid")) {
			Toolkit.getDefaultToolkit().beep();
			return;  }
		try {
			InputStream in = new FileInputStream(absolutePath);
			bufferMidiInputStream(in, addToMe);  }
		catch (FileNotFoundException e) {
			e.printStackTrace();  }
	}


	public static void bufferMidiInputStream(InputStream in, Sequencer addToMe) {
		Sequence sequence = null;
		if(in != null){
			try {
				sequence = MidiSystem.getSequence(in);  }
			catch (InvalidMidiDataException e) {
				e.printStackTrace();  }
			catch (IOException e) {
				e.printStackTrace();
		}  }

		if(sequence != null)  {
				//
	        for (Track track :  sequence.getTracks())  {
	        	
	        	double trackLength = 1;
	            for (int i = 0; i < track.size(); i++) {
	            	
	                MidiEvent event = track.get(i);
	                long tick = event.getTick();
	                double pos = (tick)/(TICKS_PER_QUARTER * 4.0); 
	                
	                MidiMessage message = event.getMessage();
	                if (message instanceof ShortMessage) {
	                	
	                    ShortMessage sm = (ShortMessage) message;
	                    if (sm.getCommand() == NOTE_ON) {
	                        int key = sm.getData1();
	                        
	                        if(Hax.HAX_ON == true) {
	                        	for(int i_m = 0; i_m < Hax.MAPPINGS.length; i_m++) {
	                        		if(Hax.MAPPINGS[i_m] == key) {
	                        			key = i_m;
	                        			i_m = Hax.MAPPINGS.length;  }
	                        	}
	                        }
	                        
	                        int velocity = sm.getData2();
	                        if(velocity != 0) {
	                        	MidiEventEntity addMe = new MidiEventEntity(addToMe, addToMe);
		                        addMe.setBeat(pos);
		                        addMe.setNote(key);
		                        if (lastNoteOn[key] != null) {
		                        	double width = pos - lastNoteOn[key].m_bounds.getX();
		                        	lastNoteOn[key].m_bounds.setWidth(width);  }
		                        lastNoteOn[key] = addMe;
	                        }
	                        else {
	                        	if (lastNoteOn[key] != null) {
		                        	double width = pos - lastNoteOn[key].m_bounds.getX();
		                        	lastNoteOn[key].m_bounds.setWidth(width);
		                        	trackLength = Math.max(lastNoteOn[key].getEndBeat(), trackLength);
		                        	if(width > 0) {
		                        		addToMe.addEntity(lastNoteOn[key]);  
		                        		System.out.println("Note added, key=" + key + " velocity: " + velocity);  }
		                        	lastNoteOn[key] = null;  }
	                        }
	                        
	                    } 
	                }
	            }
	            //SEQUENCER FILLED
	            trackLength = Math.ceil(trackLength);
	            
	            sysOut(trackLength + "");
	            addToMe.setSize(trackLength);
	        }
		}
	}







	



	public static void saveMidiSequenceAs(Sequencer fromMe, File selectedFile) {
		String path = selectedFile.getAbsolutePath();
        if(!path.endsWith(".mid") ) {
        	selectedFile = new File(path + ".mid");  }
        
		try {
			Sequence out = convertSequencerToSequence(fromMe);
			if(out != null)  {
				MidiSystem.write(out, 0, selectedFile);  }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}



	private static Sequence convertSequencerToSequence(Sequencer fromMe) {
		return convertSequencerToSequence(fromMe, fromMe.m_size);  }

	
	
	private static Sequence convertSequencerToSequence(Sequencer fromMe, double length) {
		try {
			Sequence out = new Sequence(Sequence.PPQ, TICKS_PER_QUARTER);
			Track toMe = out.createTrack();
//			for(int i = 0; i < toMe.size(); i++){
//				Effigy.sysOut(""+toMe.get(i).toString());  
//				Effigy.sysOut(""+toMe.get(i).);  }
			
			writeSequencerToTrack(fromMe, toMe, length, 0);
//			toMe.
			
			return out;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return null;
		
	}


	public static void writeSequencerToTrack(Sequencer fromMe, Track toMe, double length, double startPos) {
		Hax.MOST_RECENT_TICK = -1;
		int loopCount = 0;
		double offset = startPos*4.0*TICKS_PER_QUARTER;
		int clipTick = (int) ((startPos+length)*4.0*TICKS_PER_QUARTER);
		clipTick--;
		
		try {
			for(MidiEventEntity ptr = fromMe.m_head; ptr != null; ) {
					//
				if(ptr.getBeat() + loopCount * fromMe.m_size >= length) {
					ptr = null;  }
				
				else if (ptr.getLength() > 0){
					byte note = (byte)ptr.getNote();
					
					if(Hax.HAX_ON == true) {
						note = (byte) Hax.MAPPINGS[note];  }
					
					byte velocity = (byte)127;
					
					long tickOn = (long) (TICKS_PER_QUARTER * ptr.getBeat()*4.0); //256 ticks per 1/4 note
					tickOn += offset;
					long tickOff = (long) (TICKS_PER_QUARTER * ptr.getEndBeat()*4.0); //256 ticks per 1/4 note
					tickOff += offset;
					tickOff = Math.min(tickOff, clipTick);
					
					addNoteOffsPriorTo(tickOn, toMe);
					
					ShortMessage msg;
					msg = new ShortMessage(MidiToolBox.NOTE_ON, note, velocity);
					
					if(tickOn <= Hax.MOST_RECENT_TICK) {
						tickOn = Hax.MOST_RECENT_TICK+1;  }
					MidiEvent addMe = new MidiEvent(msg, tickOn);
					toMe.add(addMe);
					
					if(Hax.HAX_ON)  {
						Hax.MOST_RECENT_TICK = addMe.getTick();
						sysOut("added tick on: "+Hax.MOST_RECENT_TICK);  }
					
					msg = new ShortMessage(MidiToolBox.NOTE_ON, note, 0);
					addMe = new MidiEvent(msg, tickOff);
					noteOffs.add(addMe);
					
					ptr = ptr.m_next;
					if(ptr == null) {
						loopCount++;
						ptr = fromMe.m_head;  
						addNoteOffsPriorTo(clipTick + 10, toMe);  }
				}
			}
			
//			if(noteOffs.size() > 0) {
//				addNoteOffsPriorTo(tickOn, toMe);  }
			
		} 
		catch (InvalidMidiDataException e) {  e.printStackTrace();  }
	}
	

	private static void addNoteOffsPriorTo(long tickOn, Track toMe) {
		boolean priorEvents = true;
		while(priorEvents && noteOffs.size() > 0)  {
			int first = 0;
			priorEvents = false;
			
			for(int i_m = 0; i_m < noteOffs.size(); i_m++)  {
				boolean beforeNoteOn = noteOffs.get(i_m).getTick() < tickOn;
				boolean firstNoteOff = noteOffs.get(i_m).getTick() < noteOffs.get(first).getTick();
				
				//the existence of a prior event is false until at least one note is found before note on
				priorEvents = priorEvents || beforeNoteOn;
				
				if(beforeNoteOn && firstNoteOff) {
					first = i_m;  
			}	}
			
			MidiEvent nextNoteOff = noteOffs.remove(first);
			long tick = nextNoteOff.getTick();
			if(tick <= Hax.MOST_RECENT_TICK) {
				nextNoteOff.setTick(Hax.MOST_RECENT_TICK + 1);  }
			
			toMe.add(nextNoteOff);
			
			if(Hax.HAX_ON)  {
				Hax.MOST_RECENT_TICK = nextNoteOff.getTick();
				sysOut("added tick off: "+Hax.MOST_RECENT_TICK);  }
		}
		
	}



	public static void loadProject(String path, Effigy loadMe) {
		try {
			ZipFile zipFile = new ZipFile(path);
			
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			loadMe.m_sequencerList.clearSequencers();

		    while(entries.hasMoreElements())  {
		    		//
		        ZipEntry entry = entries.nextElement();

		        if(entry.toString().endsWith(".mid")) {
		        	InputStream stream = zipFile.getInputStream(entry);
		        	String name = entry.toString().replace(".mid", "");
		        	sysOut((name == "scene")+" name "+name+" = scene");
		        	if(name.indexOf("scene") == 0) {
		        		sysOut("!");
		        		EditModeManager mgmt = loadMe.m_editorManager;
		        		mgmt.m_scene.stop();
		        		mgmt.m_scene = new ClipSequencer(mgmt);
		        		mgmt.m_scene.m_name = "scene";
		        		bufferClipMidiInputStream(stream, mgmt.m_scene, loadMe);
		        	}
		        	else  {
					    Sequencer fillMe = loadMe.m_sequencerList.createSequencer(name);
					    bufferMidiInputStream(stream, fillMe);
		    }   }	}
		    
		    loadMe.m_sequencerList.setSelectedClip(0);
		    
		    zipFile.close();
		} 
		catch (IOException e) {  e.printStackTrace();  }
	}



	public static void saveProjectAs(String path, Effigy saveMe) {
		try {
			if(!path.endsWith(".feg") ) {
	        	path += ".feg";  }
			
			final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path));
		     
			
			ArrayList<Sequencer> seqs = saveMe.m_sequencerList.m_sequencers;
			for(int i = 0; i < seqs.size(); i++)  {
				String name = seqs.get(i).m_name + ".mid";
				
				ZipEntry e = new ZipEntry(name);
				out.putNextEntry(e);
				
				Sequence mid = convertSequencerToSequence(seqs.get(i));
		    	 
		    	try {  MidiSystem.write(mid, 1, out);  } 
		    	catch (IOException e1) {  e1.printStackTrace();  }
		    	out.closeEntry();
		    }
			ClipSequencer scene = saveMe.m_editorManager.m_scene;
			
			ZipEntry e = new ZipEntry("scene.mid");
			out.putNextEntry(e);
			
			Sequence mid = convertClipSequencerToSequence(scene, saveMe);
	    	 
	    	try {  MidiSystem.write(mid, 1, out);  } 
	    	catch (IOException e1) {  e1.printStackTrace();  }
	    	out.closeEntry();

		    out.close();
		} 
		
		catch (FileNotFoundException e1) {  e1.printStackTrace();  } 
		catch (IOException e2) {  e2.printStackTrace();  }
	}
	
	
	public static void saveProjectAsMidi(File selectedFile, Effigy saveMe) {
		String path = selectedFile.getAbsolutePath();
        if(!path.endsWith(".mid") ) {
        	selectedFile = new File(path + ".mid");  }
		try {
			Sequence out = new Sequence(Sequence.PPQ, TICKS_PER_QUARTER);
			Track toMe = out.createTrack();
			
			ClipSequencer scene = saveMe.m_editorManager.m_scene;
			
			for (ClipEventEntity ptr = (ClipEventEntity) scene.m_head; ptr != null; ptr = (ClipEventEntity) ptr.m_next) {
				Sequencer seq = ptr.m_clip;
				writeSequencerToTrack(seq, toMe, ptr.getLength(), ptr.getBeat());
			}
			
			MidiSystem.write(out, 0, selectedFile);
		}
		catch (InvalidMidiDataException e) {  e.printStackTrace();  }
		catch (IOException e) { e.printStackTrace();  }
		
	}
	
	
	
	
	
	
	
	
	
	
	//***LAZY LAZY CODE
	public static void bufferClipMidiInputStream(InputStream in, Sequencer addToMe,  Effigy loadMe) {
		Sequence sequence = null;
		if(in != null){
			try {
				sequence = MidiSystem.getSequence(in);  }
			catch (InvalidMidiDataException e) {
				e.printStackTrace();  }
			catch (IOException e) {
				e.printStackTrace();
		}  }

		if(sequence != null)  {
				//
	        for (Track track :  sequence.getTracks())  {
	            for (int i = 0; i < track.size(); i++) {
	            	
	                MidiEvent event = track.get(i);
	                long tick = event.getTick();
	                double pos = (tick)/(double)TICKS_PER_QUARTER; // MAGIC FUCKING NUMBER
	                
	                MidiMessage message = event.getMessage();
	                if (message instanceof ShortMessage) {
	                	
	                    ShortMessage sm = (ShortMessage) message;
	                    if (sm.getCommand() == NOTE_ON) {
	                        int key = sm.getData1();
	                        int velocity = sm.getData2();
	                        if(velocity != 0) {
	                        	ClipEventEntity addMe = new ClipEventEntity(addToMe, addToMe);
		                        addMe.setBeat(pos);
		                        addMe.setNote(0);
		                        addMe.m_clip = loadMe.m_sequencerList.m_sequencers.get(key);
		                        if (lastNoteOn[key] != null) {
		                        	double width = pos - lastNoteOn[key].m_bounds.getX();
		                        	lastNoteOn[key].m_bounds.setWidth(width);  }
		                        lastNoteOn[key] = addMe;
		                        addToMe.addEntity(addMe);
		                        System.out.println("Note on, key=" + key + " velocity: " + velocity);
	                        }
	                        else {
	                        	if (lastNoteOn[key] != null) {
		                        	double width = pos - lastNoteOn[key].m_bounds.getX();
		                        	lastNoteOn[key].m_bounds.setWidth(width);
		                        	lastNoteOn[key] = null;  }
	                        }
	                        
	                    } 
//	                    else if (sm.getCommand() == NOTE_OFF) {
//	                        int key = sm.getData1();
//	                        if (lastNoteOn[key] != null) {
//	                        	double width = pos - lastNoteOn[key].m_bounds.getX();
//	                        	lastNoteOn[key].m_bounds.setWidth(width);
//	                        	lastNoteOn[key] = null;  }
//	                    } 
	                }
	            }
	        }
		}	
	}
	
	
	
	private static Sequence convertClipSequencerToSequence(Sequencer fromMe, Effigy saveMe) {
		try {
			Sequence out = new Sequence(Sequence.PPQ, TICKS_PER_QUARTER);
			Track toMe = out.createTrack();
			
			for(MidiEventEntity ptr = fromMe.m_head; ptr != null; ptr = ptr.m_next) {
				byte note = (byte)saveMe.m_sequencerList.m_sequencers.indexOf(ptr.m_clip);
				byte velocity = (byte)127;
				
				long tickOn = (long) (256 * ptr.getBeat()); //256 ticks per 1/4 note
				long tickOff = (long) (256 * ptr.getEndBeat()); //256 ticks per 1/4 note
				
				ShortMessage msg = new ShortMessage(MidiToolBox.NOTE_ON, note, velocity);
				MidiEvent addMe = new MidiEvent(msg, tickOn);
				toMe.add(addMe);
				
				msg = new ShortMessage(MidiToolBox.NOTE_ON, note, 0);
				addMe = new MidiEvent(msg, tickOff);
				toMe.add(addMe);
			}
			
			return out;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	

	
	
}
