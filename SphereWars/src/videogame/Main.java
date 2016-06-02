package videogame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import audio.Music;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import kinect.Kinect;
import kinect.Panel;
import media.ImageHandler;
import menu.CreditsMenu;
import menu.EndMenu;
import menu.GameModeMenu;
import menu.HelpMenu;
import menu.Menu;
import menu.OptionMenu;
import menu.PauseMenu;
import menu.TitleMenu;
import scoreboard.Ranking;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla Españl - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Main.java
 * 
 * Comentarios: Clase principal del videojuego
 * 
 */
public class Main implements Runnable, KeyListener{
	public static JFrame window;
	
	public static final String TITLE = "Sphere Wars";

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

	// Musica
	Music music;

	//Menu del juego
	private Menu menu;	
	//Modo de juego
	private Game game;
	
	// Clases para kinect
	private Kinect kinect;
	private Panel panel;

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

		// Inicializamos kinect
		panel = new Panel(1, true);
		kinect = new Kinect();
		kinect.setPanel(panel);
		kinect.start(J4KSDK.COLOR | J4KSDK.DEPTH | J4KSDK.SKELETON);
		
		if(thread == null){
			thread = new Thread(this);
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.start();
		}
		menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
		menu.setDoubleBuffered(true);
		Constants.gameState = Constants.MENU;
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
		int track = 0;
		int pausedKinect = 0;
		//Constants.conTeclado = false;
		while(running){
			start = System.nanoTime();
			/*if(Constants.gameState == Constants.GAME){
				if(game != null){
					game.actionGame();
				}
			}*/
			if(game != null){
				track = panel.trackHand();
				if(track == 1 && Constants.gameState == Constants.GAME && !game.isEndMenu()){
					
					Constants.gameState = Constants.PAUSE;
					game.showPause();
					pausedKinect = 1;
					System.err.println("PAUSEANDO");
				}else if(track != 1 && pausedKinect == 1){

					System.err.println("Despausa");
					Constants.gameState = Constants.GAME;
					game.hiddenPause();
					pausedKinect = 0;
				}
				track = 0;
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
		if(menu != null && Constants.enMenu){
				menu.draw();
		}
		if(game != null && !Constants.enMenu){
			game.draw();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Constants.gameState) {
		case Constants.MENU:
			if(!Constants.esperandoTecla){
				if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
					menu.cursorDown();
					//system.out.println("Down key pressed");
				}else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
					menu.cursorUp();
					//system.out.println("Up key pressed");
				}else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					//system.out.println("right key pressed");
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						menu.cursorRight();
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.helMenu)){
						menu.cursorRight();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
					//system.out.println("left key pressed");
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						menu.cursorLeft();
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.helMenu)){
						menu.cursorLeft();
					}
				}else if(e.getKeyCode() == KeyEvent.VK_ENTER){	//pulsacion de enter
					String nuevoMenu = menu.cursorEnter();
					//system.out.println("Enter pressed. Option: "+nuevoMenu);
					//desde el menu principal
					if(Constants.tipoMenu.equalsIgnoreCase(Constants.titMenu)){
						if(!Constants.cursorDesplazado){
							if(nuevoMenu.equalsIgnoreCase("credits")){
								menu = new CreditsMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu=Constants.creMenu;
								menu.setDoubleBuffered(true);
								Constants.gameState = Constants.MENU;
								window.add(menu, BorderLayout.CENTER);
								window.pack();
							}else if(nuevoMenu.equalsIgnoreCase("start")){
								Constants.numJugadores = 1;
								Menu newmenu = new GameModeMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu = Constants.modMenu;
								newmenu.setDoubleBuffered(true);
								Constants.gameState = Constants.MENU;	
								//window.remove(menu);
								menu = newmenu;
								//window.revalidate();
								window.add(newmenu, BorderLayout.CENTER);
								window.pack();
							}else if(nuevoMenu.equalsIgnoreCase("versus")){
								Constants.numJugadores = 2;
								Constants.visualMode = Game.MODE_2D;
								menu = new GameModeMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu = Constants.modMenu;
								menu.setDoubleBuffered(true);
								Constants.gameState = Constants.MENU;
								window.add(menu, BorderLayout.CENTER);
								window.pack();
							}else if(nuevoMenu.equalsIgnoreCase("exit")){
								System.exit(0);
							}else if(nuevoMenu.equalsIgnoreCase("options")){
								menu = new OptionMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu = Constants.optMenu;
								menu.setDoubleBuffered(true);
								Constants.gameState = Constants.MENU;	//redundante
								window.add(menu, BorderLayout.CENTER);
								window.pack();
							}else if(nuevoMenu.equalsIgnoreCase("help")){
								menu = new HelpMenu(width*Constants.scale, height*Constants.scale);
								Constants.tipoMenu=Constants.helMenu;
								menu.setDoubleBuffered(true);
								Constants.gameState = Constants.MENU;
								window.add(menu, BorderLayout.CENTER);
								window.pack();
							}
						}else {
							if(Constants.visualMode == Game.MODE_2D){
								Constants.visualMode = Game.MODE_3D;
							}else{
								Constants.visualMode = Game.MODE_2D;
							}
						}
						//desde el menu de opciones
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.optMenu)){
						//System.out.println(e.getKeyCode());
						if(nuevoMenu.equalsIgnoreCase("sound")){
							//System.out.println(Constants.sound);
							Constants.sound = !Constants.sound;
							if(Constants.sound)
								music.playMenu();
								else
									music.stop();
							
						}else if(nuevoMenu.equalsIgnoreCase("cambioMano")){
							if(!Constants.conTeclado && Constants.jugador==1 && Constants.elegidoJugador){
								Constants.zurdo = !Constants.zurdo;
							}
						}else if(nuevoMenu.equalsIgnoreCase("resolution")){
							//System.out.println(Constants.scale);
							if(Constants.scale == 2){
								Constants.scale = 4;
								Constants.ax=350;
								Constants.ay=250;
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
						else if(nuevoMenu.equalsIgnoreCase("device")){
							Constants.conTeclado = !Constants.conTeclado;
						}
						else if(nuevoMenu.equalsIgnoreCase("back")){
							//system.out.println("back to main menu");
							menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
							Constants.tipoMenu = Constants.titMenu;
							menu.setDoubleBuffered(true);
							Constants.gameState = Constants.MENU;	//redundante
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
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.modMenu)){
						if(nuevoMenu.equalsIgnoreCase("maraton")){
							Constants.enMenu = false;
							if(music != null){
								music.playGame();
							}else{
								System.err.println("Richard no le des tan rapido");
							}
							game = new Game(width*Constants.scale, height*Constants.scale, Game.RUNNER, Constants.numJugadores, this);
							window.remove(menu);
							window.revalidate();
							window.add(game,BorderLayout.CENTER);
							//System.out.println(window.getComponentCount());
							window.pack();
							Constants.gameState = Constants.GAME;
						}else if(nuevoMenu.equalsIgnoreCase("treasure")){
							Constants.enMenu = false;
							if(music != null){
								music.playGame();
							}else{
								System.err.println("Richard no le des tan rapido");
							}
							game = new Game(width*Constants.scale, height*Constants.scale, Game.COINS, Constants.numJugadores, this);
							window.remove(menu);
							window.revalidate();
							window.add(game,BorderLayout.CENTER);
							window.pack();
							Constants.gameState = Constants.GAME;
						}else if(nuevoMenu.equalsIgnoreCase("back")){
							//system.out.println("back to main menu");
							menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
							Constants.tipoMenu = Constants.titMenu;
							menu.setDoubleBuffered(true);
							Constants.gameState = Constants.MENU;	//redundante
							window.add(menu, BorderLayout.CENTER);
							window.pack();
						}
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.creMenu)){
						//system.out.println("back to main menu");
						menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
						Constants.tipoMenu = Constants.titMenu;
						menu.setDoubleBuffered(true);
						Constants.gameState = Constants.MENU;	//redundante
						window.add(menu, BorderLayout.CENTER);
						window.pack();
					}else if(Constants.tipoMenu.equalsIgnoreCase(Constants.helMenu)){
						//system.out.println("back to main menu");
						menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
						Constants.tipoMenu = Constants.titMenu;
						menu.setDoubleBuffered(true);
						Constants.gameState = Constants.MENU;	//redundante
						window.add(menu, BorderLayout.CENTER);
						window.pack();
					}
					
				}
			}else{				
				//system.out.println(e.getKeyCode());
				if(estaLibre(e.getKeyCode())){
					Constants.esperandoTecla = false;
					if(Constants.teclaPausap1==Constants.guion){
						Constants.teclaPausap1 = e.getKeyCode();
					}else if(Constants.teclaPausap2==Constants.guion){
						Constants.teclaPausap2 = e.getKeyCode();
					}else if(Constants.teclaSaltop1==Constants.guion){
						Constants.teclaSaltop1 = e.getKeyCode();
						//System.out.println((char)Constants.teclaSaltop1);
					}else if(Constants.teclaSaltop2==Constants.guion){
						Constants.teclaSaltop2 = e.getKeyCode();
					}else if(Constants.teclaSprintp1==Constants.guion){
						Constants.teclaSprintp1 = e.getKeyCode();
					}else if(Constants.teclaSprintp2==Constants.guion){
						Constants.teclaSprintp2 = e.getKeyCode();
					}
				}
			}			
			break;
		case Constants.GAME:			
			if(e.getKeyCode() == Constants.teclaPausap1 || e.getKeyCode() == Constants.teclaPausap2){
				Constants.gameState = Constants.PAUSE;
				game.showPause();
				//system.out.println("PAUSA!");				
			}else{
				int option = game.keyPressed(e);
				if(option == EndMenu.QUIT){
					game.quit();
					game=null;
					//system.out.println("exit pulsado en menu end");
					menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
					window.requestFocus();
					//TODO 
					Constants.enMenu = true;
					Constants.tipoMenu = Constants.titMenu;
					menu.setDoubleBuffered(true);
					Constants.gameState = Constants.MENU;
					window.add(menu, BorderLayout.CENTER);
					window.pack();
					music.playMenu();
				}
			}
			break;
		case Constants.PAUSE:
			if(e.getKeyCode() == Constants.teclaPausap1 || e.getKeyCode() == Constants.teclaPausap2){
				Constants.gameState = Constants.GAME;
				game.hiddenPause();			
			}else{
				int option = game.keyPressed(e);
				if(option == PauseMenu.CONTINUE || option == PauseMenu.RESTART){
					Constants.gameState = Constants.GAME;
					game.hiddenPause();	
				}else if(option == PauseMenu.QUIT){
					game=null;
					//system.out.println("exit pulsado en menu pause");
					menu = new TitleMenu(width*Constants.scale, height*Constants.scale);
					Constants.enMenu = true;
					Constants.tipoMenu = Constants.titMenu;
					menu.setDoubleBuffered(true);
					Constants.gameState = Constants.MENU;
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

	private boolean estaLibre(int keyCode) {
		boolean resul = true;
		if(keyCode == Constants.teclaPausap1 || keyCode == Constants.teclaPausap2 || keyCode == Constants.teclaSaltop1 || 
				keyCode == Constants.teclaSaltop2 || keyCode == Constants.teclaSprintp1 || keyCode == Constants.teclaSprintp2){
			resul = false;
			JOptionPane.showMessageDialog(window, "La tecla "+(char)keyCode+" ya se encuentra en uso");
		}
		return resul;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(Constants.gameState == Constants.GAME && e.getKeyCode()==Constants.teclaSprintp1 || (Constants.numJugadores>1 && e.getKeyCode()==Constants.teclaSprintp2)){
			game.keyReleased(e);
		}
	}

	public Panel getPanel() {
		return panel;
	}

	public void setPanel(Panel panel) {
		this.panel = panel;
	}

}
