package videogame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import audio.Music;
import media.ImageHandler;
import menu.EndMenu;
import menu.Menu;
import menu.OptionMenu;
import menu.PauseMenu;
import menu.TitleMenu;
import scoreboard.GameScore;
import scoreboard.Ranking;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla EspaÃ±ol - 557022,
 * 			Juan Luis Burillo OrtÃ­n - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira LÃ³pez-Echazarreta - 666800
 * 	
 * Clase: Main.java
 * 
 * Comentarios: Clase principal del videojuego
 * 
 */
public class Main implements Runnable, KeyListener{
	private static JFrame window;
	
	private static final String TITLE = "Sphere Wars";

	//Dimensiones del juego
	private static int width = 320;
	private static int height = 240;	


	//Thread del juego
	private boolean running;
	private int FPS = 30;
	private long targetTime = 1000000000/FPS; //ns
	private Thread thread;

	//Tiempos de control de bucle
	long start;
	long elapsed;
	long wait;

	//Estado del juego
	int state;
	boolean pause = false;

	// Musica
	Music music;

	//Menu del juego
	private Menu menu;	//TODO: este menu puede cambiar entre menu de titulo, de pausa, de opciones?
	//TODO: de titulo y de opciones si, de pausa segun se requiera
	//Modo de juego
	private Game game;

	public static void main(String[] args){
		new Main();
		//window.getContentPane().setLayout(game3d);
	}

	private Main() {
		Constants.img_handler = new ImageHandler();
		Constants.ranking = new Ranking();
		Constants.sound = false;
		init();
	}
	private void init() {
		/* Crea el contenedor para poner los paneles */
		window = new JFrame(TITLE);
		window.setPreferredSize(new Dimension(width*Constants.scale, height*Constants.scale));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		//BoxLayout boxLayout = new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
		//window.setLayout(boxLayout);
		window.setResizable(false);
		window.setVisible(true);
		window.addKeyListener(this);
		running = true;

		if(thread == null){
			thread = new Thread(this);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
		menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
		menu.setDoubleBuffered(true);
		state = Constants.MENU;
		window.add(menu);
		//Ajusta el tamaño de la ventana según los componentes
		window.pack();
		window.setVisible(true);
		window.requestFocus();
		music = new Music();
		music.playMenu();
	}

	@Override
	public void run() {

		while(running){
			start = System.nanoTime();
			if(!pause){
				//TODO: actualizaciÃ³n del juego
				if(game != null){
					game.actionGame();
				}
			}
			draw();
			// Funcion que comprueba el tiempo que le queda a la cancion
			// para hacer el fade in out
			if(music != null) music.update();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed;
			//Duerme el hilo hasta la siguiente actualizaciÃ³n
			try {
				if(wait>0) Thread.sleep(wait/1000000);
			}
			catch(Exception e) {
				System.out.printf("start: %d\n", start);
				System.out.printf("elapsed: %d\n", elapsed);
				System.out.printf("wait: %d\n", wait);
				System.out.printf("target: %d\n", targetTime);
				e.printStackTrace();
			};
		}
	}	

	private void draw() {
		//TODO comprobar si es 2D o 3D
		if(menu != null && Constants.enMenu){
			menu.draw();

		}
		if(game != null && !Constants.enMenu){
			game.draw(!pause);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (state) {
		case Constants.MENU:
			if(!Constants.esperandoTecla){
				if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
					menu.cursorDown();
					System.out.println("Down key pressed");
				}else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
					menu.cursorUp();
					System.out.println("Up key pressed");
				}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					System.out.println("right key pressed");
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						menu.cursorRight();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					System.out.println("left key pressed");
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						menu.cursorLeft();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_ENTER){	//pulsacion de enter
					String nuevoMenu = menu.cursorEnter();
					System.out.println("Enter pressed. Option: "+nuevoMenu);
					//desde el menu principal
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						//TODO: acciones cuando se selecciona una opcion del menu principal
						if(!Constants.cursorDesplazado){
							if(nuevoMenu.equalsIgnoreCase("credits")){

							}else if(nuevoMenu.equalsIgnoreCase("start")){
								Constants.enMenu = false;
								if(music != null){
									music.playGame();
								}else{
									System.err.println("Richard no le des tan rapido");
								}
								game = new Game(width*Constants.scale, height*Constants.scale, Game.MODE_2D, Game.COINS, 1);
								window.remove(menu);
								window.revalidate();
								window.add(game,BorderLayout.CENTER);
								window.pack();
								state = Constants.GAME;
								pause = false;
							}else if(nuevoMenu.equalsIgnoreCase("exit")){
								System.exit(0);
							}else if(nuevoMenu.equalsIgnoreCase("options")){
								menu = new OptionMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu = Constants.optMenu;
								menu.setDoubleBuffered(true);
								state = Constants.MENU;	//redundante
								window.add(menu, BorderLayout.CENTER);
								window.pack();
							}else if(nuevoMenu.equalsIgnoreCase("help")){

							}
						}else {
							if(Constants.visualMode.equalsIgnoreCase("2D")){
								Constants.visualMode="3D";
							}else{
								Constants.visualMode="2D";
							}
						}
						//desde el menu de opciones
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.optMenu)){
						//TODO: poner y quitar el sonido
						if(nuevoMenu.equalsIgnoreCase("sound")){
							System.out.println(Constants.sound);
							Constants.sound = !Constants.sound;
						}
						else if(nuevoMenu.equalsIgnoreCase("resolution")){
							System.out.println(Constants.scale);
							if(Constants.scale == 2){
								Constants.scale = 4;
								Constants.ax=400;
								Constants.ay=300;
							}else if(Constants.scale == 4){
								Constants.scale = 2;
								Constants.ax=0;
								Constants.ay=0;
							}
							window.setPreferredSize(new Dimension(width*Constants.scale, height*Constants.scale));
							menu = new OptionMenu(width*Constants.scale, height*Constants.scale);
							menu.setDoubleBuffered(true);
							window.add(menu, BorderLayout.CENTER);
							window.pack();
						}					
						else if(nuevoMenu.equalsIgnoreCase("controller")){
							if(!Constants.elegidoJugador){
								Constants.elegidoJugador=true;
								if(menu instanceof OptionMenu){
									((OptionMenu)menu).cambiarCursor();
								}
							}else{
								if(Constants.jugador==1){
									Constants.jugador = 2;
								}else{
									Constants.elegidoJugador=false;
									Constants.jugador=1;
									if(menu instanceof OptionMenu){
										((OptionMenu)menu).cambiarCursor();
									}
								}
							}
						}
						//TODO: cambio entre controlador Kinect y teclado (en juego)
						else if(nuevoMenu.equalsIgnoreCase("device")){
							Constants.conTeclado = !Constants.conTeclado;
						}
						else if(nuevoMenu.equalsIgnoreCase("back")){
							System.out.println("back to main menu");
							menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
							Constants.tipoMenu = Constants.titMenu;
							menu.setDoubleBuffered(true);
							state = Constants.MENU;	//redundante
							window.add(menu, BorderLayout.CENTER);
							window.pack();
						}
						else if(nuevoMenu.equalsIgnoreCase("pause")){
							if(Constants.jugador==1){
								Constants.teclaPausap1=Constants.guion;
							}else{
								Constants.teclaPausap2=Constants.guion;
							}
							Constants.esperandoTecla=true;
						}else if(nuevoMenu.equalsIgnoreCase("jump")){
							if(Constants.jugador==1){
								Constants.teclaSaltop1=Constants.guion;
							}else{
								Constants.teclaSaltop2=Constants.guion;
							}
							Constants.esperandoTecla=true;
						}else if(nuevoMenu.equalsIgnoreCase("run")){
							if(Constants.jugador==1){
								Constants.teclaSprintp1=Constants.guion;
							}else{
								Constants.teclaSprintp2=Constants.guion;
							}
							Constants.esperandoTecla=true;
						}
					}
					//TODO: resto de casos
				}
			}else{
				if(Constants.teclaPausap1==Constants.guion){
					Constants.teclaPausap1 = e.getKeyCode();
				}else if(Constants.teclaPausap2==Constants.guion){
					Constants.teclaPausap2 = e.getKeyCode();
				}else if(Constants.teclaSaltop1==Constants.guion){
					Constants.teclaSaltop1 = e.getKeyCode();
				}else if(Constants.teclaSaltop2==Constants.guion){
					Constants.teclaSaltop2 = e.getKeyCode();
				}else if(Constants.teclaSprintp1==Constants.guion){
					Constants.teclaSprintp1 = e.getKeyCode();
				}else if(Constants.teclaSprintp2==Constants.guion){
					Constants.teclaSprintp2 = e.getKeyCode();
				}
			}			
			break;
		case Constants.GAME:			
			if(e.getKeyCode() == Constants.teclaPausap1 || e.getKeyCode() == Constants.teclaPausap2){
				pause = true;
				state = Constants.PAUSE;
				game.showPause();
				System.out.println("PAUSA!");				
			}else{
				int option = game.keyPressed(e);
				if(option == EndMenu.QUIT){
					System.out.println("exit pulsado en menu end");
					menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
					Constants.enMenu = true;
					Constants.tipoMenu = Constants.titMenu;
					menu.setDoubleBuffered(true);
					state = Constants.MENU;
					window.add(menu, BorderLayout.CENTER);
					window.pack();
					music.playMenu();
				}
			}
			break;
		case Constants.PAUSE:
			if(e.getKeyCode() == Constants.teclaPausap1 || e.getKeyCode() == Constants.teclaPausap2){
				pause = false;
				state = Constants.GAME;
				game.hiddenPause();			
			}else{
				int option = game.keyPressed(e);
				if(option == PauseMenu.CONTINUE || option == PauseMenu.RESTART){
					pause = false;
					state = Constants.GAME;
					game.hiddenPause();	
				}else if(option == PauseMenu.QUIT){
					System.out.println("exit pulsado en menu pause");
					menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
					Constants.enMenu = true;
					Constants.tipoMenu = Constants.titMenu;
					menu.setDoubleBuffered(true);
					state = Constants.MENU;
					window.add(menu, BorderLayout.CENTER);
					window.pack();
					music.playMenu();
				}
			}
		default:
			break;
		}
		//		//Duerme el hilo hasta la siguiente actualizaciÃ³n despues de despachar el evento
		//		try {
		//			Thread.sleep(wait/1000000);
		//		}
		//		catch(Exception ex) {
		//			System.out.printf("start: %d\n", start);
		//			System.out.printf("elapsed: %d\n", elapsed);
		//			System.out.printf("wait: %d\n", wait);
		//			System.out.printf("target: %d\n", targetTime);
		//			ex.printStackTrace();
		//		};
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}
}
