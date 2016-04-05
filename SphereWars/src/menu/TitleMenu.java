package menu;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class TitleMenu extends Menu{
	private String[] list_menu = {"Start", "Help", "Exit"};
	
	public TitleMenu(JPanel window) {
		super(window);
	}
	
	public void draw(Graphics2D g){
		g.setColor(new Color(255, 0, 0));
		g.drawRect(0, 0, window.getWidth(), window.getHeight());
	}
	
}
