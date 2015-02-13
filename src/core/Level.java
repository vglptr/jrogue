package core;

import java.util.Random;

import tile.PlayerTile;
import tile.TestTile1;
import tile.TestTile2;
import tile.Tile;

public class Level {
	private static final float zoomspeed=10f;
	private static final int maxZoom = 3;
	private static final int minZoom = 30;
	private static final float moveDelayConst = 0.5f;
		
	public int viewDistance = 30;
	public float cameraX = 0.0f;
	public float cameraY = 0.0f;
	public float tilesPerScreen = 30;
	
	private Tile[][] tiles = new Tile[1000][1000];
	private PlayerTile playertile=new PlayerTile();
	private Player player;
	private float moveDelay;
	
	
	public void init() {
		player = new Player();
		player.setPosition(10,10);
		playertile.init(player.getPosition().x,player.getPosition().y);
		cameraX=-player.getPosition().x-0.5f;
		cameraY=-player.getPosition().y-0.5f;
		Random r = new Random();
    	for(int i=0;i<tiles.length;i++) {
    		for(int j=0;j<tiles[i].length;j++) {
    			if(r.nextInt()%2==0)
    				tiles[i][j]=new TestTile1();
    			else
    				tiles[i][j]=new TestTile2();
    			tiles[i][j].init(i,j);
    		}
    	}
	}
	
	public void update(float delta) {
		movePlayer(delta);
		tilesPerScreen+=Controls.zoom*delta*zoomspeed;
		if(tilesPerScreen<maxZoom) tilesPerScreen=maxZoom;
		if(tilesPerScreen>minZoom) tilesPerScreen=minZoom;
    	
    	for(int i=0;i<tiles.length;i++)
    		for(int j=0;j<tiles[i].length;j++) 
    			tiles[i][j].update(delta, this);
	}
	
	public void movePlayer(float delta) {
		if(Controls.dirX==0 && Controls.dirY==0) { 
			moveDelay=0; 
		} else {
			if(moveDelay<=0) {
				if(!isWall(player.getPosition().x+Controls.dirX,player.getPosition().y+Controls.dirY)) {
					player.movePosition(Controls.dirX, Controls.dirY);
					playertile.setPosition(player.getPosition());
					cameraX-=Controls.dirX;
					cameraY-=Controls.dirY;
				}
				moveDelay=moveDelayConst;
			} else {
				moveDelay-=delta;
			}
		}		
	}
	
	public void render(float delta) {
		for(int i=player.getPosition().x-viewDistance;i<=player.getPosition().x+viewDistance;i++)
    		for(int j=player.getPosition().y-viewDistance;j<=player.getPosition().y+viewDistance;j++)
    			if(i>0 && i<tiles.length && j>0 && j<tiles[0].length)
    				tiles[i][j].render(delta, this);
		playertile.render(delta, this);	
	}
	
	public boolean isWall(int x, int y) {
		return (x<0 || x>tiles.length || y<0 || y>tiles[0].length || tiles[x][y].isWall());
	}
}
