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
	private String opcion;
	private int numPos, maxPos, gap;
	private String menuType;
	//TODO: ARREGLAR PROBLEMAS DE CURSOR. POR QUE HAGO SIEMPRE LAS COSAS MAL A LA PRIMERA
	public Cursor(String menuType){		
		this.menuType = menuType;
		numPos = 0;
		try {
			cursor = ImageIO.read(new File("images/cursor.png"));
		} catch (IOException e) {
			System.err.println("problem loading cursor");
		}
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			maxPos = Constants.titleMaxPos;
			gap = Constants.titleGap;
			pos = Constants.titleIniPos;
			iniposX = Constants.titleIniPos.getX();
			iniposY = Constants.titleIniPos.getY();
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equals(Constants.optMenu)){
			maxPos = Constants.optionMaxPos;
			gap = Constants.titleGap;
			pos = Constants.optionIniPos;
			iniposX = Constants.optionIniPos.getX();
			iniposY = Constants.optionIniPos.getY();
			opcion = Constants.list_options[numPos];
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
		System.out.println(numPos+"/"+maxPos);
		numPos = (numPos + 1) % maxPos;
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.optMenu)){
			opcion = Constants.list_options[numPos];
		}		
		int newY, newX;
		newY = iniposY + gap * numPos;
		newX = iniposX;
		if(opcion.equalsIgnoreCase("jump")){
			System.out.println("in jump "+newX +" "+newY);
			newY+=gap;
		}else if(opcion.equalsIgnoreCase("run")){
			newX+=260;
		}
		System.out.println(newY+" "+newX);
		
		pos.changePosition(newX, newY);
	}

	public void previousPosition() {
		System.out.println(numPos+"/"+maxPos);
		if(numPos > 0)
			numPos = Math.abs((numPos - 1)) % maxPos;
		else
			numPos = maxPos-1;
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.optMenu)){
			opcion = Constants.list_options[numPos];
		}
		int newY, newX;
		newY = iniposY + gap * numPos;
		newX = iniposX;
		if(opcion.equalsIgnoreCase("run")){
			newX += 260;
		}else if(opcion.equalsIgnoreCase("jump")){
			newY+=gap;
		}
		System.out.println(newY);
		
		pos.changePosition(newX, newY);
	}

	public String enter(){
		return opcion;
	}
	
	public String getOpcion() {
		return opcion;
	}
}
