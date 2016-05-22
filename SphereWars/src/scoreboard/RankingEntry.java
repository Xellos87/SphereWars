package scoreboard;

public class RankingEntry {
	private String name;
	private float score;
	
	public RankingEntry(String name, float score){
		this.name = name;
		this.score = score;
	}
	
	public String getName(){
		return name;
	}
	
	public float getScore(){
		return score;
	}
	
	public String getScoreString(){
		return String.format("%.3f", score);
	}
}
