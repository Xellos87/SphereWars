package character;

import java.awt.Graphics2D;

import audio.Audio;
import audio.AudioClip;
import java.awt.Rectangle;
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
	public static final int COLLGET = 7;

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
	private int totalX = 0;
	boolean nextMap = false;
	private AudioClip soundJump;

	public Sphere(int x, int y, int block_width,int block_height) {
		super(x,y,x_imgs[0],y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.type = NORMAL;
		this.kills = false;
		soundJump = Audio.Load("audioEffects/boing_x.wav");
		selectImage();
		resize();
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImagePlayer(x_img, y_img, width, height);
	}

	public void jump(){
		if(soundJump == null){
			System.err.println("soundnull");
			System.exit(1);
		}
		soundJump.start();
		jumpVelocity = -15;
		jump = true;
	}

	public void miniJump(){
		if(soundJump == null){
			System.err.println("soundnull");
			System.exit(1);
		}
		soundJump.start();
		jumpVelocity = -7;
		jump = true;
	}

	public void gravity(){
		//this.setVelocity(this.vx, 1);
		int maxGravity = 14;
		if(vy+1 >= maxGravity){
			this.setVelocity(vx, maxGravity);
		}
		else{
			this.setVelocity(vx, vy+1);
		}		
	}

	public int checkCollision(MapController mc, int x_ori, int y_ori){
		//TODO bug choque lateral en cambio de mapa, ej: columna en x:0 del segundo mapa
		boolean print = false;	//False para no ver mensajes
		int result = -1;
		//TODO colisiones con sprite que no ocupa todo el bloque
		int collisionInf,collisionLat,collisionSup,collisionCen;
		//Comprueba si toca fin de mapa
		if(x + width < 0){
			return COLLDEATH;
		}
		//Control de mapa
		MapObject map;
		/*if(totalX < 0){
			nextMap = false;
			map = mc.getCurrentMap();
			totalX = -block_width + map.getWidthBlocks()*mc.getWidthBlock();
		}*/
		//else{
			//Mapa actual			
			if(nextMap && mc.getPos()>0){
				map = mc.getNextMap();
			}
			else{
				nextMap = false;
				map = mc.getCurrentMap();
			}	
		//}
			
		//Calculo coordenadas respecto mapa
		//int xMap = (this.x + (this.block_width/2)) / mc.getWidthBlock() + mc.getPos();
		int xMap = (totalX + (block_width/2)) / mc.getWidthBlock();
		int yMap = Math.abs((y + (block_height/2)) / mc.getHeightBlock() - mc.getMaxHeight());
		//Comprueba si esta en el siguiente mapa
		if(xMap >= map.getWidthBlocks()){
			//xMap = xMap - map.getWidthBlocks();
			nextMap = true;
			totalX = totalX - map.getWidthBlocks()*mc.getWidthBlock();
			xMap = (totalX + (block_width/2)) / mc.getWidthBlock();
			map = mc.getNextMap();	
		}		
		if(print){
			System.out.println("-----------------------");
			mc.getCurrentMap().print();
			System.out.printf("esfera x: %d, y: %d, w: %d, h: %d tx:%d\n", x, y, xMap, yMap,totalX);
		}
		//Colision inferior	
		collisionInf = map.collision(xMap, yMap-1,x_ori,y_ori, this);
		if(collisionInf != MapObject.NOCOLLISION && print){
			System.out.printf("colision inferior x: %d, y: %d\n", xMap, yMap-1);
			map.infoOject(xMap, yMap-1);	
		}
		//Colision lateral
		collisionLat = map.collision(xMap+1, yMap,x_ori,y_ori, this);
		if(collisionLat != MapObject.NOCOLLISION && print){
			System.out.printf("colision lateral x: %d, y: %d\n", xMap+1, yMap);
			map.infoOject(xMap+1, yMap);
		}		
		//Colision superior
		collisionSup = map.collision(xMap, yMap+1,x_ori,y_ori, this);
		if(collisionSup != MapObject.NOCOLLISION && print){
			System.out.printf("colision superior x: %d, y: %d\n", xMap, yMap+1);
			map.infoOject(xMap, yMap+1);	
		}
		//Colision central
		collisionCen = map.collision(xMap, yMap,x_ori,y_ori, this);
		if(collisionCen != MapObject.NOCOLLISION && print){
			System.out.printf("colision central x: %d, y: %d\n", xMap, yMap);
			map.infoOject(xMap, yMap);
		}
		
		//Actualiza la xtotal
		totalX += mc.getVelocity() + vx;
		
		//Prioridad de colisiones	
		if(collisionInf == MapObject.KILLS || collisionLat == MapObject.KILLS || collisionSup == MapObject.KILLS || collisionCen == MapObject.KILLS){
			result = COLLDEATH;
		}
		else if(collisionInf == MapObject.DEATH || collisionCen == MapObject.DEATH){
			result = COLLKILL;
		}//TODO, colision con monedas
		else if(collisionInf == MapObject.GET || collisionLat == MapObject.GET || collisionSup == MapObject.GET || collisionCen == MapObject.GET){
			result = COLLGET;
		}
		else if(collisionInf >= MapObject.COLLISION && collisionLat >= MapObject.COLLISION){
			result = COLLINFLAT;
			//totalX = totalX - mc.getVelocity() - vx;
			//this.x = this.x - mc.getVelocity() - vx;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
			totalX = (xMap+1)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			x = map.getObject(xMap+1, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;			
			y = map.getObject(xMap, yMap-1).getPositionY() - this.getHeightScreen() + 1;
			System.out.printf("prueba x:%d, y:%d\n", x,y);
		}
		//TODO cambial el totalX, para que este bien situado en las colisiones centrales
		else if(collisionInf >= MapObject.COLLISION && collisionCen >= MapObject.COLLISION){
			result = COLLINFLAT;
			//totalX = totalX - mc.getVelocity() - vx;
			//this.x = this.x - mc.getVelocity() - vx;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
			totalX = (xMap)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			x = map.getObject(xMap, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;			
			y = map.getObject(xMap, yMap-1).getPositionY() - this.getHeightScreen() + 1;
		}
		else if(collisionSup >= MapObject.COLLISION && collisionLat >= MapObject.COLLISION){
			result = COLLSUPLAT;
			//totalX = totalX - mc.getVelocity() - vx;
			//this.x = this.x - mc.getVelocity() - vx;
			totalX = (xMap+1)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			x = map.getObject(xMap+1, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock();
			y = map.getObject(xMap, yMap+1).getPositionY() + map.getObject(xMap, yMap+1).getHeightScreen();
		}	
		else if(collisionSup >= MapObject.COLLISION && collisionCen >= MapObject.COLLISION){
			result = COLLSUPLAT;
			//totalX = totalX - mc.getVelocity() - vx;
			//this.x = this.x - mc.getVelocity() - vx;
			totalX = (xMap)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			x = map.getObject(xMap, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock();
			y = map.getObject(xMap, yMap+1).getPositionY() + map.getObject(xMap, yMap+1).getHeightScreen();
		}
		else if(collisionLat >= MapObject.COLLISION){
			result = COLLLAT;
			//totalX = totalX - mc.getVelocity() - vx;
			totalX = (xMap+1)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			//this.x = this.x - mc.getVelocity() - vx;
			x = map.getObject(xMap+1, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;
		}
		else if(collisionCen >= MapObject.COLLISION){
			result = COLLLAT;
			totalX = (xMap)*mc.getWidthBlock() - this.getWidthScreen() - mc.getVelocity()+1;
			//this.x = this.x - mc.getVelocity() - vx;
			x = map.getObject(xMap, yMap).getPositionX() - this.getWidthScreen() - mc.getVelocity()+1;
		}
		else if(collisionInf >= MapObject.COLLISION){
			result = COLLINF;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
			y = map.getObject(xMap, yMap-1).getPositionY() - this.getHeightScreen() + 1;
		}
		else if(collisionSup >= MapObject.COLLISION){
			result = COLLSUP;
			//this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock();
			y = map.getObject(xMap, yMap+1).getPositionY() + map.getObject(xMap, yMap+1).getHeightScreen();
		}		
		else{
			result = NOCOLLISION;
		}
		return result;
	}

	@Override
	public void move(){
		if(x>=maxX){
			this.setVelocity(0, vy);
		}
		if(jump){
			jump = false;
			this.setVelocity(vx, jumpVelocity);
		}
		setPosition(x+vx, y+vy);
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
