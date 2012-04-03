package apace.model.script;

import apace.PACGame;
import apace.model.Item;

/**
 * Adds an item to the player's inventory.
 * @author michaelcook
 */
public class PlayerGainItemEvent extends AbstractScriptEvent{

	Item item;
	
	public PlayerGainItemEvent(Item i){
		this.item = i;
	}
	
	@Override
	public void onExecute(PACGame game) {
		if(game.getCurrentPC() != null){
			game.getCurrentPC().addItem(item);
		}
	}

}
