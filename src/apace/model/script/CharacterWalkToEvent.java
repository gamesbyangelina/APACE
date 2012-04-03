package apace.model.script;

import java.util.LinkedList;

import apace.PACGame;
import apace.model.Point;

/**
 * Moves the character with id <code>charid</code> to the x/y co-ordinates.
 * @author michaelcook
 *
 */
public class CharacterWalkToEvent extends AbstractScriptEvent {
	
	private int x;
	private int y;
	
	public CharacterWalkToEvent(int x, int y, int charid){
		this.x = x;
		this.y = y;
		this.blocking = true;
	}

	@Override
	public void onExecute(PACGame game) {
		game.moving = true;
		game.walkingPath = new LinkedList<Point>();
		game.walkingPath.add(new Point(x,y));
		game.animDelta = 0;
		game.getCurrentPC().currentAnim = game.getCurrentPC().walkingAnim;
	}

}
