package apace.model;

import apace.PACGame.CURSOR_MODE;
import apace.model.script.ScriptedSequence;

public interface Interactable {
	
	public ScriptedSequence examine();
	
	public ScriptedSequence talkTo();
	
	public ScriptedSequence use();
	
	public ScriptedSequence useItem(Item i);
	
	public ScriptedSequence fire(CURSOR_MODE cursorMode, Item itemUsed);

}
