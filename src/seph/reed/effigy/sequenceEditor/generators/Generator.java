package seph.reed.effigy.sequenceEditor.generators;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.Sequencer;

public abstract class Generator
extends OOmject  {

	
	public double m_genLength = 1;
	
	public String m_name = "Unnamed";
	public GeneratorGUI<? extends Generator> m_gui;
	
	public Generator(OOmject i_mother) {
		super(i_mother);  }

	public abstract void generateNotes(Sequencer addToMe);
	
	public String toString() {
		return m_name;  }
	
	 
	
}
