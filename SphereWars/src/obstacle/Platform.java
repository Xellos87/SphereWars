package obstacle;

import java.awt.Graphics2D;

import graphic.Sprite;
import videogame.GameObject;

public class Platform extends GameObject implements Sprite{
	
	public Platform(String path, int x, int y, int xImg, int yImg, int width, int height) {
		super(path, x, y, xImg, yImg, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image.getSubimage(xImg, yImg, width, height), x, y, null);
	}

}
