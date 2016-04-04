package graphic;

import java.awt.Graphics2D;

/**
 * Autores: Victor Adrian Milla Español - 557022,,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 *
 * Clase: Sprite.java
 * 
 * Comentarios: Representa el objeto 2D que se representa por pantalla con la definición de los metodos que necesita
 * 
 * */
public abstract class Sprite {
	private String path_sprite;
	private int posX, posY;
	
	public Sprite(String path, int posX, int posY){
		this.path_sprite = path;
		this.posX = posX;
		this.posY = posY;
	}
	
	public abstract void draw2D(Graphics2D g2d);

}
