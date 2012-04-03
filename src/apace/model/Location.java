package apace.model;

import java.util.LinkedList;

import org.newdawn.slick.Image;

import apace.model.cast.NPC;
import apace.model.world.WalkableArea;

public class Location {
	
	public String locationName;
	public Image bgImage;
	
	public LinkedList<NPC> npcs;
	public LinkedList<Hotspot> hotspots;
	public LinkedList<Item> items;
	public WalkableArea walkable;
	
	public Location(String name, Image img){
		locationName = name;
		bgImage = img;
		npcs = new LinkedList<NPC>();
		hotspots = new LinkedList<Hotspot>();
		items = new LinkedList<Item>();
	}
	
	public Location(String name, Image img, WalkableArea walkable){
		locationName = name;
		bgImage = img;
		this.walkable = walkable;
		npcs = new LinkedList<NPC>();
		hotspots = new LinkedList<Hotspot>();
		items = new LinkedList<Item>();
	}
	
	public void addWalkable(WalkableArea walkable){
		this.walkable = walkable;
	}

	//Raytrace intersection from the target point to outside the screen.
	public boolean pointIsWalkable(int mouseX, int mouseY) {
		int normx = mouseX/walkable.resolution;
		int normy = mouseY/walkable.resolution;
		return !walkable.blocked(null, normx, normy);
	}

}
