package menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Menu.java
 * 
 * Comentarios: Clase abstracta que representa las acciones comunes de los menús
 * 
 */
@SuppressWarnings("serial")
public abstract class Menu extends JPanel{
	protected int width,height;
	protected BufferedImage image;
	protected Graphics2D g;
	
	public abstract void draw();

	public abstract void cursorDown();

	public abstract void cursorUp();

	public abstract String cursorEnter();

	public abstract void cursorRight();

	public abstract void cursorLeft();
}
