package apace;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

import apace.PACGame.CURSOR_MODE;
import apace.model.Hotspot;
import apace.model.Item;
import apace.model.Location;
import apace.model.Point;
import apace.model.SpeechLine;
import apace.model.cast.CastManager;
import apace.model.cast.Character.DIR;
import apace.model.cast.NPC;
import apace.model.cast.PlayerCharacter;
import apace.model.conversation.ConversationNode;
import apace.model.script.AbstractScriptEvent;
import apace.model.script.CharacterWalkToEvent;
import apace.model.script.ScriptedSequence;
import apace.model.world.WalkableArea;
import apace.view.InventoryScreen;

/*
 * Issues
 * + Pathfinding seems to cross boundaries between blocked zones.
 * + CRASH - Pathfind to current location.
 * + Pathfinding can cause the player to go to the nearest tile corner when starting.
 */

public abstract class PACGame extends org.newdawn.slick.BasicGame{
	
	public static final boolean DEBUGMODE = false;
	public static enum DEBUG_SHOW_TYPE {SHOW_HOTSPOTS, SHOW_WALKABLE};
	public DEBUG_SHOW_TYPE DEBUG_SHOW = DEBUG_SHOW_TYPE.SHOW_HOTSPOTS;
	
	public enum CURSOR_MODE {USE, EXAMINE, TALK, ITEM};
	CURSOR_MODE cursorMode = CURSOR_MODE.USE;
	LinkedList<Image> cursors;
	
	public Location currentLocation = null;
	public HashMap<Integer, Location> locationList = new HashMap<Integer, Location>();
	
	public LinkedList<Point> walkingPath = new LinkedList<Point>();
	private PlayerCharacter pc = null;
	public boolean moving = false;
	public int animDelta = 0;
	
	Image mouseCursor;
	InventoryScreen is;
	
	public ScriptedSequence currentScript = null;
	public boolean scripting = false;
	
	public ConversationNode currentConversation = null;
	public boolean conversing = false;
	
	public LinkedList<SpeechLine> speechQueue = new LinkedList<SpeechLine>();
	private boolean showInventory = false;
	public boolean usingItem = false;
	public Item itemInUse;
	
	private boolean lookTriggersWalkTo = false;

	public abstract void setupGameData();

	public PACGame(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		//Useful for window-based games.
		container.setUpdateOnlyWhenVisible(true);
		//Ties mouse within the window.
		container.setMouseGrabbed(true);
		//Default cursors
		cursors = new LinkedList<Image>();
		cursors.add(new Image("resources/default/cursor_use.png"));
		cursors.add(new Image("resources/default/cursor_exam.png"));
		cursors.add(new Image("resources/default/cursor_talk.png"));
		is = InventoryScreen.defaultInventory();
		mouseCursor = cursors.get(0);
		//Cast management
		CastManager.init();
		//Call user-specified code to setup characters, locations, game content 
		setupGameData();
	}
	
	

	@Override
	public void render(GameContainer gc, Graphics gr) throws SlickException {
		//Draw the background
		gr.drawImage(currentLocation.bgImage, 0, 0);
		//Debug
		if(DEBUGMODE){
			gr.setColor(new Color(1.0f, 0.0f, 0.0f, 0.25f));
			if(DEBUG_SHOW == DEBUG_SHOW_TYPE.SHOW_HOTSPOTS){
				for(Hotspot h : currentLocation.hotspots){
					gr.fillRect((float) h.area.getX(), (float) h.area.getY(), (float) h.area.getWidth(), (float) h.area.getHeight());
				}
			}
			else if(DEBUG_SHOW == DEBUG_SHOW_TYPE.SHOW_WALKABLE){
				WalkableArea w = currentLocation.walkable;
				for(int i=0; i<w.underlying.length; i++){
					for(int j=0; j<w.underlying[0].length; j++){
						if(w.underlying[i][j])
							gr.setColor(new Color(1.0f, 0.0f, 0.0f, 0.25f));
						else
							gr.setColor(new Color(1.0f, 0.0f, 0.0f, 0.0f));
						gr.fillRect((float) j*w.resolution, (float) i*w.resolution, (float) w.resolution, (float) w.resolution);
					}
				}
			}
			gr.setColor(new Color(1.0f, 0.0f, 0.0f, 0.75f));
			if(walkingPath != null && walkingPath.size() > 0){
				for(int i=0; i<walkingPath.size(); i++){
					Point p = walkingPath.get(i);
					if(i < walkingPath.size()-1){
						Point p2 = walkingPath.get(i+1);
						gr.drawLine(p.x, p.y, p2.x, p2.y);
					}
					gr.fillOval(p.x, p.y, 10, 10);
				}
			}
		}
		gr.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		//Draw any items
		for(Item i : currentLocation.items){
			i.icon.draw(i.getX(), i.getY(), 32, 32);
		}
		//Draw any NPCs
		for(NPC npc : currentLocation.npcs){
			npc.currentAnim.get(npc.currentDirection).draw(npc.getX(), npc.getY(),32,32);
		}
		//Draw the PC
		pc.currentAnim.get(pc.currentDirection).draw(pc.getX(), pc.getY(),32,32);
		//If we're doing a conversation, draw all that up.
		if(conversing){
			gr.setColor(Color.black);
			LinkedList<String> ops = currentConversation.enabled();
			gr.fillRect(0, 470-(ops.size()*25), 640, (ops.size()*25)+10);
			gr.setColor(Color.white);
			
			for(int i=ops.size(); i>0; i--){
				int yval = 480-i*25;
				if(gc.getInput().getMouseY() > yval && gc.getInput().getMouseY() < yval+25){
					gr.setColor(Color.red);
				}
				gr.drawString((ops.size()-i+1)+". "+ops.get(ops.size()-i), 10, 480-i*25);
				gr.setColor(Color.white);
			}
			
		}
		//Ditto, inventory
		else if(showInventory){
			gr.drawImage(is.background, is.image_x, is.image_y);
			if(pc != null){
				Iterator<Item> items = pc.getInventory();
				int i = 0; int j = 0;
				while(items.hasNext()){
					gr.drawImage(items.next().icon, is.image_x+is.list_x + i*40, is.image_y+is.list_y+j*40);
					i++;
					if(i > is.iconsperline){
						j++;
						i=0;
					}
				}
			}
		}
		//Render any onscreen text
		for(SpeechLine s : speechQueue){
			apace.model.cast.Character c = getCastMember(s.speakerId);
			if(s.sayAt)
				displayString(gc, gr, s.content, s.sayX, s.sayY);
				//gr.drawString(s.content, s.sayX - (gc.getDefaultFont().getWidth(s.content) /2), s.sayY);
			else
				displayString(gc, gr, s.content, c.getX() + c.getWidth()/2 , c.getY());
				//gr.drawString(s.content, c.getX() + c.getWidth()/2 - (gc.getDefaultFont().getWidth(s.content) /2), c.getY() - c.getHeight());
		}
		//Draw the cursor (TODO: This is awful)
		Input mouseInput = gc.getInput();
		gr.drawImage(mouseCursor, mouseInput.getMouseX(), mouseInput.getMouseY());
		if(DEBUGMODE)
			gr.drawString(mouseInput.getMouseX()+","+mouseInput.getMouseY(), mouseInput.getMouseX(), mouseInput.getMouseY()-40);
		
	}

	/*
	 * (x,y) represent the intented location of the text.
	 * This method looks for a usable space to displace the text in, 
	 * by adjusting the co-ordinates based on the content size.
	 */
	private void displayString(GameContainer gc, Graphics gr, String content, int x, int y){
		int MAX_DIALOGUE_WIDTH = 300;
		Font f = gr.getFont();
		
		//First, chunk it up
		LinkedList<String> strings = new LinkedList<String>();
		while(f.getWidth(content) > MAX_DIALOGUE_WIDTH){
			int i = 1; int lastGood = i;
			String partial = content.substring(0, i);
			while(f.getWidth(partial) < MAX_DIALOGUE_WIDTH){
				i = content.indexOf(" ", i+1);
				if(i == -1)
					break;
				lastGood = i;
				partial = content.substring(0, i);
			}
			
			strings.add(partial);
			
			if(i == -1){
				content = content.substring(lastGood);
				break;
			}
			content = content.substring(i);
		}
		strings.add(content);
		
		//Now we need a clear space of 300 pixels width
		if(x + 150 > gc.getWidth()){
			x = gc.getWidth() - 160;
		}
		else if(x - 150 < 0){
			x = 160;
		}
		
		int block_height = 0;
		for(String s : strings){
			block_height += 5 + f.getHeight(s);
		}
		
		y = y - block_height;
		
		if(y + block_height > gc.getHeight()){
			y = gc.getHeight() - block_height - 100;
		}
		else if(y < 0){
			y = 10;
		}
		
		
		for(int i=0; i<strings.size(); i++){
			gr.drawString(strings.get(i), x - f.getWidth(strings.get(i))/2, y);
			y += 5 + f.getHeight(strings.get(i));
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		/* Traps are arranged in very specific ways.
		 * First, anything that needs to be ALWAYS EXECUTED.
		 * Second, are we in a conversation? If so, process this and return.
		 * Third, are we executing a script? If so, process this and return.
		 * Otherwise, check for input and run like a normal game loop.
		 */
		
		if(DEBUGMODE){
			if(container.getInput().isKeyPressed(Input.KEY_F1)){
				DEBUG_SHOW = DEBUG_SHOW_TYPE.SHOW_HOTSPOTS;
			}
			else if(container.getInput().isKeyPressed(Input.KEY_F2)){
				DEBUG_SHOW = DEBUG_SHOW_TYPE.SHOW_WALKABLE;
			}
		}
		
		//General housecleaning.
		for(SpeechLine s : speechQueue){
			s.lifespan -= delta;
		}
		for(int i=0; i<speechQueue.size(); i++){
			if(speechQueue.get(i).lifespan <= 0){
				if(speechQueue.get(i).scripted){
					processScript();
				}
				speechQueue.remove(i);
				i--;
			}
			else if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				container.getInput().clearMousePressedRecord();
				if(speechQueue.get(i).scripted){
					processScript();
				}
				speechQueue.remove(i);
				i--;
			}
		}

		//Conversations are next most important.
		if(conversing){
			if(container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				container.getInput().clearMousePressedRecord();
				int my = container.getInput().getMouseY();
				LinkedList<String> ops = currentConversation.enabled();
				for(int i=ops.size(); i>0; i--){
					int yval = 480-i*25;
					if(my > yval && my < yval+25){
						currentScript = currentConversation.getOutcome(ops.get(ops.size()-i));
						conversing = false;
						startScript();
					}
				}
			}
			return;
		}
		
		if(container.getInput().isKeyPressed(Input.KEY_1)){
			cursorMode = CURSOR_MODE.USE;
			mouseCursor = cursors.get(0);
		}
		else if(container.getInput().isKeyPressed(Input.KEY_2)){
			cursorMode = CURSOR_MODE.EXAMINE;
			mouseCursor = cursors.get(1);
		}
		else if(container.getInput().isKeyPressed(Input.KEY_3)){
			cursorMode = CURSOR_MODE.TALK;
			mouseCursor = cursors.get(2);
		}
		else if(container.getInput().isKeyPressed(Input.KEY_I)){
			showInventory  = !showInventory;
		}
		
		if(showInventory){
			if(container.getInput().isMouseButtonDown(Input.KEY_ESCAPE)){
				showInventory = false;
				return;
			}
			if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				container.getInput().clearMousePressedRecord();
				int mx = container.getInput().getMouseX(); int my = container.getInput().getMouseY(); 
				
				if(is.hasExitButton && is.exitButton.contains(new Point2D.Double(mx - is.image_x, my - is.image_y))){
					showInventory = false;
					return;
				}
				
				Iterator<Item> items = pc.getInventory();
				int i = 0; int j = 0;
				Point2D mpt = new Point2D.Double(mx, my);
				while(items.hasNext()){
					Item item = items.next();
					if(new Rectangle2D.Double(is.image_x+is.list_x + i*40, is.image_y+is.list_y+j*40, 32, 32).contains(mpt)){
						if(cursorMode == CURSOR_MODE.USE){
							//showInventory = false;
							usingItem = true;
							itemInUse = item;
							cursorMode = CURSOR_MODE.ITEM;
							mouseCursor = itemInUse.icon;
							return;
						}
						else{
							if(DEBUGMODE)
								System.out.println("Trace - using item on item.");
							currentScript = item.fire(cursorMode, itemInUse);
							if(DEBUGMODE && currentScript == null)
								System.out.println("Warning - script returned was null object");
							else if(DEBUGMODE)
								System.out.println(currentScript.getHeadEvent());
							if(currentScript != null)
								startScript();
						}
						return;
					}
					i++;
					if(i > is.iconsperline){
						j++;
						i=0;
					}
				}
			}
			return;
		}

		if(moving){
			animDelta += delta;
			//animDelta -= 3;
			pc.walkingAnim.get(pc.currentDirection).update(delta);
			Point next = walkingPath.get(0);
			if(next.x - pc.getTechX() <= -3){
				pc.currentDirection = DIR.LEFT;
				pc.setX(pc.getTechX()-3);
			}
			else if(next.x - pc.getTechX() > 3){
				pc.currentDirection = DIR.RIGHT;
				pc.setX(pc.getTechX()+3);
			}
			if(next.y - pc.getTechY() <= -3){
				pc.setY(pc.getTechY()-3);
			}
			else if(next.y - pc.getTechY() > 3){
				pc.setY(pc.getTechY()+3);
			}
			
			if(Math.abs(next.x - pc.getTechX()) <= 3 && Math.abs(next.y - pc.getTechY()) <= 3){
				walkingPath.remove(0);
				System.out.println(walkingPath.size());
				if(walkingPath.size() == 0){
					moving = false;
					pc.currentAnim = pc.standingAnim;
					if(currentScript != null){
						processScript();
					}
				}
			}
			
		}
		else{
			pc.currentAnim.get(pc.currentDirection).update(delta);
		}
		//Movement, hotspots
		if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			container.getInput().clearMousePressedRecord();
			Point2D mpt = new Point2D.Double(container.getInput().getMouseX(), container.getInput().getMouseY());
			for(Hotspot h : currentLocation.hotspots){
				if(h.area.contains(mpt) && h.enabled){
					if(DEBUGMODE)
						System.out.println("Firing! At "+h.getName());
					currentScript = h.fire(cursorMode, itemInUse);
					if(currentScript != null){
						if(h.walkTo && (!(cursorMode == CURSOR_MODE.EXAMINE) || lookTriggersWalkTo)){
							currentScript.addToStart(new CharacterWalkToEvent(h.walktox, h.walktoy, 0));
						}
						startScript();
					}
					return;
				}
			}
			for(NPC n : currentLocation.npcs){
				if(n.getArea().contains(mpt)){
					currentScript = n.fire(cursorMode, itemInUse);
					if(currentScript != null)
						startScript();
					return;
				}
			}
			Input i = container.getInput();
			if(currentLocation.pointIsWalkable(i.getMouseX(), i.getMouseY())){
				moving = true;
				AStarPathFinder aspf = new AStarPathFinder(currentLocation.walkable, 20, true);
				WalkableArea walkable = currentLocation.walkable;
				if(DEBUGMODE)
					System.out.println(pc.getTechX()/walkable.resolution+","+ pc.getTechY()/walkable.resolution+","+ i.getMouseX()/walkable.resolution+","+ i.getMouseY()/walkable.resolution);
				Path p = aspf.findPath(null, pc.getTechX()/walkable.resolution, pc.getTechY()/walkable.resolution, i.getMouseX()/walkable.resolution, i.getMouseY()/walkable.resolution);
				walkingPath = new LinkedList<Point>();
				for(int k=0; k<p.getLength(); k++){
					if(DEBUGMODE)
						System.out.println(p.getX(k)*walkable.resolution+","+p.getY(k)*walkable.resolution);
					walkingPath.add(new Point(p.getX(k)*walkable.resolution, p.getY(k)*walkable.resolution));
				}
				if(DEBUGMODE)
					System.out.println(i.getMouseX()+","+i.getMouseY());
				walkingPath.add(new Point(i.getMouseX(), i.getMouseY()));
				animDelta = 0;
				pc.currentAnim = pc.walkingAnim;
			}
			
		}
		
		if(container.getInput().isKeyPressed(Input.KEY_F)){
			if(container != null){
				try{
					container.setFullscreen(!container.isFullscreen());
					container.setMouseGrabbed(container.isFullscreen());
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	
	}
	
	private void startScript(){
		scripting = true;
		if(currentScript.done()){
			scripting = false;
			currentScript.exit();
			return;
		}
		AbstractScriptEvent se = currentScript.getNextEvent();
		se.onExecute(this);
		
		if(!se.blocking)
			processScript();
	}
	
	private void processScript() {
		currentScript.popEvent();
		if(currentScript.done()){
			scripting = false;
			currentScript.exit();
			currentScript = null; //Surely?
			return;
		}
		AbstractScriptEvent se = currentScript.getNextEvent();
		se.onExecute(this);
		
		if(!se.blocking)
			processScript();
	}

	public apace.model.cast.Character getCastMember(String code){
		if(code.equalsIgnoreCase("player"))
			return pc;
		return CastManager.getNPC(code);
	}
	
	public void setPlayerCharacter(PlayerCharacter pc){
		this.pc = pc;
	}
	
	public PlayerCharacter getCurrentPC(){
		return pc;
	}

	public void setLookTriggersWalkTo(boolean walkTo){
		this.lookTriggersWalkTo = walkTo;
	}

	public void setCursorMode(CURSOR_MODE cm) {
		cursorMode = cm;
		mouseCursor = cursors.get(cm.ordinal());
	}

	public void setUsingItem(boolean b) {
		this.usingItem = b;
	}

	public boolean isUsingItem() {
		return usingItem;
	}
	
}
