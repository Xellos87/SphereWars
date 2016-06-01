package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import kinect.Panel;
import utils.Constants;

@SuppressWarnings("serial")
public class PauseMenu extends Menu {
	//Opciones a elegir
	public static final int NONE = 0;
	public static final int CONTINUE = 1;
	public static final int RESTART = 2;
	public static final int QUIT = 3;
	//Textos a mostrar
	private final String PAUSE = "PAUSA";
	private final String TXT_CONTINUE = "Continuar";
	private final String TXT_RESTART = "Reiniciar";
	private final String TXT_QUIT = "Salir";
	private final String TXT_KINECT = "Jugador no detectado";
	//Opción seleccionada
	private int option;
	//Imagen del cursor
	private BufferedImage cursor_img;
	//Fuente a utilizar
	private Font font_bold;
	//Tamaño de los textos
	private float title_size = 21.0f;
	private float option_size = 13.0f;
	//Posiciones de los textos
	private int pauseX, pauseY;
	private int continueX, continueY;
	private int restartX, restartY;
	private int quitX, quitY;
	private int cursorX, cursorY;

	private int kinectX, kinectY;
	private Panel panel;
	//Alto y ancho de la imagen
	private int cursor_width, cursor_height;
	//Movimiento de la opcion seleccionada
	private int movX;
	
	
	public PauseMenu(int width, int height, Panel panel) {
		this.width = width;
		this.height = height;
		this.panel = panel;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
		this.font_bold = Constants.font_bold;
		option = CONTINUE;
		try{
			cursor_img = ImageIO.read(new File("images/cursor.png"));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		calculatePositions();
	}
	
	private void calculatePositions() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		Font f = font_bold.deriveFont(title_size*Constants.scale);
		g.setFont(f);
		//Calculo de posicion del titulo
		int width_text = g.getFontMetrics().stringWidth(PAUSE);
		int height_text = g.getFontMetrics().getHeight();
		pauseX = width/2 - width_text/2;
		pauseY = height/4;
		//Calculo del movimiento de la opcion
		movX = width/10;
		//Calculo de la posicion de los menus
		f = font_bold.deriveFont(option_size*Constants.scale);
		g.setFont(f);
		width_text = g.getFontMetrics().stringWidth(TXT_RESTART);
		//Calculo de continuar
		continueX = width/2 - width_text/2 - movX;
		continueY = pauseY + 2*height_text;
		height_text = g.getFontMetrics().getHeight();
		//Calculo de reiniciar
		restartX = width/2 - width_text/2 - movX;
		restartY = continueY + 2*height_text;
		//Calculo de salir
		quitX = width/2 - width_text/2 - movX;
		quitY = restartY + 2*height_text;
		// Mensaje de kinect
		kinectX = width/2 - width_text/2 - movX;
		kinectY = quitY + 2*height_text;
		//Calculo del cursor
		cursor_width = movX-10;
		cursor_height = (int) (cursor_img.getHeight() / (cursor_img.getWidth() / ((float) cursor_width)));
		cursorX = continueX;
		cursorY = continueY - cursor_height/4*3;
	}

	
	public void draw(Graphics2D g2d) {
		/* Dibuja todo en pantalla */
		g2d.setColor(new Color(0, 0, 0, 180));
		g2d.fillRect(0, 0, width, height);
		
		Font f = font_bold.deriveFont(title_size*Constants.scale);
		g2d.setFont(f);
		g2d.setColor(new Color(255,255,255,255));
		//Pinta el titulo de la pantalla
		g2d.drawString(PAUSE, pauseX, pauseY);
		//Pinta las opciones
		f = font_bold.deriveFont(option_size*Constants.scale);
		g2d.setFont(f);
		//Calcula la posicion de la opcion seleccionada
		int mov_continue = 0;
		int mov_restart = 0;
		int mov_quit = 0;
		//Muestra las opciones
		switch (option) {
		case CONTINUE:
			cursorY = continueY - cursor_height/4*3;
			mov_continue = movX;
			break;
		case RESTART:
			cursorY = restartY - cursor_height/4*3;
			mov_restart = movX;
			break;
		case QUIT:
			cursorY = quitY - cursor_height/4*3;
			mov_quit = movX;
			break;
		}
		//Opcion de continuar
		g2d.drawString(TXT_CONTINUE, continueX + mov_continue, continueY);
		//Opcion de reiniciar
		g2d.drawString(TXT_RESTART, restartX + mov_restart, restartY);
		//Opcion de salir
		g2d.drawString(TXT_QUIT, quitX + mov_quit, quitY);
		// Mensaje de kinect

	
		//Cursor
		g2d.drawImage(cursor_img,cursorX,cursorY,cursor_width,cursor_height, null);
		
		if(panel.isPlayerDetected()){
			g2d.setColor(new Color(255, 0, 0, 180));
			g2d.drawString(TXT_KINECT,  kinectX, kinectY);
		}
	}

	@Override
	public void cursorDown() {
		if(option<QUIT){
			option++;
		}else{
			option = CONTINUE;
		}
	}

	@Override
	public void cursorUp() {
		if(option>CONTINUE){
			option--;
		}else{
			option = QUIT;
		}
	}

	@Override
	public String cursorEnter() {
		//TODO porque texto en vez de numero????
		return String.valueOf(option);
	}


	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void cursorRight() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void cursorLeft() {
		// TODO Auto-generated method stub
		
	}

}
