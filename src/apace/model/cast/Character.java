package apace.model.cast;

import java.util.EnumMap;

import org.newdawn.slick.Animation;

public class Character {
	public enum DIR {RIGHT, DOWN, LEFT, UP};
	public enum ANIMTYPE {WALKING, STANDING};

	public EnumMap<DIR, Animation> currentAnim;
	
	public EnumMap<DIR, Animation> walkingAnim = new EnumMap<DIR, Animation>(DIR.class);
	public EnumMap<DIR, Animation> standingAnim = new EnumMap<DIR, Animation>(DIR.class);
	public DIR currentDirection = DIR.RIGHT;
	
	private int x;
	private int y;

	private int ax;
	private int ay;
	
	public Character(int x, int y, int ax, int ay){
		this.x = x;
		this.y = y;
		this.ax = ax;
		this.ay = ay;
	}
	
	public int getX(){
		return x - ax;
	}
	
	public int getY(){
		return y - ay;
	}
	
	public int getTechX(){
		return x;
	}
	public void setX(int x){
		this.x = x;
	}
	public int getTechY(){
		return y;
	}
	public void setY(int y){
		this.y = y;
	}
	
	public int getWidth(){
		return currentAnim.get(currentDirection).getWidth();
	}
	public int getHeight(){
		return currentAnim.get(currentDirection).getHeight();
	}
	
	public void addAnimation(ANIMTYPE type, Animation right, Animation left, Animation up, Animation down){
		EnumMap<DIR, Animation> anim = new EnumMap<DIR, Animation>(DIR.class);
		anim.put(DIR.RIGHT, right);
		anim.put(DIR.LEFT, left);
		anim.put(DIR.DOWN, down);
		anim.put(DIR.UP, up);
		
		switch(type){
		case STANDING: this.standingAnim = anim; break;
		case WALKING: this.walkingAnim = anim; break;
		}
	}
	
	public void setCurrentAnimation(ANIMTYPE type){
		switch(type){
		case STANDING: this.currentAnim = standingAnim; break;
		case WALKING: this.currentAnim = walkingAnim; break;
		}
	}
	
}
