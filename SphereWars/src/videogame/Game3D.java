package videogame;

//BorderLayout stuff//
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

//Canvas3D
import javax.media.j3d.Canvas3D;

//The Universe
import com.sun.j3d.utils.universe.SimpleUniverse;

//The BranchGroup
import javax.media.j3d.BranchGroup;

//For the Box
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Box;

import com.sun.j3d.utils.geometry.Sphere;

import javax.vecmath.*;

//The directional light
import javax.media.j3d.DirectionalLight;

//For the bouding sphere of the light source
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
//import javax.media.j3d;
import javax.media.j3d.Transform3D;
//Transformgroup
import javax.media.j3d.TransformGroup;
import com.sun.j3d.utils.behaviors.mouse.*;
//import character.Sphere;
import map.MapController;

@SuppressWarnings("serial")
public class Game3D extends Canvas3D implements MouseMotionListener, KeyListener {
	int width, height;
	/**
	 * The SimpleUniverse object
	 */
	protected SimpleUniverse simpleU;

	/**
	 * The root BranchGroup Object.
	 */
	protected BranchGroup rootBranchGroup;
	////////////////////////////////////////////////////////////////
	// Rotation/moving Speed
	private static final double TURNSPEED = 0.0005;
	private static final double MOVESPEED = 0.05;
	private TransformGroup camera = null;
	// Used to for the last posistion of the mouse
	private int lastX = 0;
	// Needed for changing the camera position
	private Transform3D transform;
	private Vector3d vector;
	////////////////////////////////////////////////////////////////

	// Canvas3D canvas3D;
	/**
	 * Constructor that consturcts the window with the given name.
	 * 
	 * @param name
	 *            The name of the window, in String format
	 */

	public Game3D(int width, int height, int numplayers) {
		super(SimpleUniverse.getPreferredConfiguration());
		// The next line will construct the window and name it
		// with the given name
		// super();
		this.width = width;
		this.height = height;
		// Perform the initial setup, just once
		initial_setup();
		camera_setup();
		
		
		addBox(0.5f, 0.5f, 0.5f, new Color3f(1,0,0), new Color3f(1,0,0));
		addDirectionalLight(new Vector3f(0f, 0f, -1),
				        new Color3f(1f, 1f, 1f));
		finalise();
	}

	private void camera_setup() {
		// Add the this as MouseMotionListener
		//this.addMouseMotionListener(this);
		this.addKeyListener(this);
		// Get TransformGroup that hold the Camera
		this.camera = simpleU.getViewingPlatform().getViewPlatformTransform();
		// simpleU.addBranchGraph(rootBranchGroup);
		// Setting nominal setting for viewing
		// simpleU.getViewingPlatform().setNominalViewingTransform();
		// rootBranchGroup.addChild(camera);
		MouseRotate myMouseRotate = new MouseRotate();
		myMouseRotate.setTransformGroup(camera);
		myMouseRotate.setSchedulingBounds(new BoundingSphere());
		rootBranchGroup.addChild(myMouseRotate);
	}

	/**
	 * Perform the essential setups for the Java3D
	 */
	protected void initial_setup() {
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
		// canvas3D.startRenderer();
		// this.addBox(0.4f, 0.5f, 0.6f, new Color3f(1, 0, 0), new Color3f(1, 0,
		// 0));
		// this.addDirectionalLight(new Vector3f(0f, 0f, -1),
		// new Color3f(1f, 1f, 0f));
		// this.finalise();

		// this.show();

	}

	/**
	 * Adds a light source to the universe
	 * 
	 * @param direction
	 *            The inverse direction of the light
	 * @param color
	 *            The color of the light
	 */
	public void addDirectionalLight(Vector3f direction, Color3f color) {
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

	/**
	 * Adds a box to the universe
	 * 
	 * @param x
	 *            The x dimension of the box
	 * @param y
	 *            The y dimension of the box
	 * @param z
	 *            The z dimension of the box
	 */
	public void addBox(float x, float y, float z, Color3f diffuse, Color3f spec) {
		// Add a box with the given dimension

		// First setup an appearance for the box
		Appearance app = new Appearance();
		Material mat = new Material();
		mat.setDiffuseColor(diffuse);
		mat.setSpecularColor(spec);
		mat.setShininess(5.0f);

		app.setMaterial(mat);
		Box box = new Box(x, y, z, app);
		
		TransformGroup tg = new TransformGroup();
		tg.addChild(box);
		
		
		// Create a TransformGroup and make it the parent of the box
		//TransformGroup tg = new TransformGroup();
		//Sphere s = new Sphere(0.1f);
		//tg.addChild(s);
		// Ball ball = new Ball(1f);
		// tg.addChild(box);

		// Then add it to the rootBranchGroup
		rootBranchGroup.addChild(tg);

		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// MouseRotate myMouseRotate = new MouseRotate();
		// myMouseRotate.setTransformGroup(tg);
		// myMouseRotate.setSchedulingBounds(new BoundingSphere());
		// rootBranchGroup.addChild(myMouseRotate);
		//
		// MouseTranslate myMouseTranslate = new MouseTranslate();
		// myMouseTranslate.setTransformGroup(tg);
		// myMouseTranslate.setSchedulingBounds(new BoundingSphere());
		// rootBranchGroup.addChild(myMouseTranslate);
		//
		// MouseZoom myMouseZoom = new MouseZoom();
		// myMouseZoom.setTransformGroup(tg);
		// myMouseZoom.setSchedulingBounds(new BoundingSphere());
		// rootBranchGroup.addChild(myMouseZoom);

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
	/*
	 * public Canvas3D getCanvas(){ return canvas3D; }
	 */
	/*
	 * private int width,height; //Contenedor del jugador private Sphere player;
	 * //Flag que indica si la partida ha acabado private boolean end_game;
	 * //Valor de alfa para oscurecimiento de pantalla por muerte y valor maximo
	 * private int alpha_death; private final int MAX_ALPHA = 100; private final
	 * String DEATH_STR = "Has muerto..."; //Indica si se ha de esperar a la
	 * muerte de otro jugador private boolean wait_other_player; //Puntuaciones
	 * para mostrar en scoreboard private double score_distance; private int
	 * score_coins; private int score_time; //TODO, eliminar si no se implementa
	 * //Pruebas 3D private GraphicsConfiguration config = null; private
	 * Canvas3D canvas = null; private SimpleUniverse universe = null; private
	 * BranchGroup root = null; private TransformGroup camera = null;
	 * 
	 * private Cube cube;
	 * 
	 * public Game3D(int width, int height, int num_players){ this.width =
	 * width; this.height = height; setDoubleBuffered(false);
	 * setPreferredSize(new Dimension(width, height)); setFocusable(true);
	 * requestFocus(); cube = new Cube(75, 75, 200, 50); this.wait_other_player
	 * = num_players > 1;
	 * 
	 * init_component();
	 * 
	 * init_score(); }
	 * 
	 * private void init_component() { System.loadLibrary("jawt");
	 * 
	 * this.setLayout(new BorderLayout());
	 * 
	 * //Set up the canvas this.config =
	 * SimpleUniverse.getPreferredConfiguration(); this.canvas = new
	 * Canvas3D(config);
	 * 
	 * //Add the this as MouseMotionListener
	 * //this.canvas.addMouseMotionListener(this);
	 * //this.canvas.addKeyListener(this);
	 * 
	 * //Pass the canvas to the constructor of the Universe this.universe = new
	 * SimpleUniverse(canvas);
	 * 
	 * //Add the canvas to the applet this.add("Center", this.canvas);
	 * 
	 * //Get TransformGroup that hold the Camera this.camera =
	 * this.universe.getViewingPlatform().getViewPlatformTransform();
	 * 
	 * //Add things to the universe this.root = new BranchGroup();
	 * this.root.addChild(new ColorCube(0.2));
	 * this.universe.addBranchGraph(root);
	 * 
	 * //Setting nominal setting for viewing
	 * universe.getViewingPlatform().setNominalViewingTransform(); }
	 * 
	 * private void init_score() { this.score_distance = 0; this.score_coins =
	 * 0; }
	 * 
	 * public void draw(Graphics2D g2d,int x_ori, int y_ori, MapController
	 * map_cont, boolean not_pause){ cube.drawCube(g2d,x_ori,y_ori); }
	 * 
	 * 
	 * public class Cube { int x, y, size, shift; Point[] cubeOnePoints; Point[]
	 * cubeTwoPoints; public Cube(int x, int y, int size, int shift) { this.x =
	 * x; this.y = y; this.size = size; this.shift = shift; cubeOnePoints =
	 * getCubeOnePoints(); cubeTwoPoints = getCubeTwoPoints(); }
	 * 
	 * private Point[] getCubeOnePoints() { Point[] points = new Point[4];
	 * points[0] = new Point(x, y); points[1] = new Point(x + size, y);
	 * points[2] = new Point(x + size, y + size); points[3] = new Point(x, y +
	 * size); return points; }
	 * 
	 * private Point[] getCubeTwoPoints() { int newX = x + shift; int newY = y +
	 * shift; Point[] points = new Point[4]; points[0] = new Point(newX, newY);
	 * points[1] = new Point(newX + size, newY); points[2] = new Point(newX +
	 * size, newY + size); points[3] = new Point(newX, newY + size); return
	 * points; }
	 * 
	 * public void drawCube(Graphics g,int x_ori, int y_ori) {
	 * g.drawRect(x_ori+x, y_ori+y, size, size); g.drawRect(x_ori+x + shift,
	 * y_ori+y + shift, size, size); // draw connecting lines for (int i = 0; i
	 * < 4; i++) { g.drawLine(x_ori+cubeOnePoints[i].x,
	 * y_ori+cubeOnePoints[i].y, x_ori+cubeTwoPoints[i].x,
	 * y_ori+cubeTwoPoints[i].y); } }
	 */

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (this.transform == null) {
			// Creating the object we need
			this.transform = new Transform3D();
			this.vector = new Vector3d();

			// Making it the same as used
			camera.getTransform(transform);
			transform.get(vector);
		}

		switch (e.getKeyChar()) {
		case 'a':
			// moves camera to the left
			vector.x -= Game3D.MOVESPEED;
			break;
		case 'd':
			// moves camera to the right
			vector.x += Game3D.MOVESPEED;
			break;
		case 'w':
			vector.y += Game3D.MOVESPEED;
			break;
		case 's':
			vector.y -= Game3D.MOVESPEED;
		}

		// Setting the object back
		transform.set(vector);
		camera.setTransform(transform);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (this.transform == null) {

			// Creating the object we need
			this.transform = new Transform3D();
			this.vector = new Vector3d();

			// Making it the same as used
			camera.getTransform(transform);
			transform.get(vector);
		}

		// Increasing or decreasing the x value of the Vector3d
		vector.x += (e.getX() - this.lastX) * Game3D.TURNSPEED;
		this.lastX = e.getX();
		transform.set(vector);
		camera.setTransform(transform);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}

/*
 * package videogame;
 * 
 * //BorderLayout stuff import java.awt.*; import javax.swing.*;
 * 
 * //Canvas3D import javax.media.j3d.Canvas3D;
 * 
 * //The Universe import com.sun.j3d.utils.universe.SimpleUniverse;
 * 
 * 
 * 
 * //The BranchGroup import javax.media.j3d.BranchGroup;
 * 
 * //For the Box import com.sun.j3d.utils.geometry.*; import
 * com.sun.j3d.utils.geometry.Box;
 * 
 * import com.sun.j3d.utils.geometry.Sphere;
 * 
 * import javax.vecmath.*;
 * 
 * //The directional light import javax.media.j3d.DirectionalLight;
 * 
 * //For the bouding sphere of the light source import
 * javax.media.j3d.BoundingSphere; import javax.media.j3d.Appearance; import
 * javax.media.j3d.Material; //import javax.media.j3d;
 * 
 * //Transformgroup import javax.media.j3d.TransformGroup; import
 * com.sun.j3d.utils.behaviors.mouse.*; //import character.Sphere; import
 * map.MapController;
 * 
 * @SuppressWarnings("serial") public class Game3D extends Canvas3D{ int width,
 * height;
 *//**
	 * The SimpleUniverse object
	 */
/*
 * protected SimpleUniverse simpleU;
 * 
 *//**
	 * The root BranchGroup Object.
	 */
/*
 * protected BranchGroup rootBranchGroup; // Canvas3D canvas3D;
 *//**
	 * Constructor that consturcts the window with the given name.
	 * 
	 * @param name
	 *            The name of the window, in String format
	 */
/*
 * 
 * public Game3D(int width, int height, int numplayers) { super(SimpleUniverse
 * .getPreferredConfiguration()); // The next line will construct the window and
 * name it // with the given name //super(); this.width = width; this.height =
 * height; // Perform the initial setup, just once initial_setup(); }
 * 
 *//**
	 * Perform the essential setups for the Java3D
	 */
/*
 * protected void initial_setup() { // A JFrame is a Container -- something that
 * can hold // other things, e.g a button, a textfield, etc.. // however, for a
 * container to hold something, you need // to specify the layout of the
 * storage. For our // example, we would like to use a BorderLayout. // The next
 * line does just this: //getContentPane().setLayout(new BorderLayout());
 * 
 * // The next step is to setup graphics configuration // for Java3D. Since
 * different machines/OS have differnt // configuration for displaying stuff,
 * therefore, for // java3D to work, it is important to obtain the correct //
 * graphics configuration first. GraphicsConfiguration config = SimpleUniverse
 * .getPreferredConfiguration();
 * 
 * // Since we are doing stuff via java3D -- meaning we // cannot write pixels
 * directly to the screen, we need // to construct a "canvas" for java3D to
 * "paint". And // this "canvas" will be constructed with the graphics //
 * information we just obtained. //canvas3D = new Canvas3D(config);
 * this.setSize(width, height); // And we need to add the "canvas to the centre
 * of our // window.. //getContentPane().add("Center", canvas3D);
 * 
 * // Creates the universe simpleU = new SimpleUniverse(this);
 * 
 * // First create the BranchGroup object rootBranchGroup = new BranchGroup();
 * //canvas3D.startRenderer(); // this.addBox(0.4f, 0.5f, 0.6f, new Color3f(1,
 * 0, 0), new Color3f(1, 0, 0)); // this.addDirectionalLight(new Vector3f(0f,
 * 0f, -1), // new Color3f(1f, 1f, 0f)); //this.finalise();
 * 
 * //this.show(); }
 * 
 *//**
	 * Adds a light source to the universe
	 * 
	 * @param direction
	 *            The inverse direction of the light
	 * @param color
	 *            The color of the light
	 */
/*
 * public void addDirectionalLight(Vector3f direction, Color3f color) { //
 * Creates a bounding sphere for the lights BoundingSphere bounds = new
 * BoundingSphere(); bounds.setRadius(1000d);
 * 
 * // Then create a directional light with the given // direction and color
 * DirectionalLight lightD = new DirectionalLight(color, direction);
 * lightD.setInfluencingBounds(bounds);
 * 
 * // Then add it to the root BranchGroup rootBranchGroup.addChild(lightD); }
 * 
 *//**
	 * Adds a box to the universe
	 * 
	 * @param x
	 *            The x dimension of the box
	 * @param y
	 *            The y dimension of the box
	 * @param z
	 *            The z dimension of the box
	 */
/*
 * public void addBox(float x, float y, float z, Color3f diffuse, Color3f spec)
 * { // Add a box with the given dimension
 * 
 * // First setup an appearance for the box Appearance app = new Appearance();
 * Material mat = new Material(); mat.setDiffuseColor(diffuse);
 * mat.setSpecularColor(spec); mat.setShininess(5.0f);
 * 
 * app.setMaterial(mat); Box box = new Box(x, y, z, app);
 * 
 * // Create a TransformGroup and make it the parent of the box TransformGroup
 * tg = new TransformGroup(); Sphere s = new Sphere(0.6f); tg.addChild(s);
 * //Ball ball = new Ball(1f); //tg.addChild(box);
 * 
 * // Then add it to the rootBranchGroup rootBranchGroup.addChild(tg);
 * 
 * tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
 * tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
 * 
 * MouseRotate myMouseRotate = new MouseRotate();
 * myMouseRotate.setTransformGroup(tg); myMouseRotate.setSchedulingBounds(new
 * BoundingSphere()); rootBranchGroup.addChild(myMouseRotate);
 * 
 * MouseTranslate myMouseTranslate = new MouseTranslate();
 * myMouseTranslate.setTransformGroup(tg);
 * myMouseTranslate.setSchedulingBounds(new BoundingSphere());
 * rootBranchGroup.addChild(myMouseTranslate);
 * 
 * MouseZoom myMouseZoom = new MouseZoom(); myMouseZoom.setTransformGroup(tg);
 * myMouseZoom.setSchedulingBounds(new BoundingSphere());
 * rootBranchGroup.addChild(myMouseZoom); }
 * 
 *//**
	 * Finalise everything to get ready
	 *//*
	 * public void finalise() { // Then add the branch group into the Universe
	 * simpleU.addBranchGraph(rootBranchGroup);
	 * 
	 * // And set up the camera position
	 * simpleU.getViewingPlatform().setNominalViewingTransform(); } public
	 * Canvas3D getCanvas(){ return canvas3D; } private int width,height;
	 * //Contenedor del jugador private Sphere player; //Flag que indica si la
	 * partida ha acabado private boolean end_game; //Valor de alfa para
	 * oscurecimiento de pantalla por muerte y valor maximo private int
	 * alpha_death; private final int MAX_ALPHA = 100; private final String
	 * DEATH_STR = "Has muerto..."; //Indica si se ha de esperar a la muerte de
	 * otro jugador private boolean wait_other_player; //Puntuaciones para
	 * mostrar en scoreboard private double score_distance; private int
	 * score_coins; private int score_time; //TODO, eliminar si no se implementa
	 * //Pruebas 3D private GraphicsConfiguration config = null; private
	 * Canvas3D canvas = null; private SimpleUniverse universe = null; private
	 * BranchGroup root = null; private TransformGroup camera = null;
	 * 
	 * private Cube cube;
	 * 
	 * public Game3D(int width, int height, int num_players){ this.width =
	 * width; this.height = height; setDoubleBuffered(false);
	 * setPreferredSize(new Dimension(width, height)); setFocusable(true);
	 * requestFocus(); cube = new Cube(75, 75, 200, 50); this.wait_other_player
	 * = num_players > 1;
	 * 
	 * init_component();
	 * 
	 * init_score(); }
	 * 
	 * private void init_component() { System.loadLibrary("jawt");
	 * 
	 * this.setLayout(new BorderLayout());
	 * 
	 * //Set up the canvas this.config =
	 * SimpleUniverse.getPreferredConfiguration(); this.canvas = new
	 * Canvas3D(config);
	 * 
	 * //Add the this as MouseMotionListener
	 * //this.canvas.addMouseMotionListener(this);
	 * //this.canvas.addKeyListener(this);
	 * 
	 * //Pass the canvas to the constructor of the Universe this.universe = new
	 * SimpleUniverse(canvas);
	 * 
	 * //Add the canvas to the applet this.add("Center", this.canvas);
	 * 
	 * //Get TransformGroup that hold the Camera this.camera =
	 * this.universe.getViewingPlatform().getViewPlatformTransform();
	 * 
	 * //Add things to the universe this.root = new BranchGroup();
	 * this.root.addChild(new ColorCube(0.2));
	 * this.universe.addBranchGraph(root);
	 * 
	 * //Setting nominal setting for viewing
	 * universe.getViewingPlatform().setNominalViewingTransform(); }
	 * 
	 * private void init_score() { this.score_distance = 0; this.score_coins =
	 * 0; }
	 * 
	 * public void draw(Graphics2D g2d,int x_ori, int y_ori, MapController
	 * map_cont, boolean not_pause){ cube.drawCube(g2d,x_ori,y_ori); }
	 * 
	 * 
	 * public class Cube { int x, y, size, shift; Point[] cubeOnePoints; Point[]
	 * cubeTwoPoints; public Cube(int x, int y, int size, int shift) { this.x =
	 * x; this.y = y; this.size = size; this.shift = shift; cubeOnePoints =
	 * getCubeOnePoints(); cubeTwoPoints = getCubeTwoPoints(); }
	 * 
	 * private Point[] getCubeOnePoints() { Point[] points = new Point[4];
	 * points[0] = new Point(x, y); points[1] = new Point(x + size, y);
	 * points[2] = new Point(x + size, y + size); points[3] = new Point(x, y +
	 * size); return points; }
	 * 
	 * private Point[] getCubeTwoPoints() { int newX = x + shift; int newY = y +
	 * shift; Point[] points = new Point[4]; points[0] = new Point(newX, newY);
	 * points[1] = new Point(newX + size, newY); points[2] = new Point(newX +
	 * size, newY + size); points[3] = new Point(newX, newY + size); return
	 * points; }
	 * 
	 * public void drawCube(Graphics g,int x_ori, int y_ori) {
	 * g.drawRect(x_ori+x, y_ori+y, size, size); g.drawRect(x_ori+x + shift,
	 * y_ori+y + shift, size, size); // draw connecting lines for (int i = 0; i
	 * < 4; i++) { g.drawLine(x_ori+cubeOnePoints[i].x,
	 * y_ori+cubeOnePoints[i].y, x_ori+cubeTwoPoints[i].x,
	 * y_ori+cubeTwoPoints[i].y); } } }
	 */
