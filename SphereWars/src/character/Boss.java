package character;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import audio.Audio;
import audio.AudioClip;
import graphic.Sprite;
import utils.Constants;
import utils.Position;
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
	// Propiedades del boss
	private int health;
	private int state;
	private int directionX;
	private int directionY;
	private int vx;
	private int vy;
	// Variables relacionadas con la animación del movimiento en pantalla
	// y el tiempo de pausa entre secuencias completas de movimiento
	private int tick_counter;
	private int max_counter = 10;
	private int stopCounterX = 180;
	private int stopCounterY = 90;
	private int damageCounter = 180;
	private int tick_damage;
	private int stopTickX;
	private int stopTickY;
	private int acumulate_mov = 0;
	// Dirección del boss
	private final int RIGHT = 0;
	private final int LEFT = 1;
	private final int STOP = -1;
	private final int UP = 1;
	private final int DOWN = 0;
	//visibilidad del boss
	private boolean visible;
	private int anchoPantalla;
	//posiciones del boss
	private final int IZQ= -1;
	private final int DCHA = 1;
	private final int ARRIBA=1;
	private final int MEDIO=0;
	private final int ABAJO=-1;
	private Position posicion;
	private Position siguientePosicion;
	//orientaciones del boss
	//TODO: ojo! estan nombradas al reves
	private BufferedImage fly1left;
	private BufferedImage fly2left;
	private BufferedImage fly1right;
	private BufferedImage fly2right;
	private BufferedImage deadright;
	private BufferedImage deadleft;
	private boolean facingleft = false;
	//probabilidad de las acciones del boss
	private double vuelveArriba = 1;
	private double random = 1;
	private final double up = 1/6.0;
	private final double mid = 1/3.0;
	private final double down = 1/2.0;
	private int yAnterior;
	private int altoPantalla;
	private int xAnterior;
	//el boss se hace visible después de 60 segundos
	private long tiempoInicio;
	private long hazteVisible;

	private int x_ori,y_ori;
	private BufferedImage fly1redright;
	private BufferedImage fly2redright;
	private BufferedImage fly1redleft;
	private BufferedImage fly2redleft;
	private boolean reseteo = false;
	public boolean collides = false;
	
	// Audio
	private AudioClip deathSound;
	
	public Boss(int x, int y, int block_width, int block_height,boolean esVisible, int anchoPantalla, int altoPantalla) {
		super(x, y, x_imgs[0], y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.tick_counter = 0;
		this.stopTickX=0;
		this.stopTickY=0;
		this.tick_damage=0;
		this.state = FLY1;
		this.directionX = STOP;
		this.directionY = STOP;
		this.kills = true;
		this.health = 3;
		this.visible = esVisible;
		this.anchoPantalla = anchoPantalla;
		this.altoPantalla = altoPantalla;
		this.posicion = new Position(DCHA,ARRIBA);
		this.siguientePosicion = new Position(DCHA,ARRIBA);
		this.yAnterior = this.x;
		this.xAnterior = this.y;
		rellenarImagenes();
		if(facingleft){
			image = fly1right;
		}else{
			image = fly1left;
		}
		this.tiempoInicio = System.currentTimeMillis();
		this.hazteVisible = tiempoInicio + 30000;
		deathSound = Audio.Load("audioEffects/flie.wav");
	}

	private void rellenarImagenes() {
		fly1right = resizeDouble(Constants.img_handler.getImageEnemie(x_imgs[0], y_imgs[0], width_imgs[0], height_imgs[0]));
		fly2right = resizeDouble(Constants.img_handler.getImageEnemie(x_imgs[1], y_imgs[1], width_imgs[1], height_imgs[1]));
		deadright = resizeDouble(Constants.img_handler.getImageEnemie(x_imgs[2], y_imgs[2], width_imgs[2], height_imgs[2]));
		fly1left = rotateImage(fly1right);
		fly2left = rotateImage(fly2right);
		deadleft = rotateImage(deadright);
		try {
			fly1redright = resizeDouble(ImageIO.read(new File("images/redBoss_fly1.png")));
			fly2redright = resizeDouble(ImageIO.read(new File("images/redBoss_fly2.png")));
		} catch (IOException e) {
			System.err.println("problem loading red boss");
		}		
		fly1redleft = rotateImage(fly1redright);
		fly2redleft = rotateImage(fly2redright);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	private BufferedImage rotateImage(BufferedImage imagen) {
		//Si la dirección es la izquierda, rota la imagen como si fuera un espejo
		int w = imagen.getWidth();  
		int h = imagen.getHeight();  
		BufferedImage newImage = new BufferedImage(w, h, imagen.getType());
		Graphics2D g2 = newImage.createGraphics();  
		g2.drawImage(imagen,w, 0, -w, h,null);
		return newImage;		
	}

	@Override
	public void draw2D(Graphics2D g2d, int x_ori, int y_ori) {
		if(health!=1){
			g2d.drawImage(image, x_ori+x, y_ori+y, null);
		}else{
			if(state == FLY1){
				if(!facingleft){
					g2d.drawImage(fly1redright, x_ori+x, y_ori+y, null);
				}else{
					g2d.drawImage(fly1redleft, x_ori+x, y_ori+y, null);
				}				
			}else if(state == FLY2){
				if(!facingleft){
					g2d.drawImage(fly2redright, x_ori+x, y_ori+y, null);
				}else{
					g2d.drawImage(fly2redleft, x_ori+x, y_ori+y, null);
				}
			}
		}		
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
		this.x_ori=x_ori;
		this.y_ori=y_ori;
	}
	
	public void death(){
		this.state = DEAD;
		x_img = x_imgs[state];
		y_img = y_imgs[state];
		width = width_imgs[state];
		height = height_imgs[state];
		if(facingleft){
			image = deadleft;
		}else{
			image = deadright;
		}
		directionX=STOP;
		directionY=STOP;
		vy=12;
		rellenarImagenes();
		deathSound.start();
		reseteo = true;
	}
	
	public int action(boolean not_pause, int xPlayer, int yPlayer, Rectangle playerBox) {
		if(reseteo){
			this.tiempoInicio = System.currentTimeMillis() + 3000;
			this.hazteVisible = System.currentTimeMillis() + 30000;
			reseteo = false;
			System.out.println("reseteo tiempos");
		}
		if(!visible && (System.currentTimeMillis() >= hazteVisible)){
			this.visible=true;
			System.out.println("-----empieza el boss!!");
		}		
		else if(state == DEAD && System.currentTimeMillis() > tiempoInicio){
			state = FLY1;
			random=1;
			health = 3;
			System.out.println("-----reinicio boss");
			visible = false;
		}
		if(not_pause && state != DEAD){
			tick_counter++;
			stopTickX++;
			stopTickY++;
			tick_damage++;
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				if(state == FLY2){
					state = FLY1;
					if(facingleft){
						image = fly1left;
					}else{
						image = fly1right;
					}
				}else{
					state = FLY2;
					if(facingleft){
						image = fly2left;
					}else{
						image = fly2right;
					}
				}
				x_img = x_imgs[state];
				y_img = y_imgs[state];
				width = width_imgs[state];
				height = height_imgs[state];
			}
			//logica del boss
			if(stopTickX<stopCounterX){
				directionX = STOP;
				directionY = STOP;
			}else{
				cambioDireccionX();
				if(stopTickY>=stopCounterY){
					cambioDireccionY(xPlayer,yPlayer);
				}				
			}
			if(stopTickY<stopCounterY){
				directionY=STOP;
			}else{
				
			}
			xAnterior = x;
			//movimiento del boss
			if(directionX != STOP){	
				x+=vx / Constants.speedActions;
			}
			
			yAnterior = y;
			y+=vy / Constants.speedActions;
			//control 
			if(y >= altoPantalla -120){
				y = altoPantalla - 120;
			}else if(y<=20){
				y=20;
			}
			//cuando llegue a la mitad
			if((x<=anchoPantalla/2 && xAnterior > anchoPantalla/2)
					|| (x>anchoPantalla/2 && xAnterior <= anchoPantalla/2)){
				posicion = new Position(siguientePosicion.getX(),siguientePosicion.getY());
				cambioDireccionY(xPlayer,yPlayer);
			}
			//cuando llegue al final
			if(x<=10){
				posicion = new Position(siguientePosicion.getX(),siguientePosicion.getY());				
			}if(x>=anchoPantalla -100){
				posicion = new Position(siguientePosicion.getX(),siguientePosicion.getY());
			}
		}
		int hitPlayer = -1;
		if(tick_damage >= damageCounter){
			collides = true;
		}else{
			collides = false;
		}
		//comprobacion de colisiones
		if(collides && visible && state!=DEAD){
			Rectangle bossBox = this.getBox(x_ori, y_ori);
			Rectangle intesect = playerBox.intersection(bossBox);
			System.out.println(intesect);
			System.out.printf("p_y:%d, b_y:%d \n",playerBox.y,bossBox.y);
			if(state != DEAD && bossBox.intersects(playerBox)){
				if(playerBox.y<=bossBox.y){
					
					if(tick_damage >= damageCounter){
						this.health--;
						System.out.println("---Han herido al jefe!!");
						tick_damage = 0;
						hitPlayer=1;
					}
					if(health==0){
						death();
						System.out.println("---Han matado al jefe!!");
					}else if(health == 1){
						this.fly1left=fly1redleft;
						this.fly2left=fly2redleft;
						this.fly1right=fly1redright;
						this.fly2right=fly2redright;
					}
				}else{
					hitPlayer = 0;
					System.out.println("---Han herido al jugador!!");
					tick_damage = 0;
				}
			}
		}
		if(state == DEAD){
			y+=vy  / Constants.speedActions;
		}
		return hitPlayer;
	}

	private void cambioDireccionX() {
		if(visible){
			//movimiento en X - siempre igual, barrido completo de un lado a otro
			if(x>=20 && directionX == STOP){
				directionX = LEFT;
				vx = -12;
			}else if(x<20 && directionX == LEFT){
				stopTickX=0;
				vx=0;
				facingleft = true;
			}else if(x<20 && directionX == STOP){
				directionX = RIGHT;
				vx=12;
			}else if((x >= anchoPantalla - 100) && directionX == RIGHT){
				stopTickX=0;
				vx=0;
				facingleft = false;
			}
		}		
	}

	private void cambioDireccionY(int xPlayer, int yPlayer){
		if(visible){
			Random r= new Random();
			//movimiento en y - depende de la salud del jefe y de unas probabilidades dadas
			if(health==3){
				randomMove(r);
			}else if(health==2){
				random = 1/2.0;
				double aleatorio = r.nextDouble();
				if(aleatorio < random){	//aleatorio
					randomMove(r);
				}else{					//va a por el jugador
					int difY = y - yPlayer; 
					vy = difY / -10;
				}
			}else if(health==1){
				random = 1/3.0;
				vuelveArriba = 0.5;
				double aleatorio = r.nextDouble();
				if(aleatorio < random){	//aleatorio
					randomMove(r);
				}else{					//va a por el jugador
					int difY = y - yPlayer; 
					vy = difY / -10;
				}
			}
		}		
	}
	
	private void randomMove(Random r) {
		if(posicion.getX() != MEDIO && posicion.getY() == ARRIBA
				&& (posicion.getX()==siguientePosicion.getX() || posicion.getY()==siguientePosicion.getY())){
			double direccion = r.nextDouble();
			directionY = DOWN;
			System.out.println(directionY);
			if(direccion < down){
				siguientePosicion = new Position(MEDIO,ABAJO);					
				vy=12;
				System.out.println("move middle down");
			}else if(direccion < down+mid){
				siguientePosicion = new Position(MEDIO,MEDIO);
				vy=6;
				System.out.println("move middle middle");
			}else{
				siguientePosicion = new Position(MEDIO,ARRIBA);
				vy=0;
				System.out.println("move middle up");
			}
		}
		else if(posicion.getX() == MEDIO){
			directionY = UP;
			vy = (-vy);
			if(!facingleft){
				siguientePosicion = new Position(IZQ,ARRIBA);
				System.out.println("desde medio hacia la izquierda");
			}else{
				siguientePosicion = new Position(DCHA,ARRIBA);
				System.out.println("desde medio hacia la derecha");	
			}
			System.out.println("move back up");
		}
		stopTickY=0;
	}

	protected BufferedImage resizeDouble(BufferedImage imagen){
		//Comprobaciones por si la imagen no es cuadrada
		int w;
		int h;
		int width = imagen.getWidth();
		int height = imagen.getHeight();
		if(width == height){
			w = block_width;
			h = block_height;
		}else if(width > height){
			w = block_width;
			h = (int) (((float)block_width) / width * height);
		}else{
			w = (int) (((float)block_height) / height* width);
			h = block_height;
		}
		h *= 2;
		w *= 2;
		real_block_height=h;
		real_block_width=w;
		real_x_block = 0;//(block_width-real_block_width);
		real_y_block = 0;//(block_height-real_block_height);
		//Escala la imagen
		Image tmp = imagen.getScaledInstance(w, h, Image.SCALE_DEFAULT);
	    BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
		return dimg;
	}
}
