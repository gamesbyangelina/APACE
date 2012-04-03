package apace.model.world;

import java.awt.geom.Rectangle2D;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class WalkableArea implements TileBasedMap {
	
	public boolean[][] underlying;
	public int width;
	public int height;
	public int resolution;
	
	public WalkableArea(int x, int y, int resolution){
		underlying = new boolean[y/resolution][x/resolution];
		this.width = x;
		this.height = y;
		this.resolution = resolution;
	}
	
	public void makeWalkable(int xp, int yp, int wp, int hp){
		int x = xp/resolution;
		int y = yp/resolution;
		int w = wp/resolution;
		int h = hp/resolution;
		
		for(int i=x; i<x+w; i++){
			for(int j=y; j<y+h; j++){
				underlying[j][i] = true;
			}
		}
	}
	
	public void makeBlocked(int xp, int yp, int wp, int hp){
		int x = xp/resolution;
		int y = yp/resolution;
		int w = wp/resolution;
		int h = hp/resolution;
		
		for(int i=x; i<x+w; i++){
			for(int j=y; j<y+h; j++){
				underlying[j][i] = false;
			}
		}
	}

	@Override
	public boolean blocked(PathFindingContext arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return !underlying[arg2][arg1];
	}

	@Override
	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub 
		if(blocked(arg0, arg1, arg2))
			return 1000;
		return 1;
	}

	@Override
	public int getHeightInTiles() {
		// TODO Auto-generated method stub
		return underlying.length;
	}

	@Override
	public int getWidthInTiles() {
		// TODO Auto-generated method stub
		return underlying[0].length;
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
