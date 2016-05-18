package videogame;

import java.awt.Dimension;
import java.awt.Graphics2D;
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
	

	public Game2D(int width, int height){
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);

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

	public void draw(Graphics2D g2d,int x_ori, int y_ori, MapController map_cont, boolean not_pause) {
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		//Image offscreen = createImage(width,height);
		//Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		/* Dibuja todo en pantalla */
		//Efecto Parallax(fondo movil)
		back_parallax.draw(g2d,x_ori,y_ori);
		//Jugador
		player.draw2D(g2d,x_ori,y_ori);
		//Mapa
		map_cont.draw2D(g2d,x_ori,y_ori,not_pause);

		//Vuelca en el panel lo que se ha dibujado
		//getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		//getGraphics().dispose();
	}


	public void actionGame(int x_ori,int y_ori,MapController map_cont) {
		/* Accion del parallax */
		back_parallax.move();
		/* Acciones a realizar */
		//TODO velocidad con la velocidad de plataformas
		int block = player.checkCollision(map_cont,x_ori,y_ori);
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
			restart(map_cont);
			break;			
		default:
			player.gravity();
			break;
		}
		//Mueve plataformas
		map_cont.move();	
		player.move();
	}

	private void restart(MapController map_cont){
		map_cont.restart();
		loadImages();
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
			player.jump();
			System.out.println("Up key pressed");
		}
	}

}
