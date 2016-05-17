package obstacle;

import java.awt.Graphics2D;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Liquid extends GameObject implements Sprite{
	//Estilo del liquido
	public static final int WATER = 0;
	public static final int MAGMA = 2;
	//
	public static final int SURFACE = 0;
	public static final int DEEP = 1;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={432,504,432,504};
	private static int[] y_imgs={576,216,792,0};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70};
	private static int[] height_imgs={70,70,70,70};
	//Tipo de elemento
	private int type;
	private int nature;

	public Liquid(int x, int y, int block_width, int block_height, int type, int nature) {
		super(x, y, x_imgs[nature+type], y_imgs[nature+type], width_imgs[nature+type], height_imgs[nature+type], block_width, block_height);
		this.type = type;
		this.nature = nature;
		this.kills = true;
		selectImage();
		resize();
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
	}

	public int getWidthImage(){
		return width_imgs[nature+type];
	}

	public int getHeightImage(){
		return height_imgs[nature+type];
	}

}
