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
 * 
 */
public class Kinect extends J4KSDK {
	//ViewerPanel3D viewer = null;
	JLabel label = null;
	boolean mask_players = false;
	//public boolean skel = false;
	private Skeleton skeletons[];
	private Panel myPanel;

	public void maskPlayers(boolean flag) {
		mask_players = flag;
	}

	public void setPanel(Panel pan){
		myPanel = pan;
	}
	//public Skeleton skeletons[] = null;

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

//	public void setViewer(ViewerPanel3D viewer) {
//		this.viewer = viewer;
//	}

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
		// viewer.skeletons = new Skeleton[getSkeletonCountLimit()];
		//System.out.println(" Esqueleto recibido");

		myPanel.skel = true;
		for (int i = 0; i < getSkeletonCountLimit(); i++) {
			skeletons[i] = Skeleton.getSkeleton(i, flags, positions, orientations, state, this);
			myPanel.skeletons[i] = skeletons[i];
			//myPanel.skeletons[i] = Skeleton.getSkeleton(i, flags, positions, orientations, state, this);
		}
		/*double[] transf = new double[16];
		double[] inv_transf = new double[16];
		for (int i = 0; i < 6; i++){
			if (skeletons[i] != null) {
				//System.out.println("Kin: Esqueleto: " + i + " recibido");
				if (skeletons[i].getTimesDrawn() <= 10 && skeletons[i].isTracked()) {
					System.out.println("Dibuja avatar");
					//skeletons[i].getRightForearmTransform(transf, inv_transf);
					//viewer.skeletons[i].get
					//skeletons[i].draw(gl);
	    			//skeletons[i].increaseTimesDrawn();
				}
			}
		}
		/*
		System.out.println("Nueva transf");
		for(int i = 0 ; i < 16 ; i++){
			System.out.print(transf[i] + " ");
			if(i%4 == 3){
				System.out.println();
			}
		}*/
		
	}
	
	@Override
	public void onColorFrameEvent(byte[] data) {
		// System.out.println("Color recibido");
		//if (viewer == null || viewer.videoTexture == null)
		//	return;
		//viewer.videoTexture.update(getColorWidth(), getColorHeight(), data);
	}

	@Override
	public void onInfraredFrameEvent(short[] data) {
		//if (viewer == null || viewer.videoTexture == null)
		//	return;
	}

	
}