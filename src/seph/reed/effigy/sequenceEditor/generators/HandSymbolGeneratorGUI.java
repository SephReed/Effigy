package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.OOPject.OOmject;

public class HandSymbolGeneratorGUI <GEN extends HandSymbolGenerator>
extends GeneratorGUI<GEN> {
	
	public JComboBox<HandSymbolGenerator.Pattern> m_patternChooser;
	public JComboBox<String> m_handModeChooser;

	public HandSymbolGeneratorGUI(OOmject i_holder, GEN i_soul) {
		super(i_holder, i_soul);  }

	@Override
	protected void createComponent() {
		this.createSubcomponents();
		
		m_component = new JFancyPanel();
		m_component.setOpaque(false);
		m_component.setLayout(new MigLayout());
		
		m_component.add(m_handModeChooser,"wrap");
		m_component.add(m_length);
		m_component.add(m_patternChooser);
	}

	
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_patternChooser = new JComboBox<HandSymbolGenerator.Pattern>(m_soul.m_patterns);
		m_patternChooser.setOpaque(false);
		m_patternChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_currentPattern = m_patternChooser.getSelectedIndex();  }
		});
		
		m_handModeChooser = new JComboBox<String>(HandSymbolGenerator.m_handModes);
		m_handModeChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_currentHandMode = m_handModeChooser.getSelectedIndex();  } 
		});
	}
}
