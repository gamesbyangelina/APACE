package apace.model.script;

import java.util.LinkedList;

import apace.PACGame;

public class ScriptedSequence {
	
	int currentEvent = 0;
	private LinkedList<AbstractScriptEvent> sequence;
	
	public ScriptedSequence(){
		sequence = new LinkedList<AbstractScriptEvent>();
	}
	
	public ScriptedSequence(AbstractScriptEvent... events){
		sequence = new LinkedList<AbstractScriptEvent>();
		for(AbstractScriptEvent event : events){
			sequence.add(event);
		}
	}
	
	public ScriptedSequence add(AbstractScriptEvent event){
		sequence.add(event);
		return this;
	}
	
	public void addToStart(AbstractScriptEvent event){
		sequence.add(0, event);
	}

	public AbstractScriptEvent getNextEvent() {
		return sequence.get(currentEvent);
	}
	
	public AbstractScriptEvent getHeadEvent(){
		return sequence.get(0);
	}
	
	public void popEvent(){
		currentEvent++;
	}

	public boolean done() {
		return currentEvent >= sequence.size();
	}
	
	public void exit(){
		currentEvent = 0;
	}

}
