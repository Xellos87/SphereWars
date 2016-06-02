package menu;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphic.Cursor;
import utils.Constants;
import videogame.Parallax;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: GameModeMenu.java
 * 
 * Comentarios: Menú de opción del modo de juego,
 * menu intermedio entre la pantalla de titulo principal
 * y el juego. Tiene tres opciones: modo cazatesoros,
 * modo maraton y volver al menu principal.
 * 
 */
@SuppressWarnings("serial")
public class GameModeMenu extends Menu {
	//imagenes del menu
	private BufferedImage starBack;
	private BufferedImage mountainBack;
	private BufferedImage title;
	private BufferedImage tesoros;
	private BufferedImage maraton;
	private BufferedImage back;
	
	private int width;
	private int height;
	
	private Cursor cursor;
	
	private Parallax parallax;

	/**
	 * constructor de la clase, crea el menu
	 * @param width
	 * @param height
	 */
	public GameModeMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		cargarImagenes();
		// set background
		cursor = new Cursor(Constants.modMenu);
		initParallax();
	}

	/**
	 * carga las imagenes del menu
	 */
	private void cargarImagenes() {
		try {
			starBack = ImageIO.read(new File(Constants.starBackName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + Constants.starBackName);
		}
		try {
			mountainBack = ImageIO.read(new File(Constants.mountainBackName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.mountainBackName);
		}
		try {
			title = ImageIO.read(new File(Constants.titleName));
		} catch (IOException e) {
			System.err.println("Problem with font " + Constants.titleName);
		}
		try {
			tesoros = ImageIO.read(new File(Constants.tesorosName));
		} catch (IOException e) {
			System.err.println("Problem with font " + Constants.tesorosName);
		}
		try {
			maraton = ImageIO.read(new File(Constants.maratonName));
		} catch (IOException e) {
			System.err.println("Problem with font " + Constants.maratonName);
		}
		try {
			back = ImageIO.read(new File(Constants.backName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.backName);
		}
	}

	/**
	 * inicia el parallax
	 */
	private void initParallax() {
		String[] names = { Constants.starBackName, Constants.mountainBackName };
		int[] velocity = { 2, 1 };
		int[] posx = { 0, 0 };
		int[] posy = { 0, height - mountainBack.getHeight() };
		parallax = new Parallax(2, names, velocity, posx, posy, width, height);
	}

	/**
	 * dibuja todos los elementos es sus posiciones
	 */
	@Override
	public void draw() {
		Image offscreen = createImage(width, height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);
		offgc.setComposite(comp);
		if (mountainBack != null && starBack != null) {
			parallax.move();
			parallax.draw(offgc, 0, 0);
		}
		// dibujar opciones de menu
		if (title != null) {
			offgc.drawImage(title, Constants.titlePos.getX() + Constants.ax, Constants.titlePos.getY() + Constants.ay,
					(int) (title.getWidth() / 5), (int) (title.getHeight() / 5), null);
		}
		if(tesoros!=null && !cursor.getOpcion().equalsIgnoreCase("treasure")){
			offgc.drawImage(tesoros, Constants.tesorosPos.getX()+Constants.ax, Constants.tesorosPos.getY()+Constants.ay, tesoros.getWidth()/2,tesoros.getHeight()/2, null);
		}else{
			offgc.drawImage(tesoros, Constants.tesorosPos.getX()+Constants.desplazamiento+Constants.ax, Constants.tesorosPos.getY()+Constants.ay, tesoros.getWidth()/2,tesoros.getHeight()/2, null);
		}
		if(maraton!=null && !cursor.getOpcion().equalsIgnoreCase("maraton")){
			offgc.drawImage(maraton, Constants.maratonPos.getX()+Constants.ax, Constants.maratonPos.getY()+Constants.ay, maraton.getWidth()/2,maraton.getHeight()/2, null);
		}else{
			offgc.drawImage(maraton, Constants.maratonPos.getX()+Constants.desplazamiento+Constants.ax, Constants.maratonPos.getY()+Constants.ay, maraton.getWidth()/2,maraton.getHeight()/2, null);
		}
		if(back!=null && !cursor.getOpcion().equalsIgnoreCase("back")){
			offgc.drawImage(back, Constants.backModePos.getX()+Constants.ax, Constants.backModePos.getY()+Constants.ay, (int)(back.getWidth()/2), (int)(back.getHeight()/2), null);
		}else{
			offgc.drawImage(back, Constants.backModePos.getX()+Constants.desplazamiento+Constants.ax, Constants.backModePos.getY()+Constants.ay, back.getWidth()/2,back.getHeight()/2, null);
		}
		// dibujar cursor
		if (cursor != null) {
			offgc.drawImage(cursor.getImage(), cursor.getPosition().getX() + Constants.ax,
					cursor.getPosition().getY() + Constants.ay, cursor.getWidth() / 4, cursor.getHeight() / 4, null);
		}
		getGraphics().drawImage(offscreen, 0, 0, width, height, null);
		if(Constants.gameState == Constants.GAME){
			getGraphics().dispose();
		}
	}

	@Override
	public void cursorDown() {
		cursor.nextPosition();
	}

	@Override
	public void cursorUp() {
		cursor.previousPosition();
	}

	@Override
	public String cursorEnter() {
		return cursor.enter();
	}

	@Override
	public void cursorRight() {
	}

	@Override
	public void cursorLeft() {
	}
}
