package graphic;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Constants;
import utils.Position;

public class Cursor {
	private Image cursor;
	//private Position iniPos;
	private int iniposX;
	private int iniposY;
	private Position pos;
	private int numPos, maxPos, gap;
	//TODO: ARREGLAR PROBLEMAS DE CURSOR. POR QUE HAGO SIEMPRE LAS COSAS MAL A LA PRIMERA
	public Cursor(String menuType){
		
		numPos = 0;
		try {
			cursor = ImageIO.read(new File("images/cursor.png"));
		} catch (IOException e) {
			System.err.println("problem loading cursor");
		}
		if(menuType.equalsIgnoreCase("title")){
			maxPos = Constants.titleMaxPos;
			gap = Constants.titleGap;
			pos = Constants.titleIniPos;
			iniposX = Constants.titleIniPos.getX();
			iniposY = Constants.titleIniPos.getY();
		}
	}
	
	public void changePosition(int x, int y){
		pos.changePosition(x, y);
	}
	
	public Position getPosition(){
		return this.pos;
	}
	
	public Image getImage(){
		return this.cursor;
	}
	
	public int getWidth(){
		return this.cursor.getWidth(null);
	}
	
	public int getHeight(){
		return this.cursor.getHeight(null);
	}

	public void nextPosition() {
		System.out.println(numPos);
		numPos = (numPos + 1) % maxPos;
		int newY = iniposY + gap * numPos;
		System.out.println(newY);
		int newX = iniposX;
		pos.changePosition(newX, newY);
		
	}

	public void previousPosition() {
		System.out.println(numPos);
		if(numPos > 0)
			numPos = Math.abs((numPos - 1)) % maxPos;
		else
			numPos = maxPos-1;
		int newY = iniposY + gap * numPos;
		System.out.println(newY);
		int newX = iniposX;
		pos.changePosition(newX, newY);
	}
}
