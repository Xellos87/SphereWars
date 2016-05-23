package audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.libraries.LibraryJavaSound;
import utils.Constants;

public class Music {
	final int TIME = 5000; // En ms

	private Random r;

	private String dirMenu = "music/menu";
	private String dirGame = "music/game";
	private String dirBoss = "music/boss";
	private String actualSong = "";	
	private int actualLength = -1;
	private String nextSong = "";
	private int nextLength = -1;
	private SoundSystem mySoundSystem;
	private boolean fade = false;
	private float fadeTime;
	//float volume = 1f;
	
	// Se pone a cierto cuando a la cancion le quedan TIME ms
	// para cargar la siguiente cancion
	private boolean changing = true;

	// Indica la cancion actual que se esta reproduciendo
	private int actual;
	private int next;

	// Listado de archivos de audio
	private ArrayList<File> actualList;
	private ArrayList<File> gameFiles;
	private ArrayList<File> menuFiles;
	private ArrayList<File> bossFiles;
	
	private ArrayList<SongLength> lengths;

	public Music() {
		try 
        { 
            //SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class ); 
            SoundSystemConfig.addLibrary( LibraryJavaSound.class ); 
            SoundSystemConfig.setCodec( "ogg", CodecJOgg.class ); 
        } 
        catch( SoundSystemException e ) 
        { 
            System.err.println("error linking with the plug-ins" ); 
        } 
		lengths = readDuration();
		//menuFiles = getAllFiles(new File(dirMenu));
		gameFiles = getAllFiles(new File(dirGame));
		menuFiles = getAllFiles(new File(dirMenu));
		bossFiles = getAllFiles(new File(dirBoss));
		//menuBf = load(menuFiles);
		
		actual = 0;
		actualList = menuFiles;
		//player = new LillePlay(menuBf.get(0),null);
		//System.out.println("Duracion " + player.getDuration());
		r = new Random();
		mySoundSystem = new SoundSystem();
		
		
	}
	
	private ArrayList<SongLength> readDuration(){
		ArrayList<SongLength> durations = new ArrayList<SongLength>();
		String path = "music/duration.txt";
	    File file = new File(path);
	    Scanner input = null;
        try {
			input = new Scanner(file);
			Scanner sc = new Scanner(file);

	        while (sc.hasNextLine()) {
	        	String song = sc.next();
	        //	System.out.print(song);
	            int i = sc.nextInt();
	           // System.out.println( " " + i);
	            SongLength dur = new SongLength(song,i);
	            durations.add(dur);
	        }
	        sc.close();
		} catch (FileNotFoundException e) {
			// 
			//e.printStackTrace();
			System.out.println("Error: Archivo no encontrado " + file);
		}
		return durations;
	}

	private int songLength(){
		int j = -1;
		for(SongLength song: lengths){
			if(song.getSong().equals(actualSong))
				return song.getDuration();
		}
		
		return j;
	}
	private int songLength(String name){
		int j = -1;
		for(SongLength song: lengths){
			if(song.getSong().equals(name))
				return song.getDuration();
		}
		
		return j;
	}
	public void play(int i) {
		File f = actualList.get(i);
		String name = f.getName();
		// Si no hay sonido no ejecutamos sonido
		if (!Constants.sound) return;
		try {
			if(!mySoundSystem.playing()){
				actualSong = name;
				actualLength = songLength();
				System.out.println("Reproduciendo " + actualSong + " " + actualLength);
				//mySoundSystem.backgroundMusic(sourcename, url, identifier, toLoop);
				mySoundSystem.backgroundMusic(actualSong, f.toURI().toURL(), actualSong, false);
				mySoundSystem.setVolume(actualSong, 1.0f);
			}else{
				System.out.println("Fade " + name);
				nextSong = name;
				nextLength = songLength(name);	
				//mySoundSystem.fadeOutIn(sourcename, url, identifier, 1000, milisIn);
				mySoundSystem.fadeOutIn(name, f.toURI().toURL(), name, 1000, TIME);
			
				//mySoundSystem.backgroundMusic(actualSong, f.toURI().toURL(), actualSong, true);
				//mySoundSystem.setVolume(actualSong, 1.0f);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Error al reproducir la cancion " + f.getName());
			
		}
		if(actualLength == -1){
			System.err.println("Error al obtener la duracion de la cancion");
		}
	}

	private static ArrayList<File> getAllFiles(File curDir) {
		ArrayList<File> audioFiles = new ArrayList<File>();
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isDirectory())
				getAllFiles(f);
			if (f.isFile()) {
				//System.out.println(f.getName());
				audioFiles.add(f);
			}
		}
		return audioFiles;

	}

	private ArrayList<BufferedInputStream> load(ArrayList<File> files) {
		FileInputStream fs = null;
		BufferedInputStream bf;
		ArrayList<BufferedInputStream> filesBf = new ArrayList<BufferedInputStream>();
		for (File f : files) {
			try {

				fs = new FileInputStream(f);
				bf = new BufferedInputStream(fs);
				filesBf.add(bf);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.err.println("Error: Cargando archivo de audio " + f.getName());
			}
		}
		return filesBf;
	}
/*
	public void subeVol() {
		volume += 0.05;
		player.setVolume(volume);
		// player.ge
	}

	public void bajaVol() {
		volume -= 0.05;
		player.setVolume(volume);
	}*/

	public void update() {
		if(actualSong.equals("") || !mySoundSystem.playing())	return;
		float timeLeft = (actualLength * 1000) - mySoundSystem.millisecondsPlayed(actualSong);
		//int timeLeft = player.getDuration() - player.getPosition();
		//System.out.println(actualSong);
		System.out.println(timeLeft + " " + mySoundSystem.getVolume(actualSong) + " " + mySoundSystem.getVolume(nextSong));
		//System.out.print("\b\b\b\b\b\b\b\b\b\b\b");

		// La cancion que estaba sonando acaba, actualizamos a la nueva cancion
		//if(actualSong.equals("") || !mySoundSystem.playing()){
		//mySoundSystem.
		//if(changing == false && !mySoundSystem.playing(actualSong)){
		if(fade && (mySoundSystem.millisecondsPlayed(actualSong) - fadeTime > TIME)){
			mySoundSystem.stop(actualSong);
			actualSong = nextSong;
			actualLength = nextLength;
			nextSong = "";
			nextLength = -1;
			changing = true;
			actual = next;
			fade = false;
		}else
		if(changing == false && timeLeft < 0){
			//System.out.println("Cambiando cancion");
			mySoundSystem.stop(actualSong);
			actualSong = nextSong;
			actualLength = nextLength;
			changing = true;
			actual = next;
			fade = false;
		}
		if (timeLeft < TIME || fade) {
			if (changing == true) {
				int nueva = -1;
				do {
					nueva = r.nextInt(actualList.size());
				} while (nueva == actual);
				
				changing = false;
				File n = actualList.get(nueva);
				try {
					nextSong = n.getName();
					nextLength = songLength(nextSong);
					//System.err.println("");
					//System.err.println("Introduciendo nueva cancion " + nextSong + " " + nextLength);
					mySoundSystem.backgroundMusic(nextSong, n.toURI().toURL(), nextSong, false);
					//mySoundSystem.fadeOutIn(nextSong, n.toURI().toURL(), nextSong, TIME, TIME);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//System.out.println((timeLeft / ((float)TIME)));
			if(fade){
				float t = mySoundSystem.millisecondsPlayed(actualSong) - fadeTime;
				mySoundSystem.setVolume(actualSong,1- (t / ((float)TIME)));
				mySoundSystem.setVolume(nextSong,(t / ((float)TIME)));
			}else{
				mySoundSystem.setVolume(actualSong, (timeLeft / ((float)TIME)));
				mySoundSystem.setVolume(nextSong,1-(timeLeft / ((float)TIME)));
			}

		}
	}
	public void playMenu(){
		actualList = menuFiles;
		int nueva = -1;
		
		//System.out.println("En playmenu");
		nueva = r.nextInt(actualList.size());
		next = nueva;
		//nextSong = actualList.get(nueva).getName();
		play(nueva);
	}
	public void playGame(){
		actualList = gameFiles;
		int nueva = -1;
		nueva = r.nextInt(actualList.size());
		next = nueva;
		fade = true;
		fadeTime = mySoundSystem.millisecondsPlayed(actualSong);
		//nextSong = actualList.get(nueva).getName();
		play(nueva);
	}
	public void playBoss(){
		actualList = bossFiles;
		int nueva = -1;
		nueva = r.nextInt(actualList.size());
		next = nueva;
		//nextSong = actualList.get(nueva).getName();
		play(nueva);
	}

	public boolean isFade() {
		return fade;
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}
}
