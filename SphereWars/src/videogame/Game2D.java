package videogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.swing.JPanel;

import character.Sphere;
import map.MapController;
import obstacle.Platform;

@SuppressWarnings("serial")
public class Game2D extends JPanel {
	private int width,height;
	//Imagen del juego
	private BufferedImage image;
	private Graphics2D g;
	//Fondo con efecto Parallax que da sensación de movimiento
	private Parallax back_parallax;
	//Contenedor del jugador
	private Sphere player;

	//PRUEBA COLISIONES(A la mierda Fdo:Richard :P)
	//private Platform platform,platform1;
	//private ArrayList<Platform> platforms;
	//Esto es lo bueno 
	private MapController map_cont;
	
	public Game2D(int width, int height){
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();

		image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		//platforms = new ArrayList<>();
		map_cont = new MapController(width,height);
		loadImages();
	}

	private void loadImages() {
		/* Carga el efecto de fondo Parallax */
		int num = 1;
		String back_images[] = {"images/sky_clear.jpg"};
		int velocity[] = {2};
		int posX[] = {0};
		int posY[] = {0};
		back_parallax = new Parallax(num, back_images, velocity, posX, posY, width, height);
		/* Carga el personaje en pantalla con su posición */
		player = new Sphere("images/ball.gif", 50, 150, 30, 30);
		player.setVelocity(0, 3);
		//Plataformas de prueba
		/*platform = new Platform("images/platforms.png", 50, 240, 650, 0, 70, 70);
		platform.setVelocity(-1, 0);
		platform1 = new Platform("images/platforms.png", 120, 240, 650, 0, 70, 70);
		platform1.setVelocity(-1, 0);
		platforms.add(platform);
		platforms.add(platform1);
		*/
	}

	public void draw() {
		//Dibuja todo en pantalla
		
		back_parallax.draw(g);
		
		player.draw2D(g);
		//platform.draw2D(g);
		//platform1.draw2D(g);
		
		//
		map_cont.draw2D(g);

		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(image, 0, 0,width, height,null);
		getGraphics().dispose();
	}

	public void actionGame() {
		/* Acciones a realizar */
		
		//Prueba plataformas
		boolean block = true;
		/*for(Platform p: platforms){
			if(player.intersects(p)){
				block = true;
			}
		}*/
		if(!block){
			//Gravedad
			player.move();
		}
		//Mueve plataformas
		map_cont.move();
		/*for(Platform p: platforms){
			p.move();
		}*/
		
	}

}
