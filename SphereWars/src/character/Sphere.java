package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphic.Sprite;

public class Sphere implements Sprite{
	private BufferedImage image;
	private int posX;
	private int posY;
	
	public Sphere(String path, int posX, int posY) {
		try {
			image = ImageIO.read(new File(path));
			this.posX = posX;
			this.posY = posY;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image.getSubimage(2, 0, 30, 30), posX, posY, null);
	}

}
