package obstacle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphic.Sprite;
import videogame.GameObject;

public class Spike extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int UPPER = 0;
	public static final int LOWER = 2;
	public static final int RIGHT = 1;
	public static final int LEFT = 3;
	//
	private int direction;

	public Spike(String path, int x, int y, int xImg, int yImg, int width, int height,int block_width,int block_height, int direction) {
		super(path, x, y, width, height, block_width, block_height);
		//Carga solo el fragmento que necesita la imagen
		image = image.getSubimage(xImg, yImg, width, height);
		this.direction = direction;
		rotateImage();
		resize();
	}

	private void rotateImage(){
		if(direction != UPPER){
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			Graphics2D g2 = newImage.createGraphics();
			g2.rotate(direction*Math.PI/2, w/2, h/2);  
			g2.drawImage(image,null,0,0);
			image = newImage;
		}
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image, x, y, null);
	}


}
