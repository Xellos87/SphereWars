package videogame;

import java.awt.BorderLayout;

import javax.swing.JFrame;

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
public class Main {
	
	private static JFrame window;
	private static final String TITLE = "Sphere Wars";

	public static void main(String[] args){
		window = new JFrame(TITLE);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(new BorderLayout());
		window.setResizable(false);
		window.setVisible(true);

		Game game = new Game();
		game.init();
		window.add(game, BorderLayout.CENTER);
		//Ajusta el tamaño de la ventana según los componentes
		window.pack();
		
	}	
}
