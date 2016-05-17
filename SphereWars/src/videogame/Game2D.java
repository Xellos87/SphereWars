package videogame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;

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
		setDoubleBuffered(true);
		setFocusable(false);

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
		player = new Sphere(0,0,30,30);
		player.setVelocity(2, 3);
	}

	public void draw() {
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		Image offscreen = createImage(width,height);
		System.out.printf("w:%d, h:%d\n", width,height);
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
		//TODO velocidad con la velocidad de plataformas
		int block = player.checkCollision(map_cont);
		switch (block) {
		case 0:	//Colision inferior
			player.setVelocity(2, 0);
			break;
		case 1:	//Colision superior
			player.setVelocity(2, 0);
			player.gravity();
			break;
		case 2:	//Colision lateral
			player.setVelocity(0, player.vy);
			player.gravity();
			break;
		case 3:
			player.setVelocity(0, 0);
			break;
		case 4:
			player.setVelocity(0, player.vy);
			player.gravity();
			break;
		case 5:
			restart();
			break;			
		default:
			player.gravity();
			break;
		}
		//Mueve plataformas
		map_cont.move();	
		player.move();
	}

	private void restart(){
		map_cont = new MapController(width,height);
		loadImages();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
			player.jump();
			System.out.println("Up key pressed");
		}
	}

}
