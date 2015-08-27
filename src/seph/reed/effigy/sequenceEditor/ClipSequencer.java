package seph.reed.effigy.sequenceEditor;

import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class ClipSequencer
extends Sequencer{

	public ClipSequencer(OOmject i_mother) {
		super(i_mother, "Scene 1");  
		m_bounds.setHeight(1);  
		m_gui = new SequencerGUI(this, this) {
			@Override
			public void initListeners() {
				SequencerListener<ClipEventEntity> m_listener = new SequencerListener<ClipEventEntity>(this, m_soul, m_lens, ANCESTOR(Effigy.class).m_editorManager) {
					@Override
					public  ClipEventEntity createNewEnt(){
						return new ClipEventEntity(m_soul, ANCESTOR(Effigy.class).m_sequencerList.getSelectedSequencer());
					}
				};
				m_component.addMouseListener(m_listener);
				m_component.addMouseMotionListener(m_listener);
				m_component.addMouseWheelListener(m_listener);
			}
		};
	}
	
	@Override
	public void noteOnEvent(MidiEventEntity note) {
		((ClipEventEntity)note).m_clip.reset();  }
	
	@Override
	public void noteOffEvent(MidiEventEntity note) {
		((ClipEventEntity)note).m_clip.stop();  }
}
