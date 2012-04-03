package apace.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class TileMapper extends BasicGame{
	
	public TileMapper(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws SlickException{
		AppGameContainer app = 
			new AppGameContainer(new TileMapper("Tile Mapper 0.0"));
		
		app.setDisplayMode(740, 480, false);
		app.setVSync(true);
		app.setShowFPS(false);
		
		app.start();
	}
	
	public Image[][][] map;
	LinkedList<Image> tile_map = new LinkedList<Image>();
	int index = 0;
	int selected = 0;
	private int layer = 0;

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		
		// TODO Auto-generated method stub
		for(int i=0; i<map.length; i++){
			for(int j=0; j<map[i].length; j++){
				arg1.drawImage(map[i][j][0], i*32, j*32);
				if(map[i][j][1] != null)
					arg1.drawImage(map[i][j][1], i*32, j*32);
			}
		}
		
		for(int i=index; i<index+15; i++){
			arg1.drawImage(tile_map.get(i), 650, (i-index)*32);
			if(selected == i){
				float linewidth = arg1.getLineWidth();
				arg1.setLineWidth(2);
				arg1.drawRect(650, (i-index)*32, 32, 32);
				arg1.setLineWidth(linewidth);
			}
		}
		for(int i=index+15; i<index+30; i++){
			arg1.drawImage(tile_map.get(i), 682, (i-index-15)*32);
			if(selected == i){
				float linewidth = arg1.getLineWidth();
				arg1.setLineWidth(2);
				arg1.drawRect(650+32, (i-index)*32, 32, 32);
				arg1.setLineWidth(linewidth);
			}
		}
	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		SpriteSheet environment_tiles = new SpriteSheet(new Image("resources/lofi_environment.png"),32,32);
		
		for(int i=0; i<environment_tiles.getHeight()/32; i++){
			for(int j=0; j<environment_tiles.getWidth()/32; j++){
				tile_map.add(environment_tiles.getSubImage(i,j));
			}
		}
		
		map = new Image[20][15][2];
		for(int i=0; i<20; i++){
			for(int j=0; j<15; j++){
				map[i][j][0] = environment_tiles.getSubImage(0,0);
			}
		}
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		Input i = arg0.getInput();
		if(i.isKeyDown(Input.KEY_DOWN) && index+30 < tile_map.size()){
			index++;
		}
		else if(i.isKeyDown(Input.KEY_UP) && index > 0){
			index--;
		}
		
		if(i.isKeyPressed(Input.KEY_X)){
			ByteBuffer bb = ByteBuffer.allocateDirect(640*480*4);
			BufferedImage bi = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
			
			arg0.getGraphics().getArea(0, 0, 640, 480, bb);
			for(int m=0; m<480; m++){
				for(int n=0; n<640; n++){
					byte r = bb.get(m*640*4 + n*4);
					byte g = bb.get(m*640*4 + n*4 + 1);
					byte b = bb.get(m*640*4 + n*4 + 2);
					byte a = bb.get(m*640*4 + n*4 + 3);
					int argb = ((0xFF & a) << 24) | ((0xFF & r) << 16) |
		            ((0xFF & g) << 8) | (0xFF & b);
					bi.setRGB(n, 479-m, argb);
				}
			}
			
			File f = new File("Screenshot"+System.currentTimeMillis()+".png");
			try {
			    ImageIO.write(bi, "png", f);
			} catch (IOException e) {
			}
			
			
		}
		
		if(i.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			int x = i.getMouseX();
			int y = i.getMouseY();
			if(x > 650 && x < 650+32){
				selected = index + y/32;
			}
			else if(x > 682 && x < 682 + 32){
				selected = index + 15 + y/32;
			}
			else if(x < 640){
				map[x/32][y/32][layer ] = tile_map.get(selected).copy();
			}
		}
		
		if(i.isKeyPressed(Input.KEY_1)){
			layer = 1;
		}
		else if(i.isKeyPressed(Input.KEY_0)){
			layer = 0;
		}
	}

}
