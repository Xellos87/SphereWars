package videogame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import character.Boss;
import character.Sphere;
import map.MapController;

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
	//valores de movimiento aleatorio del boss (iniciales)
	private double random = 1;
	private double low = 0.5;
	private double mid = 1/3.0;
	private double air = 1 - low - mid;
	private double vuelveArriba = 1;
	private double intervalo = 3;	//segundos quieto entre cada movimiento

	public Game2D(int width, int height, int num_player, int widthBlock, int heightBlock){
		this.width = width;
		this.height = height;
		this.end_game = false;
		this.alpha_death = 0;
		this.num_player = num_player;
		this.wait_other_player = num_player > 1;
		this.blockWidth=widthBlock;
		this.blockHeight=heightBlock;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);

		loadImages();

		init_score();
	}

	private void init_score() {
		this.score_distance = 0;
		this.score_coins = 0;
	}

	private void loadImages() {
		/* Carga el efecto de fondo Parallax */
		int num = 1;
		String back_images[] = {"images/sky_clear_extended.png"};
		int velocity[] = {2};
		int posX[] = {0};
		int posY[] = {0};
		back_parallax = new Parallax(num, back_images, velocity, posX, posY, width, height);
		/* Carga el personaje en pantalla con su posición */
		player = new Sphere(0,0,30,30);
		player.setVelocity(0, 3);
		//carga el boss
		boss = new Boss(width-90,20,blockWidth,blockHeight,true, width);
	}

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
			g2d.setFont(new Font("Arial", Font.BOLD, 36));
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
		boss.action(not_pause);		
	}


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
			player.move();
			if(end_game){
				//animacion de muerte
				player.setVelocity(0, -15);
			}
		}
		if(end_game && player.y<height){
			//animacion de muerte
			player.gravity();
			player.move();
		}
		return end_game;
	}

	public void restart(MapController map_cont){
		map_cont.restart();
		loadImages();
		init_score();
		end_game = false;
		alpha_death = 0;
		wait_other_player = num_player > 1;
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
			player.jump();
			System.out.println("Up key pressed");
		}
	}

	public int getCoins(){
		return score_coins;
	}

	public double getDistance(){
		return score_distance;
	}

	public double getTime(){
		return score_time;
	}

	public void deathOtherPlayer() {
		wait_other_player = false;
	}

}
