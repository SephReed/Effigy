package seph.reed.effigy;

import java.util.ArrayList;
import java.util.Timer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.extras.music.midi.MidiToolBox;

//import me.Thumbz.RobotSoul.RobotSoul;
//import me.Thumbz.RobotSoul.Options.RSMidiDevice;



public class MidiRouter 
extends OOmject
{
	public ArrayList<MidiDevice> m_midiFrom;
	public ArrayList<MidiDevice> m_midiTo;

	public Timer m_timer;
	
	public MidiRouter(OOmject i_mother)  {
		super(i_mother);
		initializePorts();
		m_timer = new Timer();  }

	public void initializePorts()  {	
		m_midiFrom = new ArrayList<MidiDevice>();
		m_midiTo = new ArrayList<MidiDevice>();


		MidiDevice device;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

		for (int i = 0; i < infos.length; i++)   {
			try  {
				device = MidiSystem.getMidiDevice(infos[i]);

				//open each device
				device.open();
				String name = device.getDeviceInfo().getDescription();

				try  {
					@SuppressWarnings("unused")
					Transmitter trans = device.getTransmitter();
					m_midiFrom.add(device);  
					System.out.println("Midi From #"+(m_midiFrom.size()-1)+": "+name+" Was Opened");  }
				
				catch (MidiUnavailableException e)  {
					m_midiTo.add(device);
					System.out.println("Midi To #"+(m_midiTo.size()-1)+": "+name+" Was Opened");  }
			} 
			catch (MidiUnavailableException e)   {
				System.out.println(e.toString());
				System.out.println("A Midi Device failed to open");  }   
		}
	}
	
	
	
	public void sendMidiOut(ShortMessage sendMe) {
		if(Hax.HAX_ON == true) {
			int status = sendMe.getStatus();
			status = MidiToolBox.NOTE_ON;
			int key = sendMe.getData1();
			key = Hax.MAPPINGS[key];
			int vel = sendMe.getData2();
				//
			try {  sendMe = new ShortMessage(status, key, vel);  } 
			catch (InvalidMidiDataException e) {  e.printStackTrace();  }
			
//			sysOut(MidiToolBox.midiMessageToString(sendMe));
		}
		
		for(int i = 0; i < m_midiTo.size(); i++) {
			try {
				m_midiTo.get(i).getReceiver().send(sendMe, System.currentTimeMillis());  }  
//				System.out.println("Midi Sent to midi_to #"+i);  } 
			catch (MidiUnavailableException e) {  e.printStackTrace();  }
//			List<Receiver> targets = m_midiTo.get(i).getReceivers();
//			for(int i_r = 0; i_r < targets.size(); i_r++) {
//				targets.get(i_r).send(sendMe, System.currentTimeMillis());
//				System.out.println("Midi Sent to midi_to #"+i);
//			}
		}
	}

}
