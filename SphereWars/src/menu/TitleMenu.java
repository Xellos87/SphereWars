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
import videogame.Game;
import videogame.Parallax;

@SuppressWarnings("serial")
public class TitleMenu extends Menu {
	BufferedImage starBack;
	BufferedImage mountainBack;
	BufferedImage title;
	BufferedImage jugar;
	BufferedImage help;
	BufferedImage opciones;
	BufferedImage creditos;
	BufferedImage salir;
	BufferedImage versus;
	BufferedImage dosD;
	BufferedImage tresD;
	
	boolean firstTime = true;
	
	Parallax parallax;
	private Cursor cursor;

	public TitleMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		cargarImagenes();
		// set background
		
		cursor = new Cursor(Constants.titMenu);
		
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
		try {
			dosD = ImageIO.read(new File(Constants.dosdName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + Constants.dosdName);
		}
		try {
			tresD = ImageIO.read(new File(Constants.tresdName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + Constants.tresdName);
		}
		try {
			versus = ImageIO.read(new File(Constants.versusName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + Constants.versusName);
		}
		try{
			title = ImageIO.read(new File(Constants.titleName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.titleName);
		}
		try{
			jugar = ImageIO.read(new File(Constants.jugarName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.jugarName);
			e.printStackTrace();
		}
		try{
			help = ImageIO.read(new File(Constants.helpName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.jugarName);
		}
		try{
			opciones = ImageIO.read(new File(Constants.opcionesName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.opcionesName);
		}
		try{
			creditos = ImageIO.read(new File(Constants.creditosName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.creditosName);
		}
		try{
			salir = ImageIO.read(new File(Constants.salirName));
		}catch(IOException e){
			System.err.println("Problem with font "+Constants.salirName);
		}
	}

	public void draw() {
		//TODO: corregir bug de desplazamiento al volver de 2D/3D
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
		//dibujar opciones de menu
		if(title!=null){
			offgc.drawImage(title, Constants.titlePos.getX()+Constants.ax, Constants.titlePos.getY()+Constants.ay, (int)(title.getWidth()/5), (int)(title.getHeight()/5), null);
		}
		if(jugar!=null && !cursor.getOpcion().equalsIgnoreCase("start")){	
			offgc.drawImage(jugar, Constants.jugarPos.getX()+Constants.ax, Constants.jugarPos.getY()+Constants.ay, jugar.getWidth()/2,jugar.getHeight()/2, null);
		}else{
			offgc.drawImage(jugar, Constants.jugarPos.getX()+Constants.desplazamiento+Constants.ax, Constants.jugarPos.getY()+Constants.ay, jugar.getWidth()/2,jugar.getHeight()/2, null);
		}
		if(dosD!=null && Constants.visualMode == Game.MODE_2D){
			offgc.drawImage(dosD, Constants.dosDjPos.getX()+Constants.ax, Constants.dosDjPos.getY()+Constants.ay, dosD.getWidth()/2,dosD.getHeight()/2, null);
		}else{
			offgc.drawImage(dosD, Constants.dosDjPos.getX()+Constants.ax, Constants.dosDjPos.getY()+Constants.ay, (int)(dosD.getWidth()/2.5),(int)(dosD.getHeight()/2.5), null);
			
		}
		if(tresD!=null && Constants.visualMode == Game.MODE_3D){
			offgc.drawImage(tresD, Constants.tresDjPos.getX()-5+Constants.ax, Constants.tresDjPos.getY()+Constants.ay, tresD.getWidth()/2,tresD.getHeight()/2, null);
		}else{
			offgc.drawImage(tresD, Constants.tresDjPos.getX()+Constants.ax, Constants.tresDjPos.getY()+Constants.ay, (int)(tresD.getWidth()/2.5),(int)(tresD.getHeight()/2.5), null);
			
		}
		if(versus!=null && !cursor.getOpcion().equalsIgnoreCase("versus")){	
			offgc.drawImage(versus, Constants.versusPos.getX()+Constants.ax, Constants.versusPos.getY()+Constants.ay, versus.getWidth()/2,versus.getHeight()/2, null);
		}else{
			offgc.drawImage(versus, Constants.versusPos.getX()+Constants.desplazamiento+Constants.ax, Constants.versusPos.getY()+Constants.ay, versus.getWidth()/2,versus.getHeight()/2, null);
		}
		if(help!=null && !cursor.getOpcion().equalsIgnoreCase("help")){
			offgc.drawImage(help, Constants.helpPos.getX()+Constants.ax, Constants.helpPos.getY()+Constants.ay, help.getWidth()/2,help.getHeight()/2, null);
		}else{
			offgc.drawImage(help, Constants.helpPos.getX()+Constants.desplazamiento+Constants.ax, Constants.helpPos.getY()+Constants.ay, help.getWidth()/2,help.getHeight()/2, null);
		}
		if(creditos!=null && !cursor.getOpcion().equalsIgnoreCase("credits")){	
			offgc.drawImage(creditos, Constants.creditPos.getX()+Constants.ax, Constants.creditPos.getY()+Constants.ay, creditos.getWidth()/2,creditos.getHeight()/2, null);
		}else{
			offgc.drawImage(creditos, Constants.creditPos.getX()+Constants.desplazamiento+Constants.ax, Constants.creditPos.getY()+Constants.ay, creditos.getWidth()/2,creditos.getHeight()/2, null);
		}
		if(opciones!=null && !cursor.getOpcion().equalsIgnoreCase("options")){
			offgc.drawImage(opciones, Constants.opcionesPos.getX()+Constants.ax, Constants.opcionesPos.getY()+Constants.ay, opciones.getWidth()/2,opciones.getHeight()/2, null);
		}else{
			offgc.drawImage(opciones, Constants.opcionesPos.getX()+Constants.desplazamiento+Constants.ax, Constants.opcionesPos.getY()+Constants.ay, opciones.getWidth()/2,opciones.getHeight()/2, null);
		}
		if(salir!=null && !cursor.getOpcion().equalsIgnoreCase("Exit")){
			offgc.drawImage(salir, Constants.salirPos.getX()+Constants.ax, Constants.salirPos.getY()+Constants.ay, salir.getWidth()/2,salir.getHeight()/2, null);
		}else{
			offgc.drawImage(salir, Constants.salirPos.getX()+Constants.desplazamiento+Constants.ax, Constants.salirPos.getY()+Constants.ay, salir.getWidth()/2,salir.getHeight()/2, null);
		}
		//dibujar cursor
		if(cursor != null){
			offgc.drawImage(cursor.getImage(), cursor.getPosition().getX()+Constants.ax,cursor.getPosition().getY()+Constants.ay, cursor.getWidth()/4, cursor.getHeight()/4, null);
		}
		
		 getGraphics().drawImage(offscreen,0,0,width,height,null);
		 getGraphics().dispose();		
	}

	private void initParallax() {
		String[] names = {Constants.starBackName,Constants.mountainBackName};
		int[] velocity = {2,1};
		int[] posx= {0,0};
		int[] posy = {0,height - mountainBack.getHeight()};
		parallax = new Parallax(2,names,velocity,posx,posy,width,height);
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
		cursor.right();
	}

	@Override
	public void cursorLeft() {
		cursor.left();
	}
}
