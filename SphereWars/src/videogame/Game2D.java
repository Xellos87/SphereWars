package videogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game2D extends JPanel {
	private int width,height;
	//Imagen del juego
	private BufferedImage image;
	private Graphics2D g;


	public Game2D(int width, int height){
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	public void draw() {
		//Dibuja todo en pantalla
		g.setColor(new Color(0, 0, 255));
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(0, 255, 0));
		g.fillOval(50, 50, 100, 100);
		
		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(image, 0, 0,width, height,null);
		getGraphics().dispose();
	}

}
