package audio;

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
