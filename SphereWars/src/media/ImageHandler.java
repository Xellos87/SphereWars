package media;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHandler {
	//Contenedores de imagenes de los elementos que estan en pantalla
	private BufferedImage tiles_image;
	private BufferedImage player_image;
	private BufferedImage items_image;
	private BufferedImage enemies_image;
	private BufferedImage hud_image;
	//Ruta de aceso a las imagenes
	private String path_tiles = "images/platforms.png";
	private String path_player = "images/ball.gif";
	private String path_items = "images/items_spritesheet.png";
	private String path_enemies = "images/enemies_spritesheet.png";
	private String path_hud = "images/hud_spritesheet.png";
	
	public ImageHandler(){
		initHandler();
	}
	
	private void initHandler() {
		try {
			tiles_image = ImageIO.read(new File(path_tiles));
			player_image = ImageIO.read(new File(path_player));
			items_image = ImageIO.read(new File(path_items));
			enemies_image = ImageIO.read(new File(path_enemies));
			hud_image = ImageIO.read(new File(path_hud));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedImage getImageTile(int x, int y, int width, int height){
		BufferedImage image = tiles_image.getSubimage(x, y, width, height);
		return image;
	}
	
	public BufferedImage getImagePlayer(int x, int y, int width, int height){
		BufferedImage image = player_image.getSubimage(x, y, width, height);
		return image;
	}
	
	public BufferedImage getImageItem(int x, int y, int width, int height){
		BufferedImage image = items_image.getSubimage(x, y, width, height);
		return image;
	}
	
	public BufferedImage getImageEnemie(int x, int y, int width, int height){
		BufferedImage image = enemies_image.getSubimage(x, y, width, height);
		return image;
	}
	
	public BufferedImage getImageHud(int x, int y, int width, int height){
		BufferedImage image = hud_image.getSubimage(x, y, width, height);
		return image;
	}
}
