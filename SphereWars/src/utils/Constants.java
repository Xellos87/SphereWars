package utils;

public class Constants {
	public static  String[] list_menu = { "Start", "Help", "options", "credits","Exit" };
	//rutas a las imagenes del menu de titulos
	public static final String starBackName = "images/base_background.png";
	public static final String mountainBackName = "images/mountain_edited.png";
	public static final String titleName = "fonts/title.png";
	public static final String jugarName = "fonts/menu_jugar.png";
	public static final String helpName = "fonts/menu_help.png";
	public static final String salirName = "fonts/menu_salir.png";
	public static final String opcionesName = "fonts/menu_opciones.png";
	//posiciones (iniciales) de los elementos del menu de titulo
	public static final Position titlePos = new Position(10,40);	//posicion del titulo
	public static Position jugarPos = new Position(105,100);		//posicion de "jugar"
	public static Position helpPos = new Position(105,140);		
	public static Position opcionesPos = new Position(105,180);		
	public static Position salirPos = new Position(105,250);		
	
	public static  float alphaComp = 1f;	
	
	//posiciones del cursor	
	public static final Position titleIniPos = new Position(100,100);	//posicion inicial del cursor en menu inicio
	public static  int titleMaxPos = 5;	//posiciones cursor en menu de titulo
	public static  int titleGap = 40;	//espacio entre posiciones en menu de titulo
	public static final int desplazamiento = 40;
	
	//booleanos menus
	public static boolean enMenu = true;
	//TODO: aplicar escala para constantes de tama�o en los menus
}
