package videogame;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import menu.Menu;
import menu.TitleMenu;

public class Game extends JPanel implements Runnable {
	
	//Dimensiones del juego
	private static final int WIDTH = 320;
	private static final int HEIGHT = 240;	
	private static int SCALE = 2;	
	
	//Thread del juego
	private boolean running;
	private int FPS = 60;
	private int targetTime = 1000000000/FPS; //ns
	private Thread thread;
	
	//Imagen del juego
	private BufferedImage image;
	private Graphics2D g;
	
	//Menu del juego
	private Menu menu;
	
	//Maquina de estados
	private int gameState;
	private final int TITLE_MENU = 0;
	private final int START_GAME = 1;

	public Game(){
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	public void init() {
		running = true;
		image = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE, 
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		if(thread == null){
			thread = new Thread(this);
			//TODO: añadir keyListener
			thread.start();
		}
		menu = new TitleMenu(this);
	}
	
	@Override
	public void run() {
		//Tiempos de control de bucle
		long start;
		long elapsed;
		long wait;
		
		while(running){
			start = System.nanoTime();
			
			//TODO: actualización del juego
			
			draw();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed;
			//Duerme el hilo hasta la siguiente actualización
			try {
				Thread.sleep(wait/1000000);
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
			menu.draw2D(g);
		}
		
		getGraphics().drawImage(image, 0, 0,
				WIDTH * SCALE, HEIGHT * SCALE,
				null);
		getGraphics().dispose();
	}

}
