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

import utils.Constants;
import videogame.Game;
import videogame.Parallax;

public class HelpMenu extends Menu{
	private BufferedImage starBack;
	private BufferedImage mountainBack;
	private BufferedImage title;
	private BufferedImage slide1;
	private BufferedImage slide3;
	private BufferedImage slide5;
	
	private final String slide1Name = "images/help_slide1.png";
	private final String slide3Name = "images/help_slide3.png";
	private final String slide5Name = "images/help_slide5.png";
	
	private Parallax parallax;
	
	private int numSlides = 6;
	private int slideActual = 0;
	
	private final String volver = "enter - volver";
	private final String moverse = "avance - flechas laterales";
	private final String titteclado = "teclado";
	private final String teclados24 = "pulsa la tecla ";
	private final String teclados242 = " para J2";
	private final String titkinect = "kinect";
	private final String kinects2 = "levanta la mano!";
	private final String kinects4 = "adelanta tu mano!";
	private final String slide6 = "salta sobre";
	private final String slide62 = "tus enemigos!";
	
	private Font mayus = Constants.font_bold.deriveFont(22.0f);
	private Font central = Constants.font_bold.deriveFont(35.0f);
	
	private Color titulo = new Color(37,33,92);
	
	public HelpMenu(int width, int height) {
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
		try{
			slide1 = ImageIO.read(new File(slide1Name));
		}catch(IOException e){
			System.err.println("Problem with font "+slide1Name);
		}
		try{
			slide3 = ImageIO.read(new File(slide3Name));
		}catch(IOException e){
			System.err.println("Problem with font "+slide3Name);
		}
		try{
			slide5 = ImageIO.read(new File(slide5Name));
		}catch(IOException e){
			System.err.println("Problem with font "+slide5Name);
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
		
		offgc.setFont(mayus);
		offgc.setColor(Color.white);
		offgc.drawString(volver, Constants.titlePos.getX()-50+Constants.ax, Constants.titlePos.getY()+430+Constants.ay);
		offgc.drawString(moverse, Constants.titlePos.getX()-40+Constants.ax, Constants.titlePos.getY()+400+Constants.ay);
		offgc.drawString((this.slideActual+1)+"-"+(this.numSlides), Constants.titlePos.getX()+495+Constants.ax, Constants.titlePos.getY()+430+Constants.ay);
		
		offgc.setFont(central);		
		if(this.slideActual==0 && slide1!=null){
			offgc.drawImage(slide1, Constants.titlePos.getX()+Constants.ax-10, Constants.titlePos.getY()+Constants.ay+100, (int)(slide1.getWidth()/1.3), (int)(slide1.getHeight()/1.3), null);
		}else if(this.slideActual==1){
			int car = Constants.teclaSaltop1;
			String aux=""+(char)car;
			if(car == 32){
				aux = "espacio";
			}else if(car == 10){
				aux = "enter";
			}else if(car == 38){
				aux = "up";
			}
			offgc.drawString(teclados24, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+180+Constants.ay);
			offgc.drawString(aux+" para J1", Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+220+Constants.ay);
			car = Constants.teclaSaltop2;
			aux=""+(char)car;
			if(car == 32){
				aux = "espacio";
			}else if(car == 10){
				aux = "enter";
			}else if(car == 38){
				aux = "up";
			}
			offgc.drawString("o "+aux+teclados242, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+260+Constants.ay);
			offgc.drawString(kinects2, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+360+Constants.ay);
		}else if(this.slideActual==2){
			offgc.drawImage(slide3, Constants.titlePos.getX()+Constants.ax-10, Constants.titlePos.getY()+Constants.ay+100, (int)(slide3.getWidth()/1.3), (int)(slide3.getHeight()/1.3), null);
		}else if(this.slideActual==3){
			int car = Constants.teclaSprintp1;
			String aux=""+(char)car;
			if(car == 32){
				aux = "espacio";
			}else if(car == 10){
				aux = "enter";
			}else if(car == 38){
				aux = "up";
			}
			offgc.drawString(teclados24, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+180+Constants.ay);
			offgc.drawString(aux+" para J1", Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+220+Constants.ay);
			car = Constants.teclaSprintp2;
			aux=""+(char)car;
			if(car == 32){
				aux = "espacio";
			}else if(car == 10){
				aux = "enter";
			}else if(car == 38){
				aux = "up";
			}
			offgc.drawString("o "+aux+teclados242, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+260+Constants.ay);
			offgc.drawString(kinects4, Constants.titlePos.getX()-30+Constants.ax, Constants.titlePos.getY()+360+Constants.ay);
		}else if(this.slideActual==4){
			offgc.drawImage(slide5, Constants.titlePos.getX()+Constants.ax-10, Constants.titlePos.getY()+Constants.ay+100, (int)(slide5.getWidth()/1.3), (int)(slide5.getHeight()/1.3), null);
		}else if(this.slideActual==5){
			offgc.drawString(slide6, Constants.titlePos.getX()+50+Constants.ax, Constants.titlePos.getY()+220+Constants.ay);
			offgc.drawString(slide62, Constants.titlePos.getX()+50+Constants.ax, Constants.titlePos.getY()+260+Constants.ay);
		}
		
		offgc.setColor(titulo);
		offgc.setFont(central.deriveFont(38.0f));
		if(this.slideActual==1 || this.slideActual == 3){
			offgc.drawString(titteclado, Constants.titlePos.getX()+20+Constants.ax, Constants.titlePos.getY()+140+Constants.ay);
			offgc.drawString(titkinect, Constants.titlePos.getX()+20+Constants.ax, Constants.titlePos.getY()+320+Constants.ay);
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
		this.slideActual = (this.slideActual+1)%this.numSlides;
	}

	@Override
	public void cursorLeft() {
		if(slideActual > 0)
			slideActual = Math.abs((slideActual - 1)) % numSlides;
		else
			slideActual = numSlides-1;
	}

}
