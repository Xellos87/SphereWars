package map;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Random;

import javax.media.j3d.TransformGroup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import character.Bot;
import character.Sphere;
import item.Treasure;
import obstacle.Liquid;
import obstacle.Platform;
import obstacle.Spike;
import utils.Constants;

public class MapController {
	//Numero de bloques maximos para la altura
	private final int MAX_HEIGHT = 9;
	//Lista de mapas para cargar
	private final String MAPS[] = {"maps/map05.xml"};
	//Indice del mapa actual
	private int current_map;
	//Posición en bloque dentro del mapa, y pixel dentro del bloque
	private int pos_block;
	private int pixel_block;
	//Tama�o que va a ocupar cada bloque en pantalla
	private int block_width;
	private int block_height;
	//Numero de bloques en pantalla
	private int block_width_screen;
	private int block_height_screen;
	//Mapa actual y siguiente
	private MapObject first_map;
	private MapObject second_map;
	//Velocidades
	private int speedLow;
	private int speedHigh;

	public MapController(int width, int height){
		current_map = 0;
		pos_block = 0;
		pixel_block = 0;
		block_height = height / MAX_HEIGHT;
		block_width = block_height;
		//Se suma 1 ya que la división entre enteros si no es exacta desprecia los decimales, asi ocupa todo el ancho
		block_width_screen = width / block_width + 1;
		block_height_screen = height / block_height;
		System.out.printf("bloques de ancho: %d, alto:%d\n", block_width_screen,block_height_screen);
		//Calcula la velocidad en funcion del bloque
		speedLow = block_width / 8;
		speedHigh = block_width / 6;
		//Carga aleatoriamente los 2 primeros mapas
		loadMap();
		loadMap();
	}

	public void loadMap() {
		int index = MapController.getNumberMap(current_map, MAPS.length);

		System.out.printf("Mapa: %d\n", index);
		//Pone el segundo mapa como primero, el nuevo se carga en el segundo
		first_map = second_map;
		//Parsear el fichero de mapas
		parseMapXML(index);
		current_map++;
	}

	private void parseMapXML(int index) {
		int world = Platform.WORLD_FIELD;
		int nature = Liquid.WATER;
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(MAPS[index]));
			doc.getDocumentElement().normalize();
			//Obtiene el ancho y alto del mapa
			Node header = doc.getElementsByTagName("map").item(0);
			Element element_header = (Element) header;
			int height = block_height_screen;//Integer.parseInt(element_header.getAttribute("height"));
			//TODO control de la altura, como mucho 10, si no se mantiene en todos comprobar lo que pasa
			//height = Math.min(height, block_height_screen);
			int width = Integer.parseInt(element_header.getAttribute("width"));
			//Crea una nuevo MapObject para contener la info del mapa
			second_map = new MapObject(width, height, block_width, block_height);
			//Recorre todos los elementos plataform para agregarlo al mapa
			NodeList lst_plat = element_header.getElementsByTagName("plataform");
			for(int i=0; i<lst_plat.getLength(); i++){
				Element element_plat = (Element) lst_plat.item(i);
				//Inicio y final en X(anchura)
				int x_start = Integer.parseInt(element_plat.getAttribute("x_start"));
				int x_end = Integer.parseInt(element_plat.getAttribute("x_end"));
				//Inicio y final en Y(altura)
				int y_start = Integer.parseInt(element_plat.getAttribute("y_start"));
				int y_end = Integer.parseInt(element_plat.getAttribute("y_end"));

				//Recorre todas las posiciones en las que agregar el elemento
				for(int y=y_start; y<=y_end; y++){
					for(int x=x_start; x<=x_end; x++){
						//Tipo de plataforma
						int type;
						if(y==y_end){
							//En lo mas alto
							if(x==x_start && x==x_end && y_start == y_end){
								type = Platform.ALONE_BLOCK;
							}else if(x==x_start && x==x_end){
								type = Platform.GROUND;
							}else if(x==x_start && y_start == y_end){
								type = Platform.BORDER_LEFT;
							}else if(x==x_end && y_start == y_end){
								type = Platform.BORDER_RIGHT;
							}else{
								type = Platform.GROUND;
							}
						}else{
							type = Platform.UNDERGROUND;
						}

						Platform p = new Platform( x*block_width, (height-y)*block_height,block_width,block_height,type,world);
						second_map.addObject(p,x,y);
						//System.out.printf("Agregado en x:%d, y:%d\n", x*block_width,(height-y)*block_height);
						//System.out.printf("Tam en pantalla, ancho: %d, alto: %d\n",block_width,block_height);
					}
				}
			}
			//Recorre todos los elementos spike para agregarlo al mapa
			NodeList lst_spike = element_header.getElementsByTagName("spike");
			for(int i=0; i<lst_spike.getLength(); i++){
				Element element_spike = (Element) lst_spike.item(i);
				//Dirección de plataforma()
				String type = element_spike.getAttribute("direction");
				int direction = Spike.UPPER;
				if(type.equals("low")){
					direction = Spike.LOWER;
				}else if(type.equals("right")){
					direction = Spike.RIGHT;
				}else if(type.equals("left")){
					direction = Spike.LEFT;
				}
				//Inicio y final en X(anchura)
				int x_start = Integer.parseInt(element_spike.getAttribute("x_start"));
				int x_end = Integer.parseInt(element_spike.getAttribute("x_end"));
				//Inicio y final en Y(altura)
				int y_start = Integer.parseInt(element_spike.getAttribute("y_start"));
				int y_end = Integer.parseInt(element_spike.getAttribute("y_end"));

				//Recorre todas las posiciones en las que agregar el elemento
				for(int y=y_start; y<=y_end; y++){
					for(int x=x_start; x<=x_end; x++){
						//Valor bueno de y = 240
						Spike sp = new Spike(x*block_width, (height-y)*block_height,block_width,block_height,direction);
						second_map.addObject(sp,x,y);
						//System.out.printf("Agregado en x:%d, y:%d\n", x*pixel_width,(height-y)*pixel_height);
					}
				}
			}
			//Recorre todos los elementos liquid para agregarlo al mapa
			NodeList lst_liquid = element_header.getElementsByTagName("liquid");
			for(int i=0; i<lst_liquid.getLength(); i++){
				Element element_liquid = (Element) lst_liquid.item(i);
				//Inicio y final en X(anchura)
				int x_start = Integer.parseInt(element_liquid.getAttribute("x_start"));
				int x_end = Integer.parseInt(element_liquid.getAttribute("x_end"));
				//Inicio y final en Y(altura)
				int y_start = Integer.parseInt(element_liquid.getAttribute("y_start"));
				int y_end = Integer.parseInt(element_liquid.getAttribute("y_end"));

				//Recorre todas las posiciones en las que agregar el elemento
				for(int y=y_start; y<=y_end; y++){
					for(int x=x_start; x<=x_end; x++){
						//Tipo de plataforma
						int type;
						if(y==y_end){
							//Supercifie
							type = Liquid.SURFACE;
						}else{
							type = Liquid.DEEP;
						}
						Liquid l = new Liquid( x*block_width, (height-y)*block_height,block_width,block_height,type,nature);
						second_map.addObject(l,x,y);
						//System.out.printf("Agregado en x:%d, y:%d\n", x*block_width,(height-y)*block_height);
						//System.out.printf("Tam en pantalla, ancho: %d, alto: %d\n",block_width,block_height);
					}
				}
			}
			//Recorre todos los elementos bot para agregarlo al mapa
			NodeList lst_bot = element_header.getElementsByTagName("bot");
			for(int i=0; i<lst_bot.getLength(); i++){
				Element element_bot = (Element) lst_bot.item(i);
				//X(anchura)
				int x = Integer.parseInt(element_bot.getAttribute("x"));
				//Y(altura)
				int y = Integer.parseInt(element_bot.getAttribute("y"));

				int type = Bot.SLIME;

				Bot b = new Bot(x*block_width, (height-y)*block_height,block_width,block_height,type);
				second_map.addObject(b,x,y);
			}
			//Recorre todos los elementos treasure para agregarlo al mapa
			NodeList lst_treasure = element_header.getElementsByTagName("treasure");
			for(int i=0; i<lst_treasure.getLength(); i++){
				Element element_treasure = (Element) lst_treasure.item(i);
				//X(anchura)
				int x = Integer.parseInt(element_treasure.getAttribute("x"));
				//Y(altura)
				int y = Integer.parseInt(element_treasure.getAttribute("y"));

				String type_s = element_treasure.getAttribute("type");

				int type = Treasure.COIN;
				if(type_s.equals("gem")){
					type = Treasure.GEM;
				}

				Treasure t = new Treasure(x*block_width, (height-y)*block_height,block_width,block_height,type);
				second_map.addObject(t,x,y);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void draw2D(Graphics2D g, int x_ori, int y_ori, boolean not_pause) {
		int width_map1;
		int width_map2 = 0;
		//Se suma 1 bloque al último mapa para evitar que aparezca de repente durante el desplazamiento
		if((first_map.getWidthBlocks() - pos_block - 1) > block_width_screen){
			//Solo hace falta el primer mapa
			width_map1 = pos_block + block_width_screen + 1;
		}else{
			//Hay que representar parte del primer mapa y del segundo
			width_map1 = first_map.getWidthBlocks();
			width_map2 = block_width_screen - (width_map1 - pos_block) + 1;
			//System.out.printf("Ancho del segundo mapa: %d \n", width_map2);
		}
		//System.out.printf("Ancho a primer mapa: %d \n", width_map1);
		//Dibuja desde el primer mapa
		int init_x = pos_block;
		if(pos_block>0){
			//Control para que se dibujen elementos que se mueven al salir de pantalla
			init_x = pos_block-1;
		}
		for(int x=init_x; x<width_map1; x++){
			int pos_x = ((x-pos_block)*block_width) - pixel_block;
			for(int y=0; y<first_map.getHeightBlocks(); y++){
				first_map.draw2D(g,x,y,x_ori,y_ori,pos_x,not_pause);
			}
		}
		//System.out.printf("----------\n");
		//Dibuja desde el segundo mapa si lo necesita
		for(int x=0; x<width_map2; x++){
			int pos_x = ((width_map1-pos_block+x)*block_width) - pixel_block;
			for(int y=0; y<second_map.getHeightBlocks(); y++){
				second_map.draw2D(g,x,y,x_ori,y_ori,pos_x,not_pause);
			}
		}
	}

	public double move() {
		//TODO: 9 niveles de alto, 2 bloque de alto en salto, y 3 o 5 de largo segun velocidad
		//5 velocidad normal y 7 velocidad rapida
		//TODO: usar speedLow y speedHigh, los valores fijos no funcionan bien si cambia la resolución de pantalla del juego
		int speed = speedLow;
		pixel_block += speed;
		if(pixel_block / block_width >= 1){
			pos_block++;
			pixel_block = pixel_block % block_width;
		}
		if(pos_block >= first_map.getWidthBlocks()){
			first_map = second_map;
			pos_block = 0;
			//Carga el siguiente mapa desde un hilo
			new Thread(new Runnable() {

				@Override
				public void run() {
					loadMap();
				}
			}).start();
		}
		return ((double)speed) / block_width;
	}

	public int getNumMaps(){
		return current_map;
	}

	public int getWidthBlock(){
		return block_width;
	}

	public int getHeightBlock(){
		return block_height;
	}

	public MapObject getCurrentMap(){
		return first_map;
	}

	public MapObject getNextMap(){
		return second_map;
	}

	public int getPos(){
		return pos_block;
	}

	public static synchronized int getNumberMap(int current, int num_maps){
		int index;
		if(Constants.map_index.size() <= current){
			Random rnd = new Random(System.currentTimeMillis());
			index = rnd.nextInt(num_maps);
		}else{
			index = Constants.map_index.get(current);
		}
		Constants.map_index.add(index);
		return index;
	}

	public void restart() {
		current_map = 0;
		pos_block = 0;
		pixel_block = 0;
		//
		loadMap();
		loadMap();
	}

	public int removeTresure(Sphere player, int direction, int x_ori, int y_ori) {
		int value = 0;
		//Calculo coordenadas respecto mapa
		int xMap = (player.getPositionX() + (player.getWidthScreen()/2)) / getWidthBlock() + getPos();
		//Apaño, Richard es malvado y carga el mapa con las "y" invertidas
		int yMap = Math.abs((player.getPositionY() + (player.getHeightScreen()/2)) / getHeightBlock() - MAX_HEIGHT);
		//Otiene el mapa que tiene la X e Y calculada
		MapObject map = getCurrentMap();
		if(xMap >= map.getWidthBlocks()){
			xMap = xMap - map.getWidthBlocks();
			map = getNextMap();	
		}
		//Posibles posiciones de los tesoros(Actual,superior,inferior,lateral,lat superior,lat inferior)
		int[] x_pos = {0,0,0,1,1,1};
		int[] y_pos = {0,1,-1,0,1,-1};
		for(int i=0; i<x_pos.length;i++){
			int x_obj = xMap + x_pos[i];
			int y_obj = yMap + y_pos[i];
			if(x_obj >= 0 && x_obj < map.getWidthBlocks() && y_obj >= 0 && y_obj < map.getHeightBlocks()){
				//Posición dentro del mapa
				if(map.getObject(x_obj, y_obj) != null && map.getObject(x_obj, y_obj) instanceof Treasure){
					if(player.intersects(map.getObject(x_obj, y_obj), x_ori, y_ori)){
						//Obtiene el valor del tesoro y lo borra
						value += ((Treasure)map.getObject(x_obj, y_obj)).getValue();
						map.removeObject(x_obj,y_obj);
					}
				}
			}
		}
		return value;
	}

	//TODO devolver la velocidad correspondiente
	public int getVelocity() {
		return speedLow;
	}
	
	public int getBlockMov(){
		return pixel_block;
	}
	
	public int getMaxHeight(){
		return MAX_HEIGHT;
	}
	
	public TransformGroup get3DModel(){
		return first_map.get3DModel();
	}
}
