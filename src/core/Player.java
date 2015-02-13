package core;

import util.Position;

public class Player {
	private Position pos=new Position(0,0);
	
	public void setPosition(Position pos) {
		this.pos=pos;
	}
	
	public void setPosition(int x, int y) {
		this.pos.x=x;
		this.pos.y=y;
	}
	
	public Position getPosition() {
		return pos;
	}

	public void movePosition(int dirX, int dirY) {
		this.pos.x+=dirX;
		this.pos.y+=dirY;
	}
}
