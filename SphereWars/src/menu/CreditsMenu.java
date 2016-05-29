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

public class CreditsMenu extends Menu{
	BufferedImage starBack;
	BufferedImage mountainBack;
	BufferedImage title;
	
	Parallax parallax;
	
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
		
		
		initParallax();
	}
	
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
	
	private void initParallax() {
		String[] names = {Constants.starBackName,Constants.mountainBackName};
		int[] velocity = {2,1};
		int[] posx= {0,0};
		int[] posy = {0,height - mountainBack.getHeight()};
		parallax = new Parallax(2,names,velocity,posx,posy,width,height);
	}
	
	@Override
	public void draw() {	
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);		
		offgc.setComposite(comp);
		//dibujar fondo
		//if (starBack != null) {
		//	offgc.drawImage(starBack, 0, 0, width,height, null);
		//}		
		if (mountainBack != null && starBack != null) {
			parallax.move();
			parallax.draw(offgc,0,0);
		}		
		if(title!=null){
			offgc.drawImage(title, Constants.titlePos.getX()+Constants.ax, Constants.titlePos.getY()+Constants.ay, (int)(title.getWidth()/5), (int)(title.getHeight()/5), null);
		}
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
