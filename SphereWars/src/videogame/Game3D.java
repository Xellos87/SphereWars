package videogame;

//BorderLayout stuff//
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

//Canvas3D
import javax.media.j3d.Canvas3D;

//The Universe
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import character.Boss;
import character.Bot;
import character.Sphere;

import javax.media.j3d.BranchGroup;

import javax.vecmath.*;

import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.VirtualUniverse;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;

import map.MapController;
import utils.Constants;

@SuppressWarnings("serial")
public class Game3D extends Canvas3D implements KeyListener{
	//Dimensión de la pantalla de juego
	private int width, height;
	//Contenedor del jugador
	private Sphere player;
	//Contenedor del jefe
	private Boss boss;
	//Flag que indica si la partida ha acabado
	private boolean end_game;
	//Puntuaciones para mostrar en scoreboard
	private double score_distance;
	private int score_coins;
	//Universo sobre el que se desarrolla la escena
	private SimpleUniverse simpleU;
	//Grupo que contiene todos los objetos de la escena
	private BranchGroup rootBranchGroup;

	//Metodo principal
	private Main main;
	//Mapa del juego
	private MapController map;
	//Contenedor del mapa al que se le pueden aplicar diferentes transformaciones
	private TransformGroup map_cont;
	private BranchGroup map_group;
	private BranchGroup sphere_group;
	private BranchGroup boss_group;
	private OrbitBehavior orbit;

	public Game3D(int width, int height, MapController map, Main main) {
		super(SimpleUniverse.getPreferredConfiguration());
		//Inicializacion de variables
		this.width = width;
		this.height = height;
		this.map = map;
		this.main = main;
		this.end_game = false;
		//Establece las opciones del canvas
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		setDoubleBufferEnable(true);
		//Inicializa la puntuacion
		init_score();
		//Inicia la configuracion del mundo
		initial_setup();
		//Establece la posicion y movimientos de la camara
		camera_setup();
		//Carga el contenedor del mapa y los mapas iniciales
		loadMap();
		//Carga la iluminacion
		addLights();
		//Carga el jugador
		initPlayer();
		//Carga el boss 
		initBoss();
		//Finaliza la creacion del mundo
		finalise();
		//coloca la camara en su lugar
		orbit.goHome();
		//Agrega el listener del teclado
		this.addKeyListener(this);

		//Ejemplo de borrar un elemento del tipo tesoro del mapa
		//((Treasure)map.getCurrentMap().getObject(7, 8)).removeObject();
	}


	private void initPlayer() {
		player = new Sphere(0, 0, (int)(map.getWidthBlock()*0.8), (int)(map.getHeightBlock()*0.8),1);
		main.getPanel().setSphere(player);
		TransformGroup tg = player.get3DModel();
		sphere_group = new BranchGroup();
		sphere_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		sphere_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphere_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		sphere_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		sphere_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		sphere_group.setCapability(TransformGroup.ALLOW_PARENT_READ);
		sphere_group.addChild(tg);
		rootBranchGroup.addChild(sphere_group);
	}

	private void initBoss(){
		boss = new Boss(width-90,20,map.getWidthBlock(),map.getHeightBlock(),false, width, height, main.music);
		/*TransformGroup tg = boss.get3DModel();
		boss_group = new BranchGroup();
		boss_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		boss_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		boss_group.setCapability(TransformGroup.ALLOW_PARENT_READ);
		boss_group.addChild(tg);
		rootBranchGroup.addChild(boss_group);*/
	}

	private void addLights() {
		//Agrega iluminacion blanca
		addDirectionalLight(new Vector3f(0f, 0f, -1f),
				new Color3f(1f, 1f, 1f));
		//Agrega iluminacion blanca
		addDirectionalLight(new Vector3f(0f, 1f, 0f),
				new Color3f(1f, 1f, 1f));
		//Agrega iluminacion blanca
		addDirectionalLight(new Vector3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f));
		//Agrega iluminacion blanca
		addAmbientalLight();
	}

	private void loadMap() {
		//Crea el contenedor del mapa y le establece los permisos
		map_cont = new TransformGroup();
		map_cont.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		map_cont.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		map_cont.setCapability(TransformGroup.ALLOW_PARENT_READ);
		//Carga los primeros mapas en el contenedor
		map_cont.addChild(map.get3DFirstMap());
		map_cont.addChild(map.get3DSecondMap());
		map_group = new BranchGroup();
		map_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		map_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		map_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		map_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		map_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		map_group.setCapability(TransformGroup.ALLOW_PARENT_READ);
		map_group.addChild(map_cont);
		//Agrega el contenedor de mapa a la raiz
		rootBranchGroup.addChild(map_group);
	}

	private void init_score() {
		this.score_distance = 0;
		this.score_coins = 0;
	}

	private void camera_setup() {
		//Establece el movimiento de la camara de forma que orbita alrededor del origen
		//permite movmiento de rotacion, traslacion y zoom
		orbit = new OrbitBehavior(this, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(new BoundingSphere());
		Transform3D home=new Transform3D();
		simpleU.getViewingPlatform().getViewPlatformTransform().getTransform(home);
		//System.out.println(home.toString());
		double[] homeM = {1.0, 0.0, 0.0, 0.8, 0.0, 1.0, 0.0, -0.4, 0.0, 0.0, 1.0, 2.41421356/2*Constants.scale, 0.0, 0.0, 0.0, 1.0};
		Transform3D homeT = new Transform3D(homeM);
		orbit.setHomeTransform(homeT);		
		ViewingPlatform vp = simpleU.getViewingPlatform();
		vp.setViewPlatformBehavior(orbit);
	}

	/**
	 * Perform the essential setups for the Java3D
	 */
	private void initial_setup() {
		//Establece el tamaño del canvas
		setSize(width, height);
		//Crea el universon en el que se representa la escena
		simpleU = new SimpleUniverse(this);
		//Raiz sobra la que se agregan los elementos visualizar, se le agregan permisos para
		//agregar mas elementos y modificarlos
		rootBranchGroup = new BranchGroup();
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		rootBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
		//Establece el fondo del universo
		Background background = new Background(new Color3f(0.3476f,0.6484f,0.9257f));
		BoundingSphere sphere = new BoundingSphere(new Point3d(0,0,0), 100000);
		background.setApplicationBounds(sphere);
		rootBranchGroup.addChild(background);
	}

	private void addDirectionalLight(Vector3f direction, Color3f color) {
		//Establece la esfera en la que afecta la luz
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(1000d);
		//Crea la luz y establece el color y la direccion
		DirectionalLight lightD = new DirectionalLight(color, direction);
		lightD.setInfluencingBounds(bounds);
		//Agrega la luz a la raiz de elementos
		rootBranchGroup.addChild(lightD);

	}

	private void addAmbientalLight() {
		//Establece la esfera en la que afecta la luz
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(1000d);
		//Crea la luz y establece el color y la direccion
		Color3f ambientColour = new Color3f(0.5f, 0.5f, 0.5f);
		AmbientLight ambientLight = new AmbientLight(ambientColour);
		ambientLight.setInfluencingBounds(bounds);
		//Agrega la luz a la raiz de elementos
		rootBranchGroup.addChild(ambientLight);
	}

	public void finalise() {
		//Se agrega la raiz de todos los elementos a visualizar al universo
		simpleU.addBranchGraph(rootBranchGroup);
		//Establece la vista de la camara
		simpleU.getViewingPlatform().setNominalViewingTransform();
	}

	@Override
	public void paint(Graphics arg0) {
		super.paint(arg0);
		requestFocus();
	}

	public boolean actionGame(int x_ori,int y_ori) {
		x_ori = Constants.xOri;
		y_ori = Constants.yOri;
		if(!end_game){
			//Mueve el mapa
			double dist = map.move();
			//mueve la camara para que siga la esfera
			//orbit.setRotationCenter(new Point3d(player.x,player.y,0));
			//Realiza las transformaciones para mover el mapa
			Transform3D translate = new Transform3D();
			Vector3f translate_vector = new Vector3f();
			map_cont.getTransform(translate);
			translate.get(translate_vector);
			if(Constants.scale==2){
				translate_vector.x -= dist*0.076f;
			}else if(Constants.scale==4){
				translate_vector.x -= dist*0.076f*2;
			}
			
			translate.set(translate_vector);
			map_cont.setTransform(translate);
			//Establece la puntuacion de distancia
			score_distance += dist;
			//Comprueba si hay que cargar un nuevo mapa
			if(map.hasNewMap()){
				//Hay que cargar un mapa nuevo
				TransformGroup nextMap = map.get3DSecondMap();
				BranchGroup bg = new BranchGroup();
				bg.addChild(nextMap);
				map_cont.addChild(bg);
			}
			//Mueve los bot del mapa si los hubiera, solo del mapa actual y el siguiente
			/* Acciones a realizar */
			//TODO velocidad con la velocidad de plataformas
			int block = player.checkCollision(map,x_ori,y_ori);
			switch (block) {
			case Sphere.COLLINF:	//Colision inferior
				player.setVelocity(2, 0);
				break;
			case Sphere.COLLSUP:	//Colision superior
				player.setVelocity(2, 0);
				player.gravity();
				break;
			case Sphere.COLLLAT:	//Colision lateral
				player.setVelocity(0, player.vy);
				player.gravity();
				break;
			case Sphere.COLLINFLAT:
				player.setVelocity(0, 0);
				break;
			case Sphere.COLLSUPLAT:
				player.setVelocity(0, player.vy);
				player.gravity();
				break;
			case Sphere.COLLDEATH:
				end_game = true;
				break;
			case Sphere.COLLKILL:
				player.miniJump();
				break;
			case Sphere.COLLGET:
				score_coins += map.removeTresure(player,block,x_ori,y_ori);
				player.gravity();
				break;
			default:
				//System.out.println("Gravedad");
				player.setVelocity(2, player.vy);
				player.gravity();
				break;
			}	
			map.updateMap(x_ori, y_ori, Constants.gameState != Constants.PAUSE);
			map.moveBot();
			player.move();
			if(end_game){
				//animacion de muerte
				player.setVelocity(0, -15);
			}
			//TODO: separar movimiento de accion y permitir que el boss se siga moviendo en el draw?
			if(boss.isVisible() && boss.reseteo){
				//Borra el boss del mundo
				rootBranchGroup.removeChild(boss_group);
				boss_group.removeChild(boss.get3DModel());
				boss_group = null;
			}
			if(!boss.isVisible() && (System.currentTimeMillis() >= boss.hazteVisible)){
				//Mete el boss en el mundo
				TransformGroup tg = boss.get3DModel();
				boss_group = new BranchGroup();
				boss_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
				boss_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
				boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
				boss_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
				boss_group.setCapability(TransformGroup.ALLOW_PARENT_READ);
				boss_group.addChild(tg);
				rootBranchGroup.addChild(boss_group);
			}
			//TODO: separar movimiento de accion y permitir que el boss se siga moviendo en el draw?
			int col = boss.action(true, player.x, player.y, player.getBox(x_ori, y_ori));
			if(col==0){
				//Colision con el jefe, comprobar si es colision de muerte
				if(boss.isVisible() && boss.collides && player.bossCollision(boss.getBox(x_ori, y_ori))){
					end_game=true;
				} 
			}else if(col == 1){
				player.miniJump();
			}
		}
		return end_game;
	}

	public void quitGame(){
		setFocusable(false);
		removeKeyListener(this);
	}

	public int getCoins(){
		return score_coins;
	}

	public double getDistance(){
		return score_distance;
	}

	public BufferedImage createBufferedImageFromCanvas3D(){
		//Crea el contexto para cargar los graficos del canvas
		GraphicsContext3D  ctx = getGraphicsContext3D();
		int w = getWidth();
		int h = getHeight();
		//Establece la imagen que contiene el canvas del mismo tamaño
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		ImageComponent2D im = new ImageComponent2D(ImageComponent.FORMAT_RGB, bi);
		//Prepara la rasterizacion
		Raster ras = new Raster(new Point3f( -1.0f, -1.0f, -1.0f ),
				Raster.RASTER_COLOR, 0, 0, w, h, im, null );
		//Vuelca toda la informacion del canvas y lo rasteriza
		ctx.flush(true);
		ctx.readRaster(ras);
		//Devuelve la imagen del raster
		return ras.getImage().getImage();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//((Bot)map.getCurrentMap().getObject(3, 5)).death();
		if(!end_game && Constants.gameState == Constants.GAME){
			if(e.getKeyChar() == '.'){
				orbit.goHome();
			}
			else if(e.getKeyCode() == Constants.teclaSaltop1 && Constants.gameState != Constants.PAUSE){
				player.jump();
			}
			else if(e.getKeyCode() == Constants.teclaSprintp1 && Constants.gameState != Constants.PAUSE){
				Constants.sprintp1 = true;
				map.setSpeedHigh();
			}
			else{
				//Propaga el evento de pulsar al main para tratarlo
				main.keyPressed(e);
			}
		}else{
			//Propaga el evento de pulsar al main para tratarlo
			main.keyPressed(e);
		}		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == Constants.teclaSprintp1 && Constants.gameState != Constants.PAUSE){
			Constants.sprintp1 = false;
			map.setSpeedLow();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void restart(MapController map) {
		this.end_game = false;
		this.map = map;
		if(rootBranchGroup != null) {
			rootBranchGroup.removeChild(map_group);
			rootBranchGroup.removeChild(sphere_group);
			if(boss_group != null){
				rootBranchGroup.removeChild(boss_group);
			}
		}
		//Inicializa la puntuacion
		init_score();
		//Carga el contenedor del mapa y los mapas iniciales
		loadMap();
		//Carga el boss 
		initBoss();
		//Carga el jugador
		initPlayer();
		//coloca la camara en su lugar
		orbit.goHome();
	}
}