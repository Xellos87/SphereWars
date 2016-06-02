package audio;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: SongLength.java
 * 
 * Comentarios: Clase que contiene la duracion para una cancion
 * 
 */
public class SongLength {
	private String song;
	private int length;
	public SongLength(String s, int du){
		song = s;
		length = du;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public int getDuration() {
		return length;
	}
	public void setDuration(int duration) {
		this.length = duration;
	}
}
