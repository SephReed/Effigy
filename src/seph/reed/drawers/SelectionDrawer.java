package seph.reed.drawers;

import java.awt.Color;

import scott.thumbz.jaromin.Animations.Drawing;
import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import scott.thumbz.jaromin.Perspective.SpriteGridPixelPerspective;
import seph.reed.effigy.sequenceEditor.Sequencer;
import Helpers.Painter;

public class SelectionDrawer 
extends Drawing{

//	public EditModeManager m_editCenter;
	public Sequencer m_soul;
	public PixelPerspective m_sight;
	
	public SelectionDrawer(OOmject i_mother, Sequencer i_soul,
			SpriteGridPixelPerspective m_lens) {
		super(i_mother);

		m_soul = i_soul;
		m_sight = m_lens;
	}


	@Override
	public void paint(Painter joe) {
		joe.setColor(new Color(120, 255, 120, 150));
		
		double pos = m_soul.m_selectionPos;
		if(pos >= m_sight.bounds.getW() && pos <= m_sight.bounds.getE()) {
			int xPx = m_sight.convertXtoPx(pos);
			joe.fillRect(xPx, 0, 10, m_sight.canvas.getHeight());
		}
		
	}

}
