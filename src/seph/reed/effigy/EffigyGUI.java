package seph.reed.effigy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import scott.thumbz.jaromin.Animations.GrowShrinkRainbowCircles;
import scott.thumbz.jaromin.Components.JFancyPanel;
import scott.thumbz.jaromin.GUIManager.GUIManager;
import scott.thumbz.jaromin.OOPject.OOmject;
import seph.reed.effigy.sequenceEditor.EditModeManager;

public class EffigyGUI 
extends GUIManager<Effigy, JFrame> {

//	public MigLayout m_layout;
	protected GrowShrinkRainbowCircles m_circlesAnimation;
	public EffigyMenuBar m_menu;
	public JComboBox<EditMode> m_editChooser;
	public EditMode[] m_editPanes;
	public JFancyPanel m_content;
	public EditModeTabs m_editModeTabs;
	
	public EffigyGUI(OOmject i_holder, Effigy i_soul) {
		super(i_holder, i_soul);  }
	

	@Override
	protected void createComponent() {
		
		m_menu = new EffigyMenuBar(this, m_soul);
		
		m_editPanes = new EditMode[3];
		m_editPanes[0] = new EditMode("Clip", EditModeManager.CLIP_MODE);
		m_editPanes[1] = new EditMode("Scene", EditModeManager.SCENE_MODE);
		m_editPanes[2] = new EditMode("Generator", EditModeManager.GEN_MODE);
		
		m_editModeTabs = new EditModeTabs();
		
		m_editChooser = new JComboBox<EditMode>(m_editPanes);
		m_editChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int mode = m_editPanes[m_editChooser.getSelectedIndex()].m_mode;
				m_soul.m_editorManager.setMode(mode);}
		});
		
		m_content = new JFancyPanel();
		m_content.setLayout(new MigLayout("fill, inset 0, gap 0!", "[left][center]", "[shrink, fill][grow, fill]"));
		m_content.setBackground(new Color(5, 5, 5));
		m_content.setSize(800,800);
		m_content.setOpaque(true);
		
//		m_circlesAnimation = new GrowShrinkRainbowCircles(this);
//		m_circlesAnimation.start();
//		m_content.addDrawing(m_circlesAnimation);
		
		GrowShrinkRainbowCircles m_animation = new GrowShrinkRainbowCircles(null);
		m_animation.start();
		m_animation.setOpacity(45);
		m_animation.setDesaturation(220);
		m_content.setDrawing(m_animation);
		
		
		
		m_component = new JFrame();
		m_component.add(m_content);
		
		
		JPanel leftBar = new JPanel();
		leftBar.setBackground(new Color(6,6,6,90));
		leftBar.setLayout(new MigLayout("fill, gap 0, inset 0", "[]", "[]push[]"));
		leftBar.add(m_soul.m_sequencerList.m_gui.getComponent(), "aligny top, growy, w 150!, wrap");
//		JPanel editChooserPanel = new JPanel();
//		editChooserPanel.setBackground(Effigy.COLOR_1);
//		
//		editChooserPanel.add(m_editModeTabs);
		leftBar.add(m_editModeTabs, "aligny bottom, x 15");
		
		m_content.add(leftBar, "growy");
		m_content.add(m_soul.m_hands.m_gui.getComponent(), "width 800!, height 495!, wrap");
		m_content.add(m_soul.m_editorManager.m_gui.getComponent(), "growy, growx, spanx");
		
		m_component.setJMenuBar(m_menu.getComponent());
		m_component.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		addInputMap();
	}

	
	
	
	public class EditModeTabs extends JPanel {
		private static final long serialVersionUID = 8445677278436596739L;

		public EditModeTabs() {
			ButtonGroup bG = new ButtonGroup();
			for(int i = 0; i < m_editPanes.length; i++) {
				TabButton addMe = new TabButton(m_editPanes[i].m_name);
				final int mode = m_editPanes[i].m_mode;
				addMe.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						m_soul.m_editorManager.setMode(mode);  }
				});
				bG.add(addMe);
				this.add(addMe);
			}
		    this.setOpaque(false);
		    this.setLayout( new MigLayout("gap 0, inset 0"));
		}
	}
	
	public class TabButton extends JRadioButton{
		private static final long serialVersionUID = -3950843973600241042L;

		public TabButton(String m_name) {
			super(m_name);  
			
			this.setOpaque(true);
			this.setBackground(Effigy.COLOR_6);
			this.setForeground(Effigy.COLOR_5);  
//			this.setFont(font);

			this.setIcon(new EmptyIcon());
			
			final TabButton fuckHax = this;
			this.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if(fuckHax.isSelected()) {
						fuckHax.setBackground(Effigy.COLOR_1);  }
					else {
						fuckHax.setBackground(Effigy.COLOR_6);  }
				}
			});
		}
		
		@Override
		public void setSelected(boolean isOn) {
			super.setSelected(isOn);
			if(isOn) {
				this.setBackground(Effigy.COLOR_1);  }
			else {
				this.setBackground(Effigy.COLOR_2);  }
		}

//		@Override
//		public void paint(Graphics g){
////			super.paint(g);
//			g.drawString(this.getText(), this.getX(), this.getY());  }
	 }

//
//	private void addInputMap() {
//		m_component.getIn
//	}


//	public Clip getCurrentClip() {
//		return m_soul.m_editorManager;
//	}

	
	public final class EmptyIcon implements Icon {

		  private int width;
		  private int height;
		  
		  public EmptyIcon() {
		    this(0, 0);
		  }
		  
		  public EmptyIcon(int width, int height) {
		    this.width = width;
		    this.height = height;
		  }

		  public int getIconHeight() {
		    return height;
		  }

		  public int getIconWidth() {
		    return width;
		  }

		  public void paintIcon(Component c, Graphics g, int x, int y) {
		  }

	}
	
	public class EditMode {
		public String m_name;
		public int m_mode;
	
		public EditMode(String i_name, int i_mode) {
			m_name = i_name;
			m_mode = i_mode;  }
		
		public String toString() {
			return m_name;  }
	}

}
