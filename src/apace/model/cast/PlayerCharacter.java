package apace.model.cast;

import java.util.Iterator;
import java.util.LinkedList;

import apace.model.Item;

public class PlayerCharacter extends Character{

	LinkedList<Item> inventory = new LinkedList<Item>();
	
	public PlayerCharacter(int x, int y, int ax, int ay){
		super(x,y,ax,ay);
	}

	public void addItem(Item item) {
		inventory.add(item);
	}
	
	public Iterator<Item> getInventory(){
		return inventory.iterator();
	}

	public void removeItem(Item target) {
		for(int i=0; i<inventory.size(); i++){
			Item item = inventory.get(i);
			if(item.name.equalsIgnoreCase(target.name)){
				inventory.remove(item);
				return;
			}
		}
	}

}
