package scoreboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import map.MapController;
import utils.Constants;
import videogame.Game;

@SuppressWarnings("serial")
public class GameScore extends JPanel{
	//Elemento que se pueden pintar en el marcador
	private final int COIN = 0;
	private final int SEP = 1;
	//Dimensiones del panel
	private int width;
	private int height;
	//Numero de jugadores en pantalla, para poner diferentes marcadores
	private int numPlayers;
	//Tipo de juego para poner unos marcadores u otros
	private int type;
	//Datos para extraer las imagenes de su spritesheet
	private int[] x_imgs = {55,0};
	private int[] y_imgs = {0,239};
	private int[] width_imgs = {47,30};
	private int[] height_imgs = {47,28};
	//Imagenes del marcador
	private BufferedImage marker;
	private BufferedImage separator;
	//Puntuaciones de cada jugador
	private String dist_p1, dist_p2;
	private String coins_p1, coins_p2;
	private String time_p1, time_p2;

	public GameScore(int width, int height, int numPlayers, int typeGame){
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;
		this.type = typeGame;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);
		initScoreBoard();
	}

	private void initScoreBoard() {
		switch (type) {
		case Game.RUNNER:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case Game.COINS:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case Game.TIME:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		}
		separator = Constants.img_handler.getImageHud(x_imgs[SEP], y_imgs[SEP], width_imgs[SEP], height_imgs[SEP]);
		//Inicializa los marcadores
		setScoreDistanceP1(0);
		setScoreDistanceP2(0);
		setScoreCoinsP1(0);
		setScoreCoinsP2(0);
	}

	public void draw(Graphics2D g2d){
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		//Image offscreen = createImage(width,height);
		//Graphics2D offgc = (Graphics2D) offscreen.getGraphics();

		/* Dibuja en pantalla */
		g2d.setColor(new Color(0, 0, 0,255));
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(new Color(255,255,255,255));
		g2d.setFont(new Font("Arial", Font.BOLD, 16));
		g2d.drawString("Player 1", 10, 15);
		if(numPlayers > 1){
			g2d.drawString("Player 2", width/2 + 10, 15);
		}

		switch (type) {
		case Game.RUNNER:
			g2d.setFont(new Font("Arial", Font.BOLD, 36));
			g2d.drawString(dist_p1, 20+marker.getWidth()+separator.getWidth(), height-10);
			if(numPlayers > 1){
				g2d.drawString(dist_p2,width/2+ 20+marker.getWidth()+separator.getWidth(), height-10);
			}
			break;
		case Game.COINS:
			g2d.drawImage(marker, 10, height-marker.getHeight() -10, null);
			g2d.drawImage(separator, 15+marker.getWidth(), height-separator.getHeight() -10, null);
			g2d.setFont(new Font("Arial", Font.BOLD, 36));
			g2d.drawString(coins_p1, 20+marker.getWidth()+separator.getWidth(), height-10);
			if(numPlayers > 1){
				g2d.drawImage(marker, width/2 + 10, height-marker.getHeight() -10, null);
				g2d.drawImage(separator, width/2 + 15+marker.getWidth(), height-separator.getHeight() -10, null);
				g2d.drawString(coins_p2,width/2+ 20+marker.getWidth()+separator.getWidth(), height-10);
			}
			break;
		case Game.TIME:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		}
		
		//
		

		//Vuelca en el panel lo que se ha dibujado
		//getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		//getGraphics().dispose();
	}

	public void setScoreDistanceP1(double dist) {
		dist_p1 = String.format("%08.2f", dist);
	}
	
	public void setScoreDistanceP2(double dist) {
		dist_p2 = String.format("%08.2f", dist);
	}
	
	public void setScoreCoinsP1(int coins){
		coins_p1 = String.format("%03d", coins);
	}
	
	public void setScoreCoinsP2(int coins){
		coins_p2 = String.format("%03d", coins);
	}
}
