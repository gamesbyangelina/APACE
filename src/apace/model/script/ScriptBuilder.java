package apace.model.script;

public class ScriptBuilder {
	
	ScriptedSequence seq;
	
	public ScriptBuilder(){
		seq = new ScriptedSequence();
	}
	
	public ScriptBuilder say(String s){
		seq.add(new CharacterSayEvent(s));
		return this;
	}
	
	public ScriptBuilder say(String s, String id){
		seq.add(new CharacterSayEvent(s, id));
		return this;
	}
	
	public ScriptBuilder sayAt(String s, String id, int x, int y){
		seq.add(new CharacterSayAtEvent(s, id, x, y));
		return this;
	}
	
	public ScriptBuilder startConversation(int id){
		seq.add(new ConversationStartEvent(id));
		return this;
	}

	public ScriptedSequence buildScript(){
		return seq;
	}

	public ScriptBuilder disableOption(int i, String option){
		seq.add(new DisableConversationOptionEvent(i, option));
		return this;
	}

	public ScriptBuilder enableOption(int i, String string){
		seq.add(new EnableConversationOptionEvent(i, string));
		return this;
	}

	public ScriptBuilder enterRoomAt(int i, int x, int y){
		seq.add(new PlayerGoToRoomAtEvent(i, x, y));
		return this;
	}
	
}
