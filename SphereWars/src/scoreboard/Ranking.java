package scoreboard;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import videogame.Game;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Ranking.java
 * 
 * Comentarios: Clase para trabajar con los rankings
 * 
 */
public class Ranking {
	private final int MAX_RANK = 10;
	//Entradas de los ranking
	private RankingEntry[] runner_ranking;
	private RankingEntry[] coins_ranking;
	//Ruta del xml y etiquetas que usa
	private String path_ranking = "ranking/ranking.xml";
	private String path_ranking_aux = "ranking/ranking.xml";
	private String header_xml = "ranking";
	private String group_xml = "rank";
	private String type_group_xml = "type";
	private String entry_xml = "entry";
	private String pos_entry_xml = "pos";
	private String name_entry_xml = "name";
	private String score_entry_xml = "score";
	private String type_runner_xml = "runner";
	private String type_coins_xml = "coins";
	
	/**
	 * Constructor
	 */
	public Ranking(){
		if(!readRanking()){
			runner_ranking = new RankingEntry[MAX_RANK];
			coins_ranking = new RankingEntry[MAX_RANK];
		}else{
			//Lectura correcta, comprobación de lectura
			/*System.out.println("Ranking de runner");
			for(int i=0; i<runner_ranking.length; i++){
				System.out.printf("%d - %s : %d\n",i+1,runner_ranking[i].getName(),runner_ranking[i].getScore());
			}
			System.out.println("Ranking de points");
			for(int i=0; i<points_ranking.length; i++){
				System.out.printf("%d - %s : %d\n",i+1,points_ranking[i].getName(),points_ranking[i].getScore());
			}*/
		}
	}

	/**
	 * Lee el mapa del fichero xml
	 * 
	 * @return true si y solo si la lectura ha sido correcta
	 */
	private boolean readRanking() {
		//Leer del xml de ranking
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(path_ranking));
			doc.getDocumentElement().normalize();
			//Obtiene el elemento que es cabecera de los demas
			Node header = doc.getElementsByTagName(header_xml).item(0);
			Element element_header = (Element) header;
			NodeList lst_rank = element_header.getElementsByTagName(group_xml);
			for(int i=0; i<lst_rank.getLength(); i++){
				Element element_rank = (Element) lst_rank.item(i);
				//Para cada elemento comprobar su tipo
				String type = element_rank.getAttribute(type_group_xml);
				RankingEntry[] entrys = new RankingEntry[MAX_RANK];
				//Introdcimos todas la entradas
				NodeList lst_entry = element_rank.getElementsByTagName(entry_xml);
				for(int k=0; k<lst_entry.getLength(); k++){
					Element element_entry = (Element) lst_entry.item(k);
					int pos = Integer.parseInt(element_entry.getAttribute(pos_entry_xml));
					String name = element_entry.getAttribute(name_entry_xml);
					float score = Float.parseFloat(element_entry.getAttribute(score_entry_xml));
					
					entrys[pos-1] = new RankingEntry(name, score);
				}
				//Inserta el ranking en el correspondiente
				if(type.equals(type_runner_xml)){
					runner_ranking = entrys;
				}else if(type.equals(type_coins_xml)){
					coins_ranking = entrys;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Escribe el ranking
	 */
	private void writeRanking(){
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			Document doc = docBuilder.newDocument();
			//Elemento de la cabecera
			Element header = doc.createElement(header_xml);
			doc.appendChild(header);
			//Primer elemento del tipo runner
			addElement(doc,header,type_runner_xml,runner_ranking,Game.RUNNER);
			//Segundo elemento del tipo points
			addElement(doc,header,type_coins_xml,coins_ranking,Game.COINS);
			
			//Guardamos el fichero
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path_ranking_aux));
			transformer.transform(source, result);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Añade un elemento
	 * 
	 * @param doc, documento en el que se agrega
	 * @param header, cabecera
	 * @param type_xml, tipo de juego
	 * @param ranking, array de entradas
	 * @param type, tipo de juego
	 */
	private void addElement(Document doc, Element header, String type_xml, RankingEntry[] ranking, int type) {
		//Cabecera del grupo
		Element group = doc.createElement(group_xml);
		header.appendChild(group);
		//Atributo de la cabecera
		Attr type_group = doc.createAttribute(type_group_xml);
		type_group.setValue(type_xml);
		group.setAttributeNode(type_group);
		//Insertamos todos los elementos del ranking
		for(int i=0; i<ranking.length; i++){
			//Entrada de ranking
			Element entry = doc.createElement(entry_xml);
			group.appendChild(entry);
			//Atributos del ranking
			//Posicion
			Attr pos = doc.createAttribute(pos_entry_xml);
			pos.setValue(String.valueOf(i+1));
			entry.setAttributeNode(pos);
			//Nombre
			Attr name = doc.createAttribute(name_entry_xml);
			name.setValue(ranking[i].getName());
			entry.setAttributeNode(name);
			//Puntuacion
			Attr score = doc.createAttribute(score_entry_xml);
			score.setValue(ranking[i].getScoreString(type));
			entry.setAttributeNode(score);
		}
	}

	/**
	 * 
	 * @param type, tipo de juego
	 * @return array de ranking
	 */
	public RankingEntry[] getRanking(int type){
		if(type == Game.RUNNER){
			return runner_ranking;
		}else if(type == Game.COINS){
			return coins_ranking;
		}
		return null;
	}
	
	/**
	 * Obtiene la posición del ranking si graba
	 * 
	 * @param type, tipo de juego
	 * @param score, puntuación
	 * @return posición en el ranking, si no graba devuelve -1
	 */
	public int getPosRanking(int type, double score){
		int pos = -1;
		if(type == Game.RUNNER){
			pos = getPosition(runner_ranking, score);
		}else if(type == Game.COINS){
			pos = getPosition(coins_ranking, score);
		}
		return pos;
	}
	
	/**
	 * Obtiene la puntuación
	 * 
	 * @param rank, array de ranking
	 * @param score, puntuación
	 * @return pos del score en el ranking, si no entra devuelve -1
	 */
	private int getPosition(RankingEntry[] rank, double score){
		int pos = -1;
		int index = 0;
		while(pos < 0 && index < rank.length){
			if(rank[index].getScore() < score){
				pos = index;
			}
			index++;
		}
		return pos;
	}
	
	/**
	 * Actualiza la entrada
	 * 
	 * @param type, tipo de juego
	 * @param pos, posición en el que guarda la entrada
	 * @param entry, entrada a guardar
	 */
	public void updateEntry(int type, int pos, RankingEntry entry){
		if(type == Game.RUNNER){
			updateRanking(runner_ranking,pos,entry);
			writeRanking();
		}else if(type == Game.COINS){
			updateRanking(coins_ranking,pos,entry);
			writeRanking();
		}
	}
	
	/**
	 * Actualiza el ranking
	 * 
	 * @param rank, array de ranking
	 * @param pos, posición en el que guarda
	 * @param entry, entrada a guardar
	 */
	private void updateRanking(RankingEntry[] rank, int pos, RankingEntry entry){
		RankingEntry aux = entry;
		for(int i=pos-1; i<rank.length; i++){
			RankingEntry aux1 = rank[i];
			rank[i] = aux;
			aux = aux1;
		}
	}
	
}
