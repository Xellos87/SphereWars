package kinect;

import javax.swing.JLabel;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

/**
 * Autores: Victor Adrian Milla Español - 557022,
 * 			Juan Luis Burillo Ortín - 542083,
 * 			Sandra Malpica Mallo - 670607,
 * 			Richard Elvira López-Echazarreta - 666800
 * 	
 * Clase: Kinect.java
 * 
 * Comentarios: Interactua con el dispositivo Kinect
 * 	ya que no se usa los esqueletos solo realizamos actualizaciones
 * 	en el frame de los esqueletos
 * 
 */
public class Kinect extends J4KSDK {
	JLabel label = null;
	boolean mask_players = false;
	private Skeleton skeletons[];
	private Panel myPanel;

	public void maskPlayers(boolean flag) {
		mask_players = flag;
	}

	public void setPanel(Panel pan){
		myPanel = pan;
	}

	public Kinect() {
		super();
		skeletons = new Skeleton[6];
	}

	public Skeleton[] getSkel() {
		return skeletons;
	}

	public Kinect(byte type) {
		super(type);
		skeletons = new Skeleton[6];
	}


	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {

		if ( label == null)
			return;
	}

	/*
	 * The following method will run every time a new skeleton frame is received
	 * from the Kinect sensor. The skeletons are converted into Skeleton
	 * objects.
	 */
	@Override
	public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {
		myPanel.skel = true;
		for (int i = 0; i < getSkeletonCountLimit(); i++) {
			skeletons[i] = Skeleton.getSkeleton(i, flags, positions, orientations, state, this);
			myPanel.skeletons[i] = skeletons[i];
		}
	}
	
	@Override
	public void onColorFrameEvent(byte[] data) {
	}

	@Override
	public void onInfraredFrameEvent(short[] data) {
	
	}

	
}