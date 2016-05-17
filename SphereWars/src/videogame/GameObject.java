package videogame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
	//Posición en pantalla
	protected int x;
	protected int y;
	//Velocidad de movimiento
	protected int vx;
	protected int vy;
	//Coordenadas en la imagen
	protected int x_img;
	protected int y_img;
	//Dimensiones de la imagen
	protected int width;
	protected int height;
	//Dimensiones en pantalla
	protected int block_width;
	protected int block_height;
	
	//TODO: Añadir animaciones de sprites
	//Sprite
	protected BufferedImage image;
	
	//Variables de colisiones
	protected boolean kills;
	
	
	
	public GameObject(int x, int y,int x_img,int y_img, int width, int height,int block_width,int block_height){
		this.x = x;
		this.y = y;
		this.x_img = x_img;
		this.y_img = y_img;
		vx = 0;
		vy = 0;
		this.width = width;
		this.height = height;
		this.block_width = block_width;
		this.block_height = block_height;
	}
	
	protected void resize(){
		//Comprobaciones por si la imagen no es cuadrada
		int w;
		int h;
		if(width == height){
			w = block_width;
			h = block_height;
		}else if(width > height){
			w = block_width;
			h = (int) (((float)block_width) / width * height);
		}else{
			w = (int) (((float)block_height) / height * width);
			h = block_height;
		}
		//Escala la imagen
		Image tmp = image.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	    BufferedImage dimg = new BufferedImage(block_width, block_height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, block_width-w, block_height-h, null);
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
		return new Rectangle(x, y, block_width, block_height);
	}

	public void updatePositionX(int x) {
		this.x = x;
	}
	
	public int getWidth(){
		return width;
	}

	public int getHeight(){
		return height;
	}
	
	public int getWidthScreen(){
		return block_width;
	}

	public int getHeightScreen(){
		return block_height;
	}
	
	public int getPositionX(){
		return x;
	}
	
	public int getPositionY(){
		return y;
	}
	
	public boolean kills(){
		return kills;
	}
}
