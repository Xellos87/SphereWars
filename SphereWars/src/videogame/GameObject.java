package videogame;

import java.awt.Graphics2D;
import java.awt.Image;
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
	protected int width;
	protected int height;
	
	//Dimensiones en pantalla
	protected int block_width;
	protected int block_height;
	
	//TODO: Añadir animaciones de sprites
	//Sprite
	protected BufferedImage image;
	
	
	
	public GameObject(String path, int x, int y, int width, int height,int block_width,int block_height){
		this.x = x;
		this.y = y;
		vx = 0;
		vy = 0;
		this.width = width;
		this.height = height;
		this.block_width = block_width;
		this.block_height = block_height;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void resize(){
		Image tmp = image.getScaledInstance(block_width, block_height, image.SCALE_DEFAULT);
	    BufferedImage dimg = new BufferedImage(block_width, block_height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
		image = dimg;
		//System.out.printf("Nuevo tamaño, ancho: %d, alto: %d\n",image.getWidth(),image.getHeight());
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

	public void updatePositionX(int x) {
		this.x = x;
	}
}
