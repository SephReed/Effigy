package seph.reed.effigy.sequenceEditor;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import scott.thumbz.jaromin.Science.BasicInteractiveEntity2D;
import scott.thumbz.jaromin.Science.Rectangle;
import seph.reed.effigy.Effigy;
import Helpers.Painter;

public class MidiEventEntity 
extends BasicInteractiveEntity2D 
implements MouseListener, MouseWheelListener{
	
	public MidiEventEntity m_next;
	public static Color FILL = Color.PINK;
	public Sequencer m_clip;
	public EditModeManager m_editor;
	
	public MidiEventEntity(OOmject i_mother, Sequencer i_clip) {
		super(i_mother);  
	
		m_clip = i_clip;
		m_editor = ANCESTOR(Effigy.class).m_editorManager;
		
		this.m_bounds.setSize(1, .8);
		this.fillColor = FILL;
		this.mouseListeners.add(this);
		this.mouseWheelListeners.add(this);
		this.borderColor = Color.WHITE;
	}
	
	
	@Override
	public void projectVisually(PixelPerspective sight, Painter joe) {
				//
		if(sight.canSeeBoundsOf(this))  {
				//
			Rectangle entityToPixelBounds = sight.convertToPixelBounds(m_bounds);
			joe.setColor(fillColor);
			joe.fillRect(entityToPixelBounds);
			joe.setColor(borderColor);
			joe.drawRect(entityToPixelBounds);
			joe.setColor(Color.MAGENTA);
			joe.drawRect(entityToPixelBounds.modLocation(1, 1).modSize(-2, -2));
			joe.setColor(Color.YELLOW);
			joe.drawRect(entityToPixelBounds.modLocation(1, 1).modSize(-2, -2));
			joe.setColor(Color.ORANGE);
			joe.drawRect(entityToPixelBounds.modLocation(1, 1).modSize(-2, -2));
		}		
	}
	
	public double getBeat() {
		return this.m_bounds.getW();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	
	@Override
	public void mouseReleased(MouseEvent e) {}
	

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int clicks = e.getWheelRotation();
		double dWidth = (m_clip.m_focus / 4.0);
		double n_width = m_bounds.getWidth() + dWidth * clicks;
			n_width -= n_width%dWidth;
		if(clicks < 0) {
			if(n_width >= dWidth) {
				this.m_bounds.setWidth(n_width); 
				m_editor.lastNoteLength = n_width;  }
		}
		else if (clicks > 0)  {
			MidiEventEntity nextInLane = null;
			for(MidiEventEntity ptr = m_next; ptr != null && nextInLane == null; ptr = ptr.m_next) {
				if(ptr.getNote() == this.getNote()) {
					nextInLane = ptr;  }
			}
			
			
			if(nextInLane != null && getBeat()+n_width >= nextInLane.getBeat()) {
				this.m_bounds.setWidth(nextInLane.getBeat() - getBeat());
				sysOut("to end of next"+nextInLane);
				m_editor.lastNoteLength = n_width;  }
			else {
				this.m_bounds.setWidth(n_width); 
				m_editor.lastNoteLength = n_width;  }
		}
		
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		this.fillColor = Color.DARK_GRAY;  }
	
	@Override
	public void mouseExited(MouseEvent e) {
		this.fillColor = FILL;  }

	public void setBeat(double i_beat) {
		m_bounds.setW(i_beat);  }

	public void setNote(int key) {
		m_bounds.setS(key);  }

	public void setLength(double i_width) {
		m_bounds.setWidth(i_width);  }

	public double getEndBeat() {
		return m_bounds.getE();
	}

	public void setEndBeat(double beat) {
		m_bounds.setWidth(beat-getBeat());
	}

	public int getNote() {
		return (int) m_bounds.getY();  }
	

	public double getLength() {
		return m_bounds.getWidth();  }



	
	
}
