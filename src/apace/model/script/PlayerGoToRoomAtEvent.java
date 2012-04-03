package apace.model.script;

import apace.PACGame;
import apace.model.cast.PlayerCharacter;

/**
 * Sends the player character to another room at the indicated x/y co-ordinates.
 * @author michaelcook
 * @see PlayerGoToRoomEvent
 */
public class PlayerGoToRoomAtEvent extends AbstractScriptEvent {

	private int roomCode;
	private int x;
	private int y;

	public PlayerGoToRoomAtEvent(int roomCode, int x, int y){
		this.roomCode = roomCode;
		this.x = x;
		this.y = y;
		//Gotoroom events don't block because they don't have a time delay associated.
		this.blocking = false;
	}
	
	@Override
	public void onExecute(PACGame game) {
		game.currentLocation = game.locationList.get(roomCode);
		PlayerCharacter p = game.getCurrentPC();
		p.setX(x);
		p.setY(y);
	}

}
