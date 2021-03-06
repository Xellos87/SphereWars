package videogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import audio.Music;
import character.Boss;
import character.Sphere;
import kinect.Panel;
import map.MapController;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Game2D.java
 * 
 * Comentarios: Clase principal del videojuego en 2D
 * 
 */
@SuppressWarnings("serial")
public class Game2D extends JPanel {
	private int width,height;
	//Fondo con efecto Parallax que da sensación de movimiento
	private Parallax back_parallax;
	//Contenedor del jugador
	private Sphere player;
	private Boss boss;
	//Flag que indica si la partida ha acabado
	private boolean end_game;
	//Numero de jugadores
	private int num_player;
	//Valor de alfa para oscurecimiento de pantalla por muerte y valor maximo
	private int alpha_death;
	private final int MAX_ALPHA = 100;
	private final String DEATH_STR = "Has muerto...";
	//Indica si se ha de esperar a la muerte de otro jugador
	private boolean wait_other_player;
	//Puntuaciones para mostrar en scoreboard
	private double score_distance;
	private int score_coins;
	private int score_time; //TODO, eliminar si no se implementa - modo contrarreloj
	private int blockWidth;
	private int blockHeight;
	private Panel panel;
	//Musica del juego
	Music music;

	/**
	 * Constructor
	 * 
	 * @param width, ancho de la pantalla
	 * @param height, alto de la pantalla
	 * @param num_player, numero de jugadores
	 * @param widthBlock, ancho de bloque
	 * @param heightBlock, alto de bloque
	 * @param panel, objeto para interactuar con el kinect
	 * @param music, musica del juego
	 */
	public Game2D(int width, int height, int num_player, int widthBlock, int heightBlock, Panel panel, Music music){
		this.width = width;
		this.height = height;
		this.end_game = false;
		this.alpha_death = 0;
		this.num_player = num_player;
		this.wait_other_player = num_player > 1;
		this.blockWidth=widthBlock;
		this.blockHeight=heightBlock;
		this.panel = panel;
		this.music = music;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);
		loadImages();
		init_score();
	}

	/**
	 * Inicializa los marcadores
	 */
	private void init_score() {
		this.score_distance = 0;
		this.score_coins = 0;
	}

	/**
	 * Carga las imagenes del parallax, jugador y boss
	 */
	private void loadImages() {
		/* Carga el efecto de fondo Parallax */
		int num = 1;
		String back_images[] = {"images/sky_clear_extended.png"};
		int velocity[] = {2};
		int posX[] = {0};
		int posY[] = {0};
		back_parallax = new Parallax(num, back_images, velocity, posX, posY, width, height);
		/* Carga el personaje en pantalla con su posición */
		double size = blockWidth*0.8;
		player = new Sphere(0,0,(int)size,(int)size, num_player);
		panel.setSphere(player);
		//carga el boss
		boss = new Boss(width-90,20,blockWidth,blockHeight,false, width, height, music);
	}

	/**
	 * Dibuja la escena en el doble buffer
	 * 
	 * @param g2d, doble buffer en el que se dibuja
	 * @param x_ori, x de origen desde donde se pinta
	 * @param y_ori, y de origen desde donde se pinta
	 * @param map_cont, controlador del mapa
	 * @param not_pause, flag que indica si esta en pausa o no
	 */
	public void draw(Graphics2D g2d,int x_ori, int y_ori, MapController map_cont, boolean not_pause) {
		//Efecto Parallax(fondo movil)
		back_parallax.draw(g2d,x_ori,y_ori);
		//Mapa
		map_cont.draw2D(g2d,x_ori,y_ori,not_pause);
		if(end_game && wait_other_player){
			if(alpha_death < MAX_ALPHA){
				alpha_death+=5;
			}
			g2d.setColor(new Color(0,0,0,alpha_death));
			g2d.fillRect(x_ori, y_ori, width, height);
			Font f = Constants.font_bold.deriveFont(36.0f);
			g2d.setFont(f);
			g2d.setColor(new Color(255,255,255,alpha_death));
			int width_text = g2d.getFontMetrics().stringWidth(DEATH_STR);
			int height_text = g2d.getFontMetrics().getHeight();
			g2d.drawString(DEATH_STR, x_ori + width/2 - width_text/2, y_ori + height/2 - height_text/2);
		}
		//Jugador
		player.draw2D(g2d,x_ori,y_ori);
		if(boss.isVisible()){
			boss.draw2D(g2d, x_ori, y_ori);
		}
	}


	/**
	 * Acción de juego
	 * 
	 * @param x_ori, x de origen desde donde se pinta
	 * @param y_ori, y de origen desde donde se pinta
	 * @param map_cont, controlador del mapa
	 * @return true si y solo si el jugador ha muerto
	 */
	public boolean actionGame(int x_ori,int y_ori,MapController map_cont) {
		//TODO si el juego acaba se puede hacer una acción de mover el jugador por las Y
		//hasta que se salga de la pantalla
		if(!end_game){
			/* Accion del parallax */
			back_parallax.move();
			//Mueve plataformas
			score_distance += map_cont.move();
			/* Acciones a realizar */
			//TODO velocidad con la velocidad de plataformas
			int block = player.checkCollision(map_cont,x_ori,y_ori);
			switch (block) {
			case Sphere.COLLINF:	//Colision inferior
				player.setVelocity(2, 0);
				break;
			case Sphere.COLLSUP:	//Colision superior
				player.setVelocity(2, 0);
				player.gravity();
				break;
			case Sphere.COLLLAT:	//Colision lateral
				player.setVelocity(0, player.vy);
				player.gravity();
				break;
			case Sphere.COLLINFLAT:
				player.setVelocity(0, 0);
				break;
			case Sphere.COLLSUPLAT:
				player.setVelocity(0, player.vy);
				player.gravity();
				break;
			case Sphere.COLLDEATH:
				end_game = true;
				//restart(map_cont);
				break;
			case Sphere.COLLKILL:
				player.miniJump();
				break;
			case Sphere.COLLGET:
				score_coins += map_cont.removeTresure(player,block,x_ori,y_ori);
				player.gravity();
				break;
			default:
				player.setVelocity(2, player.vy);
				player.gravity();
				break;
			}	
			map_cont.updateMap(x_ori, y_ori,  Constants.gameState != Constants.PAUSE);
			player.move();
			if(end_game){
				//animacion de muerte
				player.setVelocity(0, -15);
			}
			//TODO: separar movimiento de accion y permitir que el boss se siga moviendo en el draw?
			int col = boss.action(true, player.x, player.y, player.getBox(x_ori, y_ori));
			if(col==0){
				//Colision con el jefe, comprobar si es colision de muerte
				if(boss.isVisible() && boss.collides && player.bossCollision(boss.getBox(x_ori, y_ori))){
					end_game=true;
				} 
			}else if(col == 1){
				player.miniJump();
			}
		}
		if(end_game && player.y<height){
			//animacion de muerte
			player.gravity();
			player.move();
		}
		return end_game;
	}

	/**
	 * Reinicializa el juego
	 * 
	 * @param map_cont, controlador del mapa
	 */
	public void restart(MapController map_cont){
		map_cont.restart();
		loadImages();
		init_score();
		if(music != null && music.isBossMusicPlaying()) music.playGame();
		end_game = false;
		alpha_death = 0;
		wait_other_player = num_player > 1;
	}

	/**
	 * Trata las entradas por teclado
	 * 
	 * @param e, eventos de teclado
	 * @param map_cont, controlador del mapa
	 */
	public void keyPressed(KeyEvent e, MapController map_cont) {
		if(e.getKeyCode() == Constants.teclaSaltop1 || e.getKeyCode() == Constants.teclaSaltop2){
			player.jump();
			//System.out.println("Up key pressed");
		}else if(e.getKeyCode() == Constants.teclaSprintp1){
			Constants.sprintp1 = true;
			map_cont.setSpeedHigh();
		}else if(e.getKeyCode() == Constants.teclaSprintp2){
			Constants.sprintp2 = true;
			map_cont.setSpeedHigh();
		}
	}

	/**
	 * 
	 * @return número de monedas
	 */
	public int getCoins(){
		return score_coins;
	}

	/**
	 * 
	 * @return la distancia
	 */
	public double getDistance(){
		return score_distance;
	}

	/**
	 * Indica que el otro jugador ha muerto
	 */
	public void deathOtherPlayer() {
		wait_other_player = false;
	}

	/**
	 * Trata los eventos al soltar el teclado
	 * 
	 * @param e, evento de teclado
	 * @param map_cont, controlador del mapa
	 */
	public void keyReleased(KeyEvent e, MapController map_cont) {
		if(e.getKeyCode() == Constants.teclaSprintp1){
			Constants.sprintp1 = false;
			map_cont.setSpeedLow();
		}else if(e.getKeyCode() == Constants.teclaSprintp2){
			Constants.sprintp2 = false;
			map_cont.setSpeedLow();
		}
	}

}
