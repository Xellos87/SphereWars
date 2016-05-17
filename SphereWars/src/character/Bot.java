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
	//Propiedades del bot
	private int type;
	private int state;
	private int direction;
	//Variables relacionadas con la animación del movimiento en pantalla
	private int tick_counter;
	private int max_counter = 10;
	private int acumulate_mov = 0;
	//Variables relacionadas con el cambio de dirección ante un camino imposible
	private int tick_change;
	private int max_wait_change = 10;

	public Bot(int x, int y, int block_width, int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		this.tick_counter = 0;
		this.tick_change = 0;
		this.type = type;
		this.state = WALK1;
		this.direction = RIGHT;
		selectImage();
		resize();
		rotateImage();
	}

	private void rotateImage(){
		if(direction == LEFT){
			//Si la dirección es la izquierda, rota la imagen como si fuera un espejo
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			Graphics2D g2 = newImage.createGraphics();  
			g2.drawImage(image,w, 0, -w, h,null);
			image = newImage;
		}
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageEnemie(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}

	public int action() {
		tick_counter++;
		int mov = 0;
		if(tick_counter >= max_counter){
			tick_counter -= max_counter;
			mov = width / 20;
			if(state == WALK2){
				state = WALK1;
			}else{
				state = WALK2;
			}
			x_img = x_imgs[type+state];
			y_img = y_imgs[type+state];
			width = width_imgs[type+state];
			height = height_imgs[type+state];
			selectImage();
			resize();
			rotateImage();
		}
		if(direction == RIGHT){
			mov = -mov;
		}
		acumulate_mov = acumulate_mov + mov;
		return acumulate_mov;
	}

	public void resetMov() {
		if(direction == RIGHT){
			acumulate_mov += block_width;
		}else{
			acumulate_mov -= block_width;
		}
	}

	public void changeMov() {
		tick_change++;
		if(tick_change >= max_wait_change){
			tick_change = 0;
			acumulate_mov = 0;
			if(direction == RIGHT){
				direction = LEFT;
			}else if(direction == LEFT){
				direction = RIGHT;
			}
			selectImage();
			resize();
			rotateImage();
		}
	}
}
