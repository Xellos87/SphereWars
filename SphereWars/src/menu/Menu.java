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

	public abstract void cursorDown();

	public abstract void cursorUp();

	public abstract String cursorEnter();

	public abstract void cursorRight();

	public abstract void cursorLeft();
}
