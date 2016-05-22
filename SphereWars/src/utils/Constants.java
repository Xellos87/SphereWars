package utils;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import media.ImageHandler;
import scoreboard.Ranking;

public class Constants {
	public static  String[] list_menu = { "Start", "Help", "options", "credits","Exit" };
	public static  String[] list_options = { "sound", "resolution", "controller", "jump","run", "back" };
	
	
	//rutas a las imagenes del menu de titulos
	public static final String starBackName = "images/base_background.png";
	public static final String mountainBackName = "images/mountain_edited.png";
	public static final String titleName = "fonts/title.png";
	public static final String jugarName = "fonts/menu_jugar.png";
	public static final String helpName = "fonts/menu_ayuda.png";
	public static final String salirName = "fonts/menu_salir.png";
	public static final String opcionesName = "fonts/menu_opciones.png";
	public static final String creditosName = "fonts/menu_creditos.png";
	//posiciones (iniciales) de los elementos del menu de titulo
	static int xPos = 125;
	public static final Position titlePos = new Position(60,20);	//posicion del titulo
	public static Position jugarPos = new Position(xPos,120);		//posicion de "jugar"
	public static Position helpPos = new Position(xPos,180);		
	public static Position opcionesPos = new Position(xPos,240);	
	public static Position creditPos = new Position(xPos,300);
	public static Position salirPos = new Position(xPos,360);	
	
	//rutas a las imagenes del menu de opciones
	public static final String soundName = "fonts/opciones_sonido.png";
	public static final String yesName = "fonts/opciones_si.png";
	public static final String noName = "fonts/opciones_no.png";
	public static final String resName = "fonts/opciones_resolucion.png";
	public static final String res480Name = "fonts/opciones_res480.png";
	public static final String res960Name = "fonts/opciones_res960.png";
	public static final String controllerName = "fonts/opciones_controles.png";
	public static final String keyboardName = "fonts/opciones_teclado.png";
	public static final String kinnectName = "fonts/opciones_kinnect.png";
	public static final String keyName = "fonts/opciones_teclas.png";
	public static final String jumpName = "fonts/opciones_saltar.png";
	public static final String runName = "fonts/opciones_correr.png";
	public static final String backName = "fonts/opciones_volver.png";
	//posiciones (iniciales) de los elementos del menu de opciones
	static int optX = 40;
	public static final Position soundPos = new Position(optX,110);
	public static final Position yesPos = new Position(optX+270,110);
	public static final Position noPos = new Position(optX+330,110);
	public static final Position resPos = new Position(optX,170);
	public static final Position res480Pos = new Position(optX+330,170);
	public static final Position res960Pos = new Position(optX+330,170);
	public static final Position controllerPos= new Position(optX,230);
	public static final Position keyboardPos = new Position(optX+360,230);
	public static final Position kinnectPos = new Position(optX+360,230);
	public static final Position keyPos = new Position(optX+150,290);
	public static final Position jumpPos = new Position(optX+20,350);
	public static final Position runPos = new Position(optX+280,350);
	public static final Position backPos = new Position(optX,410);
	//opciones del menu de opciones
	public static boolean sound = true;
	public static boolean conTeclado = true;
	public static int scale = 2;
	public static int teclaSalto = KeyEvent.VK_UP;
	public static int teclaPausa = KeyEvent.VK_SPACE;
	public static int teclaSprint = KeyEvent.VK_ENTER;
	
	public static  float alphaComp = 1f;	
	
	//posiciones del cursor	
	public static final Position titleIniPos = new Position(xPos-25,120);	//posicion inicial del cursor en menu inicio
	public static  int titleMaxPos = 5;	//posiciones cursor en menu de titulo
	public static  int titleGap = 60;	//espacio entre posiciones en menu de titulo
	public static final int desplazamiento = 40;
	
	public static final Position optionIniPos = new Position(optX-25,110);	//posicion inicial del cursor en menu opciones
	public static  int optionMaxPos = 6;	//posiciones cursor en menu de opciones
	//public static  int optionGap = 60;	//espacio entre posiciones en menu de titulo
	//public static final int odesplazamiento = 40;
	
	//booleanos menus
	public static boolean enMenu = true;
	public static final String titMenu = "titleMenu";
	public static final String optMenu = "optionsMenu";
	public static String tipoMenu = titMenu;
	//Constantes de estado
	public static final int MENU = 0;
	public static final int PAUSE = 1;
	public static final int GAME = 2;
	
	//Manejador de imagenes para las pantallas
	public static ImageHandler img_handler;
	//Interacciona con los ranking
	public static Ranking ranking;
	//TODO: aplicar escala para constantes de tama�o en los menus
	
	//Array de indices de mapas generados, se comparte entre los dos jugadores y permite repetir pantalla
	public static ArrayList<Integer> map_index;
}
