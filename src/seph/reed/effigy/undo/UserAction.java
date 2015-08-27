package seph.reed.effigy.undo;

public abstract class UserAction {
//	public boolean current = true;
	
	public abstract boolean redo();
	public abstract boolean undo();
}

