package apace.model.script;

import apace.PACGame;
import apace.model.Item;

/**
 * Runs <code>scriptToRun</code> iff the currently active item is the same as <code>targetItem</code>.
 * TODO: Change to be truly conditional (two branches).
 * @author michaelcook
 */
public class IfItemThenEvent extends AbstractScriptEvent {
	
	private Item targetItem;
	private ScriptedSequence scriptToRun;

	public IfItemThenEvent(Item i, ScriptedSequence s){
		this.targetItem = i;
		this.scriptToRun = s;
	}

	@Override
	public void onExecute(PACGame game) {
		if(game.itemInUse != null && game.itemInUse.name.equalsIgnoreCase(targetItem.name)){
			game.currentScript = this.scriptToRun;
		}
		else{
			return;
		}
	}

}
