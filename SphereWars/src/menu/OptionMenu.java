package menu;

import java.awt.AlphaComposite;
import java.awt.Color;
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
	//TODO: jugador1, jugador2
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
			offgc.drawImage(title, Constants.titlePos.getX(), Constants.titlePos.getY(), (int)(title.getWidth()/5), (int)(title.getHeight()/5), null);
		}
		if(sonido!=null && !cursor.getOpcion().equalsIgnoreCase("sound")){
			offgc.drawImage(sonido, Constants.soundPos.getX(), Constants.soundPos.getY(), (int)(sonido.getWidth()/2), (int)(sonido.getHeight()/2), null);
		}else{
			offgc.drawImage(sonido, Constants.soundPos.getX()+Constants.desplazamiento, Constants.soundPos.getY(), sonido.getWidth()/2,sonido.getHeight()/2, null);
		}
		if(si!=null && Constants.sound){
			offgc.drawImage(si, Constants.yesPos.getX(), Constants.yesPos.getY(), (int)(si.getWidth()/2), (int)(si.getHeight()/2), null);
		}else{
			offgc.drawImage(si, Constants.yesPos.getX(), Constants.yesPos.getY(), (int)(si.getWidth()/2.5),(int)(si.getHeight()/2.5), null);
		}
		if(no!=null && !Constants.sound){
			offgc.drawImage(no, Constants.noPos.getX(), Constants.noPos.getY(), (int)(no.getWidth()/2), (int)(no.getHeight()/2), null);
		}else{
			offgc.drawImage(no, Constants.noPos.getX(), Constants.noPos.getY(), (int)(no.getWidth()/2.5),(int)(no.getHeight()/2.5), null);
		}
		if(resolucion!=null && !cursor.getOpcion().equalsIgnoreCase("resolution")){
			offgc.drawImage(resolucion, Constants.resPos.getX(), Constants.resPos.getY(), (int)(resolucion.getWidth()/2), (int)(resolucion.getHeight()/2), null);
		}else{
			offgc.drawImage(resolucion, Constants.resPos.getX()+Constants.desplazamiento-10, Constants.resPos.getY(), resolucion.getWidth()/2,resolucion.getHeight()/2, null);
		}
		if(res480!=null && Constants.scale == 2){
			offgc.drawImage(res480, Constants.res480Pos.getX()+Constants.desplazamiento, Constants.res480Pos.getY()+5, (int)(res480.getWidth()/2.5),(int)(res480.getHeight()/2.5), null);
		}
		if(res960!=null && Constants.scale == 4){
			offgc.drawImage(res960, Constants.res960Pos.getX()+Constants.desplazamiento, Constants.res960Pos.getY()+5, (int)(res960.getWidth()/2.5),(int)(res960.getHeight()/2.5), null);
		}
		if(controles!=null && !cursor.getOpcion().equalsIgnoreCase("controller")){
			offgc.drawImage(controles, Constants.controllerPos.getX(), Constants.controllerPos.getY(), (int)(controles.getWidth()/2), (int)(controles.getHeight()/2), null);
		}else{
			offgc.drawImage(controles, Constants.controllerPos.getX()+Constants.desplazamiento-10, Constants.controllerPos.getY(), controles.getWidth()/2,controles.getHeight()/2, null);
		}
		if(teclado!=null && Constants.conTeclado){
			offgc.drawImage(teclado, Constants.keyboardPos.getX(), Constants.keyboardPos.getY()+5, (int)(teclado.getWidth()/2.5), (int)(teclado.getHeight()/2.5), null);
		}
		if(kinect!=null && !Constants.conTeclado){
			offgc.drawImage(kinect, Constants.kinnectPos.getX(), Constants.kinnectPos.getY()+5, (int)(kinect.getWidth()/2.5), (int)(kinect.getHeight()/2.5), null);
		}
		if(volver!=null && !cursor.getOpcion().equalsIgnoreCase("back")){
			offgc.drawImage(volver, Constants.backPos.getX(), Constants.backPos.getY(), (int)(volver.getWidth()/2), (int)(volver.getHeight()/2), null);
		}else{
			offgc.drawImage(volver, Constants.backPos.getX()+Constants.desplazamiento, Constants.backPos.getY(), volver.getWidth()/2,volver.getHeight()/2, null);
		}
		if(Constants.conTeclado){
			if(teclas!=null){
				offgc.drawImage(teclas, Constants.keyPos.getX(), Constants.keyPos.getY(), (int)(teclas.getWidth()/2.25), (int)(teclas.getHeight()/2.25), null);
			}
			if(saltar!=null){
				offgc.drawImage(saltar, Constants.jumpPos.getX(), Constants.jumpPos.getY(), (int)(saltar.getWidth()/2), (int)(saltar.getHeight()/2), null);
			}
			if(correr!=null){
				offgc.drawImage(correr, Constants.runPos.getX(), Constants.runPos.getY(), (int)(correr.getWidth()/2), (int)(correr.getHeight()/2), null);
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
			teclas= ImageIO.read(new File(Constants.keyName));
		} catch (IOException e) {
			System.err.println("Problem with source " + Constants.keyName);
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
	}

	@Override
	public void draw() {
		Image offscreen = createImage(width, height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		int rule = AlphaComposite.SRC_OVER;
		Composite comp = AlphaComposite.getInstance(rule, Constants.alphaComp);
		offgc.setComposite(comp);
		// dibujar fondo
		if (mountainBack != null && starBack != null) {
			parallax.move();
			parallax.draw(offgc, 0, 0);
		}
		dibujarLetras(offgc);
		// dibujar cursor
		if (cursor != null) {
			offgc.drawImage(cursor.getImage(), cursor.getPosition().getX(), cursor.getPosition().getY(),
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

}