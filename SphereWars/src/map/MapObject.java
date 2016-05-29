package map;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import character.Bot;
import graphic.Model3D;
import graphic.Sprite;
import item.Treasure;
import obstacle.Platform;
import utils.Constants;
import videogame.Game;
import videogame.GameObject;

public class MapObject {
	//Constantes colisiones
	public static final int NOCOLLISION = -1;
	public static final int COLLISION = 0;
	public static final int KILLS = 1; //Mata a pelota
	public static final int DEATH = 2; //Mata a bot
	public static final int GET = 3; //Coge el objeto
	//Ancho y alto del mapa
	private int width, height;
	//Ancho y alto de cada bloque
	private int block_width, block_height;
	//Objetos que se encuentran en el mapa
	private GameObject[][] objects;
	//Posicion en X e Y de los bots
	private ArrayList<Integer> posBotX;
	private ArrayList<Integer> posBotY;
	//Contenedor de elementos en 3D
	private TransformGroup group_object;

	public MapObject(int width, int height, int block_width, int block_height){
		this.width = width;
		this.height = height;
		this.block_width = block_width;
		this.block_height = block_height;
		objects = new GameObject[height][width];
		//Crea la lista
		posBotX = new ArrayList<Integer>();
		posBotY = new ArrayList<Integer>();
		//Contenedor de los objetos de mapa
		group_object = new TransformGroup();
		group_object.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		group_object.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		group_object.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		group_object.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		group_object.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
	}

	public void addObject(GameObject obj, int x, int y){
		objects[y][x] = obj;
		if(obj instanceof Bot){
			posBotX.add(x);
			posBotY.add(y);
		}
		if(Constants.visualMode == Game.MODE_3D){
			//System.out.printf("Add obj, x:%d, y:%d\n", x*block_width,y*block_height);
			Transform3D translate = new Transform3D();
			translate.setTranslation(new Vector3f(x*block_width*0.002f,y*block_height*0.002f,0));
			TransformGroup tg = new TransformGroup(translate);
			tg.addChild(((Model3D)obj).get3DModel());
			group_object.addChild(tg);
		}
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
				//System.out.println("Existe");
				//System.out.printf("objeto x: %d, y: %d, w: %d, h: %d tx:%d\n", mapObject.getBox(0, 0).x, mapObject.getBox(0, 0).y,x,y,0);
				//comprueba si intersecta
				//result = COLLISION;
				if(mapObject.intersects(object,x_ori,y_ori)){
					//System.out.println("Intersecta");
					result = COLLISION;
					//Comprueba si mata
					if(mapObject.kills()){
						System.out.println("Mata");
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
	
	public void moveGroup(int desp){
		Transform3D translate = new Transform3D();
		group_object.getTransform(translate);
		Vector3f trans = new Vector3f();
		translate.get(trans);
		trans.x = trans.x+desp;
		group_object.setTransform(translate);
	}
	
	public TransformGroup get3DModel(){
		return group_object;
	}
	
	public void moveBot(){
		for(int i=0; i<posBotX.size(); i++){
			Bot b = (Bot)objects[posBotY.get(i)][posBotX.get(i)];
			b.moveBot();
		}
	}
}
