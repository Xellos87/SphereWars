package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphic.Sprite;
import videogame.GameObject;

public class Sphere extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int NORMAL = 0;
	public static final int JUMP = 1;
	public static final int SPEED_UP = 2;
	public static final int SPEED_DOWN = 3;
	//
	private int[] x_imgs={2};
	private int[] y_imgs={0};
	//
	private int type;

	public Sphere(String path, int x, int y, int width, int height) {
		super(path, x, y, width, height);
		this.type = NORMAL;
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
