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
	private String name; //Nombre
	private double score; //Puntuación
	
	/**
	 * Constructor
	 * 
	 * @param name, nombre
	 * @param score, puntuacion
	 */
	public RankingEntry(String name, double score){
		this.name = name;
		this.score = score;
	}
	
	/**
	 * 
	 * @return el nombre de la entrada
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return la puntuación
	 */
	public double getScore(){
		return score;
	}
	
	/**
	 * Saca la puntuación con formato
	 * 
	 * @param type, tipo de juego
	 * @return la puntuación como cadena
	 */
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
