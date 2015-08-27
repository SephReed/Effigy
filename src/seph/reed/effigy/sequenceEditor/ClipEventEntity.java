package seph.reed.effigy.sequenceEditor;

import java.awt.Color;

import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import Helpers.Painter;

public class ClipEventEntity 
extends MidiEventEntity {
	
//	public PixelPerspective m_sight;
	
	public ClipEventEntity(OOmject i_mother, Sequencer i_clip) {
		super(i_mother, i_clip);
		FILL = new Color(0, 150, 120);
		fillColor = FILL;
	}
	
	@Override
	public void projectVisually(PixelPerspective sight, Painter joe) {
		super.projectVisually(sight, joe);
		
		int x = sight.convertXtoPx(getBeat()) + 4;
		int y = sight.convertYtoPx(0) - 4;
		joe.setColor(Color.BLACK);
		joe.drawString(m_clip.m_name, x, y);
	}

	
	
}
