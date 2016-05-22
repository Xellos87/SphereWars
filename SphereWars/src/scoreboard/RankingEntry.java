package scoreboard;

import videogame.Game;

public class RankingEntry {
	private String name;
	private double score;
	
	public RankingEntry(String name, double score){
		this.name = name;
		this.score = score;
	}
	
	public String getName(){
		return name;
	}
	
	public double getScore(){
		return score;
	}
	
	public String getScoreString(int type){
		String tmp = "";
		if(type == Game.RUNNER){
			tmp = String.format("%.3f", score);
		}else if(type == Game.COINS){
			tmp = String.format("%d", (int)score);
		}
		return tmp;
	}
}
