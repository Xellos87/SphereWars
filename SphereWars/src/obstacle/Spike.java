package obstacle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Spike extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int UPPER = 0;
	public static final int LOWER = 2;
	public static final int RIGHT = 1;
	public static final int LEFT = 3;
	//Posición de los la imagen
	private static int x_img = 347;
	private static int y_img = 30;
	//Tamaño de la imagen
	private static int width = 70;
	private static int height = 40;
	//Direccion del pincho
	private int direction;

	public Spike(int x, int y,int block_width,int block_height, int direction) {
		super(x, y, x_img, y_img, width, height, block_width, block_height);
		this.direction = direction;
		this.kills = true;
		selectImage();
		resize();
		rotateImage();
	}

	private void selectImage() {
		//Carga solo el fragmento que necesita la imagen
		//image = image.getSubimage(xImg, yImg, width, height);
		image = Constants.img_handler.getImageItem(x_img, y_img, width, height);
	}

	private void rotateImage(){
		if(direction != UPPER){
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			Graphics2D g2 = newImage.createGraphics();
			g2.rotate(direction*Math.PI/2, w/2, h/2);  
			g2.drawImage(image,null,0,0);
			image = newImage;
			//Calcula las posiciones relativas de la imagen para las colisiones
			int aux;
			switch (direction){
			case LOWER:
				real_y_block = 0;
				break;
			case LEFT:
				aux = real_block_height;
				real_block_height = real_block_width;
				real_block_width = aux;
				real_y_block = 0;
				real_x_block = block_width - real_block_width;
				break;
			case RIGHT:
				aux = real_block_height;
				real_block_height = real_block_width;
				real_block_width = aux;
				real_y_block = 0;
				real_x_block = 0;
				break;
			}
		}
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}


	public int getWidthImage(){
		return width;
	}

	public int getHeightImage(){
		return height;
	}
}
