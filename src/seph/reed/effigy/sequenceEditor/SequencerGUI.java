package seph.reed.effigy.sequenceEditor;

import java.awt.Color;
import java.util.Timer;

import javax.swing.JScrollPane;

import scott.thumbz.jaromin.Animations.Drawing;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import scott.thumbz.jaromin.Perspective.PixelPerspective;
import scott.thumbz.jaromin.Perspective.SpriteGridPixelPerspective;
import scott.thumbz.jaromin.Perspective.StretchyPixelPerspective.StretchMode;
import scott.thumbz.jaromin.Science.GridDrawer;
import scott.thumbz.jaromin.Science.SpaceDrawer;
import seph.reed.drawers.ClipBoundsDrawer;
import seph.reed.drawers.PositionLineDrawer;
import seph.reed.drawers.SelectionDrawer;
import seph.reed.effigy.Effigy;
import Helpers.Painter;

public class SequencerGUI 
extends GUIManager<Sequencer, JScrollPane>{

	public JFancyPanel m_sequence;
	
	public SpaceDrawer m_spaceDrawer;
	public PositionLineDrawer m_positionLineDrawer;
	public SelectionDrawer m_selectionDrawer;
	public GridDrawer m_gridDrawer;
	public ClipBoundsDrawer m_clipBoundsDrawer;
	public FXAreaDrawer m_fxAreaDrawer;
	
	public SpriteGridPixelPerspective m_lens;
	public Timer m_timer;
//	public MultiOnt m_viewBounds;
//	public SequencerListener m_listener;
	public EditModeManagerGUI m_editGUI;
	
	public SequencerGUI(OOmject i_mother, Sequencer sequencer) {
		super(i_mother, sequencer);  }
	

	@Override
	protected void createComponent() {
		m_component = new JScrollPane();
		m_component.setBackground(Effigy.COLOR_1);
		
		m_sequence = new JFancyPanel();
		m_sequence.setBackground(Effigy.COLOR_1);
		
		m_lens = new SpriteGridPixelPerspective(this, m_sequence);
		m_lens.m_stretch = StretchMode.Free;
		m_lens.setBounds(m_soul.m_bounds);
		m_lens.bounds.setHeight(12);
		
		m_spaceDrawer = new SpaceDrawer(this, m_soul, m_lens);
		m_gridDrawer = new GridDrawer(this, m_lens);
		m_gridDrawer.gridIncrements.setX(m_soul.m_focus);
		m_gridDrawer.m_gridColor = new Color(100, 220, 220, 80);
		m_positionLineDrawer = new PositionLineDrawer(this, m_soul, m_lens);
		m_selectionDrawer = new SelectionDrawer(this, m_soul, m_lens);

		
		m_sequence.addDrawing(m_spaceDrawer);
		m_sequence.addDrawing(m_gridDrawer);
		m_sequence.addDrawing(new FXAreaDrawer(this, m_soul, m_lens));
		
		GridDrawer measureLines = new GridDrawer(this, m_lens);
			measureLines.gridIncrements.setX(1.0);
			measureLines.gridIncrements.setY(12.0);
			m_sequence.addDrawing(measureLines);
			
		m_sequence.addDrawing(m_positionLineDrawer);
		m_sequence.addDrawing(m_selectionDrawer);
		
		
		m_component.setViewportView(m_sequence);
		m_component.setBackground(Color.DARK_GRAY);
		
		initListeners();
	}


	
	public void initListeners() {
		SequencerListener<MidiEventEntity> m_listener = new SequencerListener<MidiEventEntity>(this, m_soul, m_lens, ANCESTOR(Effigy.class).m_editorManager) {
			@Override
			public  MidiEventEntity createNewEnt(){
				return new MidiEventEntity(m_soul, m_soul);  }
		};
		m_component.addMouseListener(m_listener);
		m_component.addMouseMotionListener(m_listener);
		m_component.addMouseWheelListener(m_listener);
	}


	

	
	
	public class FXAreaDrawer extends Drawing {
		public PixelPerspective m_sight;
		public Sequencer m_sequencer;
		
		public FXAreaDrawer(OOmject i_mother, Sequencer i_sequencer,
				SpriteGridPixelPerspective m_lens) {
			super(i_mother);
			
			m_sequencer = i_sequencer;
			m_sight = m_lens;
		}


		@Override
		public void paint(Painter joe) {
			joe.setColor(new Color(250, 225, 250, 40));
			
			int width = m_sight.convertXtoPx(m_sequencer.m_size);
			joe.fillRect(0, 0, width, m_sight.convertYtoPx(12));
			joe.setColor(new Color(255, 120, 0, 80));
			joe.drawRect(0, 0, width, m_sight.convertYtoPx(12));
		}
		
	}
	

}
