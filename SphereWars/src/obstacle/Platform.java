package obstacle;

import java.awt.Graphics2D;

import graphic.Sprite;
import videogame.GameObject;

public class Platform extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int GROUND = 0;
	public static final int PLATAFORM = 10;
	public static final int BORDER_LEFT = 1;
	public static final int BORDER_RIGHT = 2;
	public static final int UNDERGROUND = 3;
	//
	private int[] x_imgs={504,504,504,576};
	private int[] y_imgs={576,648,504,865};
	//
	private int type;
	
	public Platform(String path, int x, int y, int width, int height, int type) {
		super(path, x, y, width, height);
		this.type = type;
		selectImage();
	}

	private void selectImage() {
		image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image, x, y, null);
	}
	

}
