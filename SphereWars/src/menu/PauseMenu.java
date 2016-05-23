package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

@SuppressWarnings("serial")
public class PauseMenu extends Menu {
	//Textos
	private final String PAUSE = "PAUSA";
	
	public PauseMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
	}

	
	public void draw(Graphics2D g2d) {
		/* Dibuja todo en pantalla */
		g2d.setColor(new Color(255, 0, 0, 100));
		g2d.fillRect(0, 0, width, height);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 36));
		g2d.setColor(new Color(255,255,255,255));
		//Obtiene el ancho de la palabra con la fuente configurada
		int width_text = g2d.getFontMetrics().stringWidth(PAUSE);
		int height_text = g2d.getFontMetrics().getHeight();
		g2d.drawString(PAUSE, width/2 - width_text/2, height/2 - height_text/2);
		//TODO Imprimir opciones, continuar, reiniciar, salir...
	}

	@Override
	public void cursorDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cursorUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public String cursorEnter() {
		// TODO Auto-generated method stub
		return null;
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
