package map;

import java.awt.Graphics2D;

import character.Bot;
import graphic.Sprite;
import obstacle.Platform;
import videogame.GameObject;

public class MapObject {
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

	public GameObject getObject( int x, int y){
		return objects[y][x];
	}

	public void infoOject( int x, int y){
		if(x>=0 && x<width && y>=0 && y<height){
			GameObject o = objects[y][x];
			if(objects[y][x] != null){
				System.out.printf("\tcaja x: %d, y: %d\n",o.getPositionX(), o.getPositionY());
				System.out.printf("\tposicion x: %d, y: %d\n", x, y);
			}
		}
	}

	public int getWidthBlocks() {
		return width;
	}

	public int getHeightBlocks() {
		return height;
	}

	public void draw2D(Graphics2D g, int x, int y, int disp_x) {
		if(objects[y][x] != null){
			int mov = 0;
			if(objects[y][x] instanceof Bot){
				mov = ((Bot)objects[y][x]).action();
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
							mov = 0;
						}
					}else{
						mov = 0;
					}
				}else {
					//Se mueve hacia la izquierda
					if(objects[y][x+1] == null && objects[y-1][x+1] != null){
						//Sin obstaculos y hay algo debajo
						if(objects[y-1][x+1] instanceof Platform){
							//Lo que hay debajo es una plataforma
							
						}else{
							mov = 0;
						}
					}else{
						mov = 0;
					}
				}
				objects[y][x].getWidthScreen();
			}
			//System.out.printf("Plataforma %d: %d\n", x, disp_x);
			objects[y][x].updatePositionX(mov+disp_x);
			((Sprite)objects[y][x]).draw2D(g);
		}
	}

	//Devuelve si hay colision con algun objeto del mapa
	public boolean collision(int x, int y, GameObject object){
		boolean result = false;
		if(x>=0 && x<width && y>=0 && y<height){
			if(objects[y][x] != null){
				result = objects[y][x].intersects(object);
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
}
