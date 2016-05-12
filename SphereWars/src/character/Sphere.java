package character;

import java.awt.Graphics2D;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Sphere extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int NORMAL = 0;
	public static final int JUMP = 1;
	public static final int SPEED_UP = 2;
	public static final int SPEED_DOWN = 3;
	//Posicion de la imagen a representar
	private static int[] x_imgs={2};
	private static int[] y_imgs={0};
	//Tamaño de la imagen
	private static int[] width_imgs={30};
	private static int[] height_imgs={30};
	//Estado del jugador
	private int type;

	public Sphere(int x, int y, int block_width,int block_height) {
		super(x,y,x_imgs[0],y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.type = NORMAL;
		selectImage();
		resize();
	}
	
	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImagePlayer(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image, x, y, null);
	}

	public int getWidthImage(){
		return width_imgs[type];
	}

	public int getHeightImage(){
		return height_imgs[type];
	}
	
}
