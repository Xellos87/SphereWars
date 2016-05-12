package scoreboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

import map.MapController;

@SuppressWarnings("serial")
public class GameScore extends JPanel{
	//Dimensiones del panel
	private int width;
	private int height;
	//Numero de jugadores en pantalla, para poner diferentes marcadores
	private int numPlayers;

	public GameScore(int width, int height, int numPlayers){
		this.width = width;
		this.height = height;
		this.numPlayers = numPlayers;
		setPreferredSize(new Dimension(width, height));
		System.out.printf("ancho: %d, alto: %d\n", width,height);
		initScoreBoard();
	}

	private void initScoreBoard() {
		
	}

	public void draw(){
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		
		/* Dibuja en pantalla */
		offgc.setColor(new Color(0, 0, 0,255));
		offgc.fillRect(0, 0, width, height);
		offgc.setColor(new Color(255,255,255,255));
		offgc.drawString("Player1", 10, 10);
		
		
		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		getGraphics().dispose();
	}
}
