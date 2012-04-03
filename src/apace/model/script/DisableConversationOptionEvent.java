package apace.model.script;

import apace.PACGame;
import apace.model.conversation.ConversationManager;

public class DisableConversationOptionEvent extends AbstractScriptEvent {

	private int node;
	private String option;

	public DisableConversationOptionEvent(int nodeId, String option){
		this.node = nodeId;
		this.option = option;
	}
	
	@Override
	public void onExecute(PACGame game){
		if(ConversationManager.getConversation(node) == null){
			throw new IllegalArgumentException("Tried to disable a conversation option on a node that didn't exist.");
		}
		if(!ConversationManager.getConversation(node).options.contains(option)){
			throw new IllegalArgumentException("Tried to disable a conversation option that did not exist on the target node.");
		}
		
		ConversationManager.getConversation(node).disableOption(option);
	}

}
