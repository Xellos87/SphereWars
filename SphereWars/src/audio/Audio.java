package audio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	static private ArrayList<AudioClip> sounds = new ArrayList<AudioClip>();
	/*public Audio(){
		sounds = new ArrayList<Clip>();
	}*/
	
	public static AudioClip Load(String name){
		Clip s = null;
		File f = new File(name);
		AudioClip clip = null;
		try {
			s = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Error: No se pudo obtener un clip " + name);
			return null;
		}
		try {
			s.open(AudioSystem.getAudioInputStream(f));
			clip = new AudioClip(s);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Error: No se pudo abrir el archivo de audio " + name);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Error: Tipo de archivo de audio incorrecto " + name);
		}
		sounds.add(clip);
		return clip;
	}
}
