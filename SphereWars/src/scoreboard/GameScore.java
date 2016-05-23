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
	private BufferedImage marker_img;
	//Fuente a utilizar
	private Font font_bold;
	//Puntuaciones de cada jugador
	private String dist_p1, dist_p2;
	private String coins_p1, coins_p2;
	private String time_p1, time_p2;
	//Posiciones de los elementos de pantalla
	private int player1X, player1Y;
	private int player2X, player2Y;
	private int score_p1X, score_p1Y;
	private int score_p2X, score_p2Y;
	private int marker_p1X, marker_p1Y;
	private int marker_p2X, marker_p2Y;
	//Altura y anchura del marcador
	private int marker_height, marker_width;

	public GameScore(int width, int height, int numPlayers, int typeGame){
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;
		this.type = typeGame;
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		setFocusable(false);
		this.font_bold = Constants.font_bold;
		
		initScoreBoard();
		
		calculatePositions();
	}

	private void calculatePositions() {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		//Calculo de la posicion de los nombres
		Font f = font_bold.deriveFont(28.0f);
		g.setFont(f);
		int heigth_text = g.getFontMetrics().getHeight();
		//Posicion del jugador 1
		player1X = width/15;
		player1Y = height/2 - heigth_text/4;
		//Posicion del jugador 2
		player2X = width/2 + player1X;
		player2Y = player1Y;
		//Posicion de la puntuacion de jugador 1
		f = font_bold.deriveFont(22.0f);
		g.setFont(f);
		heigth_text = g.getFontMetrics().getHeight();
		
		//Posicion de la puntuacion del jugador 2
		
		//Posicion del marcador del jugador 1
		marker_p1X = player1X;
		marker_p1Y = height/2 + heigth_text/4;
		//Posicion del marcador del jugador 2
		marker_p2X = player1X;
		marker_p2Y = marker_p2Y;
		//Altura y anchura dlel marcador
		marker_height = heigth_text;
		marker_width = (int) (marker_img.getWidth() / (marker_img.getHeight() / ((float) marker_height)));
	}

	private void initScoreBoard() {
		switch (type) {
		case Game.RUNNER:
			//marker_img = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case Game.COINS:
			marker_img = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		case Game.TIME:
			//marker_img = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		}
		//Inicializa los marcadores
		setScoreDistanceP1(0);
		setScoreDistanceP2(0);
		setScoreCoinsP1(0);
		setScoreCoinsP2(0);
	}

	public void draw(Graphics2D g2d){
		/* Dibuja en pantalla */
		//Fondo
		g2d.setColor(new Color(0, 0, 0,255));
		g2d.fillRect(0, 0, width, height);
		//Nombre del jugador 1
		Font f = font_bold.deriveFont(28.0f);
		g2d.setFont(f);
		g2d.setColor(new Color(255,255,255,255));
		g2d.drawString("Player 1", player1X, player1Y);
		//Nombre del jugador 2 si lo hay
		if(numPlayers >= 1){
			g2d.drawString("Player 2", player2X, player2Y);
		}
		//Puntuaciones
		f = font_bold.deriveFont(22.0f);
		g2d.setFont(f);
		//Marcador del jugador 1
		if(marker_img != null){
			g2d.drawImage(marker_img, marker_p1X, marker_p1Y,marker_width,marker_height, null);
		}
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
