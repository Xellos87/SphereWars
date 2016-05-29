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
import character.Sphere;
import graphic.Model3D;
import item.Treasure;

//The BranchGroup
import javax.media.j3d.BranchGroup;

import javax.vecmath.*;

//The directional light
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
//For the bouding sphere of the light source
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.AmbientLight;
//Transformgroup
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;

//import character.Sphere;
import map.MapController;
import menu.EndMenu;
import menu.PauseMenu;
import menu.TitleMenu;
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
	//
	private TransformGroup map_cont;


	public Game3D(int width, int height, MapController map, Main main) {
		super(SimpleUniverse.getPreferredConfiguration());
		this.width = width;
		this.height = height;
		this.map = map;
		this.main = main;
		this.end_game = false;
		setPreferredSize(new Dimension(width, height));
		setDoubleBufferEnable(true);
		requestFocus();

		init_score();
		// Perform the initial setup, just once
		initial_setup();
		camera_setup();


		map_cont = new TransformGroup();
		map_cont.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		map_cont.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		map_cont.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		map_cont.addChild(map.get3DFirstMap());
		map_cont.addChild(map.get3DSecondMap());
		
		
		
		
		Transform3D transform_map = new Transform3D();
		map_cont.getTransform(transform_map);
		Vector3f translate = new Vector3f();
		transform_map.get(translate);
		translate.y -= 0.3f;
		transform_map.set(translate);
		map_cont.setTransform(transform_map);
		
		
		rootBranchGroup.addChild(map_cont);

		addDirectionalLight(new Vector3f(0f, 0f, -1f),
				new Color3f(1f, 1f, 1f));

		addDirectionalLight(new Vector3f(0f, 1f, 0f),
				new Color3f(1f, 1f, 1f));

		addDirectionalLight(new Vector3f(0f, 0f, 1f),
				new Color3f(1f, 1f, 1f));

		addAmbientalLight();

		finalise();

		addKeyListener(this);
		
		((Treasure)map.getCurrentMap().getObject(7, 8)).removeObject();
	}

	private void init_score() {
		this.score_distance = 0;
		this.score_coins = 0;
	}

	private void camera_setup() {
		OrbitBehavior orbit = new OrbitBehavior(this, OrbitBehavior.REVERSE_ALL);
		orbit.setSchedulingBounds(new BoundingSphere());
		ViewingPlatform vp = simpleU.getViewingPlatform();
		vp.setViewPlatformBehavior(orbit);
	}

	/**
	 * Perform the essential setups for the Java3D
	 */
	private void initial_setup() {
		// A JFrame is a Container -- something that can hold
		// other things, e.g a button, a textfield, etc..
		// however, for a container to hold something, you need
		// to specify the layout of the storage. For our
		// example, we would like to use a BorderLayout.
		// The next line does just this:
		// getContentPane().setLayout(new BorderLayout());

		// The next step is to setup graphics configuration
		// for Java3D. Since different machines/OS have differnt
		// configuration for displaying stuff, therefore, for
		// java3D to work, it is important to obtain the correct
		// graphics configuration first.
		// GraphicsConfiguration config =
		// SimpleUniverse.getPreferredConfiguration();
		// Since we are doing stuff via java3D -- meaning we
		// cannot write pixels directly to the screen, we need
		// to construct a "canvas" for java3D to "paint". And
		// this "canvas" will be constructed with the graphics
		// information we just obtained.
		// canvas3D = new Canvas3D(config);
		this.setSize(width, height);
		// And we need to add the "canvas to the centre of our
		// window..
		// getContentPane().add("Center", canvas3D);

		// Creates the universe
		simpleU = new SimpleUniverse(this);

		// First create the BranchGroup object
		rootBranchGroup = new BranchGroup();
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		rootBranchGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
	}

	/**
	 * Adds a light source to the universe
	 * 
	 * @param direction
	 *            The inverse direction of the light
	 * @param color
	 *            The color of the light
	 */
	private void addDirectionalLight(Vector3f direction, Color3f color) {
		// Creates a bounding sphere for the lights
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(1000d);

		// Then create a directional light with the given
		// direction and color
		DirectionalLight lightD = new DirectionalLight(color, direction);
		lightD.setInfluencingBounds(bounds);

		// Then add it to the root BranchGroup
		rootBranchGroup.addChild(lightD);

	}

	private void addAmbientalLight() {
		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(1000d);
		//Set up the ambient light
		Color3f ambientColour = new Color3f(0.7f, 0.7f, 0.7f);
		AmbientLight ambientLight = new AmbientLight(ambientColour);
		ambientLight.setInfluencingBounds(bounds);
		// Then add it to the root BranchGroup
		rootBranchGroup.addChild(ambientLight);

	}

	/**
	 * Finalise everything to get ready
	 */
	public void finalise() {
		// Then add the branch group into the Universe
		simpleU.addBranchGraph(rootBranchGroup);

		// And set up the camera position
		simpleU.getViewingPlatform().setNominalViewingTransform();
	}
	
	public boolean actionGame() {
		if(!end_game){
			//Mueve plataformas
			double dist = map.move();
			
			Transform3D translate = new Transform3D();
			Vector3f translate_vector = new Vector3f();
			map_cont.getTransform(translate);
			translate.get(translate_vector);
			translate_vector.x -= dist*0.085f;
			translate.set(translate_vector);
			map_cont.setTransform(translate);
			score_distance += dist;
			
			if(map.hasNewMap()){
				System.out.println("Carga de mapa");
				//Hay que cargar un mapa nuevo
				TransformGroup nextMap = map.get3DSecondMap();
				BranchGroup bg = new BranchGroup();
				bg.addChild(nextMap);
				map_cont.addChild(bg);
				//map_cont.addChild(nextMap);
			}
		}
		return end_game;
	}

	public int getCoins(){
		return score_coins;
	}

	public double getDistance(){
		return score_distance;
	}

	public BufferedImage createBufferedImageFromCanvas3D(){
		//waitForOffScreenRendering();

		GraphicsContext3D  ctx = getGraphicsContext3D();
		int w = getWidth();
		int h = getHeight();

		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		ImageComponent2D im = new ImageComponent2D(ImageComponent.FORMAT_RGB, bi);

		Raster ras = new Raster(new Point3f( -1.0f, -1.0f, -1.0f ),
				Raster.RASTER_COLOR, 0, 0, w, h, im, null );

		ctx.flush(true);

		ctx.readRaster( ras );

		return ras.getImage().getImage();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		main.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}