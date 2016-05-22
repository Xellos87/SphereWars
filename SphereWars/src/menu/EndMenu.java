package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import scoreboard.RankingEntry;
import utils.Constants;
import videogame.Game;

//TODO: continuar, reiniciar, escribir ranking.
@SuppressWarnings("serial")
public class EndMenu extends Menu{
	//
	public static final int NONE = 0;
	public static final int RESTART = 1;
	public static final int REPEAT = 2;
	public static final int QUIT = 3;
	//
	private final String FIN_JUEGO = "GAME OVER";
	private final String PLAYER1 = "Jugador 1";
	private final String PLAYER2 = "Jugador 2";
	private final String COINS = "monedas";
	private final String METERS = "metros";
	private final String RANKING = "Ranking";
	//Alpha del fondo
	private final int MAX_ALPHA_BACK = 180;
	private int alpha_back;
	//Alpha del texto
	private final int MAX_ALPHA_TEXT = 255;
	private int alpha_text;
	//Datos sobre el juego
	private int num_players;
	private int type;
	private double score_p1;
	private double score_p2;
	private int pos_rank_p1;
	private int pos_rank_p2;
	private String name_p1;
	private String name_p2;
	private boolean write_p1;
	private boolean write_p2;
	//Contador para animaciones
	private int tick_counter;
	private final int MAX_TICK = 10;
	private boolean show;
	//Fuente y ruta de las fuente
	private Font font;
	private Font font_bold;
	private String path_font = "fonts/M04.TTF";
	private String path_font_bold = "fonts/M04B.TTF";
	//Posiciones de los textos
	private int game_overX, game_overY;
	private int player_1X, player_1Y;
	private int player_2X, player_2Y;
	private int score_p1X, score_p1Y;
	private int score_p2X, score_p2Y;
	private int ranking_titleX, ranking_titleY;
	private int ranking_entryX, ranking_entryY;
	

	public EndMenu(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		setFocusable(false);
		setOpaque(false);
		alpha_back = 0;
		alpha_text = 0;
		font = Constants.font;
		font_bold = Constants.font_bold;
		
		
		calculatePositions();
	}

	private void calculatePositions() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		//Calculo de posicion del titulo
		Font f = font_bold.deriveFont(42.0f);
		g.setFont(f);
		int width_text = g.getFontMetrics().stringWidth(FIN_JUEGO);
		game_overX = width/2 - width_text/2;
		game_overY = height/6;
		//Calculo de la posicion del player 1
		f = font_bold.deriveFont(26.0f);
		g.setFont(f);
		width_text = g.getFontMetrics().stringWidth(PLAYER1);
		int height_text = g.getFontMetrics().getHeight();
		player_1X = width/4 - width_text/2;
		player_1Y = height/4;
		//Calculo de la posicion de la puntuacion del jugador 1
		score_p1X = player_1X;
		score_p1Y = player_1Y + height_text;
		//Calculo de la posicion del player 2
		player_2X = player_1X;
		player_2Y = score_p1Y + 2*height_text ;
		//Calculo de la posicion de la puntuacion del jugador 2
		score_p2X = player_1X;
		score_p2Y = player_2Y + height_text;
		//Calculo de la posicion del titulo del ranking
		width_text = g.getFontMetrics().stringWidth(RANKING);
		ranking_titleX = 3*width/4 - width_text/2;
		ranking_titleY = player_1Y; //A la misma altura que los jugadores
		//Calculo de la primera entrada del ranking
		ranking_entryX = ranking_titleX;
		ranking_entryY = ranking_titleY + height_text;
		
	}

	public void draw(Graphics2D g2d) {
		if(isVisible()){
			/* Dibuja todo en pantalla */
			if(alpha_back< MAX_ALPHA_BACK){
				alpha_back += 5;
			}
			//Fondo
			g2d.setColor(new Color(0, 0, 0, alpha_back));
			g2d.fillRect(0, 0, width, height);
			if(alpha_text< MAX_ALPHA_TEXT){
				alpha_text += 5;
			}
			//Fuente de la letra y color
			Font f = font_bold.deriveFont(42.0f);
			g2d.setFont(f);
			g2d.setColor(new Color(255,255,255,alpha_text));
			//Titulo de fin de juego
			g2d.drawString(FIN_JUEGO, game_overX, game_overY);
			//Cabeceras del jugador 1
			f = font_bold.deriveFont(26.0f);
			g2d.setFont(f);
			g2d.drawString(PLAYER1, player_1X, player_1Y);
			//Cabecera del juegador 2 si lo hay
			if(num_players >= 1){
				//width_text = g2d.getFontMetrics().stringWidth(PLAYER2);
				g2d.drawString(PLAYER2, player_2X, player_2Y);
			}
			//Titulo del ranking
			g2d.drawString(RANKING, ranking_titleX, ranking_titleY);
			//Puntuacion del jugador 1
			f = font_bold.deriveFont(20.0f);
			g2d.setFont(f);
			String score = "";
			if(type == Game.RUNNER){
				score = String.format("%.2f %s",score_p1,METERS);
			}else if(type == Game.COINS){
				score = String.format("%d %s",(int)score_p1,COINS);
			}
			g2d.drawString(score, score_p1X, score_p1Y);
			//Puntuación del juegador 2 si lo hay
			if(num_players >= 1){
				if(type == Game.RUNNER){
					score = String.format("%.2f %s",score_p2,METERS);
				}else if(type == Game.COINS){
					score = String.format("%d %s",(int)score_p2,COINS);
				}
				g2d.drawString(score, score_p2X, score_p2Y);
			}
			//Imprime el ranking
			f = font_bold.deriveFont(15.0f);
			g2d.setFont(f);
			int height_text = g2d.getFontMetrics().getHeight();
			RankingEntry[] rank = Constants.ranking.getRanking(type);
			String rank_entry = "";
			int inc = 0;
			int aux_y = ranking_entryY;
			tick_counter++;
			if(tick_counter>=MAX_TICK){
				tick_counter-=MAX_TICK;
				show = !show;
			}
			for(int i=0; i+inc<rank.length; i++){
				if(pos_rank_p1 == i){
					if(type == Game.RUNNER){
						score = String.format("%.3f",score_p1);
					}else if(type == Game.COINS){
						score = String.format("%d",(int)score_p1);
					}
					String name = name_p1;
					if(write_p1 && show){
						int num = 3 - name.length();
						name += "-";
						for(int j=0; j<num; j++){
							name += " ";
						}
					}
					rank_entry = String.format("%2d-%4s %s",i+1,name,score);
					inc++;
				}else if(pos_rank_p2 == i){
					if(type == Game.RUNNER){
						score = String.format("%.3f",score_p2);
					}else if(type == Game.COINS){
						score = String.format("%d",(int)score_p2);
					}
					String name = name_p2;
					if(write_p2 && show){
						int num = 3 - name.length();
						name += "-";
						for(int j=0; j<num; j++){
							name += " ";
						}
					}
					rank_entry = String.format("%2d-%4s %s",i+1,name,score);
					inc++;
				}else{
					rank_entry = String.format("%2d-%4s %s", i+inc+1,rank[i].getName(),rank[i].getScoreString(type));
				}
				g2d.drawString(rank_entry, ranking_entryX, aux_y);
				aux_y += height_text;
			}
			//Imprime las opciones
			
			//TODO Imprimir puntuaciones y opciones
		}else{
			g2d.setColor(new Color(0, 0, 0, 0));
			g2d.fillRect(0, 0, width, height);
		}
	}

	@Override
	public void draw() {
	}

	@Override
	public void cursorDown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cursorUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public String cursorEnter() {
		// TODO Auto-generated method stub
		return null;
	}

	public void init(int num_players, int type, double score_p1, double score_p2) {
		this.num_players = num_players;
		this.type = type;
		this.score_p1 = score_p1;
		this.score_p2 = score_p2;
		this.pos_rank_p1 = Constants.ranking.getPosRanking(type, score_p1);
		this.pos_rank_p2 = Constants.ranking.getPosRanking(type, score_p2);
		if(pos_rank_p1 != -1 && pos_rank_p1 == pos_rank_p2){
			if(score_p1 < score_p2){
				pos_rank_p1++;
			}else{
				pos_rank_p2++;
			}
		}
		name_p1 = "p";
		name_p2 = "p";
		write_p1 = false;
		write_p2 = false;
		if(pos_rank_p1 > pos_rank_p2){
			name_p1 = "_";
			write_p1 = true;
		}else{
			name_p2 = "_";
			write_p2 = true;
		}
		show = true;
		alpha_back = 0;
		alpha_text = 0;
	}

	public int keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		return 0;
	}

}