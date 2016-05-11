package videogame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Parallax.java
 * 
 * Comentarios: Realiza el efecto parallax, mueve varias capas de fondo a diferentes velocidades para dar
 * 				sensación de profundidad, las primeros elementos son dibujados al fondo, y los datos pasados
 * 				en el constructor son para cada imagen de fondo.
 * 
 */
public class Parallax {
	/* Datos de imagen*/
	private BufferedImage[] back_images;
	private int[] velocity;
	private int[] posX;
	private int[] posY;
	private int numImages;
	/* Tamaño del lienzo */
	private int width;
	private int height;
	/* Componentes del refresco */
	private int[] tick_counter;
	/* Datos de posicion de la imagen */
	private int[] fromX;
	private int[] fromX_next;
	private int[] width_image;
	private int[] width_image_next;
	private boolean[] show_next;
	
	/**
	 * Constructor, establece los datos iniciales para construir el efecto parallax, cada dato i-esimo esta asociado entre si
	 * 
	 * @param numImages, numero de imagenes a las que se aplica el efecto, todos los array deben ser de este tamaño
	 * @param images, ruta de las imagenes
	 * @param velocity, velocidad asociada a cada imagen, valores mayores de 1, a mayor valor menor desplazamiento
	 * @param posX, posicion inicial del eje X de cada imagen
	 * @param posY, posicion inicial del eje Y de cada imagen
	 * @param width, ancho de la pantalla en la que se muestran
	 * @param height, alto de la pantalla en la que se muestran
	 */
	public Parallax(int numImages, String[] images, int[] velocity, int[] posX, int[] posY, int width, int height){
		this.velocity = velocity;
		this.numImages = numImages;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		loadImages(images,numImages);
	}
	
	/**
	 * Carga las imagenes del array de nombres a los contenedores de imagenes, e inicializa todo
	 * 
	 * @param images, array con la ruta de acceso a las imagenes
	 * @param numImages, numero de imagenes a usar con el efecto parallax
	 */
	private void loadImages(String[] images, int numImages){
		fromX = new int[numImages];
		fromX_next = new int[numImages];
		width_image = new int[numImages];
		width_image_next = new int[numImages];
		show_next = new boolean[numImages];
		back_images = new BufferedImage[numImages];
		tick_counter = new int[numImages];
		//Carga cada imagen en su contenedor
		for(int i=0; i<numImages; i++){
			try {
				back_images[i] = ImageIO.read(new File(images[i]));
				fromX[i] = 0;
				tick_counter[i] = 0;
				calculatePosition(i);
			} catch (IOException e) {
				System.err.printf("Error al leer \"%s\"\n",images[i]);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Dibuja todas las imagenes en su posición, si es necesario las mueve
	 * 
	 * @param g2d, elemento grafico sobre el que dibuja
	 */
	public void draw(Graphics2D g2d){
		for(int i=0; i<numImages; i++){
			if(tick_counter[i] >= velocity[i]){
				calculatePosition(i);
				tick_counter[i] -= velocity[i];
			}
			g2d.drawImage(back_images[i].getSubimage(fromX[i], 0, width_image[i], back_images[i].getHeight()), posX[i], posY[i], null);
			if(show_next[i]){
				g2d.drawImage(back_images[i].getSubimage(fromX_next[i], 0, width_image_next[i], back_images[i].getHeight()), width_image[i], posY[i], null);
			}
			tick_counter[i]++;
		}
	}

	private void calculatePosition(int i) {
		fromX[i]++;
		if(width_image[i] > back_images[i].getWidth() - fromX[i]){
			width_image[i] = back_images[i].getWidth() - fromX[i];
			fromX_next[i] = 0;
			width_image_next[i] = width - width_image[i];
			show_next[i] = true;
		}else{
			show_next[i] = false;
		}
		if(width_image[i] == 0){
			show_next[i] = false;
			fromX[i] = 0;
			width_image[i] = width;
		}
	}
}
