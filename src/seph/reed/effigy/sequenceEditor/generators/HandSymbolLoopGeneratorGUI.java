package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public class HandSymbolLoopGeneratorGUI <GEN extends HandSymbolLoopGenerator>
extends HandSymbolGeneratorGUI<GEN> {
	
	public JFractionInput m_rateChooser;
	public JFractionInput m_noteLengthChooser;
	
	

	public HandSymbolLoopGeneratorGUI(OOmject i_holder, GEN i_soul) {
		super(i_holder, i_soul);  }
	
	
	@Override
	protected void createComponent() {
		this.createSubcomponents();
		
		m_component = new JFancyPanel();
		m_component.setOpaque(false);
		m_component.setLayout(new MigLayout());
		
		m_component.add(m_handModeChooser,"wrap");
		m_component.add(m_length);
		m_component.add(m_patternChooser, "wrap");
		m_component.add(m_rateChooser);
		m_component.add(m_noteLengthChooser);
	}
	
	
	
	@Override
	public void createSubcomponents() {
		super.createSubcomponents();
		
		m_rateChooser = new JFractionInput("Rate", 1, 8);
		m_rateChooser.setOpaque(false);
		m_rateChooser.setLabelForeground(Effigy.COLOR_5);
		m_rateChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_rate = m_rateChooser.getValue();  }
		});
		
		
		m_noteLengthChooser = new JFractionInput("Note Length", 1, 2);
		m_noteLengthChooser.setOpaque(false);
		m_noteLengthChooser.setLabelForeground(Effigy.COLOR_5);
		m_noteLengthChooser.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_noteLength = m_noteLengthChooser.getValue();  }
		});
	}

}
