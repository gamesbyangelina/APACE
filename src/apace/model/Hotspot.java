package apace.model;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import apace.PACGame.CURSOR_MODE;
import apace.model.script.ScriptedSequence;

public class Hotspot implements Interactable{
	
	public Rectangle2D area;
	public boolean enabled = true;
	private String name;
	
	public boolean walkTo = false;
	public int walktox = 0;
	public int walktoy = 0;
	
	private ScriptedSequence examine;
	private ScriptedSequence use;
	private ScriptedSequence talkTo;
	
	private HashMap<String, ScriptedSequence> itemUseEffect = new HashMap<String, ScriptedSequence>(0);
	private ScriptedSequence defaultUseItem = new ScriptedSequence();
	
	public Hotspot(String name, int x, int y, int x2, int y2){
		this.area = new Rectangle2D.Double(x,y,x2,y2);
		this.name = name;
	}
	
	public Hotspot(String name, int x, int y, int x2, int y2, int wx, int wy){
		this.area = new Rectangle2D.Double(x,y,x2,y2);
		this.name = name;
		this.walkTo = true;
		this.walktox = wx;
		this.walktoy = wy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled(){
		return enabled;
	}
	
	public ScriptedSequence examine(){
		return examine;
	}
	
	public void setExamine(ScriptedSequence s){
		this.examine = s;
	}
	
	public ScriptedSequence talkTo(){
		return talkTo;
	}
	
	public ScriptedSequence use(){
		return use;
	}
	
	public void setUse(ScriptedSequence s){
		this.use = s;
	}
	
	public ScriptedSequence useItem(Item i){
		if(itemUseEffect.get(i.name) == null)
			return defaultUseItem;
		else
			return itemUseEffect.get(i.name);
	}
	
	public void addItemEffect(Item i, ScriptedSequence useItem) {
		itemUseEffect.put(i.name, useItem);
	}

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
	
	public void setWalkTo(int x, int y){
		this.walktox = x;
		this.walktoy = y;
		
	}

}
