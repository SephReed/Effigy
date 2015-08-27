package seph.reed.effigy.sequenceEditor;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import scott.thumbz.jaromin.Science.BasicVisualSpace2D;
import scott.thumbz.jaromin.Science.EntitySpaceMouseListener;

public abstract class SequencerListener <ENT extends MidiEventEntity>
extends EntitySpaceMouseListener<ENT>{

	public EditModeManager m_editCenter;
	public Sequencer m_sequencer;
	public Point lastPoint;
	public double lastXLocation = 0;
	
	@SuppressWarnings("unchecked")
	public SequencerListener(OOmject i_mother,
			Sequencer i_space,
			PixelPerspective i_sight,
			EditModeManager i_editCenter) {
		
		super(i_mother, (BasicVisualSpace2D<ENT>) i_space, i_sight);
		m_editCenter = i_editCenter;
		m_sequencer = i_space;
		
		if(m_editCenter == null) { sysOut("null edit center");  }
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		lastXLocation = sight.convertPxtoX(e.getPoint().x);  
		super.mouseMoved(e);  }
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		lastPoint = e.getPoint();
		if(m_editCenter.m_mode == EditModeManager.CLIP_MODE
			|| m_editCenter.m_mode == EditModeManager.SCENE_MODE) {
			if(hoveredEntity == null) {
				ENT addMe = createNewEnt();
				double x = sight.convertPxtoX(e.getPoint().x);
					x -= x%(m_sequencer.m_focus);
				double y = sight.convertPxtoY(e.getPoint().y);
					y -= y%1;
				addMe.m_bounds.setPositionSW(x, y);
				addMe.setLength(m_editCenter.lastNoteLength);
				space.addEntity(addMe);
			}
			
			else {
				m_sequencer.removeEntity(hoveredEntity);
				hoveredEntity = null;
		}	}
		else  {  // if(m_editCenter.m_mode == EditModeManager.GEN_MODE) {
			double x = sight.convertPxtoX(e.getPoint().x);
				x -= x%(m_sequencer.m_focus);
			m_editCenter.setSelectionPos(x);
		}
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(hoveredEntity == null) {
			 int notches = e.getWheelRotation();
			 notches *= -1;
			 double currentWidth = sight.getBounds().getWidth();
			 if (currentWidth >= m_sequencer.m_size && notches > 0) {} //do nothing
			 else  { 
				 double n_width;
				 if(currentWidth > 2) {  n_width = (int) (currentWidth + notches);  }
				 else  {  n_width = currentWidth * Math.pow(4.0/3.0, notches);  }
				 
				 n_width = Math.min(n_width, m_sequencer.m_size);
				 sight.bounds.setWidth(n_width); 
				 
				 double W = lastXLocation;  
				 W -= n_width/2.0;  
				 W = Math.max(W, 0); 
				 sight.bounds.setW(W);
				 
				 if(sight.bounds.getE() > m_sequencer.m_size) {
					 sight.bounds.setE(m_sequencer.m_size);  }
			 }
		}
		else { super.mouseWheelMoved(e);  }
	}
	
	
	public abstract ENT createNewEnt();

}
