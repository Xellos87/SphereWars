package videogame;

import java.awt.Rectangle;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 *
 * Clase: GameObject.java
 * 
 * Comentarios: Clase que representa los objetos del juego.
 *
 */
public class GameObject {
	
	//TODO: añadir atributos de sprites

	//posicion y direccion
	private double x;
	private double y;
	private double dx;
	private double dy;

	//dimensiones
	private int width;
	private int height;

	//caja de colisiones
	private Rectangle box;
	
	public GameObject(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		box = new Rectangle(x, y, width, height);
	}
	
	public boolean intersects(GameObject o){
		return box.intersects(o.getBox());		
	}
	
	public Rectangle getBox(){
		return box;
	}
}
