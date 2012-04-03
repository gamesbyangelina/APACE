package apace.model.script;

import apace.PACGame;
import apace.model.SpeechLine;

/**
 * The character with id <code>speakerId</code> says <code>message</code>. This will use the default
 * positioning (i.e. above the character's head, centered).
 * @author michaelcook
 */
public class CharacterSayEvent extends AbstractScriptEvent {
	
	public String message;
	public String speakerId;

	public CharacterSayEvent(String message, String speakerId){
		this.message = message;
		this.speakerId = speakerId;
		this.blocking = true;
	}
	
	public CharacterSayEvent(String message){
		this.message = message;
		this.speakerId = "player";
		this.blocking = true;
	}

	@Override
	public void onExecute(PACGame game) {
		game.speechQueue.add(new SpeechLine(message, speakerId, message.length() * 80));
	}

}
