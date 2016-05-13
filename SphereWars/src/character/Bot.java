package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Bot extends GameObject implements Sprite{
	//Tipo de enemigo
	public static final int SLIME = 0;
	public static final int TRIANGLE = 3;
	//Estados del enemigo
	private final int WALK1 = 0;
	private final int WALK2 = 1;
	private final int DEAD = 2;
	//Dirección del bot
	private final int RIGHT = 0;
	private final int LEFT = 1;
	//Posicion de la imagen
	private static int[] x_imgs = {52,0,0};
	private static int[] y_imgs = {125,125,112};
	//Tamaño de la imagen
	private static int[] width_imgs = {50,51,59};
	private static int[] height_imgs = {28,26,12};
	//
	private int type;
	private int state;
	private int direction;

	public Bot(int x, int y, int block_width, int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		this.type = type;
		this.state = WALK1;
		this.direction = RIGHT;
		selectImage();
		resize();
	}

	private void rotateImage(){
		int w = image.getWidth();  
		int h = image.getHeight();  
		BufferedImage newImage = new BufferedImage(w, h, image.getType());
		Graphics2D g2 = newImage.createGraphics();
		g2.rotate(Math.PI, w/2, h/2);  
		g2.drawImage(image,null,0,0);
		image = newImage;
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageEnemie(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		g2d.drawImage(image, x, y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox());
	}
}
