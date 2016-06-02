package graphic;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import utils.Constants;
import utils.Position;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Cursor.java
 * 
 * Comentarios: Cursor de los menús
 * 
 */
public class Cursor {
	private Image cursor;
	//private Position iniPos;
	private int iniposX;
	private int iniposY;
	private Position pos;
	private String opcion;
	private int numPos, maxPos, gap;
	private String menuType;
	
	/**
	 * Constructor de la clase cursor. inicializa sus parametros 
	 * y carga la imagen
	 * @param menuType
	 */
	public Cursor(String menuType){		
		this.menuType = menuType;
		numPos = 0;
		try {
			cursor = ImageIO.read(new File("images/cursor.png"));
		} catch (IOException e) {
			System.err.println("problem loading cursor");
		}
		//distintos comportamientos dependiendo del menu en el que se encuentra
		//distintas posiciones iniciales
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			maxPos = Constants.titleMaxPos;
			gap = Constants.titleGap;
			iniposX = Constants.titleIniPos.getX();
			iniposY = Constants.titleIniPos.getY();
			pos = new Position(iniposX,iniposY);
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equals(Constants.optMenu)){
			maxPos = Constants.optionMaxPos;
			gap = Constants.titleGap;
			iniposX = Constants.optionIniPos.getX();
			iniposY = Constants.optionIniPos.getY();
			pos = new Position(iniposX,iniposY);
			opcion = Constants.list_options[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.conMenu)){
			maxPos = Constants.controlMaxPos;
			gap = Constants.titleGap;
			iniposX = Constants.optionIniPos.getX();
			iniposY = Constants.optionIniPos.getY();
			pos = new Position(iniposX,iniposY);
			opcion = Constants.list_controller[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.modMenu)){
			maxPos = Constants.modeMaxPos;
			gap = Constants.titleGap;
			iniposX = Constants.modeIniPos.getX();
			iniposY = Constants.modeIniPos.getY();
			pos = new Position(iniposX,iniposY);
			opcion = Constants.list_modes[numPos];
		}
	}
	
	/**
	 * Cambia la posicion del cursor
	 * @param x
	 * @param y
	 */
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

	/**
	 * Salta a la siguiente posicion del cursor dentro del menu
	 */
	public void nextPosition() {
		int auxMaxPos = maxPos;
		//caso de la opcion de diestro o zurdo del kinect
		if(!Constants.conTeclado && Constants.jugador==1 && menuType.equalsIgnoreCase(Constants.conMenu)
				&& Constants.elegidoJugador){
			auxMaxPos = 3;
		}
		//aumenta en uno la posicion
		//system.out.println(numPos+"/"+auxMaxPos);
		numPos = (numPos + 1) % auxMaxPos;
		if(menuType.equalsIgnoreCase(Constants.conMenu) && Constants.jugador==2 && numPos == 1){
			numPos ++;
		}
		//obtiene la opcion de cada menu
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.optMenu)){
			opcion = Constants.list_options[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.conMenu)){
			opcion = Constants.list_controller[numPos];					
		}else if(menuType.equalsIgnoreCase(Constants.modMenu)){
			opcion = Constants.list_modes[numPos];
		}
		//movimiento del cursor
		int newY, newX;
		newY = (iniposY + gap * numPos);
		newX = iniposX;		
		pos.changePosition(newX, newY);
	}

	/**
	 * Salta a la posicion anterior del cursor
	 */
	public void previousPosition() {
		int auxMaxPos = maxPos;
		if(!Constants.conTeclado && Constants.jugador==1 && menuType.equalsIgnoreCase(Constants.conMenu)
				&& Constants.elegidoJugador){
			auxMaxPos = 3;
		}
		//system.out.println(numPos+"/"+auxMaxPos);
		if(numPos > 0)
			numPos = Math.abs((numPos - 1)) % auxMaxPos;
		else
			numPos = auxMaxPos-1;
		if(menuType.equalsIgnoreCase(Constants.conMenu) && Constants.jugador==2 && numPos == 1){
			numPos --;
		}
		if(menuType.equalsIgnoreCase(Constants.titMenu)){
			opcion = Constants.list_menu[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.optMenu)){
			opcion = Constants.list_options[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.conMenu)){
			opcion = Constants.list_controller[numPos];
		}else if(menuType.equalsIgnoreCase(Constants.modMenu)){
			opcion = Constants.list_modes[numPos];
		}
		int newY, newX;
		newY = iniposY + gap * numPos;
		newX = iniposX;
		
		pos.changePosition(newX, newY);
	}

	/**
	 * devuelve la opcion actual del menu
	 * @return
	 */
	public String enter(){
		//cambio entre diestro y xurdo del kinect
		if(!Constants.conTeclado && Constants.jugador==1 && menuType.equalsIgnoreCase(Constants.conMenu)
				&& Constants.elegidoJugador && numPos==2){
			return "cambioMano";
		}
		return opcion;
	}
	
	public String getOpcion() {
		return opcion;
	}

	/**
	 * movimiento a derecha en el menu de titulo
	 */
	public void right() {
		if(menuType.equals(Constants.titMenu)){
			int newX = Constants.dosDjPos.getX() - 60;
			int newY = Constants.dosDjPos.getY();
			pos.changePosition(newX, newY);
			Constants.cursorDesplazado=true;
		}
	}

	/**
	 * movimiento a la izquierda en el menu de titulo
	 */
	public void left() {
		if(menuType.equals(Constants.titMenu)){
			int newX = iniposX;
			int newY = iniposY;
			pos.changePosition(newX, newY);
			Constants.cursorDesplazado=false;
		}
	}
}
