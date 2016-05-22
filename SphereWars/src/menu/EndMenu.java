package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;

//TODO: continuar, reiniciar, escribir ranking.
@SuppressWarnings("serial")
public class EndMenu extends Menu{
	private final String FIN_JUEGO = "GAME OVER";
	//Alpha del fondo
	private final int MAX_ALPHA_BACK = 100;
	private int alpha_back;
	//Alpha del texto
	private final int MAX_ALPHA_TEXT = 255;
	private int alpha_text;

	public EndMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
		alpha_back = 0;
		alpha_text = 0;
	}

	public void draw(Graphics2D g2d) {
		if(isVisible()){
			/* Dibuja todo en pantalla */
			if(alpha_back< MAX_ALPHA_BACK){
				alpha_back += 5;
			}
			g2d.setColor(new Color(0, 0, 0, alpha_back));
			g2d.fillRect(0, 0, width, height);
			if(alpha_text< MAX_ALPHA_TEXT){
				alpha_text += 5;
			}
			g2d.setFont(new Font("Arial", Font.BOLD, 36));
			g2d.setColor(new Color(255,255,255,alpha_text));
			//Obtiene el ancho de la palabra con la fuente configurada
			int width_text = g2d.getFontMetrics().stringWidth(FIN_JUEGO);
			int height_text = g2d.getFontMetrics().getHeight();
			g2d.drawString(FIN_JUEGO, width/2 - width_text/2, height/2 - height_text/2);
			//TODO Imprimir puntuaciones y opciones
		}else{
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, width, height);
		}
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

	public void initAlpha() {
		alpha_back = 0;
		alpha_text = 0;
	}

}
