package audio;

import javax.sound.sampled.Clip;

import utils.Constants;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: AudioClip.java
 * 
 * Comentarios: Maneja sonidos de eventos
 * 
 */
public class AudioClip {
	private Clip sound;
	private boolean repeat = false;
	public AudioClip(Clip clip){
		sound = clip;
	
	}
	
	public void start(){
		if(!Constants.sound) return;
		if(!sound.isActive() || repeat){
			sound.stop();
			sound.setFramePosition(0);
		}
		//jump.stop();
		sound.start();
	}
	
	public void stop(){
		sound.stop();
	}
	
	public boolean isRunning(){
		return sound.isRunning();
	}
	public boolean isActive(){
		return sound.isActive();
	}
	
	public void drain(){
		sound.drain();
	}
	public void setRepeat(boolean f){
		repeat = f;
	}
}
