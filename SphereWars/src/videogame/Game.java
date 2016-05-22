package videogame;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;

import map.MapController;
import menu.EndMenu;
import menu.PauseMenu;
import scoreboard.GameScore;
import utils.Constants;

@SuppressWarnings("serial")
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
	//Menu de fin de juego
	private EndMenu end;
	//Modo actual del juego
	private int mode;
	//Tipo del juego
	private int type;
	//Numero de jugadores
	private int num_players;
	//Mapas para cada jugador
	private MapController map_p1;
	private MapController map_p2;
	//Flag para saber si los jugadores estan muertos
	private boolean death_p1;
	private boolean death_p2;
	

	public Game(int width, int height, int mode, int type, int num_players){
		this.width = width;
		this.height = height;
		this.mode = mode;
		this.type = type;
		this.num_players = num_players;
		this.height_game = (height-height_score)/num_players;
		this.death_p1 = false;
		this.death_p2 = num_players==1;
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
		//Menu de fin de juego
		end = new EndMenu(width, height);
		end.setBounds(0, 0, width, height);
		end.setVisible(false);
		add(end,new Integer(0),1);
		//Inicializa el marcador
		score = new GameScore(width, height_score, num_players, type);
		score.setBounds(0, 0, width, height_score);
		add(score, new Integer(0),2);
		System.out.printf("w:%d, hs:%d, hg:%d\n",width,height_score,height_game );
		//Inicializa el juego
		if(mode == MODE_2D){
			//Inicio de juego 2D, primer jugador
			game2d_1p = new Game2D(width, height_game,num_players);
			game2d_1p.setBounds(0, height_score, width, height_game);
			add(game2d_1p, new Integer(0),3);
			if(num_players>1){
				//Segundo jugador si lo hay
				game2d_2p = new Game2D(width, height_game,num_players);
				game2d_2p.setBounds(0, height_score+height_game, width, height_game);
				add(game2d_2p, new Integer(0),4);
			}
		}else if(mode == MODE_3D){
			//Inicio de juego en 3D
			if(num_players>1){
				
			}
		}
		//Inicializa los controladores de mapas necesarios
		Constants.map_index = new ArrayList<Integer>();
		map_p1 = new MapController(width, height_game);
		if(num_players>1){
			map_p2 = new MapController(width, height_game);
		}
	}

	public void draw(boolean not_pause){
		Image offscreen = createImage(width,height);
		Graphics2D g2d = (Graphics2D) offscreen.getGraphics();
		if(mode == MODE_2D){
			game2d_1p.draw(g2d,0,height_score,map_p1,not_pause);
			if(num_players>1){
				game2d_2p.draw(g2d,0,height_score+height_game,map_p2,not_pause);
			}
		}else if(mode == MODE_3D){
			game3d_1p.draw();
			if(num_players>1){
				game3d_2p.draw();
			}
		}
		score.draw(g2d);
		
		if(pause.isVisible()){
			pause.draw(g2d);
		}
		//if(end.isVisible()){
			end.draw(g2d);
		//}
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		getGraphics().dispose();
	}
	
	public boolean showPause(){
		if(!death_p1 || !death_p2){
			pause.setVisible(true);
			return true;
		}
		return false;
	}

	public void hiddenPause(){
		pause.setVisible(false);
	}
	
	public void actionGame(){
		if(mode == MODE_2D){
			death_p1 = game2d_1p.actionGame(0,height_score,map_p1);
			if(num_players>1){
				death_p2 = game2d_2p.actionGame(0,height_score+height_game,map_p2);
				if(death_p1){
					game2d_2p.deathOtherPlayer();
				}
				if(death_p2){
					game2d_1p.deathOtherPlayer();
				}
			}
			
			//Obtiene las puntuaciones
			if(type == RUNNER){
				double dist = game2d_1p.getDistance();
				score.setScoreDistanceP1(dist);
				if(num_players>1){
					dist = game2d_2p.getDistance();
					score.setScoreDistanceP2(dist);
				}
			}else if(type == COINS){
				int coins = game2d_1p.getCoins();
				score.setScoreCoinsP1(coins);
				if(num_players>1){
					coins = game2d_2p.getCoins();
					score.setScoreCoinsP2(coins);
				}
			}else if(type == TIME){
				
			}
		}else if(mode == MODE_3D){
			//game3d_1p.actionGame();
			if(num_players>1){
				//game3d_2p.actionGame();
			}
		}
		if(!end.isVisible() && death_p1 && death_p2){
			//Todos los jugadores has muerto, se pone el menu de fin de juego
			//Extraemos las puntuaciones de cada jugador
			double score_p1 = 0;
			double score_p2 = 0;
			if(mode == MODE_2D){
				if(type == Game.RUNNER){
					score_p1 = game2d_1p.getDistance();
				}else if(type == Game.COINS){
					score_p1 = game2d_1p.getCoins();
				}
				if(num_players>1){
					if(type == Game.RUNNER){
						score_p2 = game2d_2p.getDistance();
					}else if(type == Game.COINS){
						score_p2 = game2d_2p.getCoins();
					}
				}
			}else if(mode == MODE_3D){
				//game3d_1p.actionGame();
				if(num_players>1){
					//game3d_2p.actionGame();
				}
			}
			//Inicializamos la pantalla de fin de juego
			end.init(num_players,type,score_p1,score_p2);
			end.setVisible(true);
		}
	}

	//TODO: teclas para el segundo jugador y menu de pausa y fin de juego
	  public void keyPressed(KeyEvent e) {
	    if(pause.isVisible()){
	      //Menu de pausa abierto, se propaga
	      
	    }else if(end.isVisible()){
	      //Menu de fin abierto, se realizan las acciones
	      if(e.getKeyCode() == KeyEvent.VK_DOWN){
	        end.cursorDown();
	      }else if(e.getKeyCode() == KeyEvent.VK_UP){
	        end.cursorUp();
	      }else{
	        int opt = end.keyPressed(e);
	        switch(opt){
	        case EndMenu.RESTART:
	          
	          break;
	        case EndMenu.REPEAT:
	          
	          break;
	        case EndMenu.QUIT:
	          
	          break;
	        }
	      }
	    }else{
	      if(e.getKeyCode() == Constants.teclaSaltop1 || e.getKeyCode() == Constants.teclaSprintp1){
	        if(mode == MODE_2D){
	          System.out.println("Tecla pulsada");
	          game2d_1p.keyPressed(e);
	        }else if(mode == MODE_3D){

	        }
	      }
	      if(num_players > 1 && (e.getKeyCode() == Constants.teclaSaltop2 || e.getKeyCode() == Constants.teclaSprintp2)){
	        if(mode == MODE_2D){
	          System.out.println("Tecla pulsada");
	          game2d_2p.keyPressed(e);
	        }else if(mode == MODE_3D){

	        }
	      }
	    }
	  }
}
