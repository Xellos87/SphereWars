package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PauseMenu extends Menu {

	public PauseMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
	}

	@Override
	public void draw() {
		//Carga el doble buffer en el que se dibuja todo y luego se vuelca a pantalla
		Image offscreen = createImage(width,height);
		Graphics2D offgc = (Graphics2D) offscreen.getGraphics();
		/* Dibuja todo en pantalla */
		offgc.setColor(new Color(255, 0, 0, 100));
		offgc.fillRect(0, 0, width, height);
		
		offgc.setColor(new Color(255,255,255,255));
		offgc.drawString("PAUSA", width/2, height/2);
		
		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(offscreen, 0, 0,width, height,null);
		getGraphics().dispose();
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
