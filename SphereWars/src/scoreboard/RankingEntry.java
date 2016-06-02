package scoreboard;

import java.util.Locale;

import videogame.Game;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: RankingEntry.java
 * 
 * Comentarios: Estructura que representa una entrada del ranking
 * 
 */
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
