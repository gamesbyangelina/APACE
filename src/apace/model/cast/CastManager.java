package apace.model.cast;

import java.util.HashMap;

public class CastManager {
	
	private static HashMap<String, NPC> cast = new HashMap<String, NPC>();
	
	public static void init(){

	}
	
	public static NPC newNPC(String code, String name, int x, int y, int ax, int ay){
		NPC n = new NPC(code, name, x, y, ax, ay);
		cast.put(code, n);
		return n;
	}

	public static Character getNPC(String id) {
		return cast.get(id);
	}

	public static int getId(NPC npc) {
		for(int i=0; i<cast.size(); i++){
			if(npc.name.equalsIgnoreCase(cast.get(i).name)){
				return i;
			}
		}
		return -1;
	}

}
