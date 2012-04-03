package apace.view;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class InventoryScreen {
	
	public Image background;
	public int image_x;
	public int image_y;
	public int list_x;
	public int list_y;
	public int iconsperline;
	
	public Area exitButton;
	public boolean hasExitButton;
	
	public InventoryScreen(Image b, int lx, int ly){
		this.iconsperline = b.getWidth()-(lx*2)/40;
		this.image_x = (640-b.getWidth())/2;
		this.image_y = (480-b.getHeight())/2;
		this.list_x = lx;
		this.list_y = ly;
		this.background = b;
	}

	public static InventoryScreen defaultInventory() {
		try {
			InventoryScreen i = new InventoryScreen(new Image("resources/default/inv_bg.png"), 20, 30);
			i.addExitButton(319, 0, 80, 32);
			return i;
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return new InventoryScreen(null, 0, 0);
	}
	
	public void addExitButton(Area a){
		this.exitButton = a;
		this.hasExitButton = true;
	}
	
	public void addExitButton(int x, int y, int w, int h){
		this.exitButton = new Area(new Rectangle2D.Double(x,y,w,h));
		this.hasExitButton = true;
	}

}
