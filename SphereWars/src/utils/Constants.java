package utils;

import java.awt.Event;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import media.ImageHandler;
import scoreboard.Ranking;

public class Constants {
	public static final String[] list_menu = { "Start", "versus","Help", "options", "credits", "Exit" };
	public static final String[] list_options = { "sound", "resolution", "controller", "back" };
	public static final String[] list_controller = {"controller", "device", "pause", "jump", "run"};
	
	//rutas a las imagenes del menu de titulos
	public static final String starBackName = "images/base_background.png";
	public static final String mountainBackName = "images/mountain_edited.png";
	public static final String titleName = "fonts/title.png";
	public static final String jugarName = "fonts/menu_jugar.png";
	public static final String helpName = "fonts/menu_ayuda.png";
	public static final String salirName = "fonts/menu_salir.png";
	public static final String opcionesName = "fonts/menu_opciones.png";
	public static final String creditosName = "fonts/menu_creditos.png";
	public static final String versusName = "fonts/menu_versus.png";
	public static final String dosdName = "fonts/menu_2D.png";
	public static final String tresdName = "fonts/menu_3D.png";
	//posiciones (iniciales) de los elementos del menu de titulo
	static int xPos = 125;
	public static final Position titlePos = new Position(60,20);	//posicion del titulo
	public static Position jugarPos = new Position(xPos,110);		//posicion de "jugar"
	public static Position versusPos = new Position(xPos, 170);
	public static Position dosDjPos = new Position(xPos+300,140);
	public static Position tresDjPos = new Position(xPos+380,140);
	public static Position helpPos = new Position(xPos,230);		
	public static Position opcionesPos = new Position(xPos,290);	
	public static Position creditPos = new Position(xPos,350);
	public static Position salirPos = new Position(xPos,410);	
	//Opciones del menu principal
	public static int numJugadores = 1;	//numero jugadores
	public static String visualMode = "2D";
	public static boolean cursorDesplazado = false;
	
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
	public static final String controller1Name = "fonts/opciones_control1.png";
	public static final String controller2Name = "fonts/opciones_controles2.png";
	public static final String okName = "fonts/opciones_ok.png";
	public static final String pauseName = "fonts/opciones_pausa.png";
	//posiciones (iniciales) de los elementos del menu de opciones
	static int optX = 40;
	public static final Position soundPos = new Position(optX,110);
	public static final Position yesPos = new Position(optX+270,110);
	public static final Position noPos = new Position(optX+330,110);
	public static final Position resPos = new Position(optX,170);
	public static final Position res480Pos = new Position(optX+330,170);
	public static final Position res960Pos = new Position(optX+330,170);
	public static final Position backPos = new Position(optX,290);
	public static final Position controllerPos= new Position(optX,230);
	public static final Position keyboardPos = new Position(optX,170);
	public static final Position kinnectPos = new Position(optX,170);
	public static final Position keyPos = new Position(optX,230);
	public static final Position jumpPos = new Position(optX,290);
	public static final Position runPos = new Position(optX,350);
	public static final Position controller1Pos = new Position(optX,110);
	public static final Position controller2Pos = new Position(optX,110);
	//opciones del menu de opciones
	public static boolean sound = true;
	public static boolean conTeclado = true;	//solo para jugador uno
	public static int scale = 2;
	public static int teclaSaltop1 = KeyEvent.VK_UP;
	public static int teclaPausap1 = KeyEvent.VK_SPACE;
	public static int teclaSprintp1 = KeyEvent.VK_ENTER;
	public static int teclaSaltop2 = KeyEvent.VK_W;
	public static int teclaPausap2 = KeyEvent.VK_SPACE;
	public static int teclaSprintp2 = KeyEvent.VK_Z;
	public static boolean elegidoJugador = false;
	public static int jugador = 1;
	
	public static  float alphaComp = 1f;	
	
	//posiciones del cursor	
	public static final Position titleIniPos = new Position(xPos-25,110);	//posicion inicial del cursor en menu inicio
	public static final int titleMaxPos = list_menu.length;	//posiciones cursor en menu de titulo
	public static final int titleGap = 60;	//espacio entre posiciones en menu de titulo
	public static final int desplazamiento = 40;
	
	public static final Position optionIniPos = new Position(optX-25,110);	//posicion inicial del cursor en menu opciones
	public static final int optionMaxPos = list_options.length;	//posiciones cursor en menu de opciones
	//public static  int optionGap = 60;	//espacio entre posiciones en menu de titulo
	//public static final int odesplazamiento = 40;
	public static final int controlMaxPos = list_controller.length;
	
	
	
	//booleanos menus
	public static boolean enMenu = true;
	public static final String titMenu = "titleMenu";
	public static final String optMenu = "optionsMenu";
	public static final String conMenu = "controllerMenu";
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
	
	//Fuente y ruta de las fuente
	private static String path_font = "fonts/M04.TTF";
	private static String path_font_bold = "fonts/M04B.TTF";
	public static Font font = crearFuente();
	public static Font font_bold = crearFuenteBold();
	
	private static Font crearFuente() {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(path_font));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Font crearFuenteBold() {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(path_font_bold));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
