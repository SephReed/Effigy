package seph.reed.effigy.sequenceEditor.generators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;

public abstract class GeneratorGUI <GEN extends Generator>
extends GUIManager<GEN, JFancyPanel>{

	public JFractionInput m_length;
	
	public GeneratorGUI(OOmject i_holder, GEN i_soul) {
		super(i_holder, i_soul);  }
	
	public void createSubcomponents() {
		m_length = new JFractionInput("Gen Length", m_soul.m_genLength, 1);
		m_length.setBackground(Effigy.COLOR_1);
		m_length.setLabelForeground(Effigy.COLOR_5);
		m_length.addActionlistener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.m_genLength = m_length.getValue();
			}
		});
	}

}
