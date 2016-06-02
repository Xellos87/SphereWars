package videogame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import map.MapController;
import menu.EndMenu;
import menu.PauseMenu;
import scoreboard.GameScore;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Game.java
 * 
 * Comentarios: Clase que ejecuta en juego en las diferentes formas 2D y 3D
 * 
 */
@SuppressWarnings("serial")
public class Game extends JLayeredPane implements Runnable {
	// Modos de juego
	public final static int MODE_2D = 0;
	public final static int MODE_3D = 1;
	// Tipos de juego
	public final static int RUNNER = 0;
	public final static int COINS = 1;
	public final static int TIME = 2;
	// Alto del marcador
	private int height_score = 50 * Constants.scale;
	// Alto de la pantalla de juego
	private int height_game;
	// Altura y anchura de la pantalla
	private int width;
	private int height;
	// Juego en 2D y 3D
	private Game2D game2d_1p, game2d_2p;
	private Game3D game3d;
	// Tabla de puntuacion
	private GameScore score;
	// Menu de pausa
	private PauseMenu pause;
	// Menu de fin de juego
	private EndMenu end;
	// Modo actual del juego
	private int mode;
	// Tipo del juego
	private int type;
	// Numero de jugadores
	private int num_players;
	// Mapas para cada jugador
	private MapController map_p1;
	private MapController map_p2;
	// Flag para saber si los jugadores estan muertos
	private boolean death_p1;
	private boolean death_p2;
	// Metodo main
	private Main main;
	private boolean isPause;
	// Variables de ejecución del hilo
	private boolean running = true;
	private int ActionsForSecond = 30 * Constants.speedActions;
	private long targetTime = 1000000000 / ActionsForSecond; // ns
	private Thread thread;

	// Tiempos de control de bucle
	long start;
	long elapsed;
	long wait;

	/**
	 * Constructor
	 * 
	 * @param width, ancho de la pantalla
	 * @param height, alto de la pantalla
	 * @param type, tipo de juego
	 * @param num_players, número de jugadores
	 * @param main, metodo main
	 */
	public Game(int width, int height, int type, int num_players, Main main) {
		// Constants.sound = false;
		// this.setl
		this.width = width;
		this.height = height;
		this.mode = Constants.visualMode;
		this.type = type;
		this.main = main;
		this.num_players = num_players;
		this.height_game = (height - height_score) / num_players;
		this.death_p1 = false;
		this.death_p2 = num_players == 1;
		this.isPause = false;
		/////////////////////////////////////////
		main.window.dispose();
		main.window = new JFrame(main.TITLE);
		main.window.setPreferredSize(new Dimension(width, height));
		main.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.window.setLayout(new BorderLayout());
		// BoxLayout boxLayout = new BoxLayout(window.getContentPane(),
		// BoxLayout.Y_AXIS); // top to bottom
		// window.setLayout(boxLayout);
		main.window.setResizable(false);
		main.window.setVisible(true);
		main.window.addKeyListener(main);
		main.window.add(this);
		// Ajusta el tamaño de la ventana según los componentes
		main.window.pack();
		main.window.setVisible(true);
		main.window.requestFocus();
		///////////////////////////////////
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);
		initGame();
	}

	/**
	 * Inicializa el juego
	 */
	private void initGame() {
		// Menu de pausa
		pause = new PauseMenu(width, height,main.getPanel());
		pause.setBounds(0, 0, width, height);
		pause.setVisible(false);
		add(pause, new Integer(0), 0);
		// Menu de fin de juego
		end = new EndMenu(width, height);
		end.setBounds(0, 0, width, height);
		end.setVisible(false);
		add(end,new Integer(0),1);
		// Inicializa el marcador
		score = new GameScore(width, height_score, num_players, type);
		score.setBounds(0, 0, width, height_score);
		// add(score, new Integer(0),2);
		//system.out.printf("w:%d, hs:%d, hg:%d\n", width, height_score, height_game);
		// Inicializa los controladores de mapas necesarios
		Constants.map_index = new ArrayList<Integer>();
		map_p1 = new MapController(width, height_game,type);
		if (mode == MODE_2D && num_players > 1) {
			map_p2 = new MapController(width, height_game,type);
		}
		main.getPanel().setMapController(map_p1);
		// Inicializa el juego
		if (mode == MODE_2D) {
			// Inicio de juego 2D, primer jugador
			game2d_1p = new Game2D(width, height_game, num_players, map_p1.getWidthBlock(), map_p1.getHeightBlock(),
					main.getPanel(), main.music);
			game2d_1p.setBounds(0, height_score, width, height_game);
			add(game2d_1p, new Integer(0), 3);
			if (num_players > 1) {
				// Segundo jugador si lo hay
				game2d_2p = new Game2D(width, height_game, num_players, map_p1.getWidthBlock(), map_p1.getHeightBlock(),
						main.getPanel(),null);
				game2d_2p.setBounds(0, height_score + height_game, width, height_game);
				add(game2d_2p, new Integer(0), 4);
			}
		} else if (mode == MODE_3D) {
			// Inicio de juego en 3D
			game3d = new Game3D(width, height_game, map_p1, main);
			game3d.setBounds(0, height_score, width, height_game);
			add(game3d, new Integer(0), 3);
		}
		// Activamos el hilo de acciones del juego
		if (thread == null) {
			thread = new Thread(this);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}

	}

	/**
	 * Dibuja el juego, crea un doble buffer para evitar parpadeos
	 */
	public void draw() {
		Image offscreen = createImage(width, height);
		Graphics2D g2d = (Graphics2D) offscreen.getGraphics();
		if(mode == MODE_2D) {
			game2d_1p.draw(g2d, 0, height_score, map_p1, Constants.gameState != Constants.PAUSE);
			if (num_players > 1) {
				game2d_2p.draw(g2d, 0, height_score + height_game, map_p2, Constants.gameState != Constants.PAUSE);
			}
		}else if(mode == MODE_3D && Constants.gameState == Constants.GAME) {
			
		}
		score.draw(g2d);
		//Comprueba si el juego esta en pausa
		if (Constants.gameState == Constants.PAUSE) {
			if (Constants.visualMode == MODE_3D) {
				BufferedImage img3D = game3d.createBufferedImageFromCanvas3D();
				g2d.drawImage(img3D, 0, height_score, null);
			}
			pause.setVisible(true);
			pause.draw(g2d);
			isPause = true;
		} else {
			pause.setVisible(false);
			isPause = false;
		}
		if (end.isVisible()) {
			if (Constants.visualMode == MODE_3D) {
				BufferedImage img3D = game3d.createBufferedImageFromCanvas3D();
				g2d.drawImage(img3D, 0, height_score, null);
			}
			end.draw(g2d);
		}
		getGraphics().drawImage(offscreen, 0, 0, width, height, null);
		getGraphics().dispose();
	}

	/**
	 * 
	 * @return true si y solo si se puede poner en pausa
	 */
	public boolean showPause() {
		if (!death_p1 || !death_p2) {
			Constants.optionSelect = -1;
			pause.setVisible(true);
			return true;
		}
		return false;
	}

	/**
	 * Oculta el menu de pausa
	 */
	public void hiddenPause() {
		pause.setVisible(false);
	}

	/**
	 * Acción del juego
	 */
	public void actionGame() {
		if (Constants.visualMode == MODE_2D) {
			death_p1 = game2d_1p.actionGame(0, height_score, map_p1);
			if (num_players > 1) {
				death_p2 = game2d_2p.actionGame(0, height_score + height_game, map_p2);
				if (death_p1) {
					game2d_2p.deathOtherPlayer();
				}
				if (death_p2) {
					game2d_1p.deathOtherPlayer();
				}
			}
			// Obtiene las puntuaciones
			if (type == RUNNER) {
				double dist = game2d_1p.getDistance();
				score.setScoreDistanceP1(dist);
				if (num_players > 1) {
					dist = game2d_2p.getDistance();
					score.setScoreDistanceP2(dist);
				}
			} else if (type == COINS) {
				int coins = game2d_1p.getCoins();
				score.setScoreCoinsP1(coins);
				if (num_players > 1) {
					coins = game2d_2p.getCoins();
					score.setScoreCoinsP2(coins);
				}
			}
		} else if (Constants.visualMode == MODE_3D) {
			// Realiza el movimiento del mapa y acciones del jugador
			death_p1 = game3d.actionGame(0, height_score);
			// Obtiene las puntuaciones
			if (type == RUNNER) {
				double dist = game3d.getDistance();
				score.setScoreDistanceP1(dist);
			} else if (type == COINS) {
				int coins = game3d.getCoins();
				score.setScoreCoinsP1(coins);
			}
		}
		if (!end.isVisible() && death_p1 && death_p2) {
			// Todos los jugadores has muerto, se pone el menu de fin de juego
			// Extraemos las puntuaciones de cada jugador
			double score_p1 = 0;
			double score_p2 = 0;
			if (mode == MODE_2D) {
				if (type == Game.RUNNER) {
					score_p1 = game2d_1p.getDistance();
				} else if (type == Game.COINS) {
					score_p1 = game2d_1p.getCoins();
				}
				if (num_players > 1) {
					if (type == Game.RUNNER) {
						score_p2 = game2d_2p.getDistance();
					} else if (type == Game.COINS) {
						score_p2 = game2d_2p.getCoins();
					}
				}
			} else if (mode == MODE_3D) {
				if (type == Game.RUNNER) {
					score_p1 = game3d.getDistance();
				} else if (type == Game.COINS) {
					score_p1 = game3d.getCoins();
				}
			}
			// Inicializamos la pantalla de fin de juego
			end.init(num_players, type, score_p1, score_p2);
			end.setVisible(true);
		}
	}

	/**
	 * Ejecuta los eventos de teclado
	 * 
	 * @param e, evento de teclado
	 * @return opción seleccionada
	 */
	public int keyPressed(KeyEvent e) {
		int response = 0;
		if (pause.isVisible()) {
			// Menu de pausa abierto, se propaga
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				pause.cursorDown();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				pause.cursorUp();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String opc = pause.cursorEnter();
				if (Integer.parseInt(opc) == PauseMenu.CONTINUE) {
					response = PauseMenu.CONTINUE;
				} else if (Integer.parseInt(opc) == PauseMenu.RESTART) {
					restartGame();
					response = PauseMenu.RESTART;
				} else if (Integer.parseInt(opc) == PauseMenu.QUIT) {
					response = PauseMenu.QUIT;
				}
			}
		} else if (end.isVisible()) {
			// Menu de fin abierto, se realizan las acciones
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				end.cursorDown();
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				end.cursorUp();
			} else {
				int opt = end.keyPressed(e);
				switch (opt) {
				case EndMenu.RESTART:
					restartGame();
					break;
				case EndMenu.REPEAT:
					reinitGame();
					break;
				case EndMenu.QUIT:
					response = EndMenu.QUIT;
					end.setVisible(false); 
					break;
				}
			}
		} else {
			if (e.getKeyCode() == Constants.teclaSaltop1 || e.getKeyCode() == Constants.teclaSprintp1) {
				if (mode == MODE_2D) {
					//system.out.println("Tecla pulsada");
					game2d_1p.keyPressed(e, map_p1);
				} else if (mode == MODE_3D) {

				}
			}
			if (num_players > 1
					&& (e.getKeyCode() == Constants.teclaSaltop2 || e.getKeyCode() == Constants.teclaSprintp2)) {
				if (mode == MODE_2D) {
					//system.out.println("Tecla pulsada");
					game2d_2p.keyPressed(e, map_p2);
				}
			}
		}
		return response;
	}

	/**
	 * Eventos de teclado al soltar la tecla
	 * 
	 * @param e, evento de teclado
	 */
	public void keyReleased(KeyEvent e) {
		if(mode == MODE_2D){
			if(e.getKeyCode() == Constants.teclaSprintp1){
				game2d_1p.keyReleased(e,map_p1);
			}else if (e.getKeyCode() == Constants.teclaSprintp2 && Constants.numJugadores>1){
				game2d_2p.keyReleased(e, map_p2);
			}
		}
	}
	
	/**
	 * Reinicializa el juego
	 */
	private void restartGame() {
		Constants.map_index = new ArrayList<Integer>();
		reinitGame();
	}

	/**
	 * 
	 * @return true si y solo si el end esta visible
	 */
	public boolean isEndMenu() {
		return end.isVisible();
	}

	/**
	 * Reinicializa el juego
	 */
	private void reinitGame() {
		if (Constants.visualMode == MODE_2D) {
			map_p1 = new MapController(width, height_game, type);
			game2d_1p.restart(map_p1);
			if (num_players > 1) {
				map_p2 = new MapController(width, height_game, type);
				game2d_2p.restart(map_p2);
			}
		} else if (Constants.visualMode == MODE_3D) {
			map_p1 = new MapController(width, height_game, type);
			//game3d = new Game3D(width, height_game, map_p1, main);
			//game3d.setBounds(0, height_score, width, height_game);
			//add(game3d, new Integer(0), 3);
			game3d.restart(map_p1);
		}
		death_p1 = false;
		death_p2 = num_players == 1;
		end.setVisible(false);
	}

	@Override
	public void run() {

		while (running) {
			start = System.nanoTime();
			if (Constants.gameState == Constants.GAME && (!death_p1 || !death_p2)) {
				actionGame();
			}
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed;
			// Duerme el hilo hasta la siguiente actualizaciÃ³n
			try {
				if (wait > 0)
					Thread.sleep(wait / 1000000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

	}

	/**
	 * Quita el listener antes de cerrar el juego
	 */
	public void quit() {
		setVisible(false);
		if(Constants.visualMode == Game.MODE_3D){
			game3d.removeKeyListener(game3d);
			game3d.setFocusable(false);
		}
	}
}