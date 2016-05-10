package map;

import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import obstacle.Platform;

public class MapController {
	//Lista de mapas para cargar
	private String maps[] = {"maps/map00.xml"};
	//Indice de mapas que se genera aleatoriamente
	private ArrayList<Integer> map_index;
	//Indice del mapa actual
	private int current_map;
	//Posición en bloque dentro del mapa, y pixel dentro del bloque
	private int pos_block;
	private int pixel_block;
	//
	private int pixel_width = 69;
	private int pixel_height = 69;
	//Numero de bloques en pantalla
	private int block_width_screen;
	private int block_height_screen;
	//Mapa actual y siguiente
	private MapObject first_map;
	private MapObject second_map;

	public MapController(int width, int height){
		map_index = new ArrayList<Integer>();
		current_map = 0;
		pos_block = 0;
		pixel_block = 0;
		block_width_screen = width / pixel_width;
		block_height_screen = height / pixel_height;
		System.out.printf("bloques de ancho: %d, alto:%d\n", block_width_screen,block_height_screen);
		//Carga aleatoriamente los 2 primeros mapas
		loadMap(-1);
		loadMap(-1);
	}

	private void loadMap(int index) {
		if(index < 0){
			Random rnd = new Random(System.currentTimeMillis());
			index = rnd.nextInt(maps.length);
		}
		map_index.add(index);
		System.out.printf("Mapa: %d\n", index);
		//Pone el segundo mapa como primero, el nuevo se carga en el segundo
		first_map = second_map;
		//Parsear el fichero de mapas
		parseMapXML(index);
	}

	private void parseMapXML(int index) {
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(maps[index]));
			doc.getDocumentElement().normalize();
			//Obtiene el ancho y alto del mapa
			Node header = doc.getElementsByTagName("map").item(0);
			Element element_header = (Element) header;
			int height = Integer.parseInt(element_header.getAttribute("height"));
			//TODO ñapa
			height = Math.min(height, block_height_screen);
			int width = Integer.parseInt(element_header.getAttribute("width"));
			//Crea una nuevo MapObject para contener la info del mapa
			second_map = new MapObject(width, height);
			//Recorre todos los elementos plataform para agreagarlo al mapa
			NodeList lst_plat = element_header.getElementsByTagName("plataform");
			for(int i=0; i<lst_plat.getLength(); i++){
				Element element_plat = (Element) lst_plat.item(i);
				//Tipo de plataforma
				String type = element_plat.getAttribute("type");
				//Inicio y final en X(anchura)
				int x_start = Integer.parseInt(element_plat.getAttribute("x_start"));
				int x_end = Integer.parseInt(element_plat.getAttribute("x_end"));
				//Inicio y final en Y(altura)
				int y_start = Integer.parseInt(element_plat.getAttribute("y_start"));
				int y_end = Integer.parseInt(element_plat.getAttribute("y_end"));
				
				//Recorre todas las posiciones en las que agregar el elemento
				for(int y=y_start; y<=y_end; y++){
					for(int x=x_start; x<=x_end; x++){
						//Valor bueno de y = 240
						Platform p = new Platform("images/platforms.png", x*pixel_width, (height-y)*pixel_height, 505, 575, pixel_width, pixel_height);
						second_map.addObject(p,x,y);
						//System.out.printf("Agregado en x:%d, y:%d\n", x*pixel_width,(height-y)*pixel_height);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void draw2D(Graphics2D g) {
		int width_map1;
		int width_map2 = 0;
		//TODO ñapa
		if((first_map.getWidthBlocks() - pos_block -2) > block_width_screen){
			//Solo hace falta el primer mapa
			width_map1 = pos_block + block_width_screen+2;
		}else{
			//Hay que representar parte del primer mapa y del segundo
			width_map1 = first_map.getWidthBlocks();
			width_map2 = block_width_screen - (width_map1 - pos_block) + 2;
			System.out.printf("Ancho del segundo mapa: %d \n", width_map2);
		}
		//System.out.printf("Ancho a primer mapa: %d \n", width_map1);
		//Dibuja desde el primer mapa
		for(int x=pos_block; x<width_map1; x++){
			int pos_x = ((x-pos_block)*pixel_width) - pixel_block;
			for(int y=0; y<first_map.getHeightBlocks(); y++){
				first_map.draw2D(g,x,y,pos_x);
			}
		}
		System.out.printf("----------\n");
		//Dibuja desde el segundo mapa si lo necesita
		for(int x=0; x<width_map2; x++){
			int pos_x = ((width_map1-pos_block+x)*pixel_width) - pixel_block;
			for(int y=0; y<second_map.getHeightBlocks(); y++){
				second_map.draw2D(g,x,y,pos_x);
			}
		}
	}

	public void move() {
		int speed = 1;
		pixel_block += speed;
		if(pixel_block / pixel_width >= 1){
			pos_block++;
			pixel_block = pixel_block % pixel_width;
		}
		if(pos_block > first_map.getWidthBlocks()){
			first_map = second_map;
			loadMap(-1);
			pos_block = 0;
		}
	}
}
