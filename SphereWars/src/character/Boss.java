package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Boss extends GameObject implements Sprite {
	// Estados del enemigo (sprites)
	private final int FLY1 = 0;
	private final int FLY2 = 1;
	private final int DEAD = 2;
	// Posicion de la imagen
	private static int[] x_imgs = { 0, 0, 143 };
	private static int[] y_imgs = { 32, 0, 0 };
	// Tamaño de la imagen
	private static int[] width_imgs = { 72, 75, 59 };
	private static int[] height_imgs = { 36, 31, 33 };
	// Propiedades del bot
	private int health;
	private int state;
	private int directionX;
	private int directionY;
	// Variables relacionadas con la animación del movimiento en pantalla
	private int tick_counter;
	private int max_counter = 10;
	private int acumulate_mov = 0;
	// Variables relacionadas con el cambio de dirección
	private int tick_change;
	private int max_wait_change = 10;
	// Dirección del boss
	private final int RIGHT = 0;
	private final int LEFT = 1;
	private final int STOP = -1;
	private final int UP = 0;
	private final int DOWN = 0;
	//visibilidad del boss
	private boolean visible;

	public Boss(int x, int y, int block_width, int block_height,boolean esVisible) {
		super(x, y, x_imgs[0], y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.tick_counter = 0;
		this.tick_change = 0;
		this.state = FLY1;
		this.directionX = STOP;
		this.directionY = STOP;
		this.kills = true;
		this.health = 3;
		this.visible = esVisible;
		selectImage();
		resize();
		rotateImage();
	}

	private void rotateImage() {
		if(directionX == LEFT){
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
		image = Constants.img_handler.getImageEnemie(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d, int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}
	
	public void death(){
		this.state = DEAD;
		x_img = x_imgs[state];
		y_img = y_imgs[state];
		width = width_imgs[state];
		height = height_imgs[state];
		selectImage();
		resize();
	}
	
	public int action(boolean not_pause) {
		if(not_pause && state != DEAD){
			tick_counter++;
			int mov = 0;
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				mov = width / 20;
				if(state == FLY2){
					state = FLY1;
				}else{
					state = FLY2;
				}
				x_img = x_imgs[state];
				y_img = y_imgs[state];
				width = width_imgs[state];
				height = height_imgs[state];
				selectImage();
				resize();
				rotateImage();
			}
			if(directionX == RIGHT){
				mov = -mov;
			}
			acumulate_mov = acumulate_mov + mov;
		}
		return acumulate_mov;
	}

	public void resetMov() {
		if(directionX == RIGHT){
			acumulate_mov += block_width;
		}else if(directionX == LEFT){
			acumulate_mov -= block_width;
		}
		if(directionY == UP){
			
		}else if(directionY == DOWN){
			
		}
	}

	public void changeMov() {
		tick_change++;
		if(tick_change >= max_wait_change){
			tick_change = 0;
			acumulate_mov = 0;
			if(directionX == RIGHT){
				directionX = LEFT;
			}else if(directionX == LEFT){
				directionX = RIGHT;
			}
			selectImage();
			resize();
			rotateImage();
		}
	}
}
