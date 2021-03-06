package obstacle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix4f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;

import graphic.Model3D;
import graphic.Sprite;
import utils.Constants;
import videogame.Game;
import videogame.GameObject;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Platform.java
 * 
 * Comentarios: Objeto del videojuego que representa una plataforma
 * 
 */
public class Platform extends GameObject implements Sprite, Model3D{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int GROUND = 0;
	public static final int BORDER_LEFT = 1;
	public static final int BORDER_RIGHT = 2;
	public static final int UNDERGROUND = 3;
	public static final int BORDER_BOTH = 4;
	public static final int ALONE_BLOCK = 5;
	//Estilo del mundo
	public static final int WORLD_FIELD = 0;
	public static final int WORLD_DESSERT = 6;
	public static final int WORLD_CASTLE = 12;
	public static final int WORLD_SNOW = 18;
	public static final int WORLD_GHOST = 24;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={504,504,504,576,648,648,288,288,288,576,360,360,792,792,792,504,288,288,144,144,144,720,288,288,72,72,72,144,144,144};
	private static int[] y_imgs={576,648,504,864,0,0,576,648,504,864,792,792,144,216,72,288,792,792,792,864,720,864,144,144,432,504,360,576,648,648};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	private static int[] height_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	//Tipo de imagen a cargar, identifica los datos de la parte de imagen que lo representa
	private int type;
	private int world;
	//Contenedor del modelo3D
	private Box box;
	private BufferedImage base;
	private BufferedImage top;
	//Color del objeto, ambiental y difusa
	private Color3f field_amb = new Color3f(0.34f,0.15f,0.0f);
	private Color3f field_dif = new Color3f(0.67f, 0.34f, 0.047f);
	private Color3f dessert_amb = new Color3f(0.45f, 0.4f, 0.35f);
	private Color3f dessert_dif = new Color3f(0.9f, 0.87f, 0.75f);
	private Color3f castle_amb = new Color3f(0.35f, 0.25f, 0.35f);
	private Color3f castle_dif = new Color3f(0.67f, 0.57f, 0.75f);
	private Color3f snow_amb = new Color3f(0.5f, 0.5f, 0.5f);
	private Color3f snow_dif = new Color3f(1, 1, 1);
	

	/**
	 * Constructor de la clase
	 * @param x 
	 * @param y
	 * @param block_width
	 * @param block_height
	 * @param type
	 * @param world
	 */
	public Platform(int x, int y, int block_width,int block_height, int type, int world) {
		super(x, y, x_imgs[world+type], y_imgs[world+type], width_imgs[world+type], height_imgs[world+type], block_width, block_height);
		//System.out.printf("Agregado en x:%d, y:%d\n", x,y);
		this.type = type;
		this.world = world;
		this.kills = false;
		
		selectImage();
		resize();
		if(Constants.visualMode == Game.MODE_3D){
			if(type != UNDERGROUND){
				this.type = GROUND;
			}
			selectTexture();
			loadModel3D();
		}
	}

	/**
	 * Selecciona la imagen que ha de ser escogida en el spritesheet
	 */
	private void selectImage() {
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}
	
	/**
	 * Selecciona la textura para el modo 3D
	 */
	private void selectTexture() {
		texture = Constants.img_handler.getImageTile(x_imgs[world+type], y_imgs[world+type], width_imgs[world+type], height_imgs[world+type]);
		base = Constants.img_handler.getImageTile(x_imgs[world+UNDERGROUND], y_imgs[world+UNDERGROUND], width_imgs[world+UNDERGROUND], height_imgs[world+UNDERGROUND]);
		switch (world) {
		case WORLD_FIELD:
			top = Constants.img_handler.getImageTopField();
			break;
		case WORLD_DESSERT:
			top = Constants.img_handler.getImageTopDessert();
			break;
		case WORLD_CASTLE:
			top = Constants.img_handler.getImageTopCastle();
			break;
		case WORLD_SNOW:
			top = Constants.img_handler.getImageTopSnow();
			break;
		case WORLD_GHOST:
			top = Constants.img_handler.getImageTopGhost();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Carga el modelo 3D de la plataforma
	 */
	private void loadModel3D(){
		//Apariencia de la plataforma
		Appearance app = new Appearance();
		Appearance appTop = new Appearance();
		Appearance appBottom = new Appearance();
		//Material de la plataforma
	    Material mat = new Material();
	    mat.setAmbientColor(Constants.white);
		mat.setDiffuseColor(Constants.white);
		mat.setSpecularColor(Constants.black);
		mat.setShininess(1.0f);	 
	    app.setMaterial(mat);
	    //Carga de textura
	    TextureLoader  loader = new TextureLoader(this.texture);
	    Texture texture = loader.getTexture();
	    app.setTexture(texture);
	    TextureLoader loaderBottom = new TextureLoader(this.base);
	    Texture textureBottom = loaderBottom.getTexture();
	    appBottom.setTexture(textureBottom);
	    TextureLoader loaderTop = new TextureLoader(this.top);
	    Texture textureTop = loaderTop.getTexture();
	    appTop.setTexture(textureTop);
	    //Atributos de textura
	    TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);	
        appBottom.setTextureAttributes(texAttr);
        appTop.setTextureAttributes(texAttr);
	    //Creacion de la plataforma
        object_primitive = new Box(block_width*0.001f, block_height*0.001f, block_width*0.001f, Box.GENERATE_NORMALS + Box.GENERATE_TEXTURE_COORDS, app);		
		object_primitive.setAppearance(Box.TOP, appTop);
		object_primitive.setAppearance(Box.BOTTOM, appBottom);
		tg_model3D = new TransformGroup();
		Transform3D transform = new Transform3D();
		Matrix4f matrix = new Matrix4f();
		tg_model3D.getTransform(transform);
		tg_model3D.setTransform(transform);
		branch_group = new BranchGroup();
		branch_group.addChild(object_primitive);
		tg_model3D.addChild(branch_group);
	}

	/**
	 * Dibuja la imagen en 2D
	 */
	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
	}

	/**
	 * Devuelve el ancho de la imagen
	 * @return
	 */
	public int getWidthImage(){
		return width_imgs[world+type];
	}

	/**
	 * Devuelve la altura de la imagen
	 * @return
	 */
	public int getHeightImage(){
		return height_imgs[world+type];
	}

	/**
	 * Devuelve el un tipo de mundo para seleccionar los sprites
	 * @return
	 */
	public static int getWorld() {
		int[] world = {WORLD_FIELD,WORLD_DESSERT,WORLD_CASTLE,WORLD_SNOW,WORLD_GHOST};
		Random rnd = new Random(System.currentTimeMillis());
		int index = rnd.nextInt(world.length);
		return world[index];
	}

	/**
	 * Devuelve el tipo de liquito para un mundo pasado por parametro
	 * @param world
	 * @return
	 */
	public static int getLiquid(int world) {
		int liquid = 0;
		switch(world){
		case WORLD_FIELD:
		case WORLD_DESSERT:
		case WORLD_SNOW:
			liquid = Liquid.WATER;
			break;
		case WORLD_CASTLE:
		case WORLD_GHOST:
			liquid = Liquid.MAGMA;
			break;
		}
		return liquid;
	}
}
