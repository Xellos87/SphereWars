package videogame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	private int tick_counter;
	private int max_counter;
	/* Datos de posicion de la imagen */
	private int[] fromX;
	private int[] fromX_next;
	private int[] width_image;
	private int[] width_image_next;
	private boolean[] show_next;
	
	public Parallax(int numImages, String[] images, int[] velocity, int[] posX, int[] posY, int width, int height){
		this.velocity = velocity;
		this.numImages = numImages;
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		loadImages(images,numImages);
		this.tick_counter = 0;
	}
	
	private void loadImages(String[] images, int numImages){
		fromX = new int[numImages];
		fromX_next = new int[numImages];
		width_image = new int[numImages];
		width_image_next = new int[numImages];
		show_next = new boolean[numImages];
		back_images = new BufferedImage[numImages];
		max_counter = 0;
		//Carga cada imagen en su contenedor
		for(int i=0; i<numImages; i++){
			try {
				back_images[i] = ImageIO.read(new File(images[i]));
				fromX[i] = 0;
				max_counter = velocity[i] > max_counter ? velocity[i]: max_counter;
			} catch (IOException e) {
				System.err.printf("Error al leer \"%s\"\n",images[i]);
				e.printStackTrace();
			}
		}
	}
	
	public void draw(Graphics2D g2d){
		for(int i=0; i<numImages; i++){
			if(tick_counter % velocity[i] == 0){
				calculatePosition(i);
				g2d.drawImage(back_images[i].getSubimage(fromX[i], 0, width_image[i], back_images[i].getHeight()), posX[i], posY[i], null);
				if(show_next[i]){
					g2d.drawImage(back_images[i].getSubimage(fromX_next[i], 0, width_image_next[i], back_images[i].getHeight()), width_image[i], posY[i], null);
				}
			}
		}
		tick_counter++;
		tick_counter = tick_counter > max_counter ? 0 : tick_counter;	
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
