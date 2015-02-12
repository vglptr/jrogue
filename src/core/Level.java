package core;

import java.util.ArrayList;

import tile.TestTile1;
import tile.TestTile2;
import tile.Tile;

public class Level {
	public float cameraX=0.0f;
	public float cameraY=0.0f;
	
	private ArrayList< ArrayList <Tile>> tiles;
	
	public void init() {
		tiles=new ArrayList< ArrayList <Tile>>();
    	for(int i=0;i<20;i++) {
    		tiles.add(new ArrayList<Tile>());
    		for(int j=0;j<10;j++) {
    			if((i+j)%2==0)
    				tiles.get(i).add(new TestTile1());
    			else
    				tiles.get(i).add(new TestTile2());
    			tiles.get(i).get(j).init(i,j);
    		}
    	}
	}
	
	public void update(float delta) {
		float speed=1.0f;
		cameraX+=Controls.dirX*delta*speed;
		cameraY+=Controls.dirY*delta*speed;
    	
    	for(int i=0;i<tiles.size();i++)
    		for(int j=0;j<tiles.get(i).size();j++) 
    			tiles.get(i).get(j).update(delta, this);
	}
	
	public void render(float delta) {
		for(int i=0;i<tiles.size();i++)
    		for(int j=0;j<tiles.get(i).size();j++) 
    			tiles.get(i).get(j).render(delta, this);
	}
}
