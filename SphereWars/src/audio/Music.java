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

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Music.java
 * 
 * Comentarios: Carga los audios y los maneja
 * 
 */
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
	// Variable usada para hacer un fade si se cambia de menu con la cancion no terminada
	private boolean fade = false;
	private float fadeTime;
	// float volume = 1f;

	// Se pone a cierto cuando a la cancion le quedan TIME ms
	// para cargar la siguiente cancion e instantaneamente se pone a false
	// para no cargar varias veces la cancion
	private boolean changing = true;
	// Variable para saber si se esta haciendo un fade debido a que acaba la cancion
	private boolean onChange = false;

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
		try {
			// SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class );
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("ogg", CodecJOgg.class);
		} catch (SoundSystemException e) {
			System.err.println("error linking with the plug-ins");
		}
		lengths = readDuration();
		// Se listan todas las canciones disponibles
		gameFiles = getAllFiles(new File(dirGame));
		menuFiles = getAllFiles(new File(dirMenu));		
		bossFiles = getAllFiles(new File(dirBoss));
	
		actual = 0;
		actualList = menuFiles;
		
		r = new Random();
		mySoundSystem = new SoundSystem();

	}

	/**
	 * Lee el fichero que contiene las duraciones de las canciones
	 * @return
	 */
	private ArrayList<SongLength> readDuration() {
		ArrayList<SongLength> durations = new ArrayList<SongLength>();
		String path = "music/duration.txt";
		File file = new File(path);
		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				String song = sc.next();
				int i = sc.nextInt();
				SongLength dur = new SongLength(song, i);
				durations.add(dur);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: Archivo no encontrado " + file);
		}
		return durations;
	}

	/**
	 * Devuelve la duracion de la cancion actual
	 * @return
	 */
	private int songLength() {
		int j = -1;
		for (SongLength song : lengths) {
			if (song.getSong().equals(actualSong))
				return song.getDuration();
		}

		return j;
	}

	/** 
	 * Devuelve la duracion de la cancion llamada "name"
	 * @param name
	 * @return
	 */
	private int songLength(String name) {
		int j = -1;
		for (SongLength song : lengths) {
			if (song.getSong().equals(name))
				return song.getDuration();
		}

		return j;
	}

	/**
	 * Reproduce una nueva cancion aleatoria del tipo de juego necesario
	 * @param i
	 */
	public void play(int i) {
		File f = actualList.get(i);
		String name = f.getName();
		// Si no hay sonido no ejecutamos sonido
		
		if (!Constants.sound)
			return;
		try {
			// if (!mySoundSystem.playing()) {
			if (!fade) {
				actual = i;
				actualSong = name;
				actualLength = songLength();
			} else {
				next = i;
				nextSong = name;
				nextLength = songLength(nextSong);
			}
			mySoundSystem.backgroundMusic(name, f.toURI().toURL(), name, false);
			if (fade)
				mySoundSystem.setVolume(nextSong, 0.0f);
			else
				mySoundSystem.setVolume(actualSong, 1.0f);
			
		} catch (MalformedURLException e) {
			System.err.println("Error al reproducir la cancion " + f.getName());

		}
		if (actualLength == -1) {
			System.err.println("Error al obtener la duracion de la cancion");
		}
	}

	/**
	 * Devuelve una lista con todos los archivos contenidos en el directorio curDir
	 * @param curDir
	 * @return
	 */
	private static ArrayList<File> getAllFiles(File curDir) {
		ArrayList<File> audioFiles = new ArrayList<File>();
		File[] filesList = curDir.listFiles();
		for (File f : filesList) {
			if (f.isDirectory())
				getAllFiles(f);
			if (f.isFile()) {
				if (!f.getName().equals(".DS_Store"))
					audioFiles.add(f);
			}
		}
		return audioFiles;

	}

	/**
	 * Revisa cuanto queda de la cancion actual para empezar a reducir el volumen
	 * y realizar la transicion suave entre las canciones
	 */
	public void update() {
		if (actualSong.equals("") || !mySoundSystem.playing())
			return;
		float timeLeft = (actualLength * 1000) - mySoundSystem.millisecondsPlayed(actualSong);
		// La cancion que estaba sonando acaba, actualizamos a la nueva cancion
		if (fade && (mySoundSystem.millisecondsPlayed(actualSong) - fadeTime > TIME)) {
			// Se actualiza la cancion introducida por algun cambio
			// como que entra un boss o se pasa del menu al juego
			mySoundSystem.stop(actualSong);

			actual = next;
			actualSong = nextSong;
			actualLength = nextLength;
			nextSong = "";
			nextLength = -1;
			changing = true;
			fade = false;
		} else if (changing == false && timeLeft < 0) {
			// La cancion que estaba saliendo acaba, se elimina como cancion actual
			// y se actualiza con la que estaba empezando a reproducirse
			mySoundSystem.stop(actualSong);
			actualSong = nextSong;
			actualLength = nextLength;
			changing = true;
			actual = next;
			fade = false;
			timeLeft = (actualLength * 1000) - mySoundSystem.millisecondsPlayed(actualSong);
			onChange = false;
		}
		
		// Si queda poco tiempo para que acabe la cancion, o el usuario a forzado un cambio
		if (timeLeft < TIME || fade) {
			if (changing == true) {
				int nueva = -1;
				do {
					nueva = r.nextInt(actualList.size());
				} while (nueva == actual);

				changing = false;
				File n = actualList.get(nueva);
				try {
					// Introducimos la nueva cancion
					nextSong = n.getName();
					nextLength = songLength(nextSong);
					//System.err.println("Introduciendo nueva cancion " + nextSong + " " + nextLength);
					mySoundSystem.backgroundMusic(nextSong, n.toURI().toURL(), nextSong, false);
					onChange = true;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}

			}
			// Se actualiza el volumen de las canciones
			if (fade) {
				float t = mySoundSystem.millisecondsPlayed(actualSong) - fadeTime;
				
				mySoundSystem.setVolume(actualSong, 1 - (t / ((float) TIME)));
				mySoundSystem.setVolume(nextSong, (t / ((float) TIME)));
			} else {
				mySoundSystem.setVolume(actualSong, (timeLeft / ((float) TIME)));
				mySoundSystem.setVolume(nextSong, 1 - (timeLeft / ((float) TIME)));
			}

		}
	}

	/**
	 * Cambia el listado de reproduccion actual a las canciones del menu
	 */
	public void playMenu() {
		actualList = menuFiles;
		int nueva = -1;

		nueva = r.nextInt(actualList.size());
		next = nueva;
		if (fade || onChange)
			mySoundSystem.stop(nextSong);
		if (!actualSong.equals("")) {

			fade = true;
			fadeTime = mySoundSystem.millisecondsPlayed(actualSong);
		}
		changing = false;
		onChange = false;
		play(nueva);
	}

	/**
	 * Cambia el listado de reproduccion actual a las canciones del juego
	 */
	public void playGame() {
		actualList = gameFiles;
		int nueva = -1;
		nueva = r.nextInt(actualList.size());
		//nueva = 4;
		next = nueva;
		if (fade || onChange)
			mySoundSystem.stop(nextSong);
		if(!actualSong.equals("")){
			fade = true;
			fadeTime = mySoundSystem.millisecondsPlayed(actualSong);
		}
		changing = false;
		onChange = false;
		
		play(nueva);		
	}

	/**
	 * Cambia el listado de reproduccion actual a las canciones del jefe
	 */
	public void playBoss() {
		actualList = bossFiles;
		int nueva = -1;
		nueva = r.nextInt(actualList.size());
		next = nueva;
		if (fade || onChange)
			mySoundSystem.stop(nextSong);
		if(!actualSong.equals("")){
			fade = true;
			fadeTime = mySoundSystem.millisecondsPlayed(actualSong);
		}
		changing = false;
		onChange = false;
		
		play(nueva);
	}

	public boolean isFade() {
		return fade;
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}

	/**
	 * Detiene la reproduccion de todas las canciones
	 */
	public void stop() {
		mySoundSystem.stop(actualSong);
		if (!nextSong.equals(""))
			mySoundSystem.stop(nextSong);
		actualSong = "";
		nextSong = "";
		actual = -1;
		next = -1;
		actualLength = -1;
		nextLength = -1;
	}
	
	/**
	 * Devuelve true si se esta reproduciendo la musica del jefe
	 * @return
	 */
	public boolean isBossMusicPlaying(){
		if(actualList == bossFiles) return true;
		return false;
	}
}
