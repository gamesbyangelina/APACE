package apace.model.cast;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import apace.PACGame.CURSOR_MODE;
import apace.model.Interactable;
import apace.model.Item;
import apace.model.script.ScriptedSequence;

public class NPC extends Character implements Interactable{

	public ScriptedSequence examine;
	public ScriptedSequence use;
	public ScriptedSequence talkTo;
	public ScriptedSequence useItem;
	public String name;
	public String code;
	
	protected NPC(String code, String name, int x, int y, int ax, int ay) {
		super(x, y, ax, ay);
		this.name = name;
		this.code = code;
	}

	@Override
	public ScriptedSequence examine() {
		return examine;
	}

	@Override
	public ScriptedSequence talkTo() {
		return talkTo;
	}

	@Override
	public ScriptedSequence use() {
		return use;
	}

	@Override
	public ScriptedSequence useItem(Item i) {
		// TODO Auto-generated method stub
		return new ScriptedSequence();
	}
	
	public Area getArea(){
		int height = currentAnim.get(DIR.DOWN).getHeight();
		int width = currentAnim.get(DIR.DOWN).getWidth();
		Rectangle2D r = new Rectangle2D.Double(getX(),getY(),width,height);
		return new Area(r);
	}

	public ScriptedSequence fire(CURSOR_MODE cursorMode, Item itemUsed) {
		ScriptedSequence res = null;
		switch(cursorMode){
		case EXAMINE: res = examine(); break;
		case USE: res = use(); break;
		case TALK: res = talkTo(); break;
		}
		return res;
	}

}
