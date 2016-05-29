package obstacle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

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
	private BufferedImage base;
	//Color del objeto, ambiental y difusa
	private Color3f field_amb = new Color3f(0.34f,0.15f,0.0f);
	private Color3f field_dif = new Color3f(0.67f, 0.34f, 0.047f);
	private Color3f dessert_amb = new Color3f(0.45f, 0.4f, 0.35f);
	private Color3f dessert_dif = new Color3f(0.9f, 0.87f, 0.75f);
	private Color3f castle_amb = new Color3f(0.35f, 0.25f, 0.35f);
	private Color3f castle_dif = new Color3f(0.67f, 0.57f, 0.75f);
	private Color3f snow_amb = new Color3f(0.5f, 0.5f, 0.5f);
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
		}else{
			if(type != UNDERGROUND){
				this.type = GROUND;
			}
			selectTexture();
			loadModel3D();
		}
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageTile(x_img, y_img, width, height);
	}
	
	private void selectTexture() {
		texture = Constants.img_handler.getImageTile(x_imgs[world+type], y_imgs[world+type], width_imgs[world+type], height_imgs[world+type]);
		base = Constants.img_handler.getImageTile(x_imgs[world+UNDERGROUND], y_imgs[world+UNDERGROUND], width_imgs[world+UNDERGROUND], height_imgs[world+UNDERGROUND]);

	}
	
	private void loadModel3D(){
		//Apariencia de la plataforma
		Appearance app = new Appearance();
		Appearance appBase = new Appearance();
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
	    TextureLoader loaderBase = new TextureLoader(this.base);
	    Texture textureBase = loaderBase.getTexture();
	    appBase.setTexture(textureBase);
	    //Atributos de textura
	    TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);	
        appBase.setTextureAttributes(texAttr);        
	    //Creacion de la plataforma
        object_primitive = new Box(block_width*0.001f, block_height*0.001f, block_width*0.001f, Box.GENERATE_NORMALS + Box.GENERATE_TEXTURE_COORDS, app);		
		object_primitive.setAppearance(Box.TOP, appBase);
		object_primitive.setAppearance(Box.BOTTOM, appBase);
		tg_model3D = new TransformGroup();
		branch_group = new BranchGroup();
		branch_group.addChild(object_primitive);
		tg_model3D.addChild(branch_group);
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
}
