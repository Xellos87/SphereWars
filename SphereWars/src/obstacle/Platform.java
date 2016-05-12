package obstacle;

import java.awt.Graphics2D;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Platform extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int GROUND = 0;
	public static final int PLATAFORM = 10;
	public static final int BORDER_LEFT = 1;
	public static final int BORDER_RIGHT = 2;
	public static final int UNDERGROUND = 3;
	public static final int BORDER_BOTH = 4;
	public static final int ALONE_BLOCK = 5;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={504,504,504,576,648,648};
	private static int[] y_imgs={576,648,504,864,0,0};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70,70,70};
	private static int[] height_imgs={70,70,70,70,70,70};
	//Tipo de imagen a cargar, identifica los datos de la parte de imagen que lo representa
	private int type;

	public Platform(int x, int y, int block_width,int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		//System.out.printf("Agregado en x:%d, y:%d\n", x,y);
		this.type = type;
		selectImage();
		resize();
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d) {
		g2d.drawImage(image, x, y, null);
	}

	public int getWidthImage(){
		return width_imgs[type];
	}

	public int getHeightImage(){
		return height_imgs[type];
	}

}
