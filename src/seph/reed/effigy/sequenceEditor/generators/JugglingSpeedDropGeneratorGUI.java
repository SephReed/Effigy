package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class JugglingSpeedDropGeneratorGUI<GEN extends JugglingSpeedDropGenerator> 
extends JugglingGeneratorGUI<GEN> {

	public JFractionInput m_startRateChooser;
	public JFractionInput m_endRateChooser;
	public JComboBox<String> m_rateChangeChooser;
	
	
	public JugglingSpeedDropGeneratorGUI(OOmject i_holder, GEN i_soul) {
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
		m_component.add(m_noteLengthChooser, "wrap");
		m_component.add(m_rateChangeChooser, "wrap");
		m_component.add(m_startRateChooser);
		m_component.add(m_endRateChooser, "wrap");
	}
	
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_startRateChooser = new JFractionInput("Start Rate", 1, 4);
		m_startRateChooser.setOpaque(false);
		m_startRateChooser.setLabelForeground(Effigy.COLOR_5);
		m_startRateChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_startRate = m_startRateChooser.getValue();  }
		});
		
		
		
		m_endRateChooser = new JFractionInput("End Rate", 1, 16);
		m_endRateChooser.setOpaque(false);
		m_endRateChooser.setLabelForeground(Effigy.COLOR_5);
		m_endRateChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_endRate = m_endRateChooser.getValue();  }
		});
		
		
		m_rateChangeChooser = new JComboBox<String>(JugglingSpeedDropGenerator.rateChangeModes);
		m_rateChangeChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_rateChangeMode = m_rateChangeChooser.getSelectedIndex();  }
		});
	}

}
