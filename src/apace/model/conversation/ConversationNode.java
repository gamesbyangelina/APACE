package apace.model.conversation;

import java.util.HashMap;
import java.util.LinkedList;

import apace.model.script.ScriptedSequence;

/*
 * Strictly speaking, this class represents a conversation 'tree', but only in the strictest sense. 
 * It represents a piece of dialogue followed by several options, which themselves lead to other conversation trees.
 */
public class ConversationNode {
	
	public int conversationId;
	
	public LinkedList<String> options;
	public HashMap<String, ScriptedSequence> outcomes;
	public LinkedList<Boolean> enabled;
	
	protected ConversationNode(int nodeId){
		this.conversationId = nodeId;
		options = new LinkedList<String>();
		outcomes = new HashMap<String, ScriptedSequence>();
		enabled = new LinkedList<Boolean>();
	}
	
	public void addOption(String option, ScriptedSequence outcome){
		options.add(option);
		outcomes.put(option, outcome);
		enabled.add(true);
	}
	
	public void addOption(String option, ScriptedSequence outcome, Boolean en){
		options.add(option);
		outcomes.put(option, outcome);
		enabled.add(en);
	}
	
	public void disableOption(String option){
		for(int i=0; i<options.size(); i++){
			if(options.get(i).equalsIgnoreCase(option)){
				enabled.set(i, false);
			}
		}
	}

	public int numEnabled(){
		int i=0;
		for(Boolean e : enabled){
			if(e) i++;
		}
		return i;
	}

	public LinkedList<String> enabled(){
		LinkedList<String> res = new LinkedList<String>();
		for(int i=0; i<enabled.size() ; i++){
			if(enabled.get(i))
				res.add(options.get(i));
		}
		return res;
	}

	public ScriptedSequence getOutcome(String string){
		return outcomes.get(string);
	}

	public void enableOption(String option){
		for(int i=0; i<options.size(); i++){
			if(options.get(i).equalsIgnoreCase(option)){
				enabled.set(i, true);
			}
		}
	}

}
