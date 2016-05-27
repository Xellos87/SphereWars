package obstacle;

import java.awt.Graphics2D;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Box;

import graphic.Model3D;
import graphic.Sprite;
import utils.Constants;
import videogame.Game;
import videogame.GameObject;

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
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={504,504,504,576,648,648,288,288,288,576,360,360,792,792,792,504,288,288,144,144,144,720,288,288};
	private static int[] y_imgs={576,648,504,864,0,0,576,648,504,864,792,792,144,216,72,288,792,792,792,864,720,864,144,144};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	private static int[] height_imgs={70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
	//Tipo de imagen a cargar, identifica los datos de la parte de imagen que lo representa
	private int type;
	private int world;
	//Contenedor del modelo3D
	private Box box;
	//Color del objeto, ambiental y difusa
	private Color3f field_amb = new Color3f(0.34f,0.15f,0.0f);
	private Color3f field_dif = new Color3f(0.67f, 0.34f, 0.047f);
	private Color3f dessert_amb = new Color3f(0.9f, 0.87f, 0.75f);
	private Color3f dessert_dif = new Color3f(0.9f, 0.87f, 0.75f);
	private Color3f castle_amb = new Color3f(0.67f, 0.57f, 0.75f);
	private Color3f castle_dif = new Color3f(0.67f, 0.57f, 0.75f);
	private Color3f snow_amb = new Color3f(1, 1, 1);
	private Color3f snow_dif = new Color3f(1, 1, 1);
	

	public Platform(int x, int y, int block_width,int block_height, int type, int world) {
		super(x, y, x_imgs[world+type], y_imgs[world+type], width_imgs[world+type], height_imgs[world+type], block_width, block_height);
		//System.out.printf("Agregado en x:%d, y:%d\n", x,y);
		this.type = type;
		this.world = world;
		this.kills = false;
		
		if(Constants.visualMode == Game.MODE_2D){
			selectImage();
			resize();
		}/*else{
			Appearance app = new Appearance();
			Material mat = new Material();
			mat.setAmbientColor(earth_amb);
			mat.setDiffuseColor(earth_dif);
			app.setMaterial(mat);
			box = new Box(block_width, block_height, block_width, app);
		}*/
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}

	public int getWidthImage(){
		return width_imgs[world+type];
	}

	public int getHeightImage(){
		return height_imgs[world+type];
	}

	@Override
	public void draw3D() {
		
	}

	@Override
	public TransformGroup get3DModel() {
		//Apariencia de la plataforma
		Appearance app = new Appearance();
	    Material mat = new Material();
	    switch (world) {
		case WORLD_FIELD:
			mat.setAmbientColor(field_amb);
			mat.setDiffuseColor(field_dif);
			break;
		case WORLD_DESSERT:
			mat.setAmbientColor(dessert_amb);
			mat.setDiffuseColor(dessert_dif);
			mat.setDiffuseColor(new Color3f(0.9f, 0.87f, 0.75f));
			break;
		case WORLD_CASTLE:
			mat.setAmbientColor(castle_amb);
			mat.setDiffuseColor(castle_dif);
			mat.setDiffuseColor(new Color3f(0.67f, 0.57f, 0.75f));
			break;
		case WORLD_SNOW:
			mat.setAmbientColor(snow_amb);
			mat.setDiffuseColor(snow_dif);
			mat.setDiffuseColor(new Color3f(1, 1, 1));
			break;
		default:
			break;
		}		    
	    mat.setSpecularColor(new Color3f(0, 0, 0));
	    mat.setShininess(1.0f);	 
	    app.setMaterial(mat);
	    //Creacion de la plataforma
		Box box = new Box(0.1f, 0.1f, 0.1f, app);		
		TransformGroup tg = new TransformGroup();
		tg.addChild(box);
		return tg;
	}

}
