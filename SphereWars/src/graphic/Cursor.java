package graphic;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Constants;
import utils.Position;

public class Cursor {
	private Image cursor;
	private Position iniPos;
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
			iniPos = Constants.titleIniPos;
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
		int newY = iniPos.getY() + gap * numPos;
		System.out.println(newY);
		int newX = iniPos.getX();
		pos.changePosition(newX, newY);
		
	}

	public void previousPosition() {
		System.out.println(numPos);
		numPos = (numPos - 1) % maxPos;
		int newY = iniPos.getY() + gap * numPos;
		System.out.println(newY);
		int newX = iniPos.getX();
		pos.changePosition(newX, newY);
	}
}
