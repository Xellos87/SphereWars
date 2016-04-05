package videogame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import menu.Menu;
import menu.TitleMenu;

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
 * */
public class Main implements Runnable{
	private static JFrame window;
	private static final String TITLE = "Sphere Wars";

	//Dimensiones del juego
	private static int width = 320;
	private static int height = 240;	
	private static int scale = 2;	

	//Thread del juego
	private boolean running;
	private int FPS = 60;
	private int targetTime = 1000000000/FPS; //ns
	private Thread thread;
	
	//Menu del juego
	private Menu menu;

	public static void main(String[] args){
		new Main();
	}

	private Main() {
		init();
	}

	private void init() {
		/* Crea el contenedor para poner los paneles */
		window = new JFrame(TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.setResizable(false);
		window.setVisible(true);
		
		running = true;

		if(thread == null){
			thread = new Thread(this);
			//TODO: añadir keyListener
			thread.start();
		}
		menu = new TitleMenu(width*scale, height*scale);
		window.add(menu, BorderLayout.CENTER);
		//Ajusta el tamaño de la ventana según los componentes
		window.pack();
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
			menu.draw();
		}
	}
}
