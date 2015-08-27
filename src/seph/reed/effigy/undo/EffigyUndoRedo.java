package seph.reed.effigy.undo;

import java.util.ArrayList;

import scott.thumbz.jaromin.OOPject.OOmject;

public class EffigyUndoRedo 
extends OOmject{
	
	public ArrayList<UserAction> m_actions;
	public int currentIndex = -1;
	
	public EffigyUndoRedo(OOmject i_mother) {
		super(i_mother);  
		m_actions = new ArrayList<UserAction>();  }

	
	public void addAction(UserAction addMe)  {
		while(currentIndex != m_actions.size()-1) {
			m_actions.remove(m_actions.size()-1);  }
		m_actions.add(addMe);
		currentIndex++;
		
		sysOut(currentIndex+"");
	}
	
	


	public void undo() {
		if(currentIndex != -1) {
			m_actions.get(currentIndex).undo();
//			m_actions.get(currentIndex).current = false;
			currentIndex--;
		}
		sysOut(currentIndex+"");
	}
	
	
	public void redo() {
		if(currentIndex + 1 < m_actions.size()) {
			currentIndex++;
			m_actions.get(currentIndex).redo();
//			m_actions.get(currentIndex).current = true;
		}
		sysOut(currentIndex+"");
	}
	
	
	
	
	
	

}
