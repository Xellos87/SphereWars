package map;

import java.awt.Graphics2D;

import graphic.Sprite;
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
		//objects = new ArrayList<GameObject>();
	}
	
	public void addObject(GameObject obj, int x, int y){
		//objects.add(obj);
		objects[y][x] = obj;
	}

	public int getWidthBlocks() {
		return width;
	}
	
	public int getHeightBlocks() {
		return height;
	}

	public void draw2D(Graphics2D g, int x, int y, int disp_x) {
		if(objects[y][x] != null){
			//System.out.printf("Plataforma %d: %d\n", x, disp_x);
			objects[y][x].updatePositionX(disp_x);
			((Sprite)objects[y][x]).draw2D(g);
		}
	}
}
