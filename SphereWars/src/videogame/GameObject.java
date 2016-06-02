package videogame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Primitive;

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
	protected int z;
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
	//Disemnsiones de la imagen representado en pantalla(para colisiones, mas realista)
	protected int real_block_width;
	protected int real_block_height;
	//Posición real de la x e y dentro del bloque
	protected int real_x_block;
	protected int real_y_block;
	//Sprite
	protected BufferedImage image;
	protected BufferedImage texture;	
	//Modelo 3D
	protected TransformGroup tg_model3D;
	protected BranchGroup branch_group;
	protected Primitive object_primitive;
	//Variables de colisiones
	protected boolean kills;
	
	
	/**
	 * Constructor
	 * 
	 * @param x, posición del objeto
	 * @param y, posición del objeto
	 * @param x_img, posición en el contenedor para cargar la imagen
	 * @param y_img, posición en el contenedor para cargar la imagen
	 * @param width, ancho de la imagen en el contenedor
	 * @param height, alto de la imagen en el contenedor
	 * @param block_width, ancho de bloque en pantalla
	 * @param block_height, alto de bloque en pantalla
	 */
	public GameObject(int x, int y,int x_img,int y_img, int width, int height,int block_width,int block_height){
		this.x = x;
		this.y = y;
		this.z = 0;
		this.x_img = x_img;
		this.y_img = y_img;
		vx = 0;
		vy = 0;
		this.width = width;
		this.height = height;
		this.block_width = block_width;
		this.block_height = block_height;
	}
	
	/**
	 * Redimensiona el objeto
	 */
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
		real_block_height=h;
		real_block_width=w;
		real_x_block = block_width-real_block_width;
		real_y_block = block_height-real_block_height;
		//Escala la imagen
		Image tmp = image.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	    BufferedImage dimg = new BufferedImage(block_width, block_height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, block_width-w, block_height-h, null);
	    g2d.dispose();
		image = dimg;
		//System.out.printf("Nuevo tamaño, ancho: %d, alto: %d\n",image.getWidth(),image.getHeight());
	}
	
	/**
	 * Establece la posición
	 * 
	 * @param x, posición nueva de la x
	 * @param y, posición nueva de la y
	 */
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Establece la velocidad
	 * 
	 * @param vx, nueva velocidad de la x
	 * @param vy, nueva velocidad de la y
	 */
	public void setVelocity(int vx, int vy){
		this.vx = vx;
		this.vy = vy;
	}
	
	/**
	 * Mueve el objeto
	 */
	public void move(){
		setPosition(x+vx, y+vy);
	}
	
	/**
	 * Comprueba la intersección del objeto con otro
	 * 
	 * @param o, objeto con el que se comprueba la intersección
	 * @param x_ori, x desde la que se dibuja
	 * @param y_ori, y desde la que se dibuja
	 * @return true si y solo si hay intersección entre los objetos
	 */
	public boolean intersects(GameObject o,int x_ori, int y_ori){
		//Rectangle r = getBox(x_ori,y_ori).intersection(o.getBox(x_ori,y_ori));
		//System.out.println(r.toString());
		return getBox(x_ori,y_ori).intersects(o.getBox(x_ori,y_ori));		
	}
	
	/**
	 * Obtiene la caja de colisión
	 * 
	 * @param x_ori, x desde la que se dibuja
	 * @param y_ori, y desde la que se dibuja
	 * @return el rectangulo de colision
	 */
	public Rectangle getBox(int x_ori, int y_ori){
		return new Rectangle(x_ori+x+real_x_block, y_ori+y+real_y_block, real_block_width, real_block_height);
	}

	/**
	 * Actualiza la posición x
	 * @param x, nuevo valor
	 */
	public void updatePositionX(int x) {
		this.x = x;
	}
	
	/**
	 * 
	 * @return la anchura
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * 
	 * @return la altura
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * 
	 * @return ancho de pantalla
	 */
	public int getWidthScreen(){
		return block_width;
	}

	/**
	 * 
	 * @return alto de pantalla
	 */
	public int getHeightScreen(){
		return block_height;
	}
	
	/**
	 * 
	 * @return posición de la x
	 */
	public int getPositionX(){
		return x;
	}
	
	/**
	 * 
	 * @return posición de la y
	 */
	public int getPositionY(){
		return y;
	}
	
	/**
	 * 
	 * @return si el objeto mata al jugador
	 */
	public boolean kills(){
		return kills;
	}
	
	/**
	 * 
	 * @return el modelo 3D del objeto
	 */
	public TransformGroup get3DModel() {
		return tg_model3D;
	}
}
