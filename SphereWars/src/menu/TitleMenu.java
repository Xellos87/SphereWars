package menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import graphic.Cursor;
import utils.Constants;
import videogame.Parallax;

@SuppressWarnings("serial")
public class TitleMenu extends Menu {
	BufferedImage starBack;
	BufferedImage mountainBack;
	BufferedImage title;
	BufferedImage jugar;
	boolean firstTime = true;
	
	Parallax parallax;
	Cursor cursor;

	public TitleMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		// set background
		try {
			starBack = ImageIO.read(new File(Constants.starBackName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + Constants.starBackName);
		}
		try {
			mountainBack = ImageIO.read(new File(Constants.mountainBackName));
		} catch (IOException e) {
			System.err.println("Problem with souerce " + Constants.mountainBackName);
		}
		try{
			title = ImageIO.read(new File(Constants.titleName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.titleName);
		}
		try{
			jugar = ImageIO.read(new File("fonts/menu_jugar.png"));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.titleName);
		}
		cursor = new Cursor("title");
		
		//TODO: no chuta.
		initParallax();
	}

	//TODO: ARREGLAR PROBLEMAS DE PARPADEOS!!!
	//TODO: idea: mergear todo en una imagen y hacer el draw() solo una vez
	public void draw() {
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);
		g.setComposite(comp);	//PERMITE SUPERPONER IMAGENES CON PIXELES TRANSPARENTES
		if(firstTime){
			firstTime = true;	//NOPE
			
			//TODO: dejar todo menos el cursor fijo?
			if (starBack != null) {
				getGraphics().drawImage(starBack, 0, 0, width,height, null);
			}
			
			if (mountainBack != null) {
				getGraphics().drawImage(mountainBack, 0, 0,width,height, null);
			}		
			if(cursor != null){
				getGraphics().drawImage(cursor.getImage(), cursor.getPosition().getX(),cursor.getPosition().getY(), cursor.getWidth()/4, cursor.getHeight()/4, null);
			}
			if(title!=null){
				getGraphics().drawImage(title, Constants.titlePos.getX(), Constants.titlePos.getY(), (int)(title.getWidth()/2.25), (int)(title.getHeight()/2.25), null);
			}
			if(jugar!=null){	//PROBANDO
				getGraphics().drawImage(jugar, Constants.titlePos.getX()+80, Constants.titlePos.getY()+80, jugar.getWidth()/2,jugar.getHeight()/2, null);
			}
		}
		
		
		// Dibuja todo en pantalla
		// g.setColor(new Color(255, 0, 0));
		// g.fillRect(0, 0, width, height);
		// g.setColor(new Color(0, 255, 0));
		// g.fillOval(50, 50, 100, 100);
		// getGraphics().drawImage(starBack,0,0,width,height,null);
		//image = starBack;
		
		
		//getGraphics().dispose();
		//parallax.draw((Graphics2D)getGraphics());
		// Vuelca en el panel lo que se ha dibujado
		//getGraphics().drawImage(image, 0, 0, width, height, null);
		//getGraphics().dispose();
		
	}

	private void initParallax() {
		String[] names = {Constants.mountainBackName};
		int[] velocity = {10};
		int[] posx= {0};
		int[] posy = {0};
		parallax = new Parallax(1,names,velocity,posx,posy,width,height);
	}

	@Override
	public void cursorDown() {
		cursor.nextPosition();
	}

	@Override
	public void cursorUp() {
		cursor.previousPosition();
	}
}
