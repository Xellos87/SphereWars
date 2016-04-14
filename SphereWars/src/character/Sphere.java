package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphic.Sprite;
import videogame.GameObject;

public class Sphere extends GameObject implements Sprite{
	
	public Sphere(String path, int x, int y, int xImg, int yImg, int width, int height) {
		super(path, x, y, xImg, yImg, width, height);
		/*try {
			image = ImageIO.read(new File(path));
			this.posX = posX;
			this.posY = posY;
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image.getSubimage(xImg, yImg, width, height), x, y, null);
	}

}
