package scoreboard;

public class RankingEntry {
	private String name;
	private int score;
	
	public RankingEntry(String name, int score){
		this.name = name;
		this.score = score;
	}
	
	public String getName(){
		return name;
	}
	
	public int getScore(){
		return score;
	}
}
