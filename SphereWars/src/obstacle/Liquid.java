package obstacle;

import java.awt.Graphics2D;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.pisces.Surface;

import graphic.Model3D;
import graphic.Sprite;
import utils.Constants;
import videogame.Game;
import videogame.GameObject;

public class Liquid extends GameObject implements Sprite, Model3D{
	//Estilo del liquido
	public static final int WATER = 0;
	public static final int MAGMA = 2;
	//
	public static final int SURFACE = 0;
	public static final int DEEP = 1;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={432,504,432,504};
	private static int[] y_imgs={601,216,817,0};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70,70,70};
	private static int[] height_imgs={45,70,45,70};
	//Tipo de elemento
	private int type;
	private int nature;
	//Color del objeto, ambiental y difusa
	private Color3f water_amb = new Color3f(0,0,0.5f);
	private Color3f water_dif = new Color3f(0,0,1);
	private Color3f magma_amb = new Color3f(0.5f,0,0);
	private Color3f magma_dif = new Color3f(1,0,0);

	public Liquid(int x, int y, int block_width, int block_height, int type, int nature) {
		super(x, y, x_imgs[nature+type], y_imgs[nature+type], width_imgs[nature+type], height_imgs[nature+type], block_width, block_height);
		this.type = type;
		this.nature = nature;
		this.kills = true;
		if(Constants.visualMode == Game.MODE_2D){
			selectImage();
			resize();
		}else{
			selectTexture();
		}
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}
	
	private void selectTexture() {
		texture = Constants.img_handler.getImageTile(x_imgs[nature+1], y_imgs[nature+1], width_imgs[nature+1], height_imgs[nature+1]);
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
	}

	public int getWidthImage(){
		return width_imgs[nature+type];
	}

	public int getHeightImage(){
		return height_imgs[nature+type];
	}

	@Override
	public void draw3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransformGroup get3DModel() {
		Transform3D t = new Transform3D();
		//Apariencia del liquido
		Appearance app = new Appearance();
	    Material mat = new Material();
	    switch (nature) {
		case WATER:
			mat.setAmbientColor(water_amb);
			mat.setDiffuseColor(water_dif);
			break;
		case MAGMA:
			mat.setAmbientColor(magma_amb);
			mat.setDiffuseColor(magma_dif);
			break;
		default:
			break;
		}	    
	    mat.setSpecularColor(new Color3f(0, 0, 0));
	    mat.setShininess(5.0f);	 
	    app.setMaterial(mat);
	    //Creacion del liquido
	    Box box = null;
	    
	    //Carga de textura
	    TextureLoader  loader = new TextureLoader(texture);
	    Texture texture = loader.getTexture();
	    app.setTexture(texture);
	    
	    switch (type) {
		case SURFACE:
			box = new Box(block_width*0.001f,block_width*0.0005f,block_width*0.001f, Box.GENERATE_NORMALS + Box.GENERATE_TEXTURE_COORDS, app);			
			t.setTranslation(new Vector3f(0f,-block_width*0.0005f,0f));
			break;
		case DEEP:
			box = new Box(block_width*0.001f,block_width*0.001f,block_width*0.001f, Box.GENERATE_NORMALS + Box.GENERATE_TEXTURE_COORDS, app);
			break;
		default:
			break;
		}		 
		TransformGroup tg = new TransformGroup();
		tg.addChild(box);
		tg.setTransform(t);
		return tg;
	}

}
