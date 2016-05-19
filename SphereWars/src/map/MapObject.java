package map;

import java.awt.Graphics2D;

import character.Bot;
import graphic.Sprite;
import item.Treasure;
import obstacle.Platform;
import videogame.GameObject;

public class MapObject {

	//Constantes colisiones
	public static final int NOCOLLISION = -1;
	public static final int COLLISION = 0;
	public static final int KILLS = 1;			//Mata a pelota
	public static final int DEATH = 2;			//Mata a bot
	public static final int GET = 3; //Coge el objeto

	//Ancho y alto del mapa
	private int width, height;
	//Objetos que se encuentran en el mapa
	private GameObject[][] objects;

	public MapObject(int width, int height){
		this.width = width;
		this.height = height;
		objects = new GameObject[height][width];
	}

	public void addObject(GameObject obj, int x, int y){
		objects[y][x] = obj;
	}

	public GameObject getObject(int x, int y){
		return objects[y][x];
	}

	public void infoOject( int x, int y){
		if(x>=0 && x<width && y>=0 && y<height){
			GameObject o = objects[y][x];
			if(objects[y][x] != null){
				System.out.printf("\tcaja x: %d, y: %d\n",o.getPositionX(), o.getPositionY());
				System.out.printf("\tposicion x: %d, y: %d\n", x, y);
				//System.out.printf("\tdimensiones w: %d, h: %d\n", o.getWidth(), o.getHeight());
				System.out.printf("\tdimensiones block w: %d, h: %d\n", o.getWidthScreen(), o.getHeightScreen());
			}
		}
	}

	public int getWidthBlocks() {
		return width;
	}

	public int getHeightBlocks() {
		return height;
	}

	public void draw2D(Graphics2D g, int x, int y, int x_ori, int y_ori, int disp_x, boolean not_pause) {
		if(objects[y][x] != null){
			int mov = 0;
			if(objects[y][x] instanceof Bot){
				mov = ((Bot)objects[y][x]).action(not_pause);
				if(mov<0){
					//Se mueve hacia la derecha
					if(objects[y][x-1] == null && objects[y-1][x-1] != null){
						//Sin obstaculos y hay algo debajo
						if(objects[y-1][x-1] instanceof Platform){
							//Lo que hay debajo es una plataforma
							if(-mov >= objects[y][x].getWidthScreen()){
								//Ha pasado de casilla
								((Bot)objects[y][x]).resetMov();
								objects[y][x-1] = objects[y][x];
								objects[y][x] = null;
								x = x-1;
							}
						}else{
							//No hay plataforma
							mov = 0;
							((Bot)objects[y][x]).changeMov();
						}
					}else{
						//Hay un obstaculo o no hay elemento en el suelo
						mov = 0;
						((Bot)objects[y][x]).changeMov();
					}
				}else{
					//Se mueve hacia la izquierda
					if(objects[y][x+1] == null && objects[y-1][x+1] != null){
						//Sin obstaculos y hay algo debajo
						if(objects[y-1][x+1] instanceof Platform){
							//Lo que hay debajo es una plataforma
							//Lo que hay debajo es una plataforma
							if(mov >= objects[y][x].getWidthScreen()){
								//Ha pasado de casilla
								((Bot)objects[y][x]).resetMov();
								objects[y][x+1] = objects[y][x];
								objects[y][x] = null;
								x = x+1;
							}
						}else{
							//No hay plataforma
							mov = 0;
							((Bot)objects[y][x]).changeMov();
						}
					}else{
						//Hay un obstaculo o no hay elemento en el suelo
						mov = 0;
						((Bot)objects[y][x]).changeMov();
					}
				}
				objects[y][x].getWidthScreen();
			}
			//System.out.printf("Plataforma %d: %d\n", x, disp_x);
			objects[y][x].updatePositionX(mov+disp_x);
			((Sprite)objects[y][x]).draw2D(g, x_ori, y_ori);
		}
	}

	//Devuelve si hay colision con algun objeto del mapa
	public int collision(int x, int y,int x_ori,int y_ori, GameObject object){
		int result = NOCOLLISION;
		if(x>=0 && x<width && y>=0 && y<height){
			GameObject mapObject = objects[y][x];
			if(mapObject != null){
				//comprueba si intersecta
				if(mapObject.intersects(object,x_ori,y_ori)){
					result = COLLISION;
					//Comprueba si mata
					if(mapObject.kills()){
						result = KILLS;
					}
					if(mapObject instanceof Bot){
						Bot b = (Bot) mapObject;
						//Comprueba si mata al bot(colision por encima)
						if(object.getPositionY() < mapObject.getPositionY() &&
								mapObject.getPositionX() < object.getPositionX() &&
								mapObject.getPositionX() + mapObject.getWidthScreen() > object.getPositionX()){

							b.death();
							result = DEATH;
						}
					}
					if(mapObject instanceof Treasure){
						//TODO, comprobar si es mejor reducir area de contacto
						result = GET;
					}
				}
			}
		}
		return result;
	}

	public void print(){
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				if(objects[i][j] != null){
					System.out.printf("1 ");
				}
				else{
					System.out.printf("0 ");
				}
			}
			System.out.println();
		}
	}

	public void removeObject(int x, int y) {
		objects[y][x] = null;
	}
}
