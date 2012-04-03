package apace.model.script;

import apace.PACGame;
import apace.model.SpeechLine;

/**
 * The character with id <code>speakerId</code> says the <code>message</code> at the specified x/y co-ordinates.
 * @author michaelcook
 */
public class CharacterSayAtEvent extends AbstractScriptEvent {

	public String message;
	public String speakerId;
	
	public int x;
	public int y;

	public CharacterSayAtEvent(String message, String speakerId, int x, int y){
		this.message = message;
		this.speakerId = speakerId;
		this.blocking = true;
		this.x = x; this.y = y;
	}
	
	@Override
	public void onExecute(PACGame game) {
		game.speechQueue.add(new SpeechLine(message, speakerId, 1000, x, y));
	}

}
