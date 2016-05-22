package audio;

import javax.sound.sampled.Clip;

public class AudioClip {
	private Clip sound;
	public AudioClip(Clip clip){
		sound = clip;
	
	}
	
	public void start(){
		if(!sound.isActive()){
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
}
