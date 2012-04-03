package apace.model.script;

import apace.PACGame;

/**
 * Sends the player character to another room. Does not change the player's screen co-ordinates.
 * @author michaelcook
 * @see PlayerGoToRoomAtEvent
 */
public class PlayerGoToRoomEvent extends AbstractScriptEvent {

	private int roomCode;

	public PlayerGoToRoomEvent(int roomCode){
		this.roomCode = roomCode;
		//Gotoroom events don't block because they don't have a time delay associated.
		this.blocking = false;
	}
	
	@Override
	public void onExecute(PACGame game) {
		game.currentLocation = game.locationList.get(roomCode);
	}

}
