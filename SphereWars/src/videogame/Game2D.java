package videogame;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import character.Sphere;
import map.MapController;

@SuppressWarnings("serial")
public class Game2D extends JPanel {
	private int width,height;
	//Fondo con efecto Parallax que da sensación de movimiento
	private Parallax back_parallax;
	//Contenedor del jugador
	private Sphere player;
	//Controllador del generador de mapas
	private MapController map_cont;
	
	public Game2D(int width, int height){
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		
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
		player = new Sphere("images/ball.gif", 50, 150, 30, 30,30,30);
		player.setVelocity(0, 3);
	}

	public void draw() {
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		/* Dibuja todo en pantalla */
		//Efecto Parallax(fondo movil)
		back_parallax.draw(offgc);
		//Jugador
		player.draw2D(offgc);
		//Mapa
		map_cont.draw2D(offgc);

		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
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
		//Mueve el mapa
		map_cont.move();

		
	}

}
