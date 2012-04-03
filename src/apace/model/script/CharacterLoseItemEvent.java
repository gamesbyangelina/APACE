package apace.model.script;

import apace.PACGame;
import apace.PACGame.CURSOR_MODE;

/**
 * The player loses the active inventory item. If there is no item, nothing happens.
 * @author michaelcook
 */
public class CharacterLoseItemEvent extends AbstractScriptEvent {
	
	@Override
	public void onExecute(PACGame game) {
		if(game.isUsingItem()){
			game.setCursorMode(CURSOR_MODE.USE);
			game.getCurrentPC().removeItem(game.itemInUse);
			game.setUsingItem(false);
		}
	}

}
