package scoreboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import map.MapController;
import utils.Constants;
import utils.Position;
import videogame.Game;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: GameScore.java
 * 
 * Comentarios: Marcador que dibuja la puntuación de cada jugador
 * 
 */
@SuppressWarnings("serial")
public class GameScore extends JPanel {
	// Elemento que se pueden pintar en el marcador
	private final int COIN = 0;
	private final int FLAG = 1;
	// Dimensiones del panel
	private int width;
	private int height;
	// Numero de jugadores en pantalla, para poner diferentes marcadores
	private int numPlayers;
	// Tipo de juego para poner unos marcadores u otros
	private int type;
	// Datos para extraer las imagenes de su spritesheet
	private int[] x_imgs = { 55, 274 };
	private int[] y_imgs = { 0, 144 };
	private int[] width_imgs = { 47, 70 };
	private int[] height_imgs = { 47, 70 };
	// Imagenes del marcador
	private BufferedImage marker_img;
	// Fuente a utilizar
	private Font font_bold;
	// Puntuaciones de cada jugador
	private String dist_p1, dist_p2;
	private String coins_p1, coins_p2;
	private String time_p1, time_p2;
	// Posiciones de los elementos de pantalla
	private int player1X, player1Y;
	private int player2X, player2Y;
	private int score_p1X, score_p1Y;
	private int score_p2X, score_p2Y;
	private int marker_p1X, marker_p1Y;
	private int marker_p2X, marker_p2Y;
	// Altura y anchura del marcador
	private int marker_height, marker_width;

	private BufferedImage sprintIcon;
	private Position sprintIconp1Pos;
	private Position sprintIconp2Pos;
	private String sprintName = "images/sprint.png";

	public GameScore(int width, int height, int numPlayers, int typeGame) {
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
		// Calculo de la posicion de los nombres
		Font f = font_bold.deriveFont(14.0f*Constants.scale);
		g.setFont(f);
		int heigth_text = g.getFontMetrics().getHeight();
		int width_text = g.getFontMetrics().stringWidth("Player 1");
		// Posicion del jugador 1
		player1X = width / 25;
		player1Y = height / 2 - heigth_text / 4;
		// Posicion del jugador 2
		player2X = width / 2 + player1X;
		player2Y = player1Y;
		// Calculo de dimensiones del texto
		f = font_bold.deriveFont(11.0f*Constants.scale);
		g.setFont(f);
		heigth_text = g.getFontMetrics().getHeight();
		// Posicion del marcador del jugador 1
		marker_p1X = player1X;
		marker_p1Y = height / 2 + heigth_text / 4;
		// Posicion del marcador del jugador 2
		marker_p2X = player2X;
		marker_p2Y = marker_p1Y;
		// Altura y anchura dlel marcador
		marker_height = heigth_text;
		marker_width = (int) (marker_img.getWidth() / (marker_img.getHeight() / ((float) marker_height)));
		// Posicion de la puntuacion del jugador 1
		score_p1X = player1X;
		score_p1Y = marker_p1Y + heigth_text;
		// Posicion de la puntuacion del jugador 2
		score_p2X = player2X;
		score_p2Y = score_p1Y;
		sprintIconp1Pos = new Position(player1X + width_text, player1Y-(15*Constants.scale));
		sprintIconp2Pos = new Position(player2X + width_text, player2Y-(15*Constants.scale));
	}

	private void initScoreBoard() {
		switch (type) {
		case Game.RUNNER:
			marker_img = Constants.img_handler.getImageItem(x_imgs[FLAG], y_imgs[FLAG], width_imgs[FLAG],
					height_imgs[FLAG]);
			break;
		case Game.COINS:
			marker_img = Constants.img_handler.getImageHud(x_imgs[COIN], y_imgs[COIN], width_imgs[COIN],
					height_imgs[COIN]);
			break;
		case Game.TIME:
			// marker_img = Constants.img_handler.getImageHud(x_imgs[COIN],
			// y_imgs[COIN], width_imgs[COIN], height_imgs[COIN]);
			break;
		}
		// Inicializa los marcadores
		setScoreDistanceP1(0);
		setScoreDistanceP2(0);
		setScoreCoinsP1(0);
		setScoreCoinsP2(0);
		try {
			sprintIcon = ImageIO.read(new File(sprintName));
		} catch (IOException e) {
			System.err.println("Problem with source: " + sprintName);
		}
	}

	public void draw(Graphics2D g2d) {
		/* Dibuja en pantalla */
		// Fondo
		g2d.setColor(new Color(0, 0, 0, 255));
		g2d.fillRect(0, 0, width, height);
		// icono sprint
		if (sprintIcon != null) {
			if (Constants.sprintp1) {
				g2d.drawImage(sprintIcon, sprintIconp1Pos.getX(), sprintIconp1Pos.getY(),
						(int) (sprintIcon.getWidth() / 7), (int) (sprintIcon.getHeight() / 7), null);
			}
			if (Constants.sprintp2) {
				g2d.drawImage(sprintIcon, sprintIconp2Pos.getX(), sprintIconp2Pos.getY(),
						(int) (sprintIcon.getWidth() / 7), (int) (sprintIcon.getHeight() / 7), null);
			}
		}
		// Nombre del jugador 1
		Font f = font_bold.deriveFont(14.0f*Constants.scale);
		g2d.setFont(f);
		g2d.setColor(new Color(255, 255, 255, 255));
		g2d.drawString("Player 1", player1X, player1Y);
		// Nombre del jugador 2 si lo hay
		if (numPlayers > 1) {
			g2d.drawString("Player 2", player2X, player2Y);
		}
		// Puntuaciones
		f = font_bold.deriveFont(11.0f*Constants.scale);
		g2d.setFont(f);
		int inc_p1X = 0;
		;
		// Marcador del jugador 1
		if (marker_img != null) {
			g2d.drawImage(marker_img, marker_p1X, marker_p1Y, marker_width, marker_height, null);
			inc_p1X = marker_width;
		}
		// Puntuacion del jugador 1
		g2d.drawString(getScoreString(1), score_p1X + inc_p1X, score_p1Y);
		// Puntuacion del jugador 2
		if (numPlayers > 1) {
			int inc_p2X = 0;
			;
			// Marcador del jugador 2
			if (marker_img != null) {
				g2d.drawImage(marker_img, marker_p2X, marker_p2Y, marker_width, marker_height, null);
				inc_p2X = marker_width;
			}
			// Puntuacion del jugador 2
			g2d.drawString(getScoreString(2), score_p2X + inc_p2X, score_p2Y);
		}
	}

	private String getScoreString(int player) {
		String tmp = "";
		switch (type) {
		case Game.RUNNER:
			if (player == 1) {
				tmp = dist_p1 + "m";
			} else {
				tmp = dist_p2 + "m";
			}
			break;
		case Game.COINS:
			if (player == 1) {
				tmp = " X " + coins_p1;
			} else {
				tmp = " X " + coins_p2;
			}
			break;
		case Game.TIME:

			break;
		}
		return tmp;
	}

	public void setScoreDistanceP1(double dist) {
		dist_p1 = String.format("%08.2f", dist);
	}

	public void setScoreDistanceP2(double dist) {
		dist_p2 = String.format("%08.2f", dist);
	}

	public void setScoreCoinsP1(int coins) {
		coins_p1 = String.format("%03d", coins);
	}

	public void setScoreCoinsP2(int coins) {
		coins_p2 = String.format("%03d", coins);
	}
}
