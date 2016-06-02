package utils;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Position.java
 * 
 * Comentarios: Estructura para almacenar las coordenadas
 * 
 */
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
