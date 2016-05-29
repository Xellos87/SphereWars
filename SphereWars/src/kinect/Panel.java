package kinect;

import java.io.IOException;

import character.Sphere;

//import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.util.awt.TextRenderer;

import edu.ufl.digitalworlds.j4k.Skeleton;
import utils.Constants;
//import input.Input;

public class Panel {
	private boolean debug;
	
	public Skeleton skeletons[];
	//private Input input;
	private double initPoint[];
	private double newPos[];
	private boolean memorized = false;
	private int contMov = 0;
	private final int movLim = 10; // 1 segundo 60 fps
	private int p1Hand = 11;
	private int p2Hand = 11;
	private int players;
	private double xdelta, ydelta;
	public String mano = "";
	private boolean playerDetected = false;
	private boolean handUp = false;
	private boolean firstTime = true;
	
	private Sphere sphere;
	
	public Panel( int players, boolean debug) {
		skeletons = new Skeleton[6];
		this.players = players;
		this.debug = debug;
		// initPoint = new double[3];
	}

	// Si mano movida guardamos posicion,
	// si nueva posicion es peque�a contador++
	// si contador == 60 actualizamos initpoint
	public int trackHand() {
		if(Constants.conTeclado || Constants.enMenu) return -1;
		int player = 0;
		// Se recorre el vector entero de esqueletos
		// ya que no se sabe en que posicion guarda kinect el esqueleto
		// detectado
		for (int i = 0; i < 6; i++) {
			if (skeletons[i] != null) {
				// Si se esta siguiento el esqueleto
				if (skeletons[i].isTracked()) {
					setPlayerDetected(true);
					// Avanzamos el contador de jugador
					player++;
					int hand = 0;
					// Se cambia la mano dependiendo del jugador
					if (player == 2) {
						hand = p2Hand;
					} else {
						hand = p1Hand;
					}

					// Si se ha detectado esqueleto y no hay mano inicializada
					// se inicializa el punto de la mano
					if (initPoint == null) {
						inicializaMano(skeletons[i], hand);
					}
					/*
					 * // Si se pulsa espacio se aprende la posicion
					 * if(input.isSpace()){ input.setSpace(false); memorized =
					 * true; initPoint[0] = skeletons[i].get3DJointX(11);
					 * initPoint[1] = skeletons[i].get3DJointY(11); initPoint[2]
					 * = skeletons[i].get3DJointZ(11); }
					 */
					// if(memorized){
					if (initPoint != null) {
						double actpos[] = new double[3];
						actpos = skeletons[i].get3DJoint(hand);
						if (newPos != null) {
							xdelta += actpos[0] - newPos[0];
							ydelta += actpos[1] - newPos[1];
							System.out.println(ydelta);
							if (ydelta > 0.06 && firstTime) {
								System.out.println("Arriba: ");
								//Constants.gameState = Constants.PAUSE;
								//mano = "Arriba: " + ydelta;
								firstTime = false;
								if(sphere != null)
									sphere.jump();
								else	System.err.println("Error (Panel): No hay jugador");
								
							}
							if (xdelta > 0.3) {
								System.out.println("Delante");
							}
						}
						if (distancia(initPoint, actpos) > 0.1) {
							//System.out.println("Mano movida");
							// Si es la primera vez o la distancia aumenta
							// considerablemente
							// la actualizamos porque se sigue moviendo
							if (newPos == null || distancia(newPos, actpos) > 0.04) {
								newPos = new double[3];
								newPos[0] = actpos[0];
								newPos[1] = actpos[1];
								newPos[2] = actpos[2];
								contMov = 0;
								xdelta = 0;
								ydelta = 0;
							} else {
								// Actualizamos el contador porque la mano
								// parece estable
								contMov++;
								firstTime = true;
							}
							// Si la mano lleva estable mas de movLim
							// Se asume que el jugador no ha devuelto bien la
							// mano
							// al punto de inicio y se resetea la posicion
							// inicial si no se ha detectado un movimiento de juego
							// (Para que no se memorice si la mano esta muy alta)
							if (contMov >= movLim && !handUp) {
								System.out.println("Memorizada");
								contMov = 0;
								firstTime = true;
								initPoint = newPos;
								newPos = null;
							}
							
						} else {
							firstTime = true;
							newPos = null;
						}

					}
					// skeletons[i].getRightForearmTransform(transf,
					// inv_transf);
					// viewer.skeletons[i].get
					// skeletons[i].draw(gl);
					// skeletons[i].increaseTimesDrawn();
				}
			}
		}
		if (player < players && debug){
			
			System.err.println("Numero de jugadores encontrados erroneo");
			System.err.println("Hay: " + player + " y se esperaban " + players);
			return 1;
		}
		return 0;
	}

	private void inicializaMano(Skeleton skel, int hand) {
		initPoint = new double[3];
		initPoint[0] = skel.get3DJointX(hand);
		initPoint[1] = skel.get3DJointY(hand);
		initPoint[2] = skel.get3DJointZ(hand);
	}

	double distancia(double[] p1, double[] p2) {
		return Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1])
				+ (p1[2] - p2[2]) * (p1[2] - p2[2]));
	}
/*
	public void dibuja2(GL2 gl) {
		// GL2 gl2 = getGL2();
		gl.glDisable(GL2.GL_LIGHTING);
		gl.glLineWidth(2);
		gl.glColor3f(1f, 0f, 0f);
		for (int i = 0; i < skeletons.length; i++)
			if (skeletons[i] != null) {
				if (skeletons[i].getTimesDrawn() <= 10 && skeletons[i].isTracked()) {
					// skeletons[i].draw(gl);
					// skeletons[i].draw(gl);
					skeletons[i].increaseTimesDrawn();
				}
			}
	}*/

	// Joint 11 mano derecha
	// Joint 10 mu�eca derecha
	// Joint 9 codo derecho
	// Joint 8 hombro derecho
	// Joint 7 mano izquierda
	// Joint 6 mu�eca izquierda
	// Joint 5 codo izquierdo
	// Joint 4 hombro izquierdo
	/*
	public void dibuja(GL2 gl) {
		// System.out.println("En dibuja");
		for (int i = 0; i < 6; i++) {
			if (skeletons[i] != null) {
				// System.out.println("Distintonull");
				// System.out.println("Kin: Esqueleto: " + i + " recibido");
				if (skeletons[i].getTimesDrawn() <= 10 && skeletons[i].isTracked()) {
					// System.out.println("Dibuja avatar");
					for (int j = 0; j < 20; j++) {
						if (j > 18)
							gl.glColor3f(1, 0, 0);
						else if (j > 16)
							gl.glColor3f(0, 1, 0);
						else if (j > 14)
							gl.glColor3f(0, 0, 1);
						else if (j > 12)
							gl.glColor3f(1, 1, 0);
						else if (j > 10)
							gl.glColor3f(0, 1, 1);
						else if (j > 8)
							gl.glColor3f(1, 0, 1);
						else
							gl.glColor3f(1, 1, 1);
						/*
						 * if(j >= 8) gl.glColor3f(1, 1, 1); else if(j >= 6)
						 * gl.glColor3f(0, 1, 0); else if(j >= 4)
						 * gl.glColor3f(0, 0, 1); else if(j >= 2)
						 * gl.glColor3f(1, 1, 0); else if(j >= 0)
						 * gl.glColor3f(1, 0, 0);
						 */
		/*				double[] joint = skeletons[i].get3DJoint(j);
						// if(j == 7)
						// gl.glColor3f(1, 0, 1);
						if (j >= 4 && j <= 11)
							drawPoint(gl, joint[0], joint[1], 0.025f);
					}
					try {
						Runtime.getRuntime().exec("cls");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
					// System.out.println("Derecha: x:" +
					// skeletons[i].get3DJointX(11) + " y: " +
					// skeletons[i].get3DJointY(11) + " z: " +
					// skeletons[i].get3DJointZ(11));
					// skeletons[i].getRightForearmTransform(transf,
					// inv_transf);
					// viewer.skeletons[i].get
					// skeletons[i].draw(gl);
					// skeletons[i].increaseTimesDrawn();
				}
			}
		}
	}
/*
	private void drawPoint(GL2 gl, double x, double y, float size) {
		// gl.glColor3f(1, 0, 0);
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex3d(x - size / 2, y - size / 2, 0.0f);
		gl.glVertex3d(x + size / 2, y - size / 2, 0.0f);
		gl.glVertex3d(x + size / 2, y + size / 2, 0.0f);
		gl.glVertex3d(x - size / 2, y + size / 2, 0.0f);
		gl.glEnd();
	}*/

	public int getP1Hand() {
		return p1Hand;
	}

	public void setP1Hand(int p1Hand) {
		this.p1Hand = p1Hand;
	}

	public int getP2Hand() {
		return p2Hand;
	}

	public void setP2Hand(int p2Hand) {
		this.p2Hand = p2Hand;
	}

	public boolean isPlayerDetected() {
		return playerDetected;
	}

	public void setPlayerDetected(boolean playerDetected) {
		this.playerDetected = playerDetected;
	}

	public boolean isHandUp() {
		return handUp;
	}

	public void setHandUp(boolean handUp) {
		this.handUp = handUp;
	}

	public Sphere getSphere() {
		return sphere;
	}

	public void setSphere(Sphere sphere) {
		this.sphere = sphere;
	}
}
