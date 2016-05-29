package item;

import java.awt.Graphics2D;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.TextureLoader;

import graphic.Model3D;
import graphic.Sprite;
import utils.Constants;
import videogame.Game;
import videogame.GameObject;

public class Treasure extends GameObject implements Sprite, Model3D{
	//Tipos de tesoro
	public static final int COIN = 0;
	public static final int GEM = 1;
	//Posicion de cada imagen en su contenedor para cargarla
	private static int[] x_imgs={288,144};
	private static int[] y_imgs={360,362};
	//Tamaño de la imagen
	private static int[] width_imgs={70,70};
	private static int[] height_imgs={70,70};
	//Valor de los tesoros
	private int values[] = {1,10};
	//Tipo de tesoro
	private int type;
	//Color del objeto, ambiental y difusa 
	private Color3f coin_amb = new Color3f(0.5f, 0.5f, 0f);	
	private Color3f coin_dif = new Color3f(1f, 1f, 0f);
	private Color3f gem_amb = new Color3f(0.15f, 0.45f, 0.97f);
	private Color3f gem_dif = new Color3f(0.3f, 0.9f, 0.5f);


	public Treasure(int x, int y, int block_width, int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		this.type = type;
		this.kills = false;

		if(Constants.visualMode == Game.MODE_2D){
			selectImage();
			resize();
		}else{
			selectTexture();
			loadModel3D();
		}

		real_x_block = real_x_block + real_block_width/4;
		real_y_block = real_y_block + real_block_height/4;
		real_block_width = (int) (real_block_width/1.8);
		real_block_height = (int) (real_block_height/1.8);
	}

	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImageItem(x_img, y_img, width, height);
	}

	private void selectTexture() {
		texture = Constants.img_handler.getImageItem(x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type]);
	}

	private void loadModel3D(){
		//Apariencia del modelo
		Appearance app = new Appearance();
		Material mat = new Material();
		object_primitive = null;
		Transform3D t = new Transform3D();
		tg_model3D = new TransformGroup();
		tg_model3D.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg_model3D.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		branch_group = new BranchGroup();
		branch_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		branch_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		//Carga de textura
		TextureLoader  loader = new TextureLoader(texture);
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		app.setTexture(texture);
		switch (type) {
		case COIN:
			//Materiales de la moneda
			mat.setAmbientColor(coin_amb);
			mat.setDiffuseColor(coin_dif);
			mat.setShininess(1.0f);	 
			mat.setSpecularColor(new Color3f(0, 0, 0));
			app.setMaterial(mat);
			//Creacion de la moneda
			object_primitive = new Cylinder(block_width*0.0005f, block_width*0.001f, Cylinder.GENERATE_NORMALS + Cylinder.GENERATE_TEXTURE_COORDS, app);
			t.rotX(Math.PI/2);
			break;
		case GEM:
			//Materiales de la gema
			mat.setAmbientColor(gem_amb);
			mat.setDiffuseColor(gem_dif);
			mat.setShininess(1.0f);	 
			mat.setSpecularColor(new Color3f(0, 0, 0));
			app.setMaterial(mat);
			//Creacion de la gema
			object_primitive = new Box(block_width*0.0005f,block_width*0.0005f,block_width*0.0005f, Box.GENERATE_NORMALS + Box.GENERATE_TEXTURE_COORDS, app);
			//Rotaciones
			t.rotX(Math.PI/4);	
			Transform3D t1 = new Transform3D();
			t1.rotZ(Math.PI/4);
			t.mul(t1);
			break;
		default:
			break;
		}	 		    
		branch_group.addChild(object_primitive);
		tg_model3D.addChild(branch_group);
		tg_model3D.setTransform(t);
	}
	
	public void removeObject(){
		tg_model3D.removeChild(branch_group);
	}

	@Override
	public void draw2D(Graphics2D g2d, int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}

	public int getValue() {
		return values[type];
	}

	@Override
	public void draw3D() {
		// TODO Auto-generated method stub

	}

	@Override
	public TransformGroup get3DModel() {
		return tg_model3D;				
	}



}
