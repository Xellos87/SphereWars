package videogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JPanel;

import character.Sphere;

@SuppressWarnings("serial")
public class Game2D extends JPanel {
	private int width,height;
	//Imagen del juego
	private BufferedImage image;
	private Graphics2D g;
	private int count;
	private int MAX = 1;
	private BufferedImage sky;
	private boolean sky_next;
	//
	private int x0_sky,x1_sky;
	private int width0_sky,width1_sky;
	//
	private Sphere player;

	public Game2D(int width, int height){
		this.width = width;
		this.height = height;
		this.count = 0;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();

		image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		loadImages();
	}

	private void loadImages() {
		try {
			sky = ImageIO.read(new File("images/sky_clear.jpg"));
			x0_sky = 0;
			width0_sky = width;
		} catch (IOException e) {
			e.printStackTrace();
		}
		player = new Sphere("images/ball.gif", 50, 250);
	}

	public void draw() {
		//Dibuja todo en pantalla
		g.drawImage(sky.getSubimage(x0_sky, 0, width0_sky, sky.getHeight()), 0, 0, null);
		if(sky_next){
			g.drawImage(sky.getSubimage(x1_sky, 0, width1_sky, sky.getHeight()), width0_sky, 0, null);
		}
		
		player.draw2D(g);

		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(image, 0, 0,width, height,null);
		getGraphics().dispose();
	}

	public void actionGame() {
		count++;
		if(count == MAX*2){
			x0_sky++;
			if(width0_sky > sky.getWidth() - x0_sky){
				width0_sky = sky.getWidth() - x0_sky;
				x1_sky = 0;
				width1_sky = width - width0_sky;
				sky_next = true;
			}else{
				sky_next = false;
			}
			if(width0_sky == 0){
				sky_next = false;
				x0_sky = 0;
				width0_sky = width;
			}
			count = 0;
		}
	}

}
