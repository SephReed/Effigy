package seph.reed.effigy.sequenceEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.Components.JFractionInput;
import scott.thumbz.jaromin.Components.JNumberField;
import scott.thumbz.jaromin.Components.JReadoutKnob;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.Listeners.DoubleListener;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.Effigy;
import seph.reed.effigy.sequenceEditor.generators.Generator;

public class EditModeManagerGUI 
extends GUIManager<EditModeManager, JFancyPanel>  {
		//
	public int lastMode = -1;
	public JButton m_resetButton;
//	public int [] gridSplitsVals = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 15, 
//			16, 18, 20, 24, 32, 48, 64, 72, 96, 128, 256 };
	
//	public JFancyPanel m_tabPanel;
	public JFancyPanel m_notePanel;
	public ClipPanel m_clipPanel;
	public ScenePanel m_scenePanel;
	public JFancyPanel m_generatorPanel;
	public JButton m_noteModeButton;
	public JButton m_clipModeButton;
	public JButton m_generatorModeButton;
	
	//GEN
	public JComboBox<Generator> m_generatorDrop;
	public JFancyPanel m_currentGenPanel;
	
	
	public JButton m_generateButton;
	
	
	public EditModeManagerGUI(OOmject i_holder, EditModeManager i_soul) {
		super(i_holder, i_soul);  }

	
	@Override
	protected void createComponent() {
		m_component = new JFancyPanel();
		m_component.setLayout(new MigLayout("filly, gap 0, inset 0", "[shrink][grow]"));
		m_component.setBackground(Effigy.COLOR_1);
		
		createNotePanel();
		m_clipPanel = new ClipPanel();
		m_scenePanel = new ScenePanel();
		createGeneratorPanel();
		
		updateEditPanel();
		updateGeneratorPanel();
	}





	private void createNotePanel() {
		m_notePanel = new JFancyPanel();  }
	
	

	private void createGeneratorPanel() {
		m_generatorPanel = new JFancyPanel();
		m_generatorPanel.setLayout(new MigLayout("fill, inset 0, gap 0", "[]", "[shrink][grow]"));
		m_generatorPanel.setBackground(Effigy.COLOR_1);
	
		m_generatorDrop = new JComboBox<Generator>(m_soul.m_generators);
		m_generatorDrop.addActionListener(new  ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				m_soul.setGenerator(m_generatorDrop.getSelectedIndex());  }
		});
		m_generatorPanel.add(m_generatorDrop);
		
		m_generateButton = new JButton("Generate");
		m_generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Generator gen = m_soul.m_generators[m_soul.m_currentGenerator];
				Sequencer seq = ANCESTOR(Effigy.class).m_sequencerList.getSelectedSequencer();
				gen.generateNotes(seq);  }
		});
		m_generatorPanel.add(m_generateButton, "wrap");	
	}
	
	
	public void updateGeneratorPanel() {
		if(m_currentGenPanel != null) {
			m_generatorPanel.remove(m_currentGenPanel);  }
		if(m_soul.m_generators[m_soul.m_currentGenerator].m_gui != null) {
			m_currentGenPanel = m_soul.m_generators[m_soul.m_currentGenerator].m_gui.getComponent();  
			m_generatorPanel.add(m_currentGenPanel, "spanx, growy");
		}	}

	
	
	public void updateEditPanel() {
		int t_mode = m_soul.m_mode;
		m_component.removeAll();  
		
		if(t_mode == EditModeManager.NOTE_MODE) {
			m_component.add(m_notePanel);
			m_component.add(m_soul.getCurrentSequencer().m_gui.getComponent(), "growx, growy");  }
		else if(t_mode == EditModeManager.CLIP_MODE) {
			m_clipPanel.updateToCurrentClip();
			m_component.add(m_clipPanel);  
			m_component.add(m_soul.getCurrentSequencer().m_gui.getComponent(), "growx, growy");  }
		else if(t_mode == EditModeManager.GEN_MODE) {
			m_component.add(m_generatorPanel);
			m_component.add(m_soul.getCurrentSequencer().m_gui.getComponent(), "growx, growy");  }
		else if(t_mode == EditModeManager.SCENE_MODE) {
			m_component.add(m_scenePanel);
			m_component.add(m_soul.m_scene.m_gui.getComponent(), "growx, growy");  }
		
		
		if(lastMode == EditModeManager.SCENE_MODE && t_mode != EditModeManager.SCENE_MODE) {
			m_soul.m_scene.stop();
			m_soul.getCurrentSequencer().reset();  }
		else if(t_mode == EditModeManager.SCENE_MODE && lastMode != EditModeManager.SCENE_MODE) {
			m_soul.m_scene.reset();
			m_soul.getCurrentSequencer().stop();  }
		
		lastMode = t_mode;
		
		m_component.repaint();
	}



	
	
	
	public class ClipPanel extends JPanel {
		private static final long serialVersionUID = -5798609531206780227L;
		
		public JTextField m_nameField;
		public JReadoutKnob m_tempoKnob;
		public JFractionInput m_gridSplits;
		public JNumberField m_sizeInput;
		public JCheckBox m_showFX;
		
		public ClipPanel() {
			super();
			
			this.setLayout(new MigLayout("", "[center]"));
			this.setBackground(Effigy.COLOR_1);
			
			JLabel nameLabel = new JLabel("Name:");
			nameLabel.setForeground(Effigy.COLOR_5);
			
			m_nameField = new JTextField();
			m_nameField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m_soul.getCurrentSequencer().m_name = m_nameField.getText();  
					ANCESTOR(Effigy.class).m_sequencerList.m_gui.updateClipList();  }
			});
			m_nameField.setForeground(Effigy.COLOR_4);
			m_nameField.setBackground(Effigy.COLOR_3);
			
			
			m_gridSplits = new JFractionInput("Grid", 1.0, 4.0);
			m_gridSplits.addActionlistener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m_soul.getCurrentSequencer().setFocus(m_gridSplits.getValue());  }
			});
			m_gridSplits.setOpaque(false);
			m_gridSplits.setLabelForeground(Effigy.COLOR_5);
			
			JLabel sizeLabel = new JLabel("Size:");
			sizeLabel.setForeground(Effigy.COLOR_5);
			
			m_sizeInput = new JNumberField(4, 1, 128);
			m_sizeInput.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m_soul.getCurrentSequencer().setSize(m_sizeInput.getValue()); 
				}
			});
			m_sizeInput.setForeground(Effigy.COLOR_4);
			m_sizeInput.setBackground(Effigy.COLOR_3);

			m_showFX = new JCheckBox("Show FX");
			m_showFX.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(m_showFX.isSelected()) {
						m_soul.getCurrentSequencer().m_gui.m_lens.bounds.setHeight(18);  }
					else {
						m_soul.getCurrentSequencer().m_gui.m_lens.bounds.setHeight(12);  }
				}
			});
			m_showFX.setForeground(Effigy.COLOR_5);
			
			this.add(nameLabel, "alignx left");
			this.add(m_nameField, "wrap");
			this.add(sizeLabel, "alignx left");
			this.add(m_sizeInput, "wrap");
			this.add(m_showFX);
			this.add(m_gridSplits, "wrap");
			
			
			updateToCurrentClip();
		}

		public void updateToCurrentClip() {
			m_nameField.setText(m_soul.getCurrentSequencer().m_name);
			m_sizeInput.setValue(m_soul.getCurrentSequencer().m_size);
		}
	}


	
	
	public class ScenePanel extends JPanel {
		private static final long serialVersionUID = 4444936060769448160L;
		
		public JReadoutKnob m_tempoKnob;
		public JFractionInput m_gridSplits;
		public JNumberField m_sizeInput;
		
		public ScenePanel(){
			super();
			
			this.setLayout(new MigLayout("", "[center]"));
			this.setBackground(Effigy.COLOR_1);

			m_tempoKnob = new JReadoutKnob("Tempo", 110, 40, 300);
			m_tempoKnob.addDoubleListener(new DoubleListener() {
				@Override
				public void numericUpdate(double i_num) {
					ANCESTOR(Effigy.class).setTempo(i_num);
//					m_soul.getCurrentSequencer().setTempo(i_num);  
			}	});
			m_tempoKnob.allowDecimal(false);
			
			m_gridSplits = new JFractionInput("Grid", 1.0, 4.0);
			m_gridSplits.addActionlistener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m_soul.m_scene.setFocus(m_gridSplits.getValue());  }
			});
			
			JLabel sizeLabel = new JLabel("Size");
			sizeLabel.setForeground(Effigy.COLOR_5);
			
			m_sizeInput = new JNumberField(4, 1, 128);
			m_sizeInput.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					m_soul.m_scene.setSize(m_sizeInput.getValue()); 
				}
			});
//			m_sizeInput = new JFractionInput("Size", 4.0, 1.0);
//			m_sizeInput.addActionlistener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					m_soul.m_scene.setSize(m_sizeInput.getValue());  }
//			});

			this.add(m_gridSplits, "wrap");
			this.add(m_tempoKnob, "wrap");
			this.add(sizeLabel, "wrap");
			this.add(m_sizeInput, "wrap");
			
			
		}
	}



	

}

