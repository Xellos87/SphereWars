package character;

import java.awt.Graphics2D;
import java.awt.dnd.DragGestureEvent;

import graphic.Sprite;
import map.MapController;
import map.MapObject;
import utils.Constants;
import videogame.GameObject;

public class Sphere extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int NORMAL = 0;
	public static final int JUMP = 1;
	public static final int SPEED_UP = 2;
	public static final int SPEED_DOWN = 3;
	//Resultados colisiones
	public static final int NOCOLLISION = -1;
	public static final int COLLINF = 0;
	public static final int COLLSUP = 1;
	public static final int COLLLAT = 2;
	public static final int COLLINFLAT = 3;
	public static final int COLLSUPLAT = 4;
	public static final int COLLDEATH = 5;
	public static final int COLLKILL = 6;
	public static final int COLLINFGET = 7;
	public static final int COLLSUPGET = 8;
	public static final int COLLLATGET = 9;
	
	//Posicion de la imagen a representar
	private static int[] x_imgs={2};
	private static int[] y_imgs={0};
	//Tamaño de la imagen
	private static int[] width_imgs={30};
	private static int[] height_imgs={30};
	//Estado del jugador
	private int type;
	//Logica
	private boolean jump = false;
	private int jumpVelocity = -15;
	private int maxX = 200;  

	public Sphere(int x, int y, int block_width,int block_height) {
		super(x,y,x_imgs[0],y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.type = NORMAL;
		this.kills = false;
		selectImage();
		resize();
	}
	
	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImagePlayer(x_img, y_img, width, height);
	}

	public void jump(){
		jumpVelocity = -15;
		jump = true;
	}
	
	public void miniJump(){
		jumpVelocity = -7;
		jump = true;
	}

	public void gravity(){
		//this.setVelocity(this.vx, 1);
		int maxGravity = 15;
		if(this.vy+1 >= maxGravity){
			this.setVelocity(this.vx, maxGravity);
		}
		else{
			this.setVelocity(this.vx, this.vy+1);
		}		
	}

	public int checkCollision(MapController mc, int x_ori, int y_ori){
		boolean print = true;	//False para no ver mensajes
		int xMove = 1;
		int MAX_HEIGHT = 9;
		int result = -1;
		//TODO colisiones con sprite que no ocupa todo el bloque
		int collisionInf, collisionLat, collisionSup;
		boolean kill = false;
		boolean death = false;
		//Mapa actual
		MapObject map = mc.getCurrentMap();	
		//Calculo coordenadas respecto mapa
		int xMap = (this.x + (this.block_width/2)) / mc.getWidthBlock() + mc.getPos();
		//TODO apaño, Richard es malvado y carga el mapa con las "y" invertidas
		int yMap = Math.abs((this.y + (this.block_height/2)) / mc.getHeightBlock() - MAX_HEIGHT);
		//Comprueba si esta en el siguiente mapa
		if(xMap >= map.getWidthBlocks()){
			xMap = xMap - map.getWidthBlocks();
			map = mc.getNextMap();	
		}		
		if(print){
			System.out.println("-----------------------");
			mc.getCurrentMap().print();
			System.out.printf("esfera x: %d, y: %d, w: %d, h: %d\n",this.getPositionX(), this.getPositionY(), xMap, yMap);
		}
		//Colision inferior	
		collisionInf = map.collision(xMap, yMap-1,x_ori,y_ori, this);
		if(collisionInf >= 0 && print){
			System.out.printf("colision inferior x: %d, y: %d\n", xMap, yMap-1);
			map.infoOject(xMap, yMap-1);	
		}
		if(collisionInf < 0){
			//Colision inferior der			
			collisionInf = map.collision(xMap+1, yMap-1,x_ori,y_ori, this);
			if(collisionInf >= 0 && print){
				System.out.printf("colision inferior der x: %d, y: %d\n", xMap+1, yMap-1);
				map.infoOject(xMap+1, yMap-1);	
			}
		}	
		//Colision lateral
		collisionLat = map.collision(xMap, yMap,x_ori,y_ori, this);
		if(collisionLat < 0){
			collisionLat = map.collision(xMap+1, yMap,x_ori,y_ori, this);
		}
		if(collisionLat >= 0 && print){
			System.out.printf("colision lateral x: %d, y: %d\n", xMap+1, yMap);
			map.infoOject(xMap+1, yMap);
		}
		//Colision superior
		collisionSup = map.collision(xMap, yMap+1,x_ori,y_ori, this);
		if(collisionSup >= 0 && print){
			System.out.printf("colision superior x: %d, y: %d\n", xMap, yMap+1);
			map.infoOject(xMap, yMap);	
		}
		/*if(!collisionSup){
			//Colision superior der
			collisionSup = map.collision(xMap+1, yMap+1, this);
			if(collisionSup){
				System.out.printf("colision superior der x: %d, y: %d\n", xMap+1, yMap+1);
				map.infoOject(xMap+1, yMap+1);	
			}
		}*/		
		//Prioridad de colisiones	
		if(collisionInf == MapObject.KILLS || collisionLat == MapObject.KILLS || collisionSup == MapObject.KILLS){
			result = COLLDEATH;
		}
		else if(collisionInf == MapObject.DEATH){
			result = COLLKILL;
		}//TODO, colision con monedas
		else if(collisionInf == MapObject.GET){
			result = COLLINFGET;
		}
		else if(collisionSup == MapObject.GET){
			result = COLLSUPGET;
		}
		else if(collisionLat == MapObject.GET){
			result = COLLLATGET;
		}
		else if(collisionInf >= MapObject.COLLISION && collisionLat >= MapObject.COLLISION){
			result = COLLINFLAT;
			//this.x = this.x - xMove;
			this.x = this.x - vx - xMove;
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
		}
		else if(collisionSup >= MapObject.COLLISION && collisionLat >= MapObject.COLLISION){
			result = COLLSUPLAT;
			//this.x = this.x - xMove;
			this.x = this.x - vx - xMove;
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock();
		}	
		else if(collisionLat >= MapObject.COLLISION){
			result = COLLLAT;
			//this.x = this.x - xMove;
			this.x = this.x - vx - xMove;
		}
		else if(collisionInf >= MapObject.COLLISION){
			result = COLLINF;
			//Fix de posicion
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
		}
		else if(collisionSup >= MapObject.COLLISION){
			result = COLLSUP;
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock();
		}		
		else{
			result = NOCOLLISION;
		}
		return result;
	}

	@Override
	public void move(){
		int auxVx = vx;
		if(x>=maxX){
			auxVx = 0;
		}
		if(jump){
			jump = false;
			this.setVelocity(auxVx, jumpVelocity);
		}
		setPosition(x+auxVx, y+vy);
	}
	
	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}

	public int getWidthImage(){
		return width_imgs[type];
	}

	public int getHeightImage(){
		return height_imgs[type];
	}
	
}
