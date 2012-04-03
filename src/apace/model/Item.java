package apace.model;

import java.util.HashMap;

import org.newdawn.slick.Image;

import apace.PACGame.CURSOR_MODE;
import apace.model.script.CharacterSayAtEvent;
import apace.model.script.ScriptedSequence;

public class Item implements Interactable{
	
	public String name;
	public String description;
	public Image icon;
	private ScriptedSequence examine;
	
	private int x;
	private int y;
	
	private HashMap<String, ScriptedSequence> itemUseEffect = new HashMap<String, ScriptedSequence>();
	private ScriptedSequence defaultUseItem = new ScriptedSequence();
	
	public Item(String name, String description, Image icon, int x, int y){
		this.name = name;
		this.x = x;
		this.y = y;
		this.description = description;
		ScriptedSequence sayDescription = new ScriptedSequence();
		sayDescription.add(new CharacterSayAtEvent(description, "player", 320, 420));
		this.examine = sayDescription;
		this.icon = icon;
	}		
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}

	@Override
	public ScriptedSequence examine() {
		return examine;
	}

	@Override
	public ScriptedSequence talkTo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptedSequence use() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptedSequence useItem(Item i) {
		if(itemUseEffect.get(i.name) == null)
			return defaultUseItem;
		else{
			return itemUseEffect.get(i.name);
		}
	}
	
	public void addItemEffect(Item i, ScriptedSequence useItem) {
		itemUseEffect.put(i.name, useItem);
	}

	@Override
	public ScriptedSequence fire(CURSOR_MODE cursorMode, Item itemUsed) {
		ScriptedSequence res = null;
		switch(cursorMode){
		case EXAMINE: res = examine(); break;
		case USE: res = use(); break;
		case TALK: res = talkTo(); break;
		case ITEM: res = useItem(itemUsed); break;
		}
		return res;
	}

}
