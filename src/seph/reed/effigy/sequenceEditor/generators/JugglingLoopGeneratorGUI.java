package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class JugglingLoopGeneratorGUI<GEN extends JugglingLoopGenerator> 
extends JugglingGeneratorGUI<GEN> {
	
//	public JCheckBox m_reverseMode;
//	public JComboBox<JugglingLoopGenerator.Pattern> m_patternChooser;
//	public JFractionInput m_noteLengthChooser;
	public JFractionInput m_rateChooser;
	
	

	public JugglingLoopGeneratorGUI(OOmject i_holder, GEN i_soul) {
		super(i_holder, i_soul);  }

	
	@Override
	protected void createComponent() {
		this.createSubcomponents();
		m_component = new JFancyPanel();
		m_component.setLayout(new MigLayout("inset 0, gap 0, fill"));
		m_component.setBackground(Effigy.COLOR_1);
		
		
		m_component.add(m_reverseMode);
		m_component.add(m_patternChooser, "wrap, spanx 2");
		m_component.add(m_length);
		m_component.add(m_noteLengthChooser);
		m_component.add(m_rateChooser);
	}
	
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_rateChooser = new JFractionInput("Rate", 1, 16);
		m_rateChooser.setBackground(Effigy.COLOR_1);
		m_rateChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_rate = m_rateChooser.getValue();  }
		});
	}

}
