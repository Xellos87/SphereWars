package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class PauseMenu extends Menu {

	public PauseMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		image = new BufferedImage(width, height, 
				BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
	}

	@Override
	public void draw() {
		//Dibuja todo en pantalla
		g.setColor(new Color(255, 0, 0));
		g.fillRect(0, 0, width, height);
		g.setColor(new Color(0, 0, 255));
		g.fillOval(200, 200, 100, 100);
		//Vuelca en el panel lo que se ha dibujado
		getGraphics().drawImage(image, 0, 0,width, height,null);
		getGraphics().dispose();
	}

}
