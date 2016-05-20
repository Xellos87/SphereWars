package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

//TODO: continuar, reiniciar, escribir ranking.
@SuppressWarnings("serial")
public class EndMenu extends Menu{
	private final String FIN_JUEGO = "GAME OVER";
	
	public EndMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
	}
	
	public void draw(Graphics2D g2d) {
		/* Dibuja todo en pantalla */
		g2d.setColor(new Color(0, 0, 0, 100));
		g2d.fillRect(0, 0, width, height);
		
		g2d.setFont(new Font("Arial", Font.BOLD, 36));
		g2d.setColor(new Color(255,255,255,255));
		//Obtiene el ancho de la palabra con la fuente configurada
		int width_text = g2d.getFontMetrics().stringWidth(FIN_JUEGO);
		int height_text = g2d.getFontMetrics().getHeight();
		g2d.drawString(FIN_JUEGO, width/2 - width_text/2, height/2 - height_text/2);
		//TODO Imprimir puntuaciones y opciones
	}
	
	@Override
	public void draw() {
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

}
