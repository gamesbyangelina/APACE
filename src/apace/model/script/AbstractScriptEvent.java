package apace.model.script;

import apace.PACGame;

public abstract class AbstractScriptEvent {
	
	//If a script event is blocking, PACE will wait for the event
	//to complete before running the next event in the sequence.
	//By default, don't block as events do not have delays associated
	public boolean blocking = false;

	/*
	 * Types of script event include:
	 * 	+ Display a line of dialogue.
	 * 	+ Start a conversation.
	 * 	+ Give the player/remove from the player an inventory item.
	 *  + Remove an object from the game world.
	 *  + Enable/disable a hotspot.
	 *  + Go to another room.
	 */
	public abstract void onExecute(PACGame game);

}
