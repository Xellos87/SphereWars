package kinect;

import character.Sphere;

import edu.ufl.digitalworlds.j4k.Skeleton;
import map.MapController;
import utils.Constants;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Panel.java
 * 
 * Comentarios: Intermediario entre el kinect y el juego
 * 
 */
public class Panel {
	private boolean debug;
	
	public Skeleton skeletons[];
	//private Input input;
	private double initPointJ[], initPointF[];
	private double newPosJ[], newPosF[];
	private boolean memorized = false;
	private int contMovJ = 0, contMovF = 0;
	private final int movLim = 10; // 1 segundo 60 fps
	private int p1HandJump = 11, p1HandFast = 7;
	private int p2Hand = 11;
	private int players;
	private double xdeltaJ, ydeltaJ, xdeltaF, ydeltaF;
	public String mano = "";
	private boolean playerDetected = false;
	private boolean handUp = false, handForward = false;;
	private boolean firstTimeJ = true, firstTimeF = true;
	
	private Sphere sphere;
	private MapController mapController;
	private int notCounted = 0;
	
	public boolean skel = false;
	
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
		if(!skel){
			notCounted++;
			if(notCounted >= 10){
			
			setPlayerDetected(false);
			return 1;
			}
		}else{
			notCounted = 0;
		}
		skel = false;
		for (int i = 0; i < 6; i++) {
			if (skeletons[i] != null) {
				// Si se esta siguiento el esqueleto
				if (skeletons[i].isTracked()) {
					double d = distancia(skeletons[i].get3DJoint(p1HandJump), skeletons[i].get3DJoint(10));
					//System.out.println(skeletons[i].isJointTracked(11));
					//ImageAvatar a = new ImageAvatar();
					
					//System.out.println("s: " +  a.fitSkeleton(skeletons[i]));
					System.out.println("D: " + d);
					setPlayerDetected(true);
					// Avanzamos el contador de jugador
					player++;
					if(Constants.zurdo){
						p1HandJump = 7;
						p1HandFast = 11;
					}
					else {
						p1HandJump = 11;
						p1HandFast = 7;
					}

					// Si se ha detectado esqueleto y no hay mano inicializada
					// se inicializa el punto de la mano
					if (initPointJ == null) {
						initPointJ = inicializaMano(skeletons[i], p1HandJump);
					}
					if (initPointF == null) {
						initPointF = inicializaMano(skeletons[i], p1HandFast);
					}
					// Calculamos la logica de la mano del salto

					if (initPointJ != null) {
						double actpos[] = new double[3];
						actpos = skeletons[i].get3DJoint(p1HandJump);
						if (newPosJ != null) {
							xdeltaJ += actpos[0] - newPosJ[0];
							ydeltaJ += actpos[1] - newPosJ[1];
							//System.out.println("YD:" + ydeltaJ );
							d = distancia(skeletons[i].get3DJoint(p1HandJump), skeletons[i].get3DJoint(p1HandJump - 1));
							
							if (ydeltaJ > 2*d && firstTimeJ) {
								System.out.println("Arriba: ");
								//Constants.gameState = Constants.PAUSE;
								//mano = "Arriba: " + ydelta;
								firstTimeJ = false;
								if(sphere != null)
									sphere.jump();
								else	System.err.println("Error (Panel): No hay jugador");
								handUp = true;
							}else if(ydeltaJ < 2*d){
								handUp = false;
							}
							/*if (xdelta > 0.3) {
								System.out.println("Delante");
							}*/
						}
						if (distancia(initPointJ, actpos) > 0.1) {
							//System.out.println("Mano movida");
							// Si es la primera vez o la distancia aumenta
							// considerablemente
							// la actualizamos porque se sigue moviendo
							if (newPosJ == null || distancia(newPosJ, actpos) > 0.04) {
								newPosJ = new double[3];
								newPosJ[0] = actpos[0];
								newPosJ[1] = actpos[1];
								newPosJ[2] = actpos[2];
								contMovJ = 0;
								xdeltaJ = 0;
								ydeltaJ = 0;
							} else {
								// Actualizamos el contador porque la mano
								// parece estable
								contMovJ++;
								firstTimeJ = true;
							}
							// Si la mano lleva estable mas de movLim
							// Se asume que el jugador no ha devuelto bien la
							// mano
							// al punto de inicio y se resetea la posicion
							// inicial si no se ha detectado un movimiento de juego
							// (Para que no se memorice si la mano esta muy alta)
							if (contMovJ >= movLim && !handForward) {
								//System.out.println("Memorizada");
								contMovJ = 0;
								firstTimeJ = true;
								initPointJ = newPosJ;
								newPosJ = null;
							}
							
						} else {
							firstTimeJ = true;
							newPosJ = null;
						}

					}
					// Calculamos la logica de la mano de la aceleracion
					if (initPointF != null) {
						double actpos[] = new double[3];
						actpos = skeletons[i].get3DJoint(p1HandFast);
						if (newPosF != null) {
							xdeltaF += actpos[0] - newPosF[0];
							ydeltaF += actpos[1] - newPosF[1];
							System.out.println("XD: " + xdeltaF);
							d = distancia(skeletons[i].get3DJoint(p1HandFast), skeletons[i].get3DJoint(p1HandFast - 1));
							
							if (xdeltaF > d/100 && firstTimeF) {
								System.out.println("Delante: ");
								//Constants.gameState = Constants.PAUSE;
								//mano = "Arriba: " + ydelta;
								firstTimeF = false;
								if(sphere != null)
									mapController.setSpeedHigh();
									//sphere.;
								else	System.err.println("Error (Panel): No hay jugador");
								handForward = true;
								
							}else if(xdeltaF < d/100){

								handForward = false;
							}
							
							/*if (xdelta > 0.3) {
								System.out.println("Delante");
							}*/
						}else{
							//System.out.println("Bajando");
							mapController.setSpeedLow();
						}
						if (distancia(initPointF, actpos) > 0.1) {
							//System.out.println("Mano movida");
							// Si es la primera vez o la distancia aumenta
							// considerablemente
							// la actualizamos porque se sigue moviendo
							if (newPosF == null || distancia(newPosF, actpos) > 0.04) {
								newPosF = new double[3];
								newPosF[0] = actpos[0];
								newPosF[1] = actpos[1];
								newPosF[2] = actpos[2];
								contMovF = 0;
								xdeltaF = 0;
								ydeltaF = 0;
							} else {
								// Actualizamos el contador porque la mano
								// parece estable
//System.out.println("Bajando speed");
	//							mapController.setSpeedLow();
								contMovF++;
								firstTimeF = true;
							}
							// Si la mano lleva estable mas de movLim
							// Se asume que el jugador no ha devuelto bien la
							// mano
							// al punto de inicio y se resetea la posicion
							// inicial si no se ha detectado un movimiento de juego
							// (Para que no se memorice si la mano esta muy alta)
							if (contMovF >= movLim && !handForward) {
								System.out.println("Memorizada");
								mapController.setSpeedLow();
								contMovF = 0;
								firstTimeF = true;
								initPointF = newPosF;
								newPosF = null;
							}
							
						} else {
							firstTimeF = true;
							newPosF = null;

							//mapController.setSpeedLow();
						}

					}
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

	private double[] inicializaMano(Skeleton skel, int hand) {
		double[] initPoint = new double[3];
		initPoint[0] = skel.get3DJointX(hand);
		initPoint[1] = skel.get3DJointY(hand);
		initPoint[2] = skel.get3DJointZ(hand);
		return initPoint;
	}

	double distancia(double[] p1, double[] p2) {
		return Math.sqrt((p1[0] - p2[0]) * (p1[0] - p2[0]) + (p1[1] - p2[1]) * (p1[1] - p2[1])
				+ (p1[2] - p2[2]) * (p1[2] - p2[2]));
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

	public MapController getMapController() {
		return mapController;
	}

	public void setMapController(MapController mapController) {
		this.mapController = mapController;
	}
}
