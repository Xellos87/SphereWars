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

@SuppressWarnings("serial")
public class GameScore extends JPanel{
	//Tipos de juego
	public static final int RUNNER = 0;
	public static final int POINTS = 1;
	public static final int TIME = 2;
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
		case RUNNER:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case POINTS:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case TIME:
			marker = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		}
		separator = Constants.img_handler.getImageHud(x_imgs[SEP], y_imgs[SEP], width_imgs[SEP], height_imgs[SEP]);
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

		//
		g2d.drawImage(marker, 10, height-marker.getHeight() -10, null);
		g2d.drawImage(separator, 15+marker.getWidth(), height-separator.getHeight() -10, null);
		g2d.setFont(new Font("Arial", Font.BOLD, 36));
		g2d.drawString("000", 20+marker.getWidth()+separator.getWidth(), height-10);
		if(numPlayers > 1){
			g2d.drawImage(marker, width/2 + 10, height-marker.getHeight() -10, null);
			g2d.drawImage(separator, width/2 + 15+marker.getWidth(), height-separator.getHeight() -10, null);
			g2d.drawString("000",width/2+ 20+marker.getWidth()+separator.getWidth(), height-10);
		}

		//Vuelca en el panel lo que se ha dibujado
		//getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		//getGraphics().dispose();
	}
}
