package menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class Menu extends JPanel{
	protected int width,height;
	protected BufferedImage image;
	protected Graphics2D g;
	
	public abstract void draw();
}
