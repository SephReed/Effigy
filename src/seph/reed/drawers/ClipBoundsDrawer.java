package seph.reed.drawers;

import java.awt.Color;

import scott.thumbz.jaromin.Animations.Drawing;
import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import scott.thumbz.jaromin.Perspective.SpriteGridPixelPerspective;
import seph.reed.effigy.sequenceEditor.Sequencer;
import Helpers.Painter;

public class ClipBoundsDrawer 
extends Drawing{

	public PixelPerspective m_sight;
	public Sequencer m_sequencer;
	
	public ClipBoundsDrawer(OOmject i_mother, Sequencer i_sequencer,
			SpriteGridPixelPerspective m_lens) {
		super(i_mother);
		
		m_sequencer = i_sequencer;
		m_sight = m_lens;
	}


	@Override
	public void paint(Painter joe) {
		joe.setColor(new Color(255, 125, 50, 40));
//		
//		double pos = m_editCenter.m_selectionPos;
//		if(pos >= m_sight.bounds.getW() && pos <= m_sight.bounds.getE()) {
			int width = m_sight.convertXtoPx(m_sequencer.m_size);
			joe.fillRect(0, 0, width, m_sight.canvas.getHeight());
			joe.setColor(Color.ORANGE);
			joe.drawRect(0, 0, width, m_sight.canvas.getHeight());
//		}
		
	}

}
