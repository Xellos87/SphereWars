package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;

import audio.Audio;
import audio.AudioClip;
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
 * Clase: Bot.java
 * 
 * Comentarios: Objeto que representa un bot en pantalla
 * 
 */
public class Bot extends GameObject implements Sprite, Model3D{
	//Tipo de enemigo
	public static final int SLIME = 0;
	public static final int TRIANGLE = 3;
	//Estados del enemigo
	private final int WALK1 = 0;
	private final int WALK2 = 1;
	private final int DEAD = 2;
	//Dirección del bot
	private final int RIGHT = 0;
	private final int LEFT = 1;
	//Posicion de la imagen
	private static int[] x_imgs = {52,0,0};
	private static int[] y_imgs = {125,125,112};
	//Tamaño de la imagen
	private static int[] width_imgs = {50,51,59};
	private static int[] height_imgs = {28,26,12};
	//Propiedades del bot
	private int type;
	private int state;
	private int last_state;
	private int direction;
	//Variables relacionadas con la animación del movimiento en pantalla
	private int tick_counter;
	private int max_counter = 10 * Constants.speedActions;
	private int acumulate_mov = 0;
	//Parpadeo al morir
	private int num_blink;
	private int max_blink;
	private boolean visible;
	//Variables relacionadas con el cambio de dirección ante un camino imposible
	private int tick_change;
	private int max_wait_change = 10 * Constants.speedActions;
	//Sonido de muerte del bot
	private AudioClip deathSound;
	

	/**
	 * Constructor de la clase
	 * @param x
	 * @param y
	 * @param block_width
	 * @param block_height
	 * @param type
	 */
	public Bot(int x, int y, int block_width, int block_height, int type) {
		super(x, y, x_imgs[type], y_imgs[type], width_imgs[type], height_imgs[type], block_width, block_height);
		this.tick_counter = 0;
		this.tick_change = 0;
		this.acumulate_mov = 0;
		this.type = type;
		this.state = WALK1;
		this.direction = RIGHT;
		this.kills = true;
		this.visible = true;
		deathSound = Audio.Load("audioEffects/mutantdie.wav");
		selectImage();
		resize();
		rotateImage();
		if(Constants.visualMode == Game.MODE_3D){
			selectTexture();
			loadModel3D();
		}		
	}

	/**
	 * Rota la imagen dependiendo de si el bot mira a un lado u otro
	 */
	private void rotateImage(){
		if(direction == LEFT){
			//Si la dirección es la izquierda, rota la imagen como si fuera un espejo
			int w = image.getWidth();  
			int h = image.getHeight();  
			BufferedImage newImage = new BufferedImage(w, h, image.getType());
			Graphics2D g2 = newImage.createGraphics();  
			g2.drawImage(image,w, 0, -w, h,null);
			image = newImage;
		}
	}

	/**
	 * Selecciona la imagen que ha de ser escogida en el spritesheet
	 */
	private void selectImage() {
		image = Constants.img_handler.getImageEnemie(x_img, y_img, width, height);
	}
	
	/**
	 * Selecciona la textura para el modo 3D
	 */
	private void selectTexture(){
		texture = Constants.img_handler.getImageSlimeTexture();
	}

	/**
	 * Dibuja el bot en 2D
	 */
	@Override
	public void draw2D(Graphics2D g2d,int x_ori, int y_ori) {
		if(visible){
			g2d.drawImage(image, x_ori+x, y_ori+y, null);
			//Dibujo cada de colisiones
			//g2d.draw(this.getBox(x_ori,y_ori));
		}
	}

	/**
	 * Calcula la logica de el bot, comprobando si tiene algo delante para
	 * darse la vuelta en 2D
	 * @param not_pause
	 * @return
	 */
	public int action(boolean not_pause) {
		if(not_pause && state != DEAD){
			tick_counter++;
			int mov = 0;
			// Cada max_counter frames calculamos la logica
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				mov = width / 15;
				if(state == WALK2){
					state = WALK1;
				}else{
					state = WALK2;
				}
				x_img = x_imgs[type+state];
				y_img = y_imgs[type+state];
				width = width_imgs[type+state];
				height = height_imgs[type+state];
				// Se actualiza la posicion y rota la imagen de ser necesario
				updatePosition();
				selectImage();
				resize();
				rotateImage();
			}
			// Cuando la babosa cambia de direccion invertimos el movimiento
			if(direction == RIGHT){
				mov = -mov;
			}
			
			acumulate_mov = acumulate_mov + mov;
		}else if(num_blink < max_blink && not_pause && state == DEAD){
			// Timer en el que se va haciendo que la babosa parpadee
			// como animacion de muerte
			tick_counter++;
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				if(visible){
					visible = false;
					num_blink++;
				}else{
					visible = true;
				}
			}
		}
		return acumulate_mov;
	}

	/**
	 * Mueve el bot y realiza la animacion de muerte en 3D
	 * @return
	 */
	public boolean moveBot(){
		boolean remove = false;
		// Si no esta muerta actualiza la posicion del bot
		if(state != DEAD){
			tick_counter++;
			int movX = 0;
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				if(state == WALK2){
					state = WALK1;
				}else{
					state = WALK2;
				}
				movX = width / 20;
			}
			if(direction == RIGHT){
				movX = -movX;
			}
			acumulate_mov = acumulate_mov + movX;
			//Transformación del objeto
			Transform3D transform = new Transform3D();
			Vector3f translate_vector = new Vector3f();
			tg_model3D.getTransform(transform);
			transform.get(translate_vector);
			translate_vector.x += (movX*0.002f);
			transform.set(translate_vector);
			Matrix4f matrix = new Matrix4f();
			transform.get(matrix);
			matrix.m13 = -0.02f;
			matrix.m11 = 1;
			if(state == WALK2){
				matrix.m13 -= 0.005f;
				matrix.m11 = 0.7f;
			}
			transform.set(matrix);
			tg_model3D.setTransform(transform);
		}else if(tick_counter == 0 && num_blink == 0 && state == DEAD){
			tick_counter++;
			//Transformación en la que el bot se encoge
			Transform3D transform = new Transform3D();
			tg_model3D.getTransform(transform);
			Matrix4f matrix = new Matrix4f();
			transform.get(matrix);
			matrix.m13 = -0.03f;
			if(last_state == WALK1){
				matrix.m11 = 0.3f;
			}else{
				matrix.m11 = 0.2f;
			}
			transform.set(matrix);
			tg_model3D.setTransform(transform);
		}else if(num_blink < max_blink && state == DEAD){
			//Animación de muerte
			tick_counter++;
			if(tick_counter >= max_counter){
				tick_counter -= max_counter;
				if(visible){
					visible = false;
					num_blink++;
					//Hacemos que desaparezca ocultandola bajo el bloque
					Transform3D transform = new Transform3D();
					Vector3f translate_vector = new Vector3f();
					tg_model3D.getTransform(transform);
					transform.get(translate_vector);
					translate_vector.y -= 0.04f;
					transform.set(translate_vector);
					tg_model3D.setTransform(transform);
				}else{
					visible = true;
					//Hacemos que se vea de nuevo
					Transform3D transform = new Transform3D();
					Vector3f translate_vector = new Vector3f();
					tg_model3D.getTransform(transform);
					transform.get(translate_vector);
					translate_vector.y += 0.04f;
					transform.set(translate_vector);
					tg_model3D.setTransform(transform);
				}
			}
		}else if(state == DEAD && num_blink >= max_blink){
			remove = true;
		}
		return remove;
	}
	
	/**
	 * Actualiza la posicion del bot
	 * @return
	 */
	public int updatePosition() {
		int posX = 0;
		if(acumulate_mov > 0 && acumulate_mov > width){
			//Pasa a la casilla de la derecha
			posX = 1;
			acumulate_mov -=width;
		}else if(acumulate_mov < 0 && acumulate_mov < -width){
			//Pasa a la casilla de la izquierda
			posX = -1;
			acumulate_mov +=width;
		}
		return posX;
	}

	/**
	 * Reinicia el movimiento del bot dependiendo de la direccion en la que mira
	 */
	public void resetMov() {
		if(direction == RIGHT){
			acumulate_mov += block_width;
		}else{
			acumulate_mov -= block_width;
		}
	}

	/**
	 * Cambia la direccion de la babosa
	 */
	public void changeMov() {
		tick_change++;
		if(tick_change >= max_wait_change){
			tick_change = 0;
			acumulate_mov = 0;
			if(direction == RIGHT){
				direction = LEFT;
			}else if(direction == LEFT){
				direction = RIGHT;
			}
			selectImage();
			resize();
			rotateImage();
		}
	}
	
	/**
	 * Mata a la babosa
	 */
	public void death(){
		this.kills = false;
		this.last_state = state;
		this.state = DEAD;
		this.tick_counter = 0;
		this.max_counter = 5;
		this.num_blink = 0;
		this.max_blink = 5;
		this.visible = true;
		x_img = x_imgs[type+state];
		y_img = y_imgs[type+state];
		width = width_imgs[type+state];
		height = height_imgs[type+state];
		selectImage();
		resize();
		if(deathSound != null){
			deathSound.start();
		}
	}
	
	/**
	 * Carga el modelo 3D del bot
	 */
	private void loadModel3D(){
		Transform3D t = new Transform3D();
		//Apariencia del bot
		Appearance app = new Appearance();
		//Material del bot
	    Material mat = new Material();
	    mat.setAmbientColor(new Color3f(0.5f,0f,0.25f));
		mat.setDiffuseColor(new Color3f(1f,0f,0.5f));
		mat.setSpecularColor(Constants.black);
		mat.setShininess(1.0f);	 
	    app.setMaterial(mat);	    
	    //Carga de textura
	    TextureLoader  loader = new TextureLoader(texture);
	    Texture texture = loader.getTexture();
	    app.setTexture(texture);
	    //Atributos de textura
	    TextureAttributes texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        app.setTextureAttributes(texAttr);	
	    //Creacion del bot
	    object_primitive = new Cylinder(block_width*0.0007f, block_width*0.001f, Cylinder.GENERATE_NORMALS + Cylinder.GENERATE_TEXTURE_COORDS, app);
	    t.setTranslation(new Vector3f(0f,-block_width*0.0005f,0f));
	    //t.rotX(Math.PI/2);
		tg_model3D = new TransformGroup();
		tg_model3D.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tg_model3D.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tg_model3D.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		branch_group = new BranchGroup();
		branch_group.addChild(object_primitive);
		branch_group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		branch_group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		branch_group.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		
		tg_model3D.addChild(branch_group);
		tg_model3D.setTransform(t);
	}

	/**
	 * Elimina el modelo 3D para que no sea renderizado
	 */
	public void remove() {
		tg_model3D.removeChild(branch_group);
	}
}
