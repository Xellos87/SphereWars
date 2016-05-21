package item;

import java.awt.Graphics2D;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Treasure extends GameObject implements Sprite{
	//Tipos de tesoro
	public static final int COIN = 0;
	public static final int GEM = 1;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={288,144};
	private static int[] y_imgs={360,362};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70};
	private static int[] height_imgs={70,70};
	//Valor de los tesoros
	private int values[] = {1,10};
	//Tipo de tesoro
	private int type;

	public Treasure(int x, int y, int block_width, int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		this.type = type;
		this.kills = false;
		selectImage();
		resize();
		
		real_x_block = real_x_block + real_block_width/4;
		real_y_block = real_y_block + real_block_height/4;
		real_block_width = (int) (real_block_width/1.8);
		real_block_height = (int) (real_block_height/1.8);
	}
	
	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageItem(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d, int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}

	public int getValue() {
		return values[type];
	}
	
	

}
