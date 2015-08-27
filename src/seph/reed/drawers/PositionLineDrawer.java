package seph.reed.drawers;

import java.awt.Color;

import scott.thumbz.jaromin.Animations.Drawing;
import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import seph.reed.effigy.sequenceEditor.Sequencer;
import Helpers.Painter;

public class PositionLineDrawer 
extends Drawing{

	public Sequencer m_seq;
	public PixelPerspective m_sight;
	
	public PositionLineDrawer(OOmject i_mother,  Sequencer i_seq, PixelPerspective i_sight) {
		super(i_mother);  
		m_seq = i_seq;
		m_sight = i_sight;  }

	
	@Override
	public void paint(Painter joe) {
			//
		joe.setColor(new Color(255, 55, 55, 122));
		
		double pos = m_seq.m_pos;
		if(pos >= m_sight.bounds.getW() && pos <= m_sight.bounds.getE()) {
			int xPx = m_sight.convertXtoPx(pos);
			joe.fillRect(xPx, 0, 3, m_sight.canvas.getHeight());
		}
		
	}

}
