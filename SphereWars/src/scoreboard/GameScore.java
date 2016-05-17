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

	public void draw(){
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();

		/* Dibuja en pantalla */
		offgc.setColor(new Color(0, 0, 0,255));
		offgc.fillRect(0, 0, width, height);
		offgc.setColor(new Color(255,255,255,255));
		offgc.setFont(new Font("Arial", Font.BOLD, 16));
		offgc.drawString("Player 1", 10, 15);
		if(numPlayers > 1){
			offgc.drawString("Player 2", width/2 + 10, 15);
		}

		//
		offgc.drawImage(marker, 10, height-marker.getHeight() -10, null);
		offgc.drawImage(separator, 15+marker.getWidth(), height-separator.getHeight() -10, null);
		offgc.setFont(new Font("Arial", Font.BOLD, 36));
		offgc.drawString("000", 20+marker.getWidth()+separator.getWidth(), height-10);
		if(numPlayers > 1){
			offgc.drawImage(marker, width/2 + 10, height-marker.getHeight() -10, null);
			offgc.drawImage(separator, width/2 + 15+marker.getWidth(), height-separator.getHeight() -10, null);
			offgc.drawString("000",width/2+ 20+marker.getWidth()+separator.getWidth(), height-10);
		}

		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		getGraphics().dispose();
	}
}
