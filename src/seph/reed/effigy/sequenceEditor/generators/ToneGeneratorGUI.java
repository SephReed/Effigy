package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JNumberField;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class ToneGeneratorGUI <GEN extends ToneGenerator> 
extends JugglingGeneratorGUI<GEN> {

	public JNumberField m_toneChooser; 
	public JLabel m_toneLabel;
	
	public ToneGeneratorGUI(OOmject i_holder, GEN i_soul) {
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
		m_component.add(m_toneLabel);
		m_component.add(m_toneChooser);
	}
	
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_toneLabel = new JLabel("Tone:");
		m_toneLabel.setForeground(Effigy.COLOR_5);
		
		m_toneChooser = new JNumberField(80, 20, 100);
		m_toneChooser.setBackground(Effigy.COLOR_3);
		m_toneChooser.setForeground(Effigy.COLOR_4);
		m_toneChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_tone = m_toneChooser.getValue();  }
		});
	}
	
}
