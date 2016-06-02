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

import graphic.Cursor;
import utils.Constants;
import utils.Position;
import videogame.Parallax;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: OptionMenu.java
 * 
 * Comentarios: Menú de la pantalla de opciones
 * 
 */
@SuppressWarnings("serial")
public class OptionMenu extends Menu {
	BufferedImage starBack;
	BufferedImage mountainBack;
	BufferedImage title;
	Parallax parallax;
	private Cursor cursor;
	
	BufferedImage sonido;
	BufferedImage si;
	BufferedImage no;
	BufferedImage resolucion;
	BufferedImage res480;
	BufferedImage res960;
	BufferedImage controles;
	BufferedImage teclado;
	BufferedImage kinect;
	BufferedImage teclas;
	BufferedImage saltar;
	BufferedImage correr;
	BufferedImage volver;
	BufferedImage controles1;
	BufferedImage controles2;
	BufferedImage diestro;
	BufferedImage zurdo;
	
	private final String diestroName = "fonts/opciones_diestro.png";
	private final String zurdoName = "fonts/opciones_zurdo.png";
	private final Position diestroPos = new Position(Constants.optX+Constants.desplazamiento,Constants.kinnectPos.getY()+Constants.titleGap);
	private final Position zurdoPos = new Position(Constants.optX+280+Constants.desplazamiento,Constants.kinnectPos.getY()+Constants.titleGap);
	
	public OptionMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		// requestFocus();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		cargarImagenes();
		// set background
		cursor = new Cursor(Constants.optMenu);
		System.out.println("pos cursor "+cursor.getPosition().getX()+" "+cursor.getPosition().getY());
		initParallax();
	}

	private void dibujarLetras(Graphics2D offgc) {
		//dibujar opciones de menu
		if(title!=null){
			offgc.drawImage(title, Constants.titlePos.getX()+Constants.ax, Constants.titlePos.getY()+Constants.ay, (int)(title.getWidth()/5), (int)(title.getHeight()/5), null);
		}
		if(!Constants.elegidoJugador){
			if(sonido!=null && !cursor.getOpcion().equalsIgnoreCase("sound")){
				offgc.drawImage(sonido, Constants.soundPos.getX()+Constants.ax, Constants.soundPos.getY()+Constants.ay, (int)(sonido.getWidth()/2), (int)(sonido.getHeight()/2), null);
			}else{
				offgc.drawImage(sonido, Constants.soundPos.getX()+Constants.desplazamiento+Constants.ax, Constants.soundPos.getY()+Constants.ay, sonido.getWidth()/2,sonido.getHeight()/2, null);
			}
			if(si!=null && Constants.sound){
				offgc.drawImage(si, Constants.yesPos.getX()+Constants.ax, Constants.yesPos.getY()+Constants.ay, (int)(si.getWidth()/2), (int)(si.getHeight()/2), null);
			}else{
				offgc.drawImage(si, Constants.yesPos.getX()+Constants.ax, Constants.yesPos.getY()+Constants.ay, (int)(si.getWidth()/2.5),(int)(si.getHeight()/2.5), null);
			}
			if(no!=null && !Constants.sound){
				offgc.drawImage(no, Constants.noPos.getX()+Constants.ax, Constants.noPos.getY()+Constants.ay, (int)(no.getWidth()/2), (int)(no.getHeight()/2), null);
			}else{
				offgc.drawImage(no, Constants.noPos.getX()+Constants.ax, Constants.noPos.getY()+Constants.ay, (int)(no.getWidth()/2.5),(int)(no.getHeight()/2.5), null);
			}
			if(resolucion!=null && !cursor.getOpcion().equalsIgnoreCase("resolution")){
				offgc.drawImage(resolucion, Constants.resPos.getX()+Constants.ax, Constants.resPos.getY()+Constants.ay, (int)(resolucion.getWidth()/2), (int)(resolucion.getHeight()/2), null);
			}else{
				offgc.drawImage(resolucion, Constants.resPos.getX()+Constants.desplazamiento-10+Constants.ax, Constants.resPos.getY()+Constants.ay, resolucion.getWidth()/2,resolucion.getHeight()/2, null);
			}
			if(res480!=null && Constants.scale == 2){
				offgc.drawImage(res480, Constants.res480Pos.getX()+Constants.desplazamiento+Constants.ax, Constants.res480Pos.getY()+5+Constants.ay, (int)(res480.getWidth()/2.5),(int)(res480.getHeight()/2.5), null);
			}
			if(res960!=null && Constants.scale == 4){
				offgc.drawImage(res960, Constants.res960Pos.getX()+Constants.desplazamiento+Constants.ax, Constants.res960Pos.getY()+5+Constants.ay, (int)(res960.getWidth()/2.5),(int)(res960.getHeight()/2.5), null);
			}
			if(controles!=null && !cursor.getOpcion().equalsIgnoreCase("controller")){
				offgc.drawImage(controles, Constants.controllerPos.getX()+Constants.ax, Constants.controllerPos.getY()+Constants.ay, (int)(controles.getWidth()/2), (int)(controles.getHeight()/2), null);
			}else{
				offgc.drawImage(controles, Constants.controllerPos.getX()+Constants.desplazamiento-10+Constants.ax, Constants.controllerPos.getY()+Constants.ay, controles.getWidth()/2,controles.getHeight()/2, null);
			}			
			if(volver!=null && !cursor.getOpcion().equalsIgnoreCase("back")){
				offgc.drawImage(volver, Constants.backPos.getX()+Constants.ax, Constants.backPos.getY()+Constants.ay, (int)(volver.getWidth()/2), (int)(volver.getHeight()/2), null);
			}else{
				offgc.drawImage(volver, Constants.backPos.getX()+Constants.desplazamiento+Constants.ax, Constants.backPos.getY()+Constants.ay, volver.getWidth()/2,volver.getHeight()/2, null);
			}
		}else{
			if(Constants.jugador==1){
				if(controles1!=null && !cursor.getOpcion().equalsIgnoreCase("controller")){
					offgc.drawImage(controles1, Constants.controller1Pos.getX()+Constants.ax, Constants.controller1Pos.getY()+Constants.ay, (int)(controles1.getWidth()/2), (int)(controles1.getHeight()/2), null);
				}else{
					offgc.drawImage(controles1, Constants.controller1Pos.getX()+Constants.desplazamiento+Constants.ax, Constants.controller1Pos.getY()+Constants.ay, controles1.getWidth()/2,controles1.getHeight()/2, null);
				}
				if(teclado!=null && Constants.conTeclado && !cursor.getOpcion().equalsIgnoreCase("device")){
					offgc.drawImage(teclado, Constants.keyboardPos.getX()+Constants.ax, Constants.keyboardPos.getY()+Constants.ay, (int)(teclado.getWidth()/2), (int)(teclado.getHeight()/2), null);
				}else if(teclado!=null && Constants.conTeclado){
					offgc.drawImage(teclado, Constants.keyboardPos.getX()+Constants.desplazamiento+Constants.ax, Constants.keyboardPos.getY()+Constants.ay, (int)(teclado.getWidth()/2), (int)(teclado.getHeight()/2), null);
				}
				if(kinect!=null && !Constants.conTeclado && !cursor.getOpcion().equalsIgnoreCase("device")){
					offgc.drawImage(kinect, Constants.kinnectPos.getX()+Constants.ax, Constants.kinnectPos.getY()+Constants.ay, (int)(kinect.getWidth()/2), (int)(kinect.getHeight()/2), null);
				}else if(kinect!=null && !Constants.conTeclado){
					offgc.drawImage(kinect, Constants.kinnectPos.getX()+Constants.desplazamiento+Constants.ax, Constants.kinnectPos.getY()+Constants.ay, (int)(kinect.getWidth()/2), (int)(kinect.getHeight()/2), null);
				}
			}
			if(Constants.jugador==2){
				if(controles2!=null && !cursor.getOpcion().equalsIgnoreCase("controller")){
					offgc.drawImage(controles2, Constants.controller2Pos.getX()+Constants.ax, Constants.controller2Pos.getY()+Constants.ay, (int)(controles2.getWidth()/2), (int)(controles2.getHeight()/2), null);
				}else{
					offgc.drawImage(controles2, Constants.controller2Pos.getX()+Constants.desplazamiento+Constants.ax, Constants.controller2Pos.getY()+Constants.ay, controles2.getWidth()/2,controles2.getHeight()/2, null);
				}
			}			
			if(Constants.conTeclado || Constants.jugador == 2){
				if(teclas!=null && !cursor.getOpcion().equalsIgnoreCase("pause")){
					offgc.drawImage(teclas, Constants.keyPos.getX()+Constants.ax, Constants.keyPos.getY()+Constants.ay, (int)(teclas.getWidth()/2), (int)(teclas.getHeight()/2), null);
				}else{
					offgc.drawImage(teclas, Constants.keyPos.getX()+Constants.desplazamiento+Constants.ax, Constants.keyPos.getY()+Constants.ay, (int)(teclas.getWidth()/2), (int)(teclas.getHeight()/2), null);
				}
				if(saltar!=null && !cursor.getOpcion().equalsIgnoreCase("jump")){
					offgc.drawImage(saltar, Constants.jumpPos.getX()+Constants.ax, Constants.jumpPos.getY()+Constants.ay, (int)(saltar.getWidth()/2), (int)(saltar.getHeight()/2), null);
				}else{
					offgc.drawImage(saltar, Constants.jumpPos.getX()+Constants.desplazamiento+Constants.ax, Constants.jumpPos.getY()+Constants.ay, (int)(saltar.getWidth()/2), (int)(saltar.getHeight()/2), null);
				}
				if(correr!=null && !cursor.getOpcion().equalsIgnoreCase("run")){
					offgc.drawImage(correr, Constants.runPos.getX()+Constants.ax, Constants.runPos.getY()+Constants.ay, (int)(correr.getWidth()/2), (int)(correr.getHeight()/2), null);
				}else{
					offgc.drawImage(correr, Constants.runPos.getX()+Constants.desplazamiento+Constants.ax, Constants.runPos.getY()+Constants.ay, (int)(correr.getWidth()/2), (int)(correr.getHeight()/2), null);
				}
				String pauseChar,jumpChar,runChar;
				if(Constants.jugador==1){
					int car = Constants.teclaPausap1;
					String aux = ""+(char)Constants.teclaPausap1;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					pauseChar = aux;
					car = Constants.teclaSaltop1;
					//System.out.println(car);
					aux = ""+(char)Constants.teclaSaltop1;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					jumpChar = aux;
					car = Constants.teclaSprintp1;
					//System.out.println(car);
					aux = ""+(char)Constants.teclaSprintp1;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					runChar = aux;
				}else{
					int car = Constants.teclaPausap2;
					String aux = ""+(char)Constants.teclaPausap2;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					pauseChar = aux;
					car = Constants.teclaSaltop2;
					aux = ""+(char)Constants.teclaSaltop2;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					jumpChar = aux;
					car = Constants.teclaSprintp2;
					aux = ""+(char)Constants.teclaSprintp2;
					if(car == 32){
						aux = "espacio";
					}else if(car == 10){
						aux = "enter";
					}else if(car == 38){
						aux = "up";
					}else if(car == 27){
						aux = "ESC";
					}else if(car == 37){
						aux = "left";
					}else if(car == 40){
						aux = "down";
					}else if(car == 39){
						aux = "right";
					}
					runChar = aux;
				}		
				if(pauseChar.equalsIgnoreCase("-")&&System.currentTimeMillis()%1000<500){
					offgc.drawString(pauseChar, Constants.keyPos.getX()+270+Constants.ax, Constants.keyPos.getY()+40+Constants.ay);
				}else if(!pauseChar.equalsIgnoreCase("-")){
					offgc.drawString(pauseChar, Constants.keyPos.getX()+270+Constants.ax, Constants.keyPos.getY()+40+Constants.ay);
				}
				if(jumpChar.equalsIgnoreCase("-")&&System.currentTimeMillis()%1000<500){
					offgc.drawString(jumpChar, Constants.jumpPos.getX()+270+Constants.ax, Constants.jumpPos.getY()+40+Constants.ay);
				}else if(!jumpChar.equalsIgnoreCase("-")){
					offgc.drawString(jumpChar, Constants.jumpPos.getX()+270+Constants.ax, Constants.jumpPos.getY()+40+Constants.ay);
				}
				if(runChar.equalsIgnoreCase("-")&&System.currentTimeMillis()%1000<500){
					offgc.drawString(runChar, Constants.runPos.getX()+270+Constants.ax, Constants.runPos.getY()+40+Constants.ay);
				}else if(!runChar.equalsIgnoreCase("-")){
					offgc.drawString(runChar, Constants.runPos.getX()+270+Constants.ax, Constants.runPos.getY()+40+Constants.ay);
				}
			}else if(!Constants.conTeclado && Constants.jugador==1){
				if(Constants.zurdo){
					offgc.drawImage(zurdo, zurdoPos.getX()+Constants.ax-40, zurdoPos.getY()+Constants.ay,(int)(zurdo.getWidth()/2), (int)(zurdo.getHeight()/2), null);
					offgc.drawImage(diestro, diestroPos.getX()+Constants.ax, diestroPos.getY()+Constants.ay,(int)(diestro.getWidth()/2.5), (int)(diestro.getHeight()/2.5), null);
				}else{
					offgc.drawImage(zurdo, zurdoPos.getX()+Constants.ax, zurdoPos.getY()+Constants.ay,(int)(zurdo.getWidth()/2.5), (int)(zurdo.getHeight()/2.5), null);
					offgc.drawImage(diestro, diestroPos.getX()+Constants.ax, diestroPos.getY()+Constants.ay,(int)(diestro.getWidth()/2), (int)(diestro.getHeight()/2), null);
				}				
			}
		}		
	}
	
	private void initParallax() {
		String[] names = { Constants.starBackName, Constants.mountainBackName };
		int[] velocity = { 2, 1 };
		int[] posx = { 0, 0 };
		int[] posy = { 0, height - mountainBack.getHeight() };
		parallax = new Parallax(2, names, velocity, posx, posy, width, height);
	}

	private void cargarImagenes() {
		try{
			title = ImageIO.read(new File(Constants.titleName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.titleName);
		}
		try{
			diestro = ImageIO.read(new File(diestroName));
		}catch(IOException e){
			System.err.println("Problem with font "+diestroName);
		}
		try{
			zurdo = ImageIO.read(new File(zurdoName));
		}catch(IOException e){
			System.err.println("Problem with font "+zurdoName);
		}
		try {
			volver = ImageIO.read(new File(Constants.backName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.backName);
		}
		try {
			correr = ImageIO.read(new File(Constants.runName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.runName);
		}
		try {
			saltar = ImageIO.read(new File(Constants.jumpName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.jumpName);
		}
		try {
			teclas= ImageIO.read(new File(Constants.pauseName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.pauseName);
		}
		try {
			kinect= ImageIO.read(new File(Constants.kinnectName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.kinnectName);
		}
		try {
			teclado = ImageIO.read(new File(Constants.keyboardName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.keyboardName);
		}
		try {
			controles = ImageIO.read(new File(Constants.controllerName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.controllerName);
		}
		try {
			res960 = ImageIO.read(new File(Constants.res960Name));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.res960Name);
		}
		try {
			res480 = ImageIO.read(new File(Constants.res480Name));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.res480Name);
		}
		try {
			resolucion = ImageIO.read(new File(Constants.resName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.resName);
		}
		try {
			no = ImageIO.read(new File(Constants.noName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.noName);
		}
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
			sonido = ImageIO.read(new File(Constants.soundName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.soundName);
		}
		try {
			si = ImageIO.read(new File(Constants.yesName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.yesName);
		}
		try {
			controles1 = ImageIO.read(new File(Constants.controller1Name));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.controller1Name);
		}
		try {
			controles2 = ImageIO.read(new File(Constants.controller2Name));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.controller2Name);
		}
	}

	@Override
	public void draw() {
		Image offscreen = createImage(width, height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		offgc.setColor(Color.white);
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);
		offgc.setComposite(comp);
		// dibujar fondo
		if (mountainBack != null && starBack != null) {
			parallax.move();
			parallax.draw(offgc, 0, 0);
		}
		Font f = Constants.font_bold.deriveFont(40.0f);
		offgc.setFont(f);
		
		dibujarLetras(offgc);
		// dibujar cursor
		if (cursor != null) {
			offgc.drawImage(cursor.getImage(), cursor.getPosition().getX()+Constants.ax, cursor.getPosition().getY()+Constants.ay,
					cursor.getWidth() / 4, cursor.getHeight() / 4, null);
		}

		getGraphics().drawImage(offscreen, 0, 0, width, height, null);
		getGraphics().dispose();
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

	public void cambiarCursor() {
		if(Constants.elegidoJugador){
			cursor = new Cursor(Constants.conMenu);
		}else{
			cursor = new Cursor(Constants.optMenu); 
		}		
	}

}