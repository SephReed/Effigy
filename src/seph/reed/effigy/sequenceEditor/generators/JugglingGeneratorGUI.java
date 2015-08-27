package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class JugglingGeneratorGUI<GEN extends JugglingGenerator> 
extends GeneratorGUI<GEN> {
	
	public JCheckBox m_reverseMode;
	public JComboBox<JugglingLoopGenerator.Pattern> m_patternChooser;
	public JFractionInput m_noteLengthChooser;

	public JugglingGeneratorGUI(OOmject i_holder, GEN i_soul) {
		super(i_holder, i_soul);  }

	@Override
	protected void createComponent() {
		createSubcomponents();
		m_component = new JFancyPanel();
		m_component.setLayout(new MigLayout());
		m_component.setBackground(Effigy.COLOR_1);
		
		m_component.add(m_reverseMode);
		m_component.add(m_patternChooser, "wrap");
		m_component.add(m_length);
		m_component.add(m_noteLengthChooser);
	}
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_reverseMode = new JCheckBox("Reverse");
		m_reverseMode.setForeground(Effigy.COLOR_5);
		m_reverseMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.directionForward = !(m_reverseMode.isSelected());  }
		});
		
		m_patternChooser = new JComboBox<JugglingLoopGenerator.Pattern>(m_soul.m_patterns);
//		m_patternChooser.setBackground(Effigy.COLOR_1);
		m_patternChooser.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_currentPattern = m_patternChooser.getSelectedIndex();  }
		});
		
		m_noteLengthChooser = new JFractionInput("Note Length", 1, 1.0/m_soul.m_noteLength);
		m_noteLengthChooser.setBackground(Effigy.COLOR_1);
		m_noteLengthChooser.setLabelForeground(Effigy.COLOR_5);
		m_noteLengthChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_noteLength = m_noteLengthChooser.getValue();  }
		});
	}

}
