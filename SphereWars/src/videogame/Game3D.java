package videogame;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Game3D extends JPanel {
	private int width,height;

	public Game3D(int width, int height){
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
	}

	public void draw() {
		
	}

}

