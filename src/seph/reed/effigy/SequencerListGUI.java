package seph.reed.effigy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class SequencerListGUI 
extends GUIManager<SequenceList, JPanel>{
	
	public JList<Sequencer> m_list;
	public JButton m_addButton;
	public JButton m_removeButton;

	public SequencerListGUI(OOmject i_holder, SequenceList i_soul) {
		super(i_holder, i_soul);  }

	@Override
	protected void createComponent() {
		m_component = new JPanel();
//		m_component.setBackground(Effigy.COLOR_2);
		m_component.setOpaque(false);
		m_component.setLayout(new MigLayout("fill, gap 0, inset 0", "[left]"));
		
		m_addButton = new JButton("+");
		m_addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.createSequencer();  }
		});
		m_component.add(m_addButton, "split, shrinkx");
		
		m_removeButton = new JButton("-");
		m_removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.removeClip(m_soul.m_selectedClip);  }
		});
		m_component.add(m_removeButton, "wrap, shrinkx");
		
		updateClipList();  
 }

	
	public void updateClipList() {
		if(m_component == null) return;
		if(m_list != null) {  m_component.remove(m_list);  }
		
		m_list = new JList<Sequencer>(m_soul.m_sequencers.toArray(new Sequencer[m_soul.m_sequencers.size()])); //data has type Object[]
		m_list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		m_list.setLayoutOrientation(JList.VERTICAL);
		m_list.setVisibleRowCount(-1);
		m_list.setBackground(Effigy.COLOR_2);
		m_list.setForeground(Effigy.COLOR_4);
		m_list.setSelectedIndex(m_soul.m_selectedClip);
		
		m_list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//				ANCESTOR(SequenceEditor.class).setCurrentClip(m_soul.m_clips.get(m_list.getSelectedIndex()));
				m_soul.setSelectedClip(m_list.getSelectedIndex());
			}
		});
		
		m_component.add(m_list, "growx");
	}
		
//		m_list.removeAll();
		
//		for(int i = 0; i < m_soul.m_clips.size(); i++) {
//			m_list.add
//			
//			final Clip clip = m_soul.m_clips.get(i);
//			JButton addMe = new JButton(clip.toString());
////			addMe.setOpaque(false);
////			addMe.setBackground(Color.GREEN);
//			addMe.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					ANCESTOR(SequenceEditor.class).setCurrentClip(clip);  }
//			});
//			m_list.add(addMe, "wrap");
		

}
