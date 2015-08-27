package seph.reed.effigy;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.Sequencer;

public class EffigyMenuBar 
extends GUIManager<Effigy, JMenuBar>{

	public JMenu m_sequencerMenu;
	public JMenu m_projectMenu;
	public JMenu m_editMenu;
	public JMenu m_autoPlayMenu;
	public JMenu m_Menu;
	
	public EffigyMenuBar(OOmject i_holder, Effigy i_soul) {
		super(i_holder, i_soul);  }

	
	@Override
	protected void createComponent() {
		m_component = new JMenuBar();
		
		createSequencerMenu();
		createFileMenu();
		createEditMenu();
		createAutoPlayMenu();
		
		m_component.add(m_projectMenu);
		m_component.add(m_sequencerMenu);
		m_component.add(m_editMenu);
		m_component.add(m_autoPlayMenu);
	}

	
	
	
	
	private void createAutoPlayMenu() {
		m_autoPlayMenu = new JMenu("Autoplay");
		//
		EasyItem addMe = new EasyItem("Play Randomly") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				Effigy.AUTOPLAY.playRandom();  }
		};
		addMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m_autoPlayMenu.add(addMe);
		
		
		addMe = new EasyItem("Stop") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				Effigy.AUTOPLAY.stop();  }
		};
		m_autoPlayMenu.add(addMe);
		
	}


	private void createEditMenu() {
		m_editMenu = new JMenu("Edit");
		//
		EasyItem addMe = new EasyItem("Undo") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				Effigy.HISTORY.undo();  }
		};
		addMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m_editMenu.add(addMe);
		
		
		addMe = new EasyItem("Redo") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				Effigy.HISTORY.redo();  }
		};
		addMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		m_editMenu.add(addMe);
	}


	private void createFileMenu() {
		m_projectMenu = new JMenu("Project");
			//
		m_projectMenu.add(new EasyItem("Open Project") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				JFileChooser openFile = new JFileChooser();
				openFile.showOpenDialog(null);
					//
		         if(openFile.getSelectedFile() != null)  {
		        	String path = openFile.getSelectedFile().getAbsolutePath();
		         	EffigyLoadSave.loadProject(path, m_soul);  }
			}
		});
		
		m_projectMenu.add(new EasyItem("Save Project As") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				
				JFileChooser saveFile = new JFileChooser();
				saveFile.showSaveDialog(null);
		         	//
		        if(saveFile.getSelectedFile() != null)  {
		        	 EffigyLoadSave.saveProjectAs(saveFile.getSelectedFile().getAbsolutePath(), m_soul);  }
			}
		});
		
		m_projectMenu.add(new EasyItem("Export Project As Midi") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				
				JFileChooser saveFile = new JFileChooser();
				saveFile.showSaveDialog(null);
		         	//
		        if(saveFile.getSelectedFile() != null)  {
		        	 EffigyLoadSave.saveProjectAsMidi(saveFile.getSelectedFile(), m_soul);  }
			}
		});
	}

	
	
	
	//////

	public void createSequencerMenu() {
		m_sequencerMenu = new JMenu("Sequencer");
			//
		m_sequencerMenu.add(new EasyItem("Load Midi to Current Sequencer") {
			private static final long serialVersionUID = -4271455459548111876L;
			@Override
			protected void onClick() {
				JFileChooser openFile = new JFileChooser();
				openFile.showOpenDialog(null);
					//
	         if(openFile.getSelectedFile() != null)  {
	        	String path = openFile.getSelectedFile().getAbsolutePath();
	        	Sequencer seq = m_soul.m_sequencerList.getSelectedSequencer(); 
	         	EffigyLoadSave.loadMidiFromFullPath(path, seq);  }
			}
		});
		
		m_sequencerMenu.add(new EasyItem("Save Current Sequence As") {
			private static final long serialVersionUID = -4271455459548111876L;
			
			@Override
			protected void onClick() {
				JFileChooser saveFile = new JFileChooser();
				saveFile.showSaveDialog(null);
		         	//
		         if(saveFile.getSelectedFile() != null)  {
		        	 Sequencer seq = m_soul.m_sequencerList.getSelectedSequencer();
		         	 EffigyLoadSave.saveMidiSequenceAs(seq, saveFile.getSelectedFile());  }
			}
		});
		
	}





	protected abstract class EasyItem extends JMenuItem  {
			//
		private static final long serialVersionUID = 3076152631289826481L;
		
		public EasyItem(String text)  {
				//
			super(text);
			
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					onClick();  }
			});
		}
		
		protected abstract void onClick();
	}
}
