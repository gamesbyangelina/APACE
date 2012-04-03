package apace.model.script;

import apace.PACGame;
import apace.model.conversation.ConversationManager;

/**
 * Starts the conversation labelled with <code>id</code>.
 * @author michaelcook
 */
public class ConversationStartEvent extends AbstractScriptEvent {
	
	private int id;

	public ConversationStartEvent(int id){
		this.id = id;
	}

	@Override
	public void onExecute(PACGame game) {
		game.conversing = true;
		game.currentConversation = ConversationManager.getConversation(id);
	}

}
