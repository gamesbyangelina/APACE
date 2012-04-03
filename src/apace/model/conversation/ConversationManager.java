package apace.model.conversation;

import java.util.LinkedList;

public class ConversationManager {
	
	private static int nodeId = 0;
	private static LinkedList<ConversationNode> nodes = new LinkedList<ConversationNode>();
	
	public static ConversationNode newNode(){
		ConversationNode n = new ConversationNode(nodeId);
		nodes.add(n);
		nodeId++;
		return n;
	}

	public static ConversationNode getConversation(int id) {
		if(id > nodes.size() || id < 0)
			return null;
		return nodes.get(id);
	}

}
