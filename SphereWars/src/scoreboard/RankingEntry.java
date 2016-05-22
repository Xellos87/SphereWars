package scoreboard;

import java.util.Locale;

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
			tmp = String.format(Locale.ENGLISH,"%.3f", score);
		}else if(type == Game.COINS){
			tmp = String.format("%d", (int)score);
		}
		return tmp;
	}
}
