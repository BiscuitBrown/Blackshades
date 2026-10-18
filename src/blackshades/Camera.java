package blackshades;

import static org.lwjgl.opengl.GL11.*;
import blackshades.Quaternions.*;

public class Camera {
	
	public XYZ 
		position,
		oldposition,
		targetoffset;
	
	public float
 		rotation, rotation2,
 		oldrotation, oldrotation2,
 		oldoldrotation, oldoldrotation2,
 		visrotation,  visrotation2;
	
	public Camera() { 
		position = new XYZ();
		oldposition = new XYZ();
		targetoffset = new XYZ();

	}
	
	public void apply() {
		glTranslatef(-position.x,-position.y,-position.z);
	}
}
