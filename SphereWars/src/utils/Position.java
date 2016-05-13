package utils;

public class Position {
	private int y;
	private int x;
	
	public Position(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public void changePosition(int newX, int newY){
		this.x=newX;
		this.y=newY;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
}
