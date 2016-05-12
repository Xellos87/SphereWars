package videogame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import media.ImageHandler;
import menu.Menu;
import menu.TitleMenu;
import scoreboard.GameScore;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla Español - 557022,
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
	private static JFrame window;
	private static final String TITLE = "Sphere Wars";

	//Dimensiones del juego
	private static int width = 320;
	private static int height = 240;	
	private static int scale = 2;	

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
		
	//Menu del juego
	private Menu menu;	//TODO: este menu puede cambiar entre menu de titulo, de pausa, de opciones?
	//Modo de juego 2D
	private Game2D game2D;
	private GameScore game_score;

	public static void main(String[] args){
		new Main();
	}

	private Main() {
		Constants.img_handler = new ImageHandler();
		init();
	}
	private void init() {
		/* Crea el contenedor para poner los paneles */
		window = new JFrame(TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setLayout(new BorderLayout());
		BoxLayout boxLayout = new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS); // top to bottom
	    window.setLayout(boxLayout);
		window.setResizable(false);
		window.setVisible(true);
		window.addKeyListener(this);
		running = true;

		if(thread == null){
			thread = new Thread(this);
			thread.setPriority(Thread.MAX_PRIORITY);
			//TODO: añadir keyListener
			thread.start();
		}
		//menu = new TitleMenu(width*scale, height*scale);
		//menu.setDoubleBuffered(true);
		//state = Constants.MENU;
		//window.add(menu);
		game_score = new GameScore(width*scale, 100, 1);
		game_score.setDoubleBuffered(true);
		state = Constants.GAME;
		window.add(game_score);
		game2D = new Game2D(width*scale, height*scale);
		game2D.setDoubleBuffered(true);
		window.add(game2D);
		//Ajusta el tamaño de la ventana según los componentes
		window.pack();
	}

	@Override
	public void run() {
		
		while(running){
			start = System.nanoTime();
			if(!pause){
				//TODO: actualización del juego
				if(game2D != null){
					game2D.actionGame();
				}
				draw();
			}

			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed;
			//Duerme el hilo hasta la siguiente actualización
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
		if(menu != null){
			menu.draw();
			
		}
		if(game2D != null){
			game2D.draw();
		}
		if(game_score != null){
			game_score.draw();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (state) {
		case Constants.MENU:
			if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
				menu.cursorDown();
				System.out.println("Down key pressed");
			}else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
				menu.cursorUp();
				System.out.println("Up key pressed");
			}else if(e.getKeyCode() == KeyEvent.VK_ENTER){
				//TODO: mirar en que posicion esta el cursor y ejecutar lo que toque
				//elegir si hacer esto desde el menu o hacerlo desde aqui
				//menu.doaction(); o //actuar(menu.getAction());
			}
			break;
		case Constants.GAME:			
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				if(pause){
					pause = false;
				}
				else{
					pause = true;
					System.out.println("PAUSA!");
				}
			}else{
				game2D.keyPressed(e);
			}
			break;
		default:
			break;
		}
		//		//Duerme el hilo hasta la siguiente actualización despues de despachar el evento
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
		// TODO Auto-generated method stub
		
	}
}
