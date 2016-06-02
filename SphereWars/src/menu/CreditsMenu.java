package menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import scoreboard.RankingEntry;
import utils.Constants;
import videogame.Game;
import videogame.Parallax;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: CreditsMenu.java
 * 
 * Comentarios: Menú de la pantalla de creditos,
 * informa de los autores del videojuego y muestra el 
 * ranking del top 3 de cada modo de juego. 
 * Para volver al menu principal se pulsa enter.
 * 
 */
@SuppressWarnings("serial")
public class CreditsMenu extends Menu{
	//titulo y fondos del parallax
	private BufferedImage starBack;
	private BufferedImage mountainBack;
	private BufferedImage title;
	
	//strings que se escriben
	private final String autores = "autores:";
	private final String richard = "Richard Elvira";
	private final String axte = "Adrian Milla";
	private final String jl = "Juan Luis Burillo";
	private final String sandra = "Sandra Malpica";
	
	private final String maraton = "maraton";
	private final String cazatesoros = "cazatesoros";
	private final String scores = "top 3";
	
	private final String volver = "enter - volver";
	
	//fuentes para escribir
	private Font mayus = Constants.font_bold.deriveFont(40.0f);
	private Font minus = Constants.humanoid.deriveFont(42.0f);
	private Font ranking = Constants.font_bold.deriveFont(30.0f);
	
	private Parallax parallax;
	
	//los dos rankings de los distintos modos de juego
	private RankingEntry[] coins;
	private RankingEntry[] runner;
	
	//color del titulo
	private Color titulo = new Color(37,33,92);
	
	/**
	 * crea el menu
	 * @param width
	 * @param height
	 */
	public CreditsMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		cargarImagenes();
		// set background
		coins = Constants.ranking.getRanking(Game.COINS);
		runner = Constants.ranking.getRanking(Game.RUNNER);
		
		initParallax();
	}
	
	/**
	 * carga las imagenes en los buffers
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
		try{
			title = ImageIO.read(new File(Constants.titleName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.titleName);
		}
	}
	
	/**
	 * inicia el parallax
	 */
	private void initParallax() {
		String[] names = {Constants.starBackName,Constants.mountainBackName};
		int[] velocity = {2,1};
		int[] posx= {0,0};
		int[] posy = {0,height - mountainBack.getHeight()};
		parallax = new Parallax(2,names,velocity,posx,posy,width,height);
	}
	
	/**
	 * dibuja todos los elementos en su posicion
	 */
	@Override
	public void draw() {	
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);		
		offgc.setComposite(comp);
		//dibujar fondo
		if (mountainBack != null && starBack != null) {
			parallax.move();
			parallax.draw(offgc,0,0);
		}		
		if(title!=null){
			offgc.drawImage(title, Constants.titlePos.getX()+Constants.ax, Constants.titlePos.getY()+Constants.ay, (int)(title.getWidth()/5), (int)(title.getHeight()/5), null);
		}
		offgc.setFont(mayus);
		offgc.setColor(titulo);
		offgc.drawString(autores, Constants.titlePos.getX()-20+Constants.ax, Constants.titlePos.getY()+150+Constants.ay);
		offgc.setFont(ranking);
		offgc.drawString(scores, Constants.titlePos.getX()+300+Constants.ax, Constants.titlePos.getY()+110+Constants.ay);
		
		for(int i=0; i<4; i++){
			int yCoins = i*40;
			int yRunner = i*40+(40*4);
			if(i==0){
				offgc.setColor(titulo);
				offgc.setFont(ranking.deriveFont(25.0f));
				offgc.drawString(cazatesoros, Constants.titlePos.getX()+300+Constants.ax, Constants.titlePos.getY()+yCoins+140+Constants.ay);
				offgc.drawString(maraton, Constants.titlePos.getX()+300+Constants.ax, Constants.titlePos.getY()+yRunner+140+Constants.ay);
			}else{
				offgc.setColor(Color.CYAN);
				offgc.setFont(ranking);
				offgc.drawString(coins[i].getScoreString(Game.COINS)+" "+coins[i].getName(), Constants.titlePos.getX()+300+Constants.ax, Constants.titlePos.getY()+yCoins+140+Constants.ay);
				offgc.drawString(runner[i].getScoreString(Game.RUNNER).substring(0, 5)+" "+runner[i].getName(), Constants.titlePos.getX()+300+Constants.ax, Constants.titlePos.getY()+yRunner+140+Constants.ay);
			}
		}
		offgc.setFont(minus);
		offgc.setColor(Color.CYAN);
		offgc.drawString(jl, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+190+Constants.ay);
		offgc.drawString(richard, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+240+Constants.ay);
		offgc.drawString(sandra, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+290+Constants.ay);
		offgc.drawString(axte, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+340+Constants.ay);
		
		offgc.setFont(ranking.deriveFont(22.0f));
		offgc.setColor(Color.white);
		offgc.drawString(volver, Constants.titlePos.getX()-50+Constants.ax, Constants.titlePos.getY()+420+Constants.ay);
		
		getGraphics().drawImage(offscreen,0,0,width,height,null);
		getGraphics().dispose();
	}

	@Override
	public void cursorDown() {		
	}

	@Override
	public void cursorUp() {		
	}

	@Override
	public String cursorEnter() {
		return "back";
	}

	@Override
	public void cursorRight() {
		
	}

	@Override
	public void cursorLeft() {		
	}

}
