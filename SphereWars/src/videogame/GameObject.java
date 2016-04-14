package videogame;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	
	//Posición
	protected int x;
	protected int y;
	
	//Velocidad
	protected int vx;
	protected int vy;

	//Dimensiones
	protected int xImg;
	protected int yImg;
	protected int width;
	protected int height;
	
	//TODO: Añadir animaciones de sprites
	//Sprite
	protected BufferedImage image;
	
	
	
	public GameObject(String path, int x, int y, int xImg, int yImg, int width, int height){
		this.x = x;
		this.y = y;
		vx = 0;
		vy = 0;
		this.xImg = xImg;
		this.yImg = yImg;
		this.width = width;
		this.height = height;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setVelocity(int vx, int vy){
		this.vx = vx;
		this.vy = vy;
	}
	
	public void move(){
		setPosition(x+vx, y+vy);
	}
	
	//TODO: mejorar intersecciones(esquinas)
	public boolean intersects(GameObject o){
		return getBox().intersects(o.getBox());		
	}
	
	public Rectangle getBox(){
		return new Rectangle(x, y, width, height);
	}
}
