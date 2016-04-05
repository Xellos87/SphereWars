package menu;

import java.awt.Graphics2D;

import javax.swing.JPanel;

public abstract class Menu {
	protected JPanel window;
	
	public Menu(JPanel window){
		this.window = window;
	}
	
	public abstract void draw2D(Graphics2D g);
}
