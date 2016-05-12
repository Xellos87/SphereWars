package obstacle;

import java.awt.Graphics2D;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Platform extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int GROUND = 0;
	public static final int BORDER_LEFT = 1;
	public static final int BORDER_RIGHT = 2;
	public static final int UNDERGROUND = 3;
	public static final int BORDER_BOTH = 4;
	public static final int ALONE_BLOCK = 5;
	//Estilo del mundo
	public static final int WORLD_FIELD = 0;
	public static final int WORLD_DESSERT = 6;
	public static final int WORLD_CASTLE = 12;
	public static final int WORLD_SNOW = 18;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={504,504,504,576,648,648,288,288,288,576,360,360,792,792,792,504,288,288,144,144,144,720,288,288};
	private static int[] y_imgs={576,648,504,864,0,0,576,648,504,864,792,792,144,216,72,288,792,792,792,864,720,864,144,144};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	private static int[] height_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	//Tipo de imagen a cargar, identifica los datos de la parte de imagen que lo representa
	private int type;
	private int world;

	public Platform(int x, int y, int block_width,int block_height, int type, int world) {
		super(x, y, x_imgs[world+type], y_imgs[world+type], width_imgs[world+type], height_imgs[world+type], block_width, block_height);
		//System.out.printf("Agregado en x:%d, y:%d\n", x,y);
		this.type = type;
		this.world = world;
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
		return width_imgs[world+type];
	}

	public int getHeightImage(){
		return height_imgs[world+type];
	}

}
