package character;

import java.awt.Graphics2D;

import graphic.Sprite;
import map.MapController;
import map.MapObject;
import utils.Constants;
import videogame.GameObject;

public class Sphere extends GameObject implements Sprite{
	//Tipo de plataforma, depende del tipo usa un sprite u otro
	public static final int NORMAL = 0;
	public static final int JUMP = 1;
	public static final int SPEED_UP = 2;
	public static final int SPEED_DOWN = 3;
	//Posicion de la imagen a representar
	private static int[] x_imgs={2};
	private static int[] y_imgs={0};
	//Tamaño de la imagen
	private static int[] width_imgs={30};
	private static int[] height_imgs={30};
	//Estado del jugador
	private int type;
	//Logica
	private boolean jump = false;
	private int jumpVelocity = -15;
	private int maxX = 100;  

	public Sphere(int x, int y, int block_width,int block_height) {
		super(x,y,x_imgs[0],y_imgs[0], width_imgs[0], height_imgs[0], block_width, block_height);
		this.type = NORMAL;
		selectImage();
		resize();
	}
	
	private void selectImage() {
		//image = image.getSubimage(x_imgs[type], y_imgs[type], width, height);
		image = Constants.img_handler.getImagePlayer(x_img, y_img, width, height);
	}

	public void jump(){
		jump = true;
	}

	public void gravity(){
		//this.setVelocity(this.vx, 1);
		this.setVelocity(this.vx, this.vy+1);
	}

	public int checkCollision(MapController mc){
		//TODO Cambios de mapa
		int result = -1;
		boolean collisionInf = false;
		boolean collisionLat = false;
		boolean collisionSup = false;
		//Mapa actual
		MapObject map = mc.getCurrentMap();	
		//Calculo coordenadas respecto mapa
		int xMap = (this.x + this.width) / mc.getWidthBlock() + mc.getPos();
		//TODO apaño, Richard es malvado y carga el mapa con las "y" invertidas
		int yMap = Math.abs((this.y) / (mc.getHeightBlock()) - 9);
		//Colision inferior	
		System.out.println("-----------------------");
		mc.getCurrentMap().print();
		//System.out.printf("caja x: %d, y: %d",mc.getCurrentMap().getObject(0, 2).getX(), mc.getCurrentMap().getObject(0, 2).getY());
		//System.out.printf("posicion x: %d, y: %d\n", xMap, yMap);
		collisionInf = map.collision(xMap, yMap-1, this);
		/*GameObject o = mc.getCurrentMap().getObject(xMap, yMap-1);
		if(o != null){
			System.out.printf("caja x: %d, y: %d\n",o.getPositionX(), o.getPositionY());
			System.out.printf("posicion x: %d, y: %d\n", xMap, yMap-1);
		}*/
		if(collisionInf)System.out.printf("inferior x: %d, y: %d\n", xMap, yMap-1);
		if(!collisionInf){
			//Colision inferior der			
			collisionInf = map.collision(xMap+1, yMap-1, this);
			if(collisionInf)System.out.printf("inferior der x: %d, y: %d\n", xMap+1, yMap-1);
		}		
		//Colision lateral
		collisionLat = map.collision(xMap, yMap, this);
		if(!collisionLat)collisionLat = map.collision(xMap+1, yMap, this);
		if(collisionLat)System.out.printf("lateral x: %d, y: %d\n", xMap+1, yMap);
		//Colision superior
		collisionSup = map.collision(xMap, yMap+1, this);
		if(collisionSup)System.out.printf("superior x: %d, y: %d\n", xMap, yMap+1);
		/*o = mc.getCurrentMap().getObject(xMap, yMap+1);
		if(o != null){
			System.out.printf("caja sup x: %d, y: %d\n",o.getPositionX(), o.getPositionY());
			System.out.printf("posicion sup x: %d, y: %d\n", xMap, yMap+1);
		}	*/	
		if(!collisionSup){
			//Colision superior der
			collisionSup = map.collision(xMap+1, yMap+1, this);
			if(collisionSup)System.out.printf("superior der x: %d, y: %d\n", xMap+1, yMap+1);
		}
		System.out.println("-----------------------");
		//Prioridad de colisiones
		//TODO muy feo, mejorar identificacion de colisiones		
		if(collisionInf && collisionLat){
			result = 3;
			this.x = this.x- 1;
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
		}
		else if(collisionSup && collisionLat){
			result = 4;
			this.x = this.x - 1;
			this.y = (this.y / mc.getHeightBlock())*mc.getHeightBlock();
		}
		else if(collisionLat){
			result = 2;
			this.x = this.x - 1;			
		}
		else if(collisionInf && !collisionLat){
			result = 0;
			//Fix de posicion
			this.y = (this.y / mc.getHeightBlock() + 1)*mc.getHeightBlock()-this.height+1;
		}
		else if(collisionSup && !collisionLat){
			result = 1;
			this.y = (this.y / mc.getHeightBlock())*mc.getHeightBlock();
		}
		else{
			result = -1;
		}
		return result;
	}

	@Override
	public void move(){
		if(jump){
			jump = false;
			this.setVelocity(vx, jumpVelocity);
		}
		setPosition(x+vx, y+vy);
	}
	
	@Override
	public void draw2D(Graphics2D g2d) {
		//TODO acciones en la pelota, cambiar sprite al saltar, acelerar y frenar
		//TODO establecer posiciones por celdas en vez de pixeles
		g2d.drawImage(image, x, y, null);
		//Dibujo cada de colisiones
		g2d.draw(this.getBox());
	}

	public int getWidthImage(){
		return width_imgs[type];
	}

	public int getHeightImage(){
		return height_imgs[type];
	}
	
}
