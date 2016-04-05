package menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class TitleMenu extends Menu{
	private String[] list_menu = {"Start", "Help", "Exit"};
	
	public TitleMenu(JPanel window) {
		super(window);
	}
	
	public void draw2D(Graphics2D g){
		g.setColor(new Color(255, 0, 0));
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		g.setColor(new Color(0, 255, 0));
		g.fillOval(50, 50, 100, 100);
	}
	
}
