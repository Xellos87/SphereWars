package videogame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

import character.Sphere;
import map.MapController;

@SuppressWarnings("serial")
public class Game3D extends JPanel {
	private int width,height;
	//Contenedor del jugador
	private Sphere player;
	//Flag que indica si la partida ha acabado
	private boolean end_game;
	//Valor de alfa para oscurecimiento de pantalla por muerte y valor maximo
	private int alpha_death;
	private final int MAX_ALPHA = 100;
	private final String DEATH_STR = "Has muerto...";
	//Indica si se ha de esperar a la muerte de otro jugador
	private boolean wait_other_player;
	//Puntuaciones para mostrar en scoreboard
	private double score_distance;
	private int score_coins;
	private int score_time; //TODO, eliminar si no se implementa
	//Pruebas 3D
	private GraphicsConfiguration config  = null;
	private Canvas3D canvas 			  = null;
	private SimpleUniverse universe 	  = null;
	private BranchGroup root			  = null;
	private TransformGroup camera		  = null;

	private Cube cube;

	public Game3D(int width, int height, int num_players){
		this.width = width;
		this.height = height;
		setDoubleBuffered(false);
		setPreferredSize(new Dimension(width, height));
		setFocusable(true);
		requestFocus();
		cube = new Cube(75, 75, 200, 50);
		this.wait_other_player = num_players > 1;

		init_component();

		init_score();
	}

	private void init_component() {
		System.loadLibrary("jawt");
		
		this.setLayout(new BorderLayout());

		//Set up the canvas
		this.config = SimpleUniverse.getPreferredConfiguration();
		this.canvas = new Canvas3D(config);

		//Add the this as MouseMotionListener
		//this.canvas.addMouseMotionListener(this);
		//this.canvas.addKeyListener(this);

		//Pass the canvas to the constructor of the Universe
		this.universe = new SimpleUniverse(canvas);

		//Add the canvas to the applet
		this.add("Center", this.canvas);

		//Get TransformGroup that hold the Camera 
		this.camera = this.universe.getViewingPlatform().getViewPlatformTransform();

		//Add things to the universe
		this.root = new BranchGroup();
		this.root.addChild(new ColorCube(0.2));
		this.universe.addBranchGraph(root);

		//Setting nominal setting for viewing
		universe.getViewingPlatform().setNominalViewingTransform();
	}

	private void init_score() {
		this.score_distance = 0;
		this.score_coins = 0;
	}

	public void draw(Graphics2D g2d,int x_ori, int y_ori, MapController map_cont, boolean not_pause){
		cube.drawCube(g2d,x_ori,y_ori);
	}


	public class Cube {
		int x, y, size, shift;
		Point[] cubeOnePoints;
		Point[] cubeTwoPoints;
		public Cube(int x, int y, int size, int shift) {
			this.x = x;
			this.y = y;
			this.size = size;
			this.shift = shift;
			cubeOnePoints = getCubeOnePoints();
			cubeTwoPoints = getCubeTwoPoints();
		}

		private Point[] getCubeOnePoints() {
			Point[] points = new Point[4];
			points[0] = new Point(x, y);
			points[1] = new Point(x + size, y);
			points[2] = new Point(x + size, y + size);
			points[3] = new Point(x, y + size);
			return points;
		}

		private Point[] getCubeTwoPoints() {
			int newX = x + shift;
			int newY = y + shift;
			Point[] points = new Point[4];
			points[0] = new Point(newX, newY);
			points[1] = new Point(newX + size, newY);
			points[2] = new Point(newX + size, newY + size);
			points[3] = new Point(newX, newY + size);
			return points;
		}

		public void drawCube(Graphics g,int x_ori, int y_ori) {
			g.drawRect(x_ori+x, y_ori+y, size, size);
			g.drawRect(x_ori+x + shift, y_ori+y + shift, size, size);
			// draw connecting lines
			for (int i = 0; i < 4; i++) {
				g.drawLine(x_ori+cubeOnePoints[i].x, y_ori+cubeOnePoints[i].y, 
						x_ori+cubeTwoPoints[i].x, y_ori+cubeTwoPoints[i].y);
			}
		}
	}
}

