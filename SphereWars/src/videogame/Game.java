package videogame;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLayeredPane;

import menu.PauseMenu;
import scoreboard.GameScore;
import utils.Constants;

public class Game extends JLayeredPane{
	//Modos de juego
	public final static int MODE_2D = 0;
	public final static int MODE_3D = 1;
	//Tipos de juego
	public final static int RUNNER = 0;
	public final static int COINS = 1;
	public final static int TIME = 2;
	//Alto del marcador
	private int height_score = 100;
	//Alto de la pantalla de juego
	private int height_game;
	//Altura y anchura de la pantalla
	private int width;
	private int height;
	//Juego en 2D y 3D
	private Game2D game2d_1p, game2d_2p;
	private Game3D game3d_1p, game3d_2p;
	//Tabla de puntuacion
	private GameScore score;
	//Menu de pausa
	private PauseMenu pause;
	//Modo actual del juego
	private int mode;
	//Tipo del juego
	private int type;
	//Numero de jugadores
	private int num_players;

	public Game(int width, int height, int mode, int type, int num_players){
		this.width = width;
		this.height = height;
		this.mode = mode;
		this.type = type;
		this.num_players = num_players;
		this.height_game = (height/num_players) - height_score;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);
		initGame();
	}

	private void initGame() {
		//Menu de pausa
		pause = new PauseMenu(width, height);
		pause.setBounds(0, 0, width, height);
		pause.setVisible(false);
		add(pause, new Integer(0),0);
		//Inicializa el marcador
		score = new GameScore(width, height_score, num_players, type);
		score.setBounds(0, 0, width, height_score);
		add(score, new Integer(0),1);
		System.out.printf("w:%d, hs:%d, hg:%d\n",width,height_score,height_game );
		//Inicializa el juego
		if(mode == MODE_2D){
			//Inicio de juego 2D
			game2d_1p = new Game2D(width, height_game);
			game2d_1p.setBounds(0, height_score, width, height_game);
			add(game2d_1p, new Integer(0),2);
			if(num_players>1){

			}
		}else if(mode == MODE_3D){
			//Inicio de juego en 3D
			if(num_players>1){

			}
		}
	}

	public void draw(boolean not_pause){
		Image offscreen = createImage(width,height);
		Graphics2D g2d = (Graphics2D) offscreen.getGraphics();
		if(mode == MODE_2D){
			System.out.println("Pintamos el tablero");
			game2d_1p.draw(g2d,0,height_score,not_pause);
			if(num_players>1){

			}
		}else if(mode == MODE_3D){
			game3d_1p.draw();
			if(num_players>1){

			}
		}
		score.draw(g2d);
		
		if(pause.isVisible()){
			pause.draw(g2d);
		}
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		getGraphics().dispose();
	}
	
	public void showPause(){
		pause.setVisible(true);
	}

	public void hiddenPause(){
		pause.setVisible(false);
	}
	
	public void actionGame(){
		if(mode == MODE_2D){
			game2d_1p.actionGame(0,height_score);
			if(num_players>1){

			}
		}else if(mode == MODE_3D){
			//game3d_1p.actionGame();
			if(num_players>1){

			}
		}
	}


	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
			if(mode == MODE_2D){
				System.out.println("Tecla pulsada");
				game2d_1p.keyPressed(e);
			}else if(mode == MODE_3D){
				
			}
		}
	}

	public void drawPause() {
		pause.draw();
	}

}
