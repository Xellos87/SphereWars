package obstacle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;

import graphic.Model3D;
import graphic.Sprite;
import utils.Constants;
import videogame.GameObject;

public class Spike extends GameObject implements Sprite, Model3D{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int UPPER = 0;
	public static final int RIGHT = 1;
	public static final int LOWER = 2;	
	public static final int LEFT = 3;
	//Posición de los la imagen
	private static int x_img = 347;
	private static int y_img = 30;
	//Tamaño de la imagen
	private static int width = 70;
	private static int height = 40;
	//Direccion del pincho
	private int direction;
	//Color del objeto, ambiental y difusa
	private Color3f spike_amb = new Color3f(0.49f,0.49f,0.49f);
	private Color3f spike_dif = new Color3f(0.49f,0.49f,0.49f);

	public Spike(int x, int y,int block_width,int block_height, int direction) {
		super(x, y, x_img, y_img, width, height, block_width, block_height);
		this.direction = direction;
		this.kills = true;
		selectImage();
		resize();
		rotateImage();
	}

	private void selectImage() {
		//Carga solo el fragmento que necesita la imagen
		//image = image.getSubimage(xImg, yImg, width, height);
		image = Constants.img_handler.getImageItem(x_img, y_img, width, height);
	}

	private void rotateImage(){
		if(direction != UPPER){
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			Graphics2D g2 = newImage.createGraphics();
			g2.rotate(direction*Math.PI/2, w/2, h/2);  
			g2.drawImage(image,null,0,0);
			image = newImage;
			//Calcula las posiciones relativas de la imagen para las colisiones
			int aux;
			switch (direction){
			case LOWER:
				real_y_block = 0;
				break;
			case LEFT:
				aux = real_block_height;
				real_block_height = real_block_width;
				real_block_width = aux;
				real_y_block = 0;
				real_x_block = block_width - real_block_width;
				break;
			case RIGHT:
				aux = real_block_height;
				real_block_height = real_block_width;
				real_block_width = aux;
				real_y_block = 0;
				real_x_block = 0;
				break;
			}
		}
	}

	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		g2d.drawImage(image, x_ori+x, y_ori+y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox(x_ori,y_ori));
	}


	public int getWidthImage(){
		return width;
	}

	public int getHeightImage(){
		return height;
	}

	@Override
	public void draw3D() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TransformGroup get3DModel() {
		//Apariencia de la plataforma
		Appearance app = new Appearance();
		Material mat = new Material();
		mat.setAmbientColor(spike_amb);
		mat.setDiffuseColor(spike_dif);
		mat.setSpecularColor(new Color3f(0, 0, 0));
		mat.setShininess(5.0f);	 
		app.setMaterial(mat);
		//Creacion de la plataforma
		Transform3D t = new Transform3D();
				
		TransformGroup tg = new TransformGroup();		
		Cone cone = new Cone(.02f, .05f, Cone.GENERATE_NORMALS, app);			
		tg.addChild(cone);		
		Cone cone1 = new Cone(.02f, .05f, Cone.GENERATE_NORMALS, app);
		Vector3f vector = new Vector3f(.03f,0f,0f);
		t.setTranslation(vector);
		TransformGroup tg1 = new TransformGroup();
		tg1.setTransform(t);
		tg1.addChild(cone1);
		Cone cone2 = new Cone(.02f, .05f, Cone.GENERATE_NORMALS, app);
		vector = new Vector3f(-.03f,0f,0f);
		t.setTranslation(vector);
		TransformGroup tg2 = new TransformGroup();
		tg2.setTransform(t);
		tg2.addChild(cone2);
	    
		tg.addChild(tg1);
		tg.addChild(tg2);		
		
		//Rotacion 
		switch (direction) {
		case UPPER:
			t.rotX(0);
			vector = new Vector3f(.0f,0f,0f);
			break;
		case RIGHT:
			t.rotZ(-Math.PI/2);	
			vector = new Vector3f(-.03f,0f,0f);
			break;
		case LOWER:
			t.rotX(Math.PI);
			vector = new Vector3f(.0f,0f,0f);
			break;
		case LEFT:
			t.rotZ(Math.PI/2);	
			vector = new Vector3f(.03f,0f,0f);
			break;
		default:
			break;
		}		
		tg.setTransform(t);
		t.setTranslation(vector);
		tg.setTransform(t);
		
		return tg;
	}
}
